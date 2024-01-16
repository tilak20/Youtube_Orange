package com.vidbrowserdownloader.freebrowserdownload.Adapter

import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.VideoActivity
import com.vidbrowserdownloader.freebrowserdownload.databinding.TrendingItemBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.loadImg
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playerhdgallery.uplayerpro.model.SearchModel.ContentsItem

class SearchAdapter(
    val activity: Activity,
    val list: ArrayList<ContentsItem>
) :
    RecyclerView.Adapter<SearchAdapter.SearchView>() {

    class SearchView(val binding: TrendingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchView(
        TrendingItemBinding.inflate(
            LayoutInflater.from(activity)
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: SearchView, position: Int) {
        holder.binding.apply {

            if (list[position].videoRenderer == null) {
            } else {
                val item = list[position].videoRenderer!!



                txtTitle.isSelected = true
                txtTitle.text = item.title!!.runs?.get(0)!!.text

                txtTime.text =
                    if (item.publishedTimeText == null) "" else item.publishedTimeText.simpleText

                txtViews.text =
                    if (item.shortViewCountText == null) "" else item.shortViewCountText.simpleText
                txtDuration.text =
                    if (item.lengthText == null) "" else item.lengthText.simpleText

                imageFilterView.loadImg(item.thumbnail!!.thumbnails!![0]!!.url!!)

                val videoId = item.videoId
                root.setOnClickListener {
                    activity.startActivity(
                        Intent(
                            activity, VideoActivity::class.java
                        ).putExtra("videoId", videoId).putExtra("videoTime",item.lengthText!!.simpleText)
                    )
                }

            }
        }
    }
}