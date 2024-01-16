package com.vidbrowserdownloader.freebrowserdownload.Model.ymodel

data class AdaptiveFormat(
    val approxDurationMs: String,
    val audioChannels: Int,
    val audioQuality: String,
    val audioSampleRate: String,
    val averageBitrate: Int,
    val bitrate: Int,
    val colorInfo: ColorInfo,
    val contentLength: String,
    val fps: Int,
    val height: Int,
    val highReplication: Boolean,
    val indexRange: IndexRange,
    val initRange: InitRange,
    val itag: Int,
    val lastModified: String,
    val loudnessDb: Double,
    val mimeType: String,
    val projectionType: String,
    val quality: String,
    val qualityLabel: String,
    val url: String,
    val width: Int
)