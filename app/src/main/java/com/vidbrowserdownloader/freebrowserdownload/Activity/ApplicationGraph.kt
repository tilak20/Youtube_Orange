package com.vidbrowserdownloader.freebrowserdownload.Activity

import com.vidbrowserdownloader.freebrowserdownload.search_engine.SearchEngineModule
import android.annotation.SuppressLint
import android.content.Context


class ApplicationGraph(
    private val context: Context
) {

    private val searchEngineManagerInternal by lazy { SearchEngineModule().createSearchEngineManager() }

    companion object {

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        fun getSearchEngineManager() = graph!!.searchEngineManagerInternal

        @JvmStatic
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
            }
        }
    }
}
