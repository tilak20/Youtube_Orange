package com.vidbrowserdownloader.freebrowserdownload.Model.ymodel

data class Format(
    val approxDurationMs: String,
    val audioChannels: Int,
    val audioQuality: String,
    var isSelected: Boolean = false,
    val audioSampleRate: String,
    val averageBitrate: Int,
    val bitrate: Int,
    val contentLength: String? = null,
    val fps: Int,
    val height: Int,
    val itag: Int,
    val lastModified: String,
    val mimeType: String,
    val projectionType: String,
    val quality: String,
    val qualityLabel: String,
    val url: String,
    val width: Int
)