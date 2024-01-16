package com.vidbrowserdownloader.freebrowserdownload.main

import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.Application.Companion.log
import com.vidbrowserdownloader.freebrowserdownload.search_engine.SearchEngineManager
import android.os.Bundle

internal class MainActivityPresenter(
    private val screen: MainActivityContract.Screen,
    private val searchEngineManager: SearchEngineManager
) : MainActivityContract.UserAction {

    private var webViewVisible = false
    private var videoRadioButtonChecked = false
    private var search: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val firstActivityLaunch = savedInstanceState == null
        if (firstActivityLaunch) {

            setWebViewVisible(webViewVisible)
        }
    }

    override fun onNewIntent(url: String?) {
        if (url != null) {

            performSearch(url, true)
        }
    }

    override fun onResume() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("webViewVisible", webViewVisible)
    }


    override fun onRestoreInstanceState(outState: Bundle) {
        webViewVisible = outState.getBoolean("webViewVisible")
        setWebViewVisible(webViewVisible)
    }

    override fun onHomeClicked() {
        val url = searchEngineManager.getHomeUrl()

        performSearch(url, false)
    }

    override fun onClearDataClicked() {
        screen.showUrl(searchEngineManager.getHomeUrl())
        screen.clearData()
        setWebViewVisible(false)
    }

    override fun onPageLoadProgressChanged(progressPercent: Int) {

    }

    override fun onPageTouched() {
    }

    override fun onBackPressed(emptyViewVisible: Boolean) {
        if (emptyViewVisible) {
            screen.quit()
            return
        }
        if (screen.webViewCanGoBack()) {
            screen.webViewBack()
            "webview finish".log()
            return
        }
        setWebViewVisible(false)
    }

    override fun onFabClearClicked() {
        screen.showUrl(searchEngineManager.getHomeUrl())
        screen.clearData()
        setWebViewVisible(false)
    }

    override fun onInputClearClicked() {

    }

    override fun onVideoCheckedChanged(checked: Boolean) {
        videoRadioButtonChecked = checked
    }

    override fun onQuitClicked() {
        screen.quit()
    }

    override fun onSuggestionClicked(suggestion: String) {
        val search = suggestion.replace("<b>", "").replace("</b>", "")
        val url = convertSearchToUrl(search)
        screen.showUrl(url)
        setWebViewVisible(true)
    }

    override fun onSuggestionImageClicked(suggestion: String) {
        val search = suggestion.replace("<b>", "").replace("</b>", "")
    }

    // ==================================================  performSearch  ==================================================

    private fun performSearch(search: String, forceNonFloating: Boolean) {
        val url = convertSearchToUrl(search)

        screen.showUrl(url)
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
            screen.showWebView()
        } else {
            screen.hideWebView()
        }
    }
}
