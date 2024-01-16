package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivitySplashScreenBinding
import com.vidbrowserdownloader.freebrowserdownload.dialog.DialogNoNet
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import com.vidbrowserdownloader.freebrowserdownload.utils.tos
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.multidex.BuildConfig
import com.allsocialvideostatusdownloader.instavideo.Dialog.UpdateAppDialog
import com.google.ads.sdk.Ad_inti
import com.google.ads.sdk.getDataListner
import com.google.gson.JsonArray

@SuppressLint("CustomSplashScreen")
class SplashScreen : Ad_inti() {

    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        ADSinit(this, BuildConfig.VERSION_CODE, "freebrowserdownload", object : getDataListner {
            @Override
            override fun onSuccess() {
                startActivity(Intent(this@SplashScreen, StartActivity::class.java))
                finish()
            }

            override fun onUpdate(p0: String?) {
                UpdateAppDialog(this@SplashScreen)
            }

            override fun onReload(p0: String?) {
                "on Reload".log()
            }

            override fun onNotSafe() {
                DialogNoNet(this@SplashScreen) {
                    "Internet is Not Safe".tos(this@SplashScreen)
                    finish()
                }
            }

            override fun onGetExtradata(jsonArray: JsonArray?) {
            }
        })

    }

}