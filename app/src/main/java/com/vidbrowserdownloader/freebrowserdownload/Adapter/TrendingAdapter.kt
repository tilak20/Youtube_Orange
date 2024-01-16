package com.vidbrowserdownloader.freebrowserdownload.Adapter

import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.TrendingActivity
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.VideoActivity
import com.vidbrowserdownloader.freebrowserdownload.databinding.YoutubeVideoItemBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.loadImg
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vidbrowserdownloader.freebrowserdownload.Model.ItemsItem

class TrendingAdapter(
    var activity: TrendingActivity,
    private var list: ArrayList<ItemsItem>
) : RecyclerView.Adapter<TrendingAdapter.TrendingVH>() {

    class TrendingVH(var binding: YoutubeVideoItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrendingVH(
        YoutubeVideoItemBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TrendingVH, position: Int) {
        holder.binding.apply {

            val item = list[position]

            txtTitle.isSelected = true
            txtTitle.text = item.videoRenderer!!.title!!.runs?.get(0)!!.text
            txtTime.text = item.videoRenderer.publishedTimeText!!.simpleText
            txtViews.text = item.videoRenderer.shortViewCountText!!.simpleText
            txtDuration.text = item.videoRenderer.lengthText!!.simpleText

            var thumbnail = ""

//            Glide.with(activity).load(item.videoRenderer.thumbnail!!.thumbnails!![0]!!.url!!).into(imageFilterView)
            thumbnail = item.videoRenderer.thumbnail!!.thumbnails!![item.videoRenderer.thumbnail.thumbnails!!.lastIndex]!!.url.toString()
            imageFilterView.loadImg(thumbnail)

            val videoId = item.videoRenderer.videoId

            root.setOnClickListener {
                activity.startActivity(Intent(activity, VideoActivity::class.java).putExtra("videoId", videoId).putExtra("videoTime", item.videoRenderer.lengthText.simpleText))
            }

        }
    }
}