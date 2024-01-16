package com.vidbrowserdownloader.freebrowserdownload.dialog

import com.vidbrowserdownloader.freebrowserdownload.databinding.DialogNoNetBinding
import android.app.Activity
import android.app.Dialog
import android.view.WindowManager

class DialogNoNet(var activity: Activity, var onDismiss: () -> Unit) {

    var bind = DialogNoNetBinding.inflate(activity.layoutInflater)
    var dialog = Dialog(activity)

    init {
        dialog.setContentView(bind.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        dialog.setOnDismissListener {
            onDismiss.invoke()
        }
//
//        bind.root.setOnClickListener {
//        }
        dialog.show()
    }

    fun dismiss() = dialog.dismiss()
}