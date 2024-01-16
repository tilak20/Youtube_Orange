package com.allsocialvideostatusdownloader.instavideo.Dialog

import com.vidbrowserdownloader.freebrowserdownload.databinding.UpdateDialogBinding
import android.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog

class UpdateAppDialog(var activity: Activity) {
    var dialog: BottomSheetDialog = BottomSheetDialog(activity)
    var binding: UpdateDialogBinding = UpdateDialogBinding.inflate(LayoutInflater.from(activity))

    init {
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.window!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet).setBackgroundResource(R.color.transparent)
        binding.updatebtn.setOnClickListener {
            dialog.dismiss()
            rateUs()
        }
        dialog.show()
    }

    private fun rateUs() {
        try {
            val marketUri = Uri.parse("market://details?id=" + activity.packageName)
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            activity.startActivity(marketIntent)
        } catch (e: Exception) {
            val marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + activity.packageName)
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            activity.startActivity(marketIntent)
        }
    }
}