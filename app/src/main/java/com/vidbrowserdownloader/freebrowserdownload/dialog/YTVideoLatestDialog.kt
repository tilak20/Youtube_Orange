package com.vidbrowserdownloader.freebrowserdownload.dialog

import com.vidbrowserdownloader.freebrowserdownload.Activity.Download_Activity
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.Application.Companion.listofVideo
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.Application.Companion.mulaVideoList
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.databinding.YtactivityVideoLatestDialogBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.loadImg
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.util.ArrayList

class YTVideoLatestDialog(
    var activity: Activity,
    var files: ArrayList<File>
) {
    var dialog: BottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetdialogTheme)

    var binding: YtactivityVideoLatestDialogBinding =
        YtactivityVideoLatestDialogBinding.inflate(LayoutInflater.from(activity))

    init {
        dialog.setContentView(binding.root)

        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            .setBackgroundResource(
                android.R.color.transparent
            )

        binding.apply {
//            Glide.with(activity).load(files[0].absolutePath).into(imageVideo)
            imageVideo.loadImg(files[0].absolutePath)

            txtlinkSingle.text = files[0].name

        }

        binding.root.setOnClickListener {
            listofVideo.clear()
            mulaVideoList.postValue(listofVideo)

            activity.startActivity(Intent(activity, Download_Activity::class.java))
            activity.finish()


            dialog.dismiss()
        }

        dialog.setOnCancelListener {
            listofVideo.clear()
            mulaVideoList.postValue(listofVideo)
        }

        dialog.show()

    }
}