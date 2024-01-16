package com.vidbrowserdownloader.freebrowserdownload.Model.VideoModel

import java.io.File
import java.util.Date

data class UrlModel(
    val url: File,
    val duration: String,
    val width: Int,
    val height: Int,
    val formattedSize: String,
    val lastModifiedDate: Date
)