package com.vidbrowserdownloader.freebrowserdownload.Adapter

import com.vidbrowserdownloader.freebrowserdownload.Model.IconModel
import com.vidbrowserdownloader.freebrowserdownload.databinding.IconsLayoutBinding
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlaybackIconsAdapter(
    var iconModelArrayList: ArrayList<IconModel>,
    var videoPlayActivity: Activity,
    var onClick: (Int) -> Unit
) : RecyclerView.Adapter<PlaybackIconsAdapter.ViewData>() {

    class ViewData(var binding: IconsLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewData(
        IconsLayoutBinding.inflate(LayoutInflater.from(videoPlayActivity))
    )

    override fun getItemCount() = iconModelArrayList.size

    override fun onBindViewHolder(holder: ViewData, position: Int) {
        holder.binding.apply {
            playbackIcon.setImageResource(iconModelArrayList[position].imageView)
            iconTitle.text = iconModelArrayList[position].iconTitle

            root.setOnClickListener {

                onClick.invoke(position)

            }
        }
    }
}