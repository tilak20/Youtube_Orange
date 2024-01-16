@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.vidbrowserdownloader.freebrowserdownload.search_engine

import com.mercandalli.android.browser.search_engine.SearchEngine
import com.mercandalli.android.browser.search_engine.SearchEngineVideo

interface SearchEngineManager {

    fun getHomeUrl(): String

    fun createSearchUrl(
        searchInput: String
    ): String

    @SearchEngine.Companion.SearchEngineKey
    fun getSearchEngineKey(): String

    fun getSearchEngine(): SearchEngine

    fun setSearchEngineKey(@SearchEngine.Companion.SearchEngineKey searchEngineKey: String)

    fun getSearchEngines(): List<SearchEngine>

    fun createSearchVideoUrl(
        searchInput: String
    ): String

    @SearchEngineVideo.Companion.SearchEngineVideoKey
    fun getSearchEngineVideoKey(): String

    fun getSearchEngineVideo(): SearchEngineVideo

    fun setSearchEngineVideoKey(@SearchEngineVideo.Companion.SearchEngineVideoKey searchEngineVideoKey: String)

    fun getSearchEngineVideos(): List<SearchEngineVideo>
}
