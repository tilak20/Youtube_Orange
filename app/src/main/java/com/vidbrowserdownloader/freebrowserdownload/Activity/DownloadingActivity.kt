package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.Adapter.ProgressAdapter
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadProgressModel
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.constant.list
import com.vidbrowserdownloader.freebrowserdownload.constant.listofdownloadmodel
import com.vidbrowserdownloader.freebrowserdownload.constant.mulaList
import com.vidbrowserdownloader.freebrowserdownload.constant.mulaVideoList
import com.vidbrowserdownloader.freebrowserdownload.constant.shareFile
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityDownloadingBinding
import com.vidbrowserdownloader.freebrowserdownload.dialog.DeleteDialog
import com.vidbrowserdownloader.freebrowserdownload.dialog.VideoLatestDialog
import com.vidbrowserdownloader.freebrowserdownload.utils.gon
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import com.vidbrowserdownloader.freebrowserdownload.utils.tos
import com.vidbrowserdownloader.freebrowserdownload.utils.visible
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.sdk.AdsManager
import java.io.File

class DownloadingActivity : AppCompatActivity() {

    lateinit var binding: ActivityDownloadingBinding
    lateinit var adapter: ProgressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            back.setOnClickListener {
                AdsManager.getInstance().showOnbackPressAdExtra(this@DownloadingActivity) {
                    finish()
                }
            }

            mulaVideoList.observeForever {
                if (it.size != 0) {
                    if (!isFinishing) {
                        VideoLatestDialog(this@DownloadingActivity, it, "Downloading")
                    }
                }
            }

            adapter = ProgressAdapter(this@DownloadingActivity,
                listofdownloadmodel,
                onClick = { urlpath, position, view ->
                    showPopUp(urlpath, position, view)
                })

            mulaList.observeForever { it ->

                if (it.none { it.progress.toInt() != 100 }) {
                    imgEmpty.visible()
                    downloadingRv.gon()
                } else {
                    imgEmpty.gon()
                    downloadingRv.visible()
                }

                adapter.update(it.filter {
                    it.progress.toInt() != 100
                } as ArrayList<DownloadProgressModel>)
            }

            downloadingRv.adapter = adapter

            downloadingRv.adapter?.notifyDataSetChanged()
        }
    }

    fun showPopUp(path: String, position: Int, v: View) {
        val popUp = PopupMenu(this, v)
        popUp.inflate(R.menu.download_menu)

        popUp.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btnShare -> {
                    shareFile(this, path)
                    true
                }

                R.id.btnDelete -> {
                    DeleteDialog(this@DownloadingActivity) {
                        deleteImage(path)
                        list.removeAt(position)
                        adapter.notifyDataSetChanged()
                        if (list.size == 0) {
                            binding.imgEmpty.visible()
                        }
                    }
                    true
                }

                else -> false
            }
        }
        popUp.show()
    }

    fun deleteImage(filepath: String) {
        try {
            val file = File(filepath)

            if (file.exists()) {
                file.delete()
                "deleted Successfully".tos(this)
            }
        } catch (e: Exception) {
            "${e.message}".log()
        }
    }
}

