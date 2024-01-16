package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.constant.shareUs
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityDrawerBinding
import com.vidbrowserdownloader.freebrowserdownload.dialog.Rate_UsDialog
import com.vidbrowserdownloader.freebrowserdownload.utils.delayInMillis
import com.vidbrowserdownloader.freebrowserdownload.utils.toast
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.google.ads.sdk.AdsManager

class DrawerActivity : AppCompatActivity() {

    lateinit var binding: ActivityDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClick()
    }

    private fun initClick() {
        binding.apply {
            rlshare.setOnClickListener {
                shareUs(this@DrawerActivity)
            }
            rlrate.setOnClickListener {
                Rate_UsDialog(this@DrawerActivity){
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=${this@DrawerActivity.packageName}")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=${this@DrawerActivity.packageName}")
                            )
                        )
                    }
                    finish()
                }
            }
            rlprivacy.setOnClickListener {

            }
        }
    }

    override fun onBackPressed() {
        AdsManager.getInstance().showOnbackPressAdExtra(this@DrawerActivity) {
            finish()
        }
    }

}