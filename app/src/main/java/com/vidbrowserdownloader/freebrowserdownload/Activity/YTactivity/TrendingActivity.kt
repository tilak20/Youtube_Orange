package com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity

import com.vidbrowserdownloader.freebrowserdownload.Activity.Download_Activity
import com.vidbrowserdownloader.freebrowserdownload.Activity.DrawerActivity
import com.vidbrowserdownloader.freebrowserdownload.Activity.MainActivity2
import com.vidbrowserdownloader.freebrowserdownload.Model.Client
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityTrendingBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.gon
import com.vidbrowserdownloader.freebrowserdownload.utils.visible
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vidbrowserdownloader.freebrowserdownload.Adapter.TrendingAdapter
import com.vidbrowserdownloader.freebrowserdownload.ApiClient.YoutubeClient.Companion.getRetrofit
import com.vidbrowserdownloader.freebrowserdownload.ApiInterface.TrendingInterface
import com.vidbrowserdownloader.freebrowserdownload.Model.ItemsItem
import com.vidbrowserdownloader.freebrowserdownload.Model.Request
import com.vidbrowserdownloader.freebrowserdownload.Model.TrendingListBodyModel
import com.vidbrowserdownloader.freebrowserdownload.Model.TrendingListModel
import com.vidbrowserdownloader.freebrowserdownload.Model.User
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import com.google.ads.sdk.AdsManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrendingBinding
    var list: ArrayList<ItemsItem> = arrayListOf()
    private val WRITE_PERMISSIONS = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrendingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progress.visible()
        apiCalling()
        initClick()
    }

    private fun initClick() {
        binding.apply {

            lnsearch.setOnClickListener {
                startActivity(Intent(this@TrendingActivity, Search_Activity::class.java))
                finish()
            }
            btnBrowse.setOnClickListener {
                startActivity(Intent(this@TrendingActivity, MainActivity2::class.java))
            }
            btnDownload.setOnClickListener { startActivity(Intent(this@TrendingActivity, Download_Activity::class.java)) }
            moresettings.setOnClickListener {
                startActivity(Intent(this@TrendingActivity, DrawerActivity::class.java))
            }
            btnYt.setOnClickListener {
                startActivity(Intent(this@TrendingActivity, MainActivity2::class.java))
            }
        }
    }

    private fun apiCalling() {
        val bodyModel = TrendingListBodyModel(
            browseId = "FEtrending", context = com.vidbrowserdownloader.freebrowserdownload.Model.Context(
                request = Request(internalExperimentFlags = emptyList(), useSsl = true),
                client = Client(
                    hl = "en-GB",
                    gl = "IN",
                    clientName = "WEB",
                    originalUrl = "https://www.youtube.com",
                    clientVersion = "2.20230803.01.00",
                    platform = "DESKTOP"
                ),
                user = User(lockedSafetyMode = false)
            ), params = "4gIOGgxtb3N0X3BvcHVsYXI%3D"
        )

        val apiInterface = getRetrofit().create(TrendingInterface::class.java)
        apiInterface.trendingData(bodyModel).enqueue(object : Callback<TrendingListModel> {

            override fun onResponse(
                call: Call<TrendingListModel>, response: Response<TrendingListModel>
            ) {
                if (response.isSuccessful) {
                    "is successful".log()
                    binding.progress.gon()
                    list =
                        response.body()!!.contents!!.twoColumnBrowseResultsRenderer!!.tabs!![0]!!.tabRenderer!!.content!!.sectionListRenderer!!.contents!![0]!!.itemSectionRenderer!!.contents!![0]!!.shelfRenderer!!.content!!.expandedShelfContentsRenderer!!.items as ArrayList<ItemsItem>

                    binding.trendingRv.adapter = TrendingAdapter(this@TrendingActivity, list)

                    if (arePermissionsGranted()) {
                        "is granted".log()
                    } else {
                        requestPermission()
                    }

                    if (response.body()!!.equals(null)) {
                        "response null".log()
                    } else {
                        "Response: ${response.body()!!.contents!!.twoColumnBrowseResultsRenderer!!.tabs!![0]!!.tabRenderer!!.content!!.sectionListRenderer!!.contents!![0]!!.itemSectionRenderer!!.contents!![0]!!.shelfRenderer!!.content!!.expandedShelfContentsRenderer!!.items!!.size}".log()
                    }
//                    "Response: ${response.body()}".log()
                }
            }

            override fun onFailure(call: Call<TrendingListModel>, t: Throwable) {
                "error: ${t.message}".log()
            }
        })
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
                ("Permission Granted...").log()
//                saveVideoToDownloads()
            } else {
                ("Permission Denied...").log()
            }
        }
    }


    override fun onBackPressed() {
        AdsManager.getInstance().showOnbackPressAdExtra(this@TrendingActivity) {
            finish()
        }

    }

}