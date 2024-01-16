package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.TrendingActivity
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityStartBinding
import com.vidbrowserdownloader.freebrowserdownload.dialog.Rate_UsDialog
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ads.sdk.AdsManager

class StartActivity : AppCompatActivity() {

    lateinit var binding: ActivityStartBinding
    private val WRITE_PERMISSIONS = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            imgstart.setOnClickListener {
                if (arePermissionsGranted()) {
                    AdsManager.getInstance().showOnbackPressAdExtra(this@StartActivity) {
                        startActivity(Intent(this@StartActivity, TrendingActivity::class.java))
                    }
                } else {
                    requestPermission()
                }
            }

            imgPrivacy.setOnClickListener {
            }

            imgRate.setOnClickListener {
                Rate_UsDialog(this@StartActivity) {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=${this@StartActivity.packageName}")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=${this@StartActivity.packageName}")
                            )
                        )
                    }
                    finish()
                }
            }

            imgShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=${this@StartActivity.packageName}"
                )
                startActivity(Intent.createChooser(intent, "Share with"))
            }
        }
    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.WRITE_SETTINGS
                ), WRITE_PERMISSIONS
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_SETTINGS
                ), WRITE_PERMISSIONS
            )
        }
    }

    private fun arePermissionsGranted(): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val permissions: Array<String> = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this, permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        } else {
            val permissions: Array<String> = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO
            )
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this, permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                ("Permission Denied...").log()
            }
        }
    }
}