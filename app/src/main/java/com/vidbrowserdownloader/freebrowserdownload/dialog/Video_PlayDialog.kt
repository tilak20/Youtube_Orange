package com.vidbrowserdownloader.freebrowserdownload.dialog

import com.vidbrowserdownloader.freebrowserdownload.databinding.VideoPlaydialogBinding
import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager

class Video_PlayDialog constructor(activity: Activity, onClick:(String)->Unit) {

    var dialog : Dialog = Dialog(activity)
    var binding: VideoPlaydialogBinding = VideoPlaydialogBinding.inflate(LayoutInflater.from(activity))

    init {
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnplay.setOnClickListener {
            onClick.invoke("download")
            dialog.dismiss()
        }
        dialog.show()
    }
}