/*
 * Copyright 2014 A.C.R. Development
 */
@file:JvmName("Constants")

package com.vidbrowserdownloader.freebrowserdownload.constant

import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadModel
import com.vidbrowserdownloader.freebrowserdownload.Model.DownloadProgressModel
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import com.vidbrowserdownloader.freebrowserdownload.utils.onBackground
import com.vidbrowserdownloader.freebrowserdownload.utils.tos
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.anthonycr.grant.BuildConfig
import java.io.*
import java.text.DecimalFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow



// Hardcoded user agents
const val WINDOWS_DESKTOP_USER_AGENT_PREFIX = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
const val LINUX_DESKTOP_USER_AGENT =
    "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:85.0) Gecko/20100101 Firefox/85.0"
const val MACOS_DESKTOP_USER_AGENT =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.2 Safari/605.1.15"
const val ANDROID_MOBILE_USER_AGENT_PREFIX =
    "Mozilla/5.0 (Linux; Android 11; Pixel 5 Build/RQ1A.210205.004; wv)"
const val IOS_MOBILE_USER_AGENT =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 14_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1"

// URL Schemes
const val HTTP = "http://"
const val HTTPS = "https://"
const val FILE = "file://"
const val FOLDER = "folder://"

object Schemes {
    const val Fulguris = "fulguris"
    const val About = "about"
}

const val YTAPI = "YTAPI"





val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
} else PendingIntent.FLAG_UPDATE_CURRENT


var start: Boolean = true
var time: String = ""
var sizeoffile: String = ""
var sharedPreferences: SharedPreferences? = null
var editor: SharedPreferences.Editor? = null



fun putString(key: String, value: String) {
    editor!!.putString(key, value).apply()
}

fun getString(key: String) = sharedPreferences!!.getString(key, "")

fun getBoolean(key: String?): Boolean? {
    return sharedPreferences?.getBoolean(key, false)
}

fun putBoolean(key: String?, value: Boolean) {
    editor?.putBoolean(key, value)?.apply()
}

fun clearAll() {
    editor!!.clear().apply()
}

fun <T> putModel(key: String?, value: T) {
    sharedPreferences?.edit()?.putString(key, value.toString())?.apply()
}


fun restartApp(activity: Activity) {
    val intent: Intent? =
        activity.baseContext.packageManager.getLaunchIntentForPackage(activity.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    activity.startActivity(intent)
    activity.finish()
}

fun View.zoomInZoomOut() {
    val animationSet = AnimationSet(true)

    val zoomIn = ScaleAnimation(
        1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    zoomIn.duration = 1000
    zoomIn.repeatCount = Animation.INFINITE
    zoomIn.repeatMode = Animation.REVERSE

    val zoomOut = ScaleAnimation(
        1.2f, 1.0f, 1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    zoomOut.duration = 1000
    zoomOut.repeatCount = Animation.INFINITE
    zoomOut.repeatMode = Animation.REVERSE

    animationSet.addAnimation(zoomIn)
    animationSet.addAnimation(zoomOut)

    this.startAnimation(animationSet)
}

fun scanMediaFile(context: Context, path: String?) {
    path?.let {
        val paths = arrayOf(path)
        MediaScannerConnection.scanFile(context, paths, null, null)
    }
}


fun viewShakeAnimation(context: Context, v: View) {
    val animation = AnimationUtils.loadAnimation(context, R.anim.left_right_2)
    v.startAnimation(animation)
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
//                animation.setStartOffset(100);
            v.startAnimation(animation)
        }

        override fun onAnimationRepeat(animation: Animation) {}
    })
}



@RequiresApi(Build.VERSION_CODES.M)
fun hasInternetConnect(activity: Activity): Boolean {
    var isWifiConnected = false
    var isMobileConnected = false
    val cm =
        activity.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (cm.defaultProxy != null) return false

    for (ni in cm.allNetworkInfo) {
        if (ni.typeName.equals("WIFI", ignoreCase = true)) if (ni.isConnected) isWifiConnected =
            true
        if (ni.typeName.equals(
                "MOBILE", ignoreCase = true
            )
        ) if (ni.isConnected) isMobileConnected = true
    }

    return isWifiConnected || isMobileConnected
}


private fun getServiceIntent(context: Context, service: Class<out Service?>): Intent {
    return Intent(context, service)
}

fun copyClip(activity: Activity, text: String) {
    val clipboard: ClipboardManager =
        activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    ("Text Copy successfully").tos(activity)
}

fun hideSoftKeyBord(context: Activity) {
    val inputMethodManager: InputMethodManager? =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    if (inputMethodManager != null && context.currentFocus != null) {
        inputMethodManager.hideSoftInputFromWindow(context.currentFocus!!.windowToken, 0)
    }
}

fun openUri(ctx: Context, uri: String) =
    ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

fun rateUs(activity: Activity) {
    try {
        val marketUri = Uri.parse("market://details?id=" + activity.packageName)
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
        activity.startActivity(marketIntent)
    } catch (e: Exception) {
        "${e.message}".log()
        val marketUri =
            Uri.parse("https://play.google.com/store/apps/details?id=" + activity.packageName)
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
        activity.startActivity(marketIntent)
    }
}

fun shareUs(activity: Activity) {
    val i = Intent(Intent.ACTION_SEND).putExtra(
        Intent.EXTRA_TEXT,
        "I'm using ${activity.getString(R.string.app_name)}! Get the app for free at http://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    )
    i.type = "text/plain"
    activity.startActivity(Intent.createChooser(i, "Share"))
}

const val SAVE_FOLDER_NAME = "/Download/X_Browser/"

fun makeFolder() {
    onBackground {
        val file_root = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/X_Browser"
        )
        if (!file_root.exists()) {
            "Make Dir".log()
            file_root.mkdir()
        }
    }
}

fun getStringSizeLengthFile(j: Long): String {
    return try {
        val decimalFormat = DecimalFormat("0.00")
        val f = j.toFloat()
        if (f < 1048576.0f) {
            decimalFormat.format((f / 1024.0f).toDouble()) + " Kb"
        } else if (f < 1.07374182E9f) {
            decimalFormat.format((f / 1048576.0f).toDouble()) + " Mb"
        } else if (f >= 1.09951163E12f) {
            ""
        } else {
            decimalFormat.format((f / 1.07374182E9f).toDouble()) + " Gb"
        }
    } catch (e: java.lang.Exception) {
        "NaN"
    }
}

var myNotificationChannel = ""

var mulaList: MutableLiveData<ArrayList<DownloadProgressModel>> = MutableLiveData()
var listofdownloadmodel: ArrayList<DownloadProgressModel> = arrayListOf()
var mulaVideoList: MutableLiveData<ArrayList<File>> = MutableLiveData()
var listofVideo: ArrayList<File> = arrayListOf()
val list = ArrayList<DownloadModel>()
var latestVideo: MutableLiveData<ArrayList<DownloadModel>> = MutableLiveData()

fun inject(activity: Activity, web: WebView) {
    try {
        val inputStream: InputStream = activity.assets.open("app.js")
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        val encoded = android.util.Base64.encodeToString(buffer, android.util.Base64.NO_WRAP)
        web.loadUrl(
            "javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()"
        )
    } catch (e: java.lang.Exception) {
    }
}
fun createFilenameWithJapneseAndOthers(title: String): String? {
    return try {
        val cleanFileName = title.replace("[\\\\><\"|*?'%:#/]".toRegex(), " ")
        var fileName = cleanFileName.trim { it <= ' ' }.replace(" +".toRegex(), " ")
        fileName = fileName.replace("[\\\\/:*?\"<>|]".toRegex(), "")
        fileName = fileName.replace("\n", " ")
        if (fileName.length > 127) {
            fileName = fileName.substring(0, 127)
        }
        fileName
    } catch (e: Exception) {
        e.printStackTrace()
        "_"
    }
}


fun shareFile(activity: Activity, item: String) {
    ("Item: $item").log()
    val share = Intent(Intent.ACTION_SEND)
    share.type = "${if (item.endsWith(".png") || item.endsWith(".jpg")) "image" else "video"}/*"
    share.putExtra(
        Intent.EXTRA_STREAM, FileProvider.getUriForFile(
            activity,
            activity.packageName + ".fileprovider",
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


@SuppressLint("SimpleDateFormat")
fun getOutputMediaFile(node: String): File? {
    val mediaStorageDir = File(
        Environment.getExternalStorageDirectory().absolutePath + SAVE_FOLDER_NAME
    )
    if (!mediaStorageDir.exists()) {
        if (!mediaStorageDir.mkdirs()) {
            return null
        }
    }
    // Create a media file name
    val mediaFile: File
    val mImageName = "${"x_" + (System.currentTimeMillis() / 1000)}.png"
    mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
    return mediaFile
}

fun storeImage(image: Bitmap, node: String = "") {
    val pictureFile: File = getOutputMediaFile(node)!!
    try {
        val fos = FileOutputStream(pictureFile)
        image.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
    } catch (e: FileNotFoundException) {
        ("Error File Not Found: " + e.message).log()
    } catch (e: IOException) {
        ("Error accessing file: " + e.message).log()
    }
}

fun getFileSize(size: Long): String {
    if (size <= 0) return "0"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}

fun getjs(activity: Activity, str: String?): String {
    val open: InputStream
    try {
        open = activity.assets.open(str.toString())
        val bArr = ByteArray(open.available())
        open.read(bArr)
        val str2 = String(bArr)
        return try {
            open.close()
            str2
//            val final = str2.replaceFirstChar { "" }
//            if (canAdd)
//                decodeB64("$final==")
//            else
//                decodeB64(final)
        } catch (unused: IOException) {
            str2
//            decodeB64(str2)
        }
    } catch (unused2: IOException) {
        return ""
    } catch (th: Throwable) {
        th.addSuppressed(th)
    }
    return ""
}

object Hosts {
    const val Home = "home"
    const val Start = "start"
    const val Incognito = "incognito"
    const val Bookmarks = "bookmarks"
    const val History = "history"
    const val Downloads = "downloads"
    const val Noop = "noop"
    const val Blank = "blank"
}

object Uris {
    const val FulgurisHome = "${Schemes.Fulguris}://${Hosts.Home}"
    const val FulgurisStart = "${Schemes.Fulguris}://${Hosts.Start}"
    const val FulgurisIncognito = "${Schemes.Fulguris}://${Hosts.Incognito}"
    const val FulgurisBookmarks = "${Schemes.Fulguris}://${Hosts.Bookmarks}"
    const val FulgurisDownloads = "${Schemes.Fulguris}://${Hosts.Downloads}"
    const val FulgurisHistory = "${Schemes.Fulguris}://${Hosts.History}"
    const val FulgurisNoop = "${Schemes.Fulguris}://${Hosts.Noop}"

    // Custom local page schemes
    const val AboutHome = "${Schemes.About}:${Hosts.Home}"
    const val AboutIncognito = "${Schemes.About}:${Hosts.Incognito}"
    const val AboutBlank = "${Schemes.About}:${Hosts.Blank}"
    const val AboutBookmarks = "${Schemes.About}:${Hosts.Bookmarks}"
    const val AboutHistory = "${Schemes.About}:${Hosts.History}"
}

object PrefKeys {
    const val HideStatusBar = "pref_key_hide_status_bar"
    const val HideToolBar = "pref_key_hide_tool_bar"
    const val ShowToolBarWhenScrollUp = "pref_key_show_tool_bar_on_scroll_up"
    const val ShowToolBarOnPageTop = "pref_key_show_tool_bar_on_page_top"
    const val DesktopWidth = "pref_key_desktop_width_float"
    const val PullToRefresh = "pref_key_pull_to_refresh"
    const val TabBarVertical = "pref_key_tab_bar_vertical"
    const val TabBarInDrawer = "pref_key_tab_bar_in_drawer"
    const val ToolbarsBottom = "pref_key_toolbars_bottom"
}


const val UTF8 = "UTF-8"

// Default text encoding we will use
const val DEFAULT_ENCODING = UTF8

// Allowable text encodings for the WebView
@JvmField
val TEXT_ENCODINGS = arrayOf(
    UTF8,
    "Big5",
    "Big5-HKSCS",
    "CESU-8",
    "EUC-JP",
    "EUC-KR",
    "GB18030",
    "GB2312",
    "GBK",
    "IBM-Thai",
    "IBM00858",
    "IBM01140",
    "IBM01141",
    "IBM01142",
    "IBM01143",
    "IBM01144",
    "IBM01145",
    "IBM01146",
    "IBM01147",
    "IBM01148",
    "IBM01149",
    "IBM037",
    "IBM1026",
    "IBM1047",
    "IBM273",
    "IBM277",
    "IBM278",
    "IBM280",
    "IBM284",
    "IBM285",
    "IBM290",
    "IBM297",
    "IBM420",
    "IBM424",
    "IBM437",
    "IBM500",
    "IBM775",
    "IBM850",
    "IBM852",
    "IBM855",
    "IBM857",
    "IBM860",
    "IBM861",
    "IBM862",
    "IBM863",
    "IBM864",
    "IBM865",
    "IBM866",
    "IBM868",
    "IBM869",
    "IBM870",
    "IBM871",
    "IBM918",
    "ISO-2022-CN",
    "ISO-2022-JP",
    "ISO-2022-JP-2",
    "ISO-2022-KR",
    "ISO-8859-1",
    "ISO-8859-13",
    "ISO-8859-15",
    "ISO-8859-2",
    "ISO-8859-3",
    "ISO-8859-4",
    "ISO-8859-5",
    "ISO-8859-6",
    "ISO-8859-7",
    "ISO-8859-8",
    "ISO-8859-9",
    "JIS_X0201",
    "JIS_X0212-1990",
    "KOI8-R",
    "KOI8-U",
    "Shift_JIS",
    "TIS-620",
    "US-ASCII",
    "UTF-16",
    "UTF-16BE",
    "UTF-16LE",
    "UTF-32",
    "UTF-32BE",
    "UTF-32LE",
    "windows-1250",
    "windows-1251",
    "windows-1252",
    "windows-1253",
    "windows-1254",
    "windows-1255",
    "windows-1256",
    "windows-1257",
    "windows-1258",
    "windows-31j",
    "x-Big5-HKSCS-2001",
    "x-Big5-Solaris",
    "x-COMPOUND_TEXT",
    "x-euc-jp-linux",
    "x-EUC-TW",
    "x-eucJP-Open",
    "x-IBM1006",
    "x-IBM1025",
    "x-IBM1046",
    "x-IBM1097",
    "x-IBM1098",
    "x-IBM1112",
    "x-IBM1122",
    "x-IBM1123",
    "x-IBM1124",
    "x-IBM1166",
    "x-IBM1364",
    "x-IBM1381",
    "x-IBM1383",
    "x-IBM300",
    "x-IBM33722",
    "x-IBM737",
    "x-IBM833",
    "x-IBM834",
    "x-IBM856",
    "x-IBM874",
    "x-IBM875",
    "x-IBM921",
    "x-IBM922",
    "x-IBM930",
    "x-IBM933",
    "x-IBM935",
    "x-IBM937",
    "x-IBM939",
    "x-IBM942",
    "x-IBM942C",
    "x-IBM943",
    "x-IBM943C",
    "x-IBM948",
    "x-IBM949",
    "x-IBM949C",
    "x-IBM950",
    "x-IBM964",
    "x-IBM970",
    "x-ISCII91",
    "x-ISO-2022-CN-CNS",
    "x-ISO-2022-CN-GB",
    "x-iso-8859-11",
    "x-JIS0208",
    "x-JISAutoDetect",
    "x-Johab",
    "x-MacArabic",
    "x-MacCentralEurope",
    "x-MacCroatian",
    "x-MacCyrillic",
    "x-MacDingbat",
    "x-MacGreek",
    "x-MacHebrew",
    "x-MacIceland",
    "x-MacRoman",
    "x-MacRomania",
    "x-MacSymbol",
    "x-MacThai",
    "x-MacTurkish",
    "x-MacUkraine",
    "x-MS932_0213",
    "x-MS950-HKSCS",
    "x-MS950-HKSCS-XP",
    "x-mswin-936",
    "x-PCK",
    "x-SJIS_0213",
    "x-UTF-16LE-BOM",
    "X-UTF-32BE-BOM",
    "X-UTF-32LE-BOM",
    "x-windows-50220",
    "x-windows-50221",
    "x-windows-874",
    "x-windows-949",
    "x-windows-950",
    "x-windows-iso2022jp"
)

const val INTENT_ORIGIN = "URL_INTENT_ORIGIN"
