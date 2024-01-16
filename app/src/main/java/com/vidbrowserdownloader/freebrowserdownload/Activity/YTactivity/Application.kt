package com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity

import com.vidbrowserdownloader.freebrowserdownload.Activity.ApplicationGraph
import com.vidbrowserdownloader.freebrowserdownload.BuildConfig
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.Model.Downloading
import com.vidbrowserdownloader.freebrowserdownload.main.MainActivity
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.media.MediaScannerConnection
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.ads.sdk.AppManager
import com.google.gson.Gson
import io.fabric.sdk.android.Fabric
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.log10
import kotlin.math.pow

class Application : AppManager() {


    lateinit var gson: Gson

    @SuppressLint("WrongConstant")
    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences(SHARED_KEY, MODE_PRIVATE)
        editor = preferences.edit()

        setupApplicationGraph()

        // Debuggable WebView
        if (BuildConfig.DEBUG) {
            enableDebuggableWebView()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, "Now Playing Song", NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "This is important channel for showing song"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun setupApplicationGraph() {
        ApplicationGraph.init(this)
    }

    fun enableDebuggableWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
    }

    companion object {
        var SHARED_KEY = "MusicShared"
        lateinit var preferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        val CHANNEL_ID = "channel1"
        var mulaVideoList: MutableLiveData<ArrayList<File>> = MutableLiveData()
        var listofVideo: ArrayList<File> = arrayListOf()
        val list = ArrayList<DownloadModel>()

        fun putString(key: String, value: String) {
            editor.putString(key, value).apply()
        }

        fun getString(key: String): String {
            return preferences.getString(key, "").toString()
        }

        fun putBoolean(key: String, value: Boolean) {
            editor.putBoolean(key, value).apply()
        }

        fun getBoolean(key: String): Boolean {
            return preferences.getBoolean(key, false)
        }

        fun putInt(key: String, value: Int) {
            editor.putInt(key, value).apply()
        }

        fun getInt(key: String, i: Int): Int {
            return preferences.getInt(key, i)
        }

        fun Any?.log(): Unit = exc { Log.wtf("FATZ", "$this") }
        fun <T> T.tos(ctx: Context) = Toast.makeText(ctx, "$this", Toast.LENGTH_SHORT).show()

        inline fun exc(block: () -> Unit) {
            try {
                block()
            } catch (e: Exception) {
                e.message
            }
        }

        fun getFileSize(size: Long): String {
            if (size <= 0) return "0"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
        }

        @SuppressLint("SimpleDateFormat")
        fun convertUnixTimestampToDate(unixTimestamp: Long): String {
            val date = Date(unixTimestamp * 1000L) // Convert to milliseconds
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            sdf.timeZone = TimeZone.getDefault()
            return sdf.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun convertUnixTimestampToMonth(unixTimestamp: Long): String {
            val date = Date(unixTimestamp * 1000L) // Convert to milliseconds
            val sdf = SimpleDateFormat("MM")
            sdf.timeZone = TimeZone.getDefault()
            return sdf.format(date)
        }

        fun main(videoDateadded: String): String {
            val formattedDate = convertUnixTimestampToDate(videoDateadded.toLong())
            return formattedDate
        }

        fun Month(videoDateadded: String): String {
            val formattedMonth = convertUnixTimestampToMonth(videoDateadded.toLong())
            "month $formattedMonth".log()
            return formattedMonth
        }

        fun shareFile(activity: Activity, item: String) {
            ("Item: $item").log()
            val share = Intent(Intent.ACTION_SEND)
            share.type =
                "${if (item.endsWith(".png") || item.endsWith(".jpg")) "image" else "video"}/*"
            share.putExtra(
                Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                    activity,
                    activity.packageName + ".provider",
                    File(item),
                    item.lowercase(Locale.getDefault())
                )
            )
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivity(
                Intent.createChooser(
                    share,
                    "Share ${if (item.endsWith(".png") || item.endsWith(".jpg")) "Image" else "Video"}"
                )
            )
        }
    }
}