package com.vidbrowserdownloader.freebrowserdownload.utils
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.constant.getFileSize
import com.vidbrowserdownloader.freebrowserdownload.databinding.RvVideoitemBinding
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class VideoQulityAdapter(
    var activity: Activity,
    var list: ArrayList<DataList>,
    var onClick: (DataList,Int) -> Unit,
    var quality: (String) -> Unit
) : RecyclerView.Adapter<VideoQulityAdapter.VH>() {
    class VH(var binding: RvVideoitemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(RvVideoitemBinding.inflate(LayoutInflater.from(activity)))

    override fun getItemCount() = list.size

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position] as DataList

        holder.binding.apply {
            qulity.text = item.quality
            if (getFileSize(item.filesize) == "0") {

            } else {
                progrssbar.visibility = View.INVISIBLE
                firstsize.text = getFileSize(item.filesize)
            }
            selected.foreground = (if (item.isSelected) {

                quality.invoke(item.quality)

                activity.getDrawable(R.drawable.tv_line_yellow)
            } else {
                activity.getDrawable(R.drawable.tv_line_shape)
            })

            root.setOnClickListener {
                onClick.invoke(item, position)
            }
        }
    }
}