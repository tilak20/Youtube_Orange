package com.vidbrowserdownloader.freebrowserdownload.dialog

import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.databinding.RateUsdialogBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import com.vidbrowserdownloader.freebrowserdownload.utils.toast
import android.app.Activity
import android.view.LayoutInflater
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog

class Rate_UsDialog constructor(activity: Activity, onClick:(String)->Unit) {

    var dialog : BottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetdialogTheme )
    var binding: RateUsdialogBinding = RateUsdialogBinding.inflate(LayoutInflater.from(activity))
    init {
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)

        binding.ratingBar.setOnClickListener {
            "hello".log()
            dialog.dismiss()
        }
        binding.imgclose.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnrate.setOnClickListener {
            onClick.invoke("rate")
            dialog.dismiss()
        }
        dialog.show()
    }
}