package com.vidbrowserdownloader.freebrowserdownload.dialog

import com.vidbrowserdownloader.freebrowserdownload.Activity.Download_Activity
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.constant.listofVideo
import com.vidbrowserdownloader.freebrowserdownload.constant.mulaVideoList
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityVideoLatestDialogBinding
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File

class VideoLatestDialog(
    var activity: Activity,
    var files: ArrayList<File>,
    var from: String,
) {
    var dialog: BottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetdialogTheme)

    var binding: ActivityVideoLatestDialogBinding =
        ActivityVideoLatestDialogBinding.inflate(LayoutInflater.from(activity))

    init {
        dialog.setContentView(binding.root)

        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            .setBackgroundResource(
                android.R.color.transparent
            )

        binding.apply {
            Glide.with(activity).load(files[0].absolutePath).into(imageVideo)
            txtlinkSingle.text = files[0].name

        }

        binding.root.setOnClickListener {

            listofVideo.clear()
            mulaVideoList.postValue(listofVideo)

            if (from == "Browser") {
                activity.startActivity(Intent(activity, Download_Activity::class.java))
            } else {
                activity.startActivity(Intent(activity, Download_Activity::class.java))
                activity.finish()
            }

            dialog.dismiss()
        }

        dialog.setOnCancelListener {
            listofVideo.clear()
            mulaVideoList.postValue(listofVideo)
        }
        dialog.show()
    }
}