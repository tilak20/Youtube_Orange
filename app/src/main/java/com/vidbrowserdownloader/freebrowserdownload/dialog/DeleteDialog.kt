package com.vidbrowserdownloader.freebrowserdownload.dialog

import com.vidbrowserdownloader.freebrowserdownload.databinding.DeleteDialogBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.gon
import com.vidbrowserdownloader.freebrowserdownload.utils.visible
import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager


class DeleteDialog constructor(activity: Activity, onClick: (String) -> Unit) {
    var dialog: Dialog = Dialog(activity)
    var binding: DeleteDialogBinding = DeleteDialogBinding.inflate(LayoutInflater.from(activity))

    init {
        dialog.setContentView(binding.root)



        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnYes.setOnClickListener {
            onClick.invoke("Yes")
            dialog.dismiss()
        }
        binding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}