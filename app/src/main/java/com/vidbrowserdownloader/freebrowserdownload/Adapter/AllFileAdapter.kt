package com.vidbrowserdownloader.freebrowserdownload.Adapter

import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadProgressModel
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.constant.mulaList
import com.vidbrowserdownloader.freebrowserdownload.constant.shareFile
import com.vidbrowserdownloader.freebrowserdownload.databinding.DownloadItemBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.io.File
import kotlin.collections.ArrayList

class AllFileAdapter(
    val activity: Activity,
    var list: ArrayList<DownloadModel>,
    val onClick: (Int) -> Unit,
    val onVideo: (String,String) -> Unit,
) : RecyclerView.Adapter<AllFileAdapter.AllVH>() {

    private lateinit var names2: String
    private lateinit var names1: String
    lateinit var list1: ArrayList<DownloadProgressModel>

    class AllVH(val binding: DownloadItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AllVH(
        DownloadItemBinding.inflate(LayoutInflater.from(parent.context))
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: AllVH, position: Int) {
        holder.binding.apply {
            val item = list[position].url

            if (item.name.endsWith("png") || item.name.endsWith("jpg")){
                btnPlay.gon()
            } else {
                btnPlay.visible()
            }
            txtsize.visible()
            txtdate1.gon()

            btnDelete.setOnClickListener {
                onClick(position)
            }
            btnShare.setOnClickListener {
                shareFile(activity,item.path)
            }
            mulaList.observeForever { it ->
                update(it.filter {
                    it.progress != 100
                } as java.util.ArrayList<DownloadProgressModel>)
            }

            val inputString =item.name
            names1 = inputString.take(12)

            mulaList.observeForever { it ->
                it.forEach {
                    val newstring =it.name
                    names2 = newstring.take(12)

                    if (names1 == names2) {
                        if (it.progress == 100) {
                            root.isEnabled = true
                            txtitemname.setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.black
                                )
                            )
                            setSize(holder.binding, item)
                            txtsize.visible()
                            txtdate1.gon()
                        } else {
                            root.isEnabled = false
                            txtitemname.setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.gray_dark
                                )
                            )
                            txtsize.gon()
                            txtdate1.visible()
                        }
                    }

                }
            }

            imgitem.loadImg(item)
            txtitemname.text = item.name
            txtdate1.text = list[position].time
            setSize(holder.binding, item)

            "file path = = = =${item.name}   ${item.name}".log()
            root.setOnClickListener {
                onVideo(item.path,item.name)
            }
        }
    }

    //    fun getBaseFileName(inputString: String): String {
////        val lastDotIndex = inputString.lastIndexOf(".")
////        val secondLastDotIndex = inputString.lastIndexOf(".", lastDotIndex - 1)
//
////        val baseFileName = if (secondLastDotIndex >= 0) {
////            inputString.substring(0, secondLastDotIndex)
////        } else {
////            inputString.substring(0, lastDotIndex)
////        }
//
//        return
//    }
    fun setSize(binding: DownloadItemBinding, item: File) {
        val size = item.length()
        val inKB = size.toDouble() / 1024
        val inMB = size.toDouble() / (1024 * 1024)
        val mb = String.format("%.2f", inMB)
        if (inKB <= 1024) {
            binding.txtsize.text = "$mb KB"
        } else {
            binding.txtsize.text = "${inMB.toInt()} MB"
        }
    }

    override fun getItemCount() = list.size

    fun update(it: ArrayList<DownloadProgressModel>) {
        list1 = it
    }
    fun replaceAll(list: ArrayList<DownloadModel>) {
        this.list = list
        notifyDataSetChanged()
    }

}