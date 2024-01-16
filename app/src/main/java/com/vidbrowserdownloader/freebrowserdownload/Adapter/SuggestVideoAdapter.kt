package com.vidbrowserdownloader.freebrowserdownload.Adapter

import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.VideoActivity
import com.vidbrowserdownloader.freebrowserdownload.databinding.TrendingItemBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.loadImg
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vidbrowserdownloader.freebrowserdownload.Model.SuggestVideoModel.ResultsItem

class SuggestVideoAdapter(
    var activity: Activity, private var list: ArrayList<ResultsItem>, var onClick: () -> Unit
) : RecyclerView.Adapter<SuggestVideoAdapter.TrendingVH>() {

    private lateinit var videoId: String

    class TrendingVH(var binding: TrendingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrendingVH(
        TrendingItemBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TrendingVH, position: Int) {
        holder.binding.apply {

            val item = list[position]

//            list.size.log()

            if (item.compactVideoRenderer != null) {
                txtTitle.isSelected = true
                txtTitle.text = item.compactVideoRenderer.title!!.simpleText
//                txtTime.text = item.compactVideoRenderer.publishedTimeText!!.simpleText
                txtTime.text = if (item.compactVideoRenderer.publishedTimeText == null) {
                    "   "
                } else {
                    item.compactVideoRenderer.publishedTimeText.simpleText
                }
                txtViews.text =
                    if (item.compactVideoRenderer.shortViewCountText!!.simpleText == null) {
                        item.compactVideoRenderer.shortViewCountText.runs!![0]!!.text
                    } else {
                        item.compactVideoRenderer.shortViewCountText.simpleText
                    }
//                txtDuration.text = item.compactVideoRenderer.lengthText!!.simpleText
                txtDuration.text = if (item.compactVideoRenderer.lengthText == null) {
                    ""
                } else {
                    item.compactVideoRenderer.lengthText.simpleText
                }
                imageFilterView.loadImg(item.compactVideoRenderer.thumbnail!!.thumbnails!![0]!!.url!!)

                videoId = item.compactVideoRenderer.videoId.toString()
            }

            root.setOnClickListener {
                onClick()
                activity.startActivity(
                    Intent(
                        activity, VideoActivity::class.java
                    ).putExtra("videoId", item.compactVideoRenderer!!.videoId).putExtra("videoTime",item.compactVideoRenderer.lengthText!!.simpleText)
                )
            }
        }
    }
}