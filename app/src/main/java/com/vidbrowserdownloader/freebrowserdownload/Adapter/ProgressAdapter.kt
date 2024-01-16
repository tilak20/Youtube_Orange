package com.vidbrowserdownloader.freebrowserdownload.Adapter

import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadProgressModel
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.databinding.PendingdownloadBinding
import com.vidbrowserdownloader.freebrowserdownload.databinding.ProgresssDownloadItemBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.load
import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class ProgressAdapter(
    var activity: FragmentActivity,
    var list: ArrayList<DownloadProgressModel>,
    val onClick: (String, Int, View) -> Unit
) : RecyclerView.Adapter<ProgressAdapter.ProgressHolder>() {

    class ProgressHolder(val binding: PendingdownloadBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProgressHolder(
        PendingdownloadBinding.inflate(LayoutInflater.from(activity), parent, false)
    )

    override fun getItemCount() = list.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ProgressHolder, position: Int) {
        val item = list[position]

        holder.binding.apply {

            progressbar.visibility =
                if (item.progress.toInt() == 100) View.INVISIBLE else View.VISIBLE
            tkaprogress.visibility =
                if (item.progress.toInt() == 100) View.INVISIBLE else View.VISIBLE

            progressbar.progress = item.progress.toInt()

            txtCurrentsize.text = item.currentSize

            tkaprogress.text = "${item.progress}%"
            materialTextView2.text = item.name
            txtquality.text = item.quality
            imageFilterView2.load(item.src)

        }
    }

    private fun getCurrentDateFormatted(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun update(it: ArrayList<DownloadProgressModel>) {
        list = it
        notifyDataSetChanged()
    }
}

