@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.vidbrowserdownloader.freebrowserdownload.search_engine

class SearchEngineModule {

    fun createSearchEngineManager(): SearchEngineManager {
        return SearchEngineManagerImpl()
    }
}
