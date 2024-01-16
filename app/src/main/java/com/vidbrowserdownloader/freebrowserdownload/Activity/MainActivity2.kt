package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityMain2Binding
import com.vidbrowserdownloader.freebrowserdownload.main.MainActivity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.sdk.AdsManager
import java.util.Locale

class MainActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initClick()
    }

    private fun initClick() {

        binding.apply {
            btnHtd.setOnClickListener {
                startActivity(Intent(this@MainActivity2, HowToDownload::class.java))
            }
            btnDownload.setOnClickListener {
                startActivity(Intent(this@MainActivity2, Download_Activity::class.java))
            }
            btnDownloading.setOnClickListener {
                startActivity(Intent(this@MainActivity2, DownloadingActivity::class.java))
            }
            btnMyDownload.setOnClickListener {
                startActivity(Intent(this@MainActivity2, Download_Activity::class.java))
            }
            imgback.setOnClickListener { finish() }

            tvSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startIntent(
                        "Search",
                        tvSearch.text.toString().trim().lowercase(Locale.getDefault())
                    )
                }
                true
            }
            rlgoogle.setOnClickListener {
                startIntent("Google", "https://www.google.com/")
            }
            rlinstagram.setOnClickListener {
                startIntent("Instagram", "http://www.instagram.com/accounts/login")
            }
            rlfacebook.setOnClickListener {
                startIntent("Facebook", "https://www.facebook.com/login")
            }
            rltwitter.setOnClickListener {
                startIntent("Twitter", "https://twitter.com/home")
            }
            rltiktok.setOnClickListener {
                startIntent("TikTok", "https://www.tiktok.com")
            }
            rlvimeo.setOnClickListener {
                startIntent("Vimeo", "https://vimeo.com/watch")
            }
            rlwhatsapp.setOnClickListener {
                startIntent("WhatsApp", "https://web.whatsapp.com/")
            }
            rl9gag.setOnClickListener {
                startIntent("9gag", "https://9gag.com/")
            }
            rl900px.setOnClickListener {
                startIntent("900px", "https://500px.com/")
            }
            rlpinterest.setOnClickListener {
                startIntent("pinterest", "https://in.pinterest.com/")
            }
            rldailymotion.setOnClickListener {
                startIntent("dailymotion", "https://www.dailymotion.com/")
            }
        }
    }

    fun startIntent(from: String, key: String) {
        startActivity(
            Intent(this, MainActivity::class.java).putExtra("from", from).putExtra("key", key)
        )
    }

    override fun onBackPressed() {
        AdsManager.getInstance().showOnbackPressAdExtra(this@MainActivity2) {
            finish()
        }
    }
}