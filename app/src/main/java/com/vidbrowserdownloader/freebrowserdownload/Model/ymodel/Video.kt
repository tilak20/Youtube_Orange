package com.vidbrowserdownloader.freebrowserdownload.Model.ymodel

data class Video(
    val allowRatings: Boolean,
    val author: String,
    val channelId: String,
    val isCrawlable: Boolean,
    val isLiveContent: Boolean,
    val isOwnerViewing: Boolean,
    val isPrivate: Boolean,
    val isUnpluggedCorpus: Boolean,
    val lengthSeconds: String,
    val shortDescription: String,
    val thumbnail: Thumbnail,
    val title: String,
    val videoId: String,
    val viewCount: String
)