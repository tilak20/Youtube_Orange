package com.vidbrowserdownloader.freebrowserdownload.Fragment

import com.vidbrowserdownloader.freebrowserdownload.Activity.VideoActivity
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.VideoPlayAct
import com.vidbrowserdownloader.freebrowserdownload.Adapter.AllFileAdapter
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.constant.YTAPI
import com.vidbrowserdownloader.freebrowserdownload.constant.scanMediaFile
import com.vidbrowserdownloader.freebrowserdownload.constant.shareFile
import com.vidbrowserdownloader.freebrowserdownload.databinding.FragmentVideoBinding
import com.vidbrowserdownloader.freebrowserdownload.dialog.DeleteDialog
import com.vidbrowserdownloader.freebrowserdownload.utils.gon
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import com.vidbrowserdownloader.freebrowserdownload.utils.tos
import com.vidbrowserdownloader.freebrowserdownload.utils.visible
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VideoFragment : Fragment() {
    lateinit var binding: FragmentVideoBinding
    val list = ArrayList<DownloadModel>()
    var adapter: AllFileAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        list.clear()
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
//            list.reverse()
            list.sortByDescending {0
                it.time
            }
            adapter = AllFileAdapter(requireActivity(), list, onClick = {position->
                DeleteDialog(this.requireActivity()) {
                    deletefile(list[position].url.path, position)
                }
            }) {path,name ->
                startActivity(
                    Intent(requireActivity(), VideoPlayAct::class.java).putExtra("videoPath", path).putExtra("videoTitle", name)
                )
            }
            binding.videoRv.adapter = adapter
        }
        binding.videoRv.adapter?.notifyDataSetChanged()

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
                binding.videoRv.gon()
            } else {
                list.sortByDescending {
                    it.time
                }
//                list.reverse()
//                list.sortBy {
//                    it.url
//                }
                adapter!!.replaceAll(list)
            }
        }
    }

    private fun showPopUp(path: String, position: Int, v: View) {
        val popUp = PopupMenu(requireContext(), v)
        popUp.inflate(R.menu.download_menu)

        popUp.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btnShare -> {
                    shareFile(requireActivity(), path)
                    true
                }
                R.id.btnDelete -> {
                    DeleteDialog(requireActivity()) {
                        deleteImage(path)
                        list.removeAt(position)
                        adapter!!.notifyDataSetChanged()
                        if (list.size == 0) {
                            binding.imgEmpty.visible()
                        }
                    }
                    true
                }
                else -> false
            }
        }
        popUp.show()
    }

    private fun deleteImage(filepath: String) {
        try {
            val file = File(filepath)

            if (file.exists()) {
                file.delete()
                "deleted Successfully".tos(requireContext())
            }
        } catch (e: Exception) {

            "${e.message}".log()
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