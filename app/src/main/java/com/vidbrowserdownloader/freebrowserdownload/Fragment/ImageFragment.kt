package com.vidbrowserdownloader.freebrowserdownload.Fragment

import com.vidbrowserdownloader.freebrowserdownload.Activity.VideoActivity
import com.vidbrowserdownloader.freebrowserdownload.Adapter.AllFileAdapter
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.constant.scanMediaFile
import com.vidbrowserdownloader.freebrowserdownload.constant.shareFile
import com.vidbrowserdownloader.freebrowserdownload.databinding.FragmentImageBinding
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

class ImageFragment : Fragment() {
    lateinit var binding: FragmentImageBinding
    val list = ArrayList<DownloadModel>()
    var adapter: AllFileAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)

        list.clear()
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/X_Browser/").path).listFiles()
            ?.forEach {
                list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                scanMediaFile(requireContext(),it.path)
            }


        if (list.isEmpty()) {
            binding.imgEmpty.visible()
        } else {
            binding.imgEmpty.gon()
//            list.reverse()
            list.sortByDescending {
                it.time
            }
            adapter = AllFileAdapter(requireActivity(), list, onClick = { position->
                DeleteDialog(this.requireActivity()) {
                    deletefile(list[position].url.path, position)
                }
            }) {path,name->
                "name1 == = $name".log()
                startActivity(
                    Intent(requireActivity(), VideoActivity::class.java).putExtra("videoPath", path).putExtra("videoTitle", name)
                )
            }
            binding.imageRv.adapter = adapter
        }
        binding.imageRv.adapter?.notifyDataSetChanged()

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
    fun getDateAndTimeFromFile(filePath: String): String {
        val file = File(filePath)
        val lastModified = file.lastModified()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(lastModified)
        return dateFormat.format(date)
    }

    fun notifyData() {
        if (adapter != null) {
            list.clear()
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/X_Browser/").path).listFiles()
                ?.forEach {
                    list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                }

            if (list.isEmpty()) {
                binding.imgEmpty.visible()
                binding.imageRv.gon()
            } else {
//                list.reverse()
                list.sortByDescending { it.time }
                adapter!!.replaceAll(list)
            }
        }
    }

}