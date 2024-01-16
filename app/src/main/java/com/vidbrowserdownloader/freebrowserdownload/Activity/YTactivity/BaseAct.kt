package com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity

import com.vidbrowserdownloader.freebrowserdownload.R
import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Patterns
import android.view.*
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.Application.Companion.log
import androidx.annotation.RequiresApi
import java.io.*
import java.net.URL
import java.nio.charset.StandardCharsets
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.log10
import kotlin.math.pow

abstract class BaseAct<T : ViewBinding> : AppCompatActivity() {
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getActivityBinding(layoutInflater)
        setContentView(binding.root)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        initUI(savedInstanceState)
    }

    abstract fun getActivityBinding(inflater: LayoutInflater): T

    abstract fun initUI(savedInstanceState: Bundle?)

    //todo: ---------  Functions -------------

    fun <T> T.tos() = Toast.makeText(this@BaseAct, "$this", Toast.LENGTH_SHORT).show()
    fun <T> T.tosL() = Toast.makeText(this@BaseAct, "$this", Toast.LENGTH_LONG).show()


    open fun deleteCache() {
        try {
            val dir: File = cacheDir
            deleteDir(dir)
        } catch (e: java.lang.Exception) {
            somethingwentwrong()
            e.printStackTrace()
        }
    }

    open fun getJsonFromAssets(context: Context, fileName: String?): String? {
        val jsonString: String = try {
            val `is`: InputStream = context.assets.open(fileName!!)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }


    open fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

    open fun getFileSize(size: Long): String? {
        if (size <= 0) return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }



    @RequiresApi(Build.VERSION_CODES.M)
    fun openApp(pkg: String) {
        if (hasInternetConnect()) try {
            startActivity(Intent(packageManager.getLaunchIntentForPackage(pkg)))
        } catch (e: java.lang.Exception) {
            "App is not install".tos()
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
                    )
                )
            }
            e.message?.log()
        }
        else ("Internet connection error").tos()
    }

//    fun String.log() = Log.wtf("FATZ",this)


    fun openUri(uri: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

    open fun extractUrls(text: String): List<String>? {
        val containedUrls: MutableList<String> = ArrayList()
        val urlRegex =
            "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
        val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val urlMatcher = pattern.matcher(text)
        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)))
        }
        return containedUrls
    }

    fun somethingwentwrong() = ("Some thing went wrong").tos()
    fun interneterror() = ("Internet connection error").tos()

    fun rateUs() {
        try {
            val marketUri = Uri.parse("market://details?id=$packageName")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        } catch (e: Exception) {
            e.message?.log()
            val marketUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun hasInternetConnect(): Boolean {
        var isWifiConnected = false
        var isMobileConnected = false
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

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

    @RequiresApi(Build.VERSION_CODES.M)
    fun isVPN(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.getNetworkCapabilities(cm.activeNetwork)?.let {
            if (it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) return true
        }
        return false
    }

    fun shareUs() {
        val i = Intent(Intent.ACTION_SEND).putExtra(
                Intent.EXTRA_TEXT,
                "I'm using ${getString(R.string.app_name)}! Get the app for free at http://play.google.com/store/apps/details?id=${packageName}"
            )
        i.type = "text/plain"
        startActivity(Intent.createChooser(i, "Share"))
    }

    fun checkWritePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) return true
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
                111
            )
            return false
        }
        return true
    }

    open fun isMyPackedgeInstalled(packageName: String?): Boolean {
        return try {
            packageManager.getPackageInfo(packageName!!, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getRandomNumber(bound: Int) = Random().nextInt(bound)

    open fun getFilenameFromURL(str: String?): String? {
        return try {
            val stringBuilder = StringBuilder()
            stringBuilder.append(File(URL(str).path).name)
            stringBuilder.append("")
            stringBuilder.toString()
        } catch (str2: java.lang.Exception) {
            str2.printStackTrace()
            System.currentTimeMillis().toString()
        }
    }

    open fun getImageFilenameFromURL(url: String?): String? {
        return try {
            File(URL(url).path).name
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            System.currentTimeMillis().toString() + ".png"
        }
    }

    open fun getVideoFilenameFromURL(url: String?): String? {
        return try {
            File(URL(url).path).name
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            System.currentTimeMillis().toString() + ".mp4"
        }
    }

    fun convertUnixTimestampToDate(unixTimestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = Date(unixTimestamp * 1000) // Convert to milliseconds
        return dateFormat.format(date)
    }

    fun checkURL(input: CharSequence): Boolean {
        if (TextUtils.isEmpty(input)) {
            return false
        }
        val URL_PATTERN = Patterns.WEB_URL
        var isURL = URL_PATTERN.matcher(input).matches()
        if (!isURL) {
            val urlString = input.toString() + ""
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    URL(urlString)
                    isURL = true
                } catch (ignored: java.lang.Exception) {
                }
            }
        }
        return isURL
    }

    open fun SaveStatusVid(sourceuri: Uri) {
        val sourceFilename = sourceuri.path
        val root = Environment.getExternalStorageDirectory().toString() + "/Download"
        ("" + root).log()
        val myDir = File(root + "/" + getString(R.string.app_name))
        if (!myDir.exists()) myDir.mkdirs()
        //        String destinationFilename = Environment.getExternalStorageDirectory().getPath() + "/Download" + File.separatorChar + getString(R.string.app_name) + File.separatorChar + "MyStatus" + System.currentTimeMillis() / 1000 + ".mp4";
        val destinationFilename = "MyStatus" + System.currentTimeMillis() / 1000 + ".mp4"
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            inputStream = contentResolver.openInputStream(sourceuri)
            val file = File(myDir, destinationFilename)
            outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream!!.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            Toast.makeText(this, "Video saved to " + file.absolutePath, Toast.LENGTH_SHORT).show()
            (file.absolutePath + "== video").log()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save video", Toast.LENGTH_SHORT).show()
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val bis: BufferedInputStream? = null
        val bos: BufferedOutputStream? = null

//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                bis = new BufferedInputStream(Files.newInputStream(Paths.get(sourceFilename)));
//            }
//            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
//            byte[] buf = new byte[1024];
//            bis.read(buf);
//            do {
//                bos.write(buf);
//            } while (bis.read(buf) != -1);
//        } catch (IOException e) {
//            e.printStackTrace();
//            tosL("SaveStatusVid1 " + e.getMessage());
//            log( "SaveStatusVid: " + e.getMessage());
//        } finally {
//            try {
//                if (bis != null) bis.close();
//                if (bos != null) bos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//                tosL("SaveStatusVid2 " + e.getMessage());
//            }
//        }
        MediaScannerConnection.scanFile(
            this, arrayOf(destinationFilename), null
        ) { path: String?, uri: Uri? -> }
        ("Video Saved").tos()
    }


    fun getVideoResolution(videoPath: String): Pair<Int, Int> {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)

        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            ?.toIntOrNull() ?: 0
        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            ?.toIntOrNull() ?: 0

        retriever.release()

        return Pair(width, height)
    }

}