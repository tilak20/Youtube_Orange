package com.vidbrowserdownloader.freebrowserdownload.Adapter

import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.databinding.DownloadItemBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.loadImg
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import com.vidbrowserdownloader.freebrowserdownload.utils.visible
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VideoAdapter(
    val activity: Activity,
    var list: ArrayList<DownloadModel>,
    val onClick: (String, Int, View) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoVH>() {

    class VideoVH(val binding: DownloadItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VideoVH(
        DownloadItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
    )

    override fun onBindViewHolder(holder: VideoVH, position: Int) {
        holder.binding.apply {
            val item = list[position].url
            btnPlay.visible()

            imgitem.loadImg(item)
            txtitemname.text = item.name
            val size = item.length()
            val inKB = size.toDouble() / 1024
            val inMB = size.toDouble() / (1024 * 1024)
            val mb = String.format("%.2f", inMB)
            txtdate1.text = list[position].time
            "KB: $inKB || MB: $inMB".log()
            if (inKB <= 1024) {
                txtsize.text = "${inKB.toInt()} KB"
            } else {
                txtsize.text = "$mb MB"
            }
        }
    }

    override fun getItemCount() = list.size

    fun replaceVideo(list: ArrayList<DownloadModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun getDateAndTimeFromFile(filePath: String): String {
        val file = File(filePath)
        val lastModified = file.lastModified()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(lastModified)
        return dateFormat.format(date)
    }
}