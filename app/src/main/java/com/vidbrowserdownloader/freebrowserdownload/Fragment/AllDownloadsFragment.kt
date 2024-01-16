package com.vidbrowserdownloader.freebrowserdownload.Fragment

import com.vidbrowserdownloader.freebrowserdownload.Activity.VideoActivity
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.VideoPlayAct
import com.vidbrowserdownloader.freebrowserdownload.Adapter.AllFileAdapter
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.constant.YTAPI
import com.vidbrowserdownloader.freebrowserdownload.constant.scanMediaFile
import com.vidbrowserdownloader.freebrowserdownload.databinding.FragmentAllDownloadsBinding
import com.vidbrowserdownloader.freebrowserdownload.dialog.DeleteDialog
import com.vidbrowserdownloader.freebrowserdownload.utils.*
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AllDownloadsFragment : Fragment() {
    lateinit var binding: FragmentAllDownloadsBinding
    val list = ArrayList<DownloadModel>()
    var adapter: AllFileAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllDownloadsBinding.inflate(inflater, container, false)
        list.clear()

        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/X_Browser/").path).listFiles()
            ?.forEach {
                list.add(DownloadModel(it,  getDateAndTimeFromFile(it.path)))
                scanMediaFile(requireContext(),it.path)
            }
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/V_Downloader/").path).listFiles()
            ?.forEach {
                list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                scanMediaFile(requireContext(),it.path)
            }

        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/$YTAPI/").path).listFiles()
            ?.forEach {
                if (it.name.startsWith("YTD_"))
                {
                    list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                    scanMediaFile(requireContext(),it.path)
                }
            }
        if (list.isEmpty()) {
            binding.imgEmpty.visible()
        } else {
            binding.imgEmpty.gon()
            list.sortByDescending {
                it.time
            }

            adapter = AllFileAdapter(requireActivity(), list, onClick = { position->
                DeleteDialog(this.requireActivity()) {
                    deletefile(list[position].url.path,position)
                }
            }) {path,name->
                if (path.endsWith("png") || path.endsWith("jpg")) {
                    startActivity(
                        Intent(requireActivity(), VideoActivity::class.java).putExtra("videoPath", path).putExtra("videoTitle", name)
                    )
                } else {
                    startActivity(
                        Intent(activity, VideoPlayAct::class.java).putExtra("videoPath", path).putExtra("videoTitle", name)
                    )
                }


            }
            binding.allDownloadRv.adapter = adapter
        }
        binding.allDownloadRv.adapter?.notifyDataSetChanged()
        return binding.root
    }

    private fun deletefile(filepath: String, position: Int) {
        try {
            val file = File(filepath)
            if (file.exists()) {
                file.delete()
                list.removeAt(position)
                adapter!!.notifyDataSetChanged()
                if (list.size == 0) {
                    binding.imgEmpty.visible()
                }
            }
            "file deleted Successfully".tos(this.requireActivity())
        } catch (e: Exception) {
            "${e.message}".log()
        }
    }

    fun notifyData() {
        if (adapter != null) {
            list.clear()
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/X_Browser/").path).listFiles()
                ?.forEach {
                    list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                }

            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/V_Downloader/").path).listFiles()
                ?.forEach {
                    list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                }
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/$YTAPI/").path).listFiles()
                ?.forEach {
                    if (it.name.startsWith("YTD_"))
                    {
                        list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                        scanMediaFile(requireContext(),it.path)
                    }
                }

            if (list.isEmpty()) {
                binding.imgEmpty.visible()
                binding.allDownloadRv.gon()
            } else {
                list.sortByDescending {
                    it.time
                }
                adapter!!.replaceAll(list)
            }
        }
    }

    fun getDateAndTimeFromFile(filePath: String): String {
        val file = File(filePath)
        val lastModified = file.lastModified()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(lastModified)
        return dateFormat.format(date)
    }

}