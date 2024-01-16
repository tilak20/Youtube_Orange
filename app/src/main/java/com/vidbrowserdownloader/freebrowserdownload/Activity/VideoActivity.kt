package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityVideoBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.loadImg
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.sdk.AdsManager

class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdsManager.getInstance().showNativeSmall(binding.nativeads, R.layout.ad_unified)

        val item = intent.getStringExtra("videoPath").toString()
        val videoTitle = intent.getStringExtra("videoTitle").toString()

        binding.apply {
            imgback.setOnClickListener {
                AdsManager.getInstance().showOnbackPressAdExtra(this@VideoActivity) {
                    finish()
                }
            }

            if (item.endsWith("png") || item.endsWith("jpg")) {
                imgView.loadImg(item)
                "image == ${item}".log()
                imgName.text = videoTitle
            }
        }
    }

    override fun onBackPressed() {
        AdsManager.getInstance().showOnbackPressAdExtra(this@VideoActivity) {
            finish()
        }
    }
}