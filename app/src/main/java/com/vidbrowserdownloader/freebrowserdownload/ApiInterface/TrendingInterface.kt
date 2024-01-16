package com.vidbrowserdownloader.freebrowserdownload.ApiInterface

import com.vidbrowserdownloader.freebrowserdownload.Model.ContinuationModel.ContinuationBodyModel
import com.playerhdgallery.uplayerpro.model.ContinuationModel.ContinuationListModel
import com.vidbrowserdownloader.freebrowserdownload.Model.SearchModel.SearchModel
import com.playerhdgallery.uplayerpro.model.SearchModel.SearchModellist
import com.vidbrowserdownloader.freebrowserdownload.Model.SuggestVideoModel.SuggestVideo
import com.vidbrowserdownloader.freebrowserdownload.Model.SuggestVideoModel.SuggestVideoBody
import com.vidbrowserdownloader.freebrowserdownload.Model.TrendingListBodyModel
import com.vidbrowserdownloader.freebrowserdownload.Model.TrendingListModel
import com.vidbrowserdownloader.freebrowserdownload.Model.VideoModel.VideoBodyModel
import com.playerhdgallery.uplayerpro.model.VideoModel.VideoModel
import retrofit2.Call
import retrofit2.http.*

interface TrendingInterface {
    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/browse")
    fun trendingData(
        @Body bodyModel: TrendingListBodyModel,
        @Query("prettyPrint") prettyPrint: String = "false"
    ): Call<TrendingListModel>

    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/player")
    fun videoData(
        @Query("prettyPrint") prettyPrint: String = "false",
        @Query("t") t: String = "pUrtRgKL_fzG",
        @Query("id") id: String = "VBcs8DZxBGc",
        @Body bodyModel: VideoBodyModel
    ): Call<VideoModel>

    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/next")
    fun suggestVideo(
        @Query("key") key: String = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8",
        @Query("prettyPrint") prettyPrint: String = "false",
        @Body body: SuggestVideoBody
    ): Call<SuggestVideo>

    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/search")
    fun searchData(
        @Body bodyModel: SearchModel,
        @Query("key") key: String = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8",
        @Query("prettyPrint") prettyPrint: String = "false"
    ): Call<SearchModellist>


    @Headers("Content-Type: application/json")
    @POST("youtubei/v1/search")
    fun continuationData(
        @Body bodyModel: ContinuationBodyModel,
        @Query("key") key: String = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8",
        @Query("prettyPrint") prettyPrint: String = "false"
    ): Call<ContinuationListModel>

    @GET("complete/search")
    fun suggationData(
        @Query("client") client: String="youtube",
        @Query("ds") ds: String="yt",
        @Query("gl") gl: String="US",
        @Query("q") query: String="bi",
        @Query("xhr") xhr: String ="t"
    ): Call<List<Any>>
}