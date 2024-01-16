package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.Adapter.DownloadTabAdapter
import com.vidbrowserdownloader.freebrowserdownload.Fragment.AllDownloadsFragment
import com.vidbrowserdownloader.freebrowserdownload.Fragment.ImageFragment
import com.vidbrowserdownloader.freebrowserdownload.Fragment.VideoFragment
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.constant.YTAPI
import com.vidbrowserdownloader.freebrowserdownload.constant.scanMediaFile
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityDownloadBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.ads.sdk.AdsManager
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Download_Activity : AppCompatActivity() {

    lateinit var binding: ActivityDownloadBinding

    lateinit var allFragment: AllDownloadsFragment
    private lateinit var imageFragment: ImageFragment
    private lateinit var videoFragment: VideoFragment
    lateinit var downloadTabAdapter: DownloadTabAdapter
    val list = ArrayList<DownloadModel>()

    private val tabArray = arrayOf(
        "  All  ", "  Video  ", "  Images  "
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.clear()
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/X_Browser/").path).listFiles()
            ?.forEach {
                list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                scanMediaFile(this@Download_Activity, it.path)
            }

        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/V_Downloader/").path).listFiles()
            ?.forEach {
                list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                scanMediaFile(this@Download_Activity, it.path)
            }
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/$YTAPI/").path).listFiles()
            ?.forEach {
                if (it.name.startsWith("YTD_")) {
                    list.add(DownloadModel(it, getDateAndTimeFromFile(it.path)))
                    scanMediaFile(this, it.path)
                }
            }

        list.forEach {
            "file  = = = $it".log()
        }
        downloadTabAdapter = DownloadTabAdapter(this)
        binding.apply {
            back.setOnClickListener {
                AdsManager.getInstance().showOnbackPressAdExtra(this@Download_Activity) {
                    finish()
                }
            }

            allFragment = AllDownloadsFragment()
            imageFragment = ImageFragment()
            videoFragment = VideoFragment()

            downloadTabAdapter.addFragment(allFragment, "All")
            downloadTabAdapter.addFragment(videoFragment, "Video")
            downloadTabAdapter.addFragment(imageFragment, "Images")
            viewPager.adapter = downloadTabAdapter
            viewPager.offscreenPageLimit = 1
            viewPager.currentItem = 0
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabArray[position]
                when (position) {
                    0 -> {
                        allFragment.notifyData()
                    }

                    1 -> {
                        videoFragment.notifyData()
                    }

                    else -> {
                        imageFragment.notifyData()
                    }
                }
            }.attach()
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            allFragment.notifyData()
                        }

                        1 -> {
                            videoFragment.notifyData()
                        }

                        else -> {
                            imageFragment.notifyData()
                        }
                    }
                }
            })
        }
    }

    fun getDateAndTimeFromFile(filePath: String): String {
        val file = File(filePath)
        val lastModified = file.lastModified()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(lastModified)
        return dateFormat.format(date)
    }


}