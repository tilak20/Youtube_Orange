package com.vidbrowserdownloader.freebrowserdownload.main

import DetectImageDialog
import com.vidbrowserdownloader.freebrowserdownload.Activity.ApplicationGraph
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.BaseAct
import com.vidbrowserdownloader.freebrowserdownload.constant.getjs
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityMainBinding
import com.vidbrowserdownloader.freebrowserdownload.search_engine.SearchEngineManager
import com.vidbrowserdownloader.freebrowserdownload.utils.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.gson.Gson
import java.util.*

class MainActivity : BaseAct<ActivityMainBinding>(), MainActivityContract.Screen {

    lateinit var key: String
    lateinit var from: String
    var item: AhaModel? = null
    var currenturl: String? = null
    lateinit var javascript: String
    private var webViewVisible = false
    private var videoRadioButtonChecked = false
    private val browserWebViewListener = createBrowserWebViewListener()
    private var forceDestroy = false
    private var search: String = ""

    override fun getActivityBinding(inflater: LayoutInflater) =
        ActivityMainBinding.inflate(layoutInflater)

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun initUI(savedInstanceState: Bundle?) {
        createUserAction()
        getIntentData()

        binding.apply {

            layout.imgback.setOnClickListener {
                if (webViewCanGoBack()) {
                    webViewBack()
                } else {
                    finish()
                }
            }

            layout.imgClose.setOnClickListener {
                finish()
            }

            activityMainWebView.browserWebViewListener = browserWebViewListener
            activityMainWebView.setBackgroundColor(Color.TRANSPARENT)

            val firstActivityLaunch = savedInstanceState == null
            if (firstActivityLaunch) {
                setWebViewVisible(webViewVisible)
            }
            performSearch("https://www.google.com/")

            activityMainWebView.settings.javaScriptEnabled = true
            if (from == "Search") {
                performSearch("https://www.google.com/search?q=${key}")
            } else {
                performSearch(key)
            }
            activityMainWebView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            activityMainWebView.settings.userAgentString = androidWeb
            activityMainWebView.addJavascriptInterface(
                ADAPTATION_HOLDER { from, res ->
                    runOnUiThread {
                        when (from.trim()) {
                            "loadLocalJsFile" -> {
                                "loadlocaljsfile".log()
                                load_status = res
                            }

                            "setParseBtnType" -> {
                                "nativeBtn".log()
                                imagerela.visibility = if (res == "nativeBtn") VISIBLE else GONE
                            }

                            "receiveJsParseResult" -> {
                                item = Gson().fromJson(res, AhaModel::class.java)
                                if (imagerela.visibility == GONE) {
                                    if (item!!.parseType == "jpg" || item!!.parseType == "png") DetectImageDialog(
                                        this@MainActivity,
                                        item!!
                                    )
                                    else DetectVideoDialog(this@MainActivity, item!!)
                                } else {
                                    if (item!!.dataList!!.isEmpty()) {
                                        imagerela.gon()
                                        tvimagesize.gon()
                                    } else {
                                        tvimagesize.visible()
                                    }
                                    imageimage.setBackgroundColor(Color.parseColor("#FF274AC6"))
                                }
                            }

                            "clickDownloadButtonEvent" -> {}
                        }
                    }
                }, "ADAPTATION_HOLDER"
            )
            val drag = OnBoundedDragTouchListener(imagerela, activityMainWebView)
            drag.setOnDragActionListener(object : OnBoundedDragTouchListener.OnDragActionListener {
                override fun onDragStart(view: View) {}

                override fun onDragEnd(view: View) {
                    if (!tvimagesize.isVisible) {
                        ("No Detected").log()
                        NotDetectionDialog(this@MainActivity)
                    } else {
                        if (item != null) DetectVideoDialog(this@MainActivity, item!!)
                    }
                }
            })
            imagerela.setOnTouchListener(drag)

            activityMainWebView.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    if (activityMainWebView.progress >= 100) {
                        hideLoader()
                    } else {
                        showLoader(activityMainWebView.progress)
                    }
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    if (activityMainWebView.progress >= 100) {
                        hideLoader()
                    } else {
                        showLoader(activityMainWebView.progress)
                    }

                    "Load Status: $load_status".log()

                    if (load_status.isNotEmpty()) {
                        view!!.evaluateJavascript(
                            getjs(
                                this@MainActivity,
                                "newJs/" + load_status.trim()
                            )
                        ) {
                            it.log()
                        }
                    }

                    when (from) {
                        "Instagram" -> {
                            javascript = "detect_ins.js"
                        }

                        "Facebook" -> {
                            javascript = "detect_fb.js"
                        }

                        "Twitter" -> {
                            javascript = "detect_tw.js"
                        }

                        "TikTok" -> {
                            javascript = "detect_tt.js"
                        }

                        "Vimeo" -> {
                            javascript = "vimeo.js"
                        }

                        "9gag" -> {
                            javascript = "detect_9gag.js"
                        }

                        "900px" -> {
                            javascript = "detect_500px.js"
                        }

                        "pinterest" -> {
                            javascript = "pinterest_parse.js"
                        }

                        "dailymotion" -> {
                            javascript = "dailymotion_parser.js"
                        }

                        "Google" -> {
                            javascript = "null"
                        }

                        "Search" -> {
                            javascript = if (key.contains("Instagram".toLowerCase(Locale.ROOT))) {
                                "detect_ins.js"
                            } else if (key.contains("facebook".toLowerCase(Locale.ROOT))) {
                                "detect_fb.js"
                            } else if (key.contains("twitter".toLowerCase(Locale.ROOT))) {
                                "detect_tw.js"
                            } else if (key.contains("tikTok".toLowerCase(Locale.ROOT))) {
                                "detect_tt.js"
                            } else if (key.contains("vimeo".toLowerCase(Locale.ROOT))) {
                                "vimeo.js"
                            } else if (key.contains("9gag".toLowerCase(Locale.ROOT))) {
                                "detect_9gag.js"
                            } else if (key.contains("500px".toLowerCase(Locale.ROOT))) {
                                "detect_500px.js"
                            } else if (key.contains("pinterest".toLowerCase(Locale.ROOT))) {
                                "pinterest_parse.js"
                            } else if (key.contains("dailymotion".toLowerCase(Locale.ROOT))) {
                                "dailymotion_parser.js"
                            } else {
                                "null"
                            }
                        }

                        else -> {
                            javascript = "null"
                        }
                    }

                    "javascript == 1 == $javascript".log()

                    view!!.evaluateJavascript(getjs(this@MainActivity, "newJs/$javascript")) {
                        ("String $it").log()
                        currenturl = activityMainWebView.url
                    }

                }
            }
            activityMainWebView.webChromeClient = WebChromeClient()
        }
    }

    private fun getIntentData() {
        from = intent.getStringExtra("from").toString()
        key = intent.getStringExtra("key").toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (forceDestroy) {
            return
        }
        binding.activityMainWebView.browserWebViewListener = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val url = intent?.extras?.getString(EXTRA_URL)
        if (url != null) {
            Log.d("FATZ", "url == 8 == $url")
            performSearch(url)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (forceDestroy) {
            return
        }
        outState.putBoolean("webViewVisible", webViewVisible)
        binding.activityMainWebView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.activityMainWebView.restoreState(savedInstanceState)

        webViewVisible = savedInstanceState.getBoolean("webViewVisible")
        setWebViewVisible(webViewVisible)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {

            if (webViewCanGoBack()) {
                "webview back".log()
                webViewBack()
            } else {
                "activity back".log()
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun showUrl(url: String) {
        binding.activityMainWebView.load(url)
        Log.d("FATZ", "search == 7 == ${url}")
    }

    override fun reload() {
        binding.activityMainWebView.reload()
    }

    override fun webViewCanGoBack() = binding.activityMainWebView.canGoBack()

    override fun webViewBack() {
        binding.activityMainWebView.goBack()
    }

    override fun quit() {
        finish()
    }

    override fun clearData() {
        binding.activityMainWebView.clearData()
    }

    @SuppressLint("NewApi")
    override fun showLoader(progressPercent: Int) {
        binding.apply {
            activityMainProgress.visibility = VISIBLE
            activityMainProgress.setProgress(progressPercent, true)
        }
    }

    fun hideLoader() {
        binding.activityMainProgress.visibility = GONE
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.statusBarColor = color
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun setStatusBarDark(statusBarDark: Boolean) {
        val flags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility =
            if (statusBarDark) flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            else flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun showWebView() {
        binding.activityMainWebView.visibility = VISIBLE
    }

    override fun hideWebView() {
        binding.activityMainWebView.visibility = GONE
    }

    private fun createBrowserWebViewListener() = object : MainWebView.BrowserWebViewListener {
        override fun onPageFinished() {
//            userAction.onPageLoadProgressChanged(100)
            binding.apply {
                if (activityMainWebView.progress >= 100) {
                    hideLoader()
                } else {
                    showLoader(activityMainWebView.progress)
                }

                "progress 1 == ${activityMainWebView.progress}"
            }
        }

        override fun onProgressChanged() {
            binding.apply {

                if (activityMainWebView.progress >= 100) {
                    hideLoader()
                } else {
                    showLoader(activityMainWebView.progress)
                }
                "progress 2 == ${activityMainWebView.progress}"

            }
        }

        override fun onPageTouched() {
        }
    }

    private fun createUserAction(): MainActivityContract.UserAction {
        searchEngineManager = ApplicationGraph.getSearchEngineManager()

        return MainActivityPresenter(
            this, searchEngineManager
        )
    }

    companion object {
        private const val EXTRA_URL = "EXTRA_URL"
        lateinit var searchEngineManager: SearchEngineManager

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }

        @JvmStatic
        fun start(context: Context, url: String) {
            val intent = Intent(context, MainActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }

    private fun performSearch(search: String) {
        val url = convertSearchToUrl(search)
        showUrl(url)
        setWebViewVisible(true)
    }

    private fun convertSearchToUrl(search: String) = if (videoRadioButtonChecked) {
        searchEngineManager.createSearchVideoUrl(search)
    } else {
        searchEngineManager.createSearchUrl(search)
    }

    private fun setWebViewVisible(visible: Boolean) {
        webViewVisible = visible
        if (visible) {
            showWebView()
            showLoader(0)
        } else {
            hideWebView()
            hideLoader()
        }
    }

}
