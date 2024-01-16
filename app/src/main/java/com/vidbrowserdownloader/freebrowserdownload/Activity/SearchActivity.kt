package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.constant.hasInternetConnect
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivitySearchBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.allsettings
import com.vidbrowserdownloader.freebrowserdownload.utils.gon
import com.vidbrowserdownloader.freebrowserdownload.utils.log
import com.vidbrowserdownloader.freebrowserdownload.utils.toast
import com.vidbrowserdownloader.freebrowserdownload.utils.visible
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.sdk.AdsManager

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClick()
    }

    private fun initClick() {
        binding.apply {

            imgback.setOnClickListener {
                finish()
            }
            webview.allsettings()

            webview.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?, request: WebResourceRequest?
                ): Boolean {
                    val currentUrl: String? = view!!.url
                    currentUrl!!.log()
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageFinished(p1: WebView, p2: String) {
                    super.onPageFinished(p1, p2)
                    progress.gon()
                }
            }

            clearsearch.setOnClickListener {
                btnall.isChecked
                tvSearch.setText("")
                lntabs.gon()
                webview.gon()
                progress.gon()
            }
            tvSearch.setOnEditorActionListener { _, actionId, _ ->
                progress.visible()
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (hasInternetConnect(this@SearchActivity)) {
                        lntabs.visible()
                        webview.visible()
                        if (btnall.isChecked) {
                            webview.loadUrl("https://www.youtube.com/results?search_query=${tvSearch.text}")
                        } else if (btngoogle.isChecked) {
                            webview.loadUrl("https://www.google.com/search?rlz=1C5MACD_enIN1052IN1052&sxsrf=AB5stBhqrx7hVFK4Quc0WtF1l-s54zXKcQ:1689743571998&q=${tvSearch.text}&tbm=vid&sa=X&ved=2ahUKEwi16vqqgZqAAxU_d2wGHVnJC8kQ0pQJegQICRAB&biw=1920&bih=797&dpr=1")
                        } else if (btnplaylist.isChecked) {
                            webview.loadUrl("https://www.youtube.com/results?search_query=${tvSearch.text}&sp=EgIQAw%253D%253D")
                        } else if (btnchannel.isChecked) {
                            webview.loadUrl("https://www.youtube.com/results?search_query=${tvSearch.text}&sp=EgIQAg%253D%253D")
                        } else {
                            "Hello".toast(this@SearchActivity)
                        }

                        val view: View? = this@SearchActivity.currentFocus
                        if (view != null) {
                            val inputMethodManager =
                                this@SearchActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                    } else ("Internet connection error").toast(this@SearchActivity)
                }
                true
            }
            btngoogle.setOnClickListener {
                webview.loadUrl("https://www.google.com/search?rlz=1C5MACD_enIN1052IN1052&sxsrf=AB5stBhqrx7hVFK4Quc0WtF1l-s54zXKcQ:1689743571998&q=${tvSearch.text}&tbm=vid&sa=X&ved=2ahUKEwi16vqqgZqAAxU_d2wGHVnJC8kQ0pQJegQICRAB&biw=1920&bih=797&dpr=1")
                btngoogle.isChecked
            }
            btnall.setOnClickListener {
                webview.loadUrl("https://www.youtube.com/results?search_query=${tvSearch.text}")
                btnall.isChecked
            }
            btnplaylist.setOnClickListener {
                webview.loadUrl("https://www.youtube.com/results?search_query=${tvSearch.text}&sp=EgIQAw%253D%253D")
                btnplaylist.isChecked
            }
            btnchannel.setOnClickListener {
                webview.loadUrl("https://www.youtube.com/results?search_query=${tvSearch.text}&sp=EgIQAg%253D%253D")
                btnchannel.isChecked
            }
        }
    }

    override fun onBackPressed() {
        binding.apply {
            if (webview.canGoBack()) {
                webview.goBack()
            } else {
                AdsManager.getInstance().showOnbackPressAdExtra(this@SearchActivity) {
                    finish()
                }
            }
        }
    }
}
