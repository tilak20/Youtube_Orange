package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityHowToDownloadBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.sdk.AdsManager

class HowToDownload : AppCompatActivity() {
    lateinit var binding: ActivityHowToDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowToDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClick()

    }

    private fun initClick() {
        binding.apply {
            btnGotIt.setOnClickListener {
//                startActivity(Intent(this@HowToDownload, MainActivity2::class.java))
                AdsManager.getInstance().showOnbackPressAdExtra(this@HowToDownload) {
                    finish()
                }
            }
        }
    }
}