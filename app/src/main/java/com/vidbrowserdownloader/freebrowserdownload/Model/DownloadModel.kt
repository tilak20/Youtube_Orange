package com.vidbrowserdownloader.freebrowserdownload.Model

import androidx.annotation.Keep
import com.downloader.Status
import java.io.File
@Keep
data class DownloadModel(val url: File, val time: String)

data class DownloadProgressModel(
    var path: String = "No",
    var progress: Int = 1,
    var id: String = "",
    var name: String,
    var src: String = "",
    val size: String="",
    val quality: String ="",
    var status: Status = Status.RUNNING,
    var currentSize: String =""
)

data class Downloading(
    var path: String = "No",
    var progress: Long = 1,
    var id: Int = 0,
    var name: String,
    var src: String = "",
//    val size: String,
    val quality: String,
    var status: Status,
    var currentSize: String
)

data class ProgressData(
    var progress: Long = 1,
    var name: String,
)

@Keep
data class DataList(
    var name: String = "",
    var sourceUrl: String = "",
    var quality: String = "SD",
    var fromUrl: String = "",
    var thumbnailUrl: String = "",
    var headerReferer: String = "",
    var headerUserAgent: String = "",
    var duration: Int? = 0,
    var mediaUrlList: ArrayList<String>?,
    var videoId: String? = "",
    var filesize: Long = 0L,
    var isSelected: Boolean = false
)
