package com.vidbrowserdownloader.freebrowserdownload.utils

import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadProgressModel
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.constant.*
import com.vidbrowserdownloader.freebrowserdownload.databinding.DetectvideofileBinding
import android.annotation.SuppressLint
import android.app.Activity
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SetTextI18n", "NotifyDataSetChanged")
class DetectVideoDialog(var activity: Activity, res: AhaModel) {

    lateinit var formattedDate: String
    var progressByte: Long = 0
    lateinit var currentSize: String
    lateinit var status: Status
    lateinit var quality: String
    var dowid: Int = 0
    var progressUpdateTimer: Timer? = null
    var progressPer: Long = 0
    var position: Int = 0
    var dialog: BottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetdialogTheme)
    var binding: DetectvideofileBinding =
        DetectvideofileBinding.inflate(LayoutInflater.from(activity))

    companion object {
        val downloadingList = arrayListOf<DownloadProgressModel>()
        lateinit var videoUrl: String
    }


    init {
        dialog.setContentView(binding.root)

        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            .setBackgroundResource(
                android.R.color.transparent
            )
        videoUrl = res.dataList!![0].mediaUrlList!![0]

        binding.apply {

            tital.text = "${System.currentTimeMillis() / 1000}.mp4"
            image.load(res.dataList!![0].thumbnailUrl.ifEmpty { videoUrl })

            val datalist: ArrayList<DataList> = arrayListOf()

            res.dataList!!.filter { it.mediaUrlList?.size == 1 }.forEach {
                it.isSelected = false
                datalist.add(it)
            }

            if (datalist.isNotEmpty()) {
                videoUrl = datalist[0].mediaUrlList?.get(0) ?: ""
                datalist[0].isSelected = true
            }

            onBackground {
                try {
                    if (videoUrl.isNotEmpty()) {
                        val retriever = MediaMetadataRetriever()
                        videoUrl.log()
                        retriever.setDataSource(videoUrl)
                        val durationStr =
                            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        val duration = durationStr!!.toLong()
                        ("D: $duration").log()

                        val hours = TimeUnit.MILLISECONDS.toHours(duration)
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                        val durationtv = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                        if (datalist.isNotEmpty() && datalist.any { it.filesize == 0L }) {
                            callLength(datalist, 0)
                        }

                        activity.runOnUiThread {
                            videoduration.text = durationtv
                            rvvideo.adapter?.notifyDataSetChanged()
                        }
                    }
                } catch (e: Exception) {
                }
            }

            rvvideo.adapter = VideoQulityAdapter(activity, datalist, onClick = { it, pos ->
                datalist.forEach { it.isSelected = false }
                it.isSelected = true
                position = pos
                rvvideo.adapter!!.notifyDataSetChanged()
                videoUrl = it.mediaUrlList?.get(0) ?: ""
                quality = it.quality
                ("video : $videoUrl").log()
            }, quality = {
                quality = it
                ("Quality == $it").log()
            })


            downloadbtn.setOnClickListener {
                PRDownloader.initialize(activity)

                val filePath =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/V_Downloader/").path
                val fileName = "Y_${System.currentTimeMillis()}.mp4"

                val downloadId = PRDownloader.download(videoUrl, filePath, fileName).build()
                    .setOnStartOrResumeListener { "on start or resume".log() }
                    .setOnPauseListener { "on Pause".log() }
                    .setOnCancelListener { "on cancel".log() }.setOnProgressListener { progress ->
                        progressByte = progress.currentBytes
                        progressPer = progress.currentBytes * 100 / progress.totalBytes
                        CoroutineScope(Dispatchers.Default).launch {
                            currentSize = getFileSize(progress.currentBytes)

                            ("Download Progress: $currentSize ").log()

                            if (listofdownloadmodel.size != 0) {
                                listofdownloadmodel.find { it.name == fileName }!!.progress = progressPer.toInt()
                                listofdownloadmodel.find { it.name == fileName }!!.currentSize =
                                    currentSize

                                mulaList.postValue(listofdownloadmodel)
                            }
                            delay(2000)
                        }

                    }.start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            status = PRDownloader.getStatus(dowid)

                            listofdownloadmodel.find { it.name == fileName }!!.status = status

                            val downloadDirectory =
                                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/V_Downloader/").path).listFiles()

                            val mostRecentFile =
                                downloadDirectory?.maxByOrNull { it.lastModified() }

                            listofVideo.add(mostRecentFile!!)
                            mulaVideoList.postValue(listofVideo)

                            ("last modified == ${mostRecentFile.name}").log()
                            ("last modified == ${mostRecentFile.toURI()}").log()

                            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/V_Browser/").path).listFiles()
                                ?.forEach {

                                    if (it.exists()) {
                                        val lastModifiedTimestamp = it.lastModified()
                                        val sdf = SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
                                        )
                                        formattedDate = sdf.format(Date(lastModifiedTimestamp))
                                        ("File was modified : $formattedDate").log()
                                    } else {
                                        ("File not found").log()
                                    }

                                    list.add(DownloadModel(it, formattedDate))
                                    scanMediaFile(activity, it.path)
                                }
                            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/V_Downloader/").path).listFiles()
                                ?.forEach {
                                    if (it.name.endsWith(".mp4")) {
                                        if (it.exists()) {
                                            val lastModifiedTimestamp = it.lastModified()
                                            val sdf = SimpleDateFormat(
                                                "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
                                            )
                                            formattedDate = sdf.format(Date(lastModifiedTimestamp))
                                            ("File was last modified on: $formattedDate").log()
                                        } else {
                                            ("File not found").log()
                                        }

                                        list.add(DownloadModel(it, formattedDate))
                                        latestVideo.postValue(list)
                                        scanMediaFile(activity, it.path)
                                    }
                                }
                            "on complete".log()
                        }

                        override fun onError(error: Error?) {
                            "error : $error".log()
                        }
                    })

                status = PRDownloader.getStatus(dowid)
                currentSize = getFileSize(progressByte)

                listofdownloadmodel.add(
                    DownloadProgressModel(
                        filePath,
                        progressPer.toInt(),
                        downloadId.toString(),
                        fileName,
                        videoUrl,
                        getFileSize(datalist[position].filesize),
                        quality,
                        status,
                        currentSize
                    )
                )
                dialog.dismiss()

            }
//            downloadbtn.setOnClickListener {
//                val id = UUID.randomUUID().toString()
//
//                videoUrl.let {
//                    val workData = Data.Builder()
//                        .putString(DownloadWorker.urlKey1, Companion.videoUrl)
//                        .putString(DownloadWorker.tasid, id)
//                        .putString(
//                            DownloadWorker.name,
//                            "V_${System.currentTimeMillis() / 1000}.mp4"
//                        )
//                        .putString(DownloadWorker.size, getFileSize(datalist[position].filesize))
//                        .build()
//
//                    val workRequest: WorkRequest =
//                        OneTimeWorkRequest.Builder(DownloadWorker::class.java)
//                            .addTag(id)
//                            .setInputData(workData)
//                            .build()
//
//                    WorkManager.getInstance(activity).enqueueUniqueWork(
//                        id, ExistingWorkPolicy.KEEP,
//                        (workRequest as OneTimeWorkRequest)
//                    )
//
//                    dialog.dismiss()
//                }
//            }
        }
        dialog.show()
    }

    private fun callLength(datalist: ArrayList<DataList>, i: Int) {
        ("Size: $i").log()
        if (datalist.size != i && !activity.isDestroyed) {
            val url by lazy { URL(datalist[i].mediaUrlList!![0]) }
            val urlConnection by lazy { url.openConnection() as HttpURLConnection }
            urlConnection.requestMethod = "HEAD"
            urlConnection.connect()
            val contentLength: Int = urlConnection.contentLength
            datalist[i].filesize = contentLength.toLong()
            urlConnection.disconnect()
            callLength(datalist, i + 1)
        }
    }
}