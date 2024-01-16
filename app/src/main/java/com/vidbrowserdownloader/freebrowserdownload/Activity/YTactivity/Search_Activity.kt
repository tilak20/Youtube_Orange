package com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity

import com.vidbrowserdownloader.freebrowserdownload.Activity.MainActivity2
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.Application.Companion.log
import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.Application.Companion.tos
import com.vidbrowserdownloader.freebrowserdownload.constant.hasInternetConnect
import com.vidbrowserdownloader.freebrowserdownload.databinding.YtactivitySearchBinding
import com.vidbrowserdownloader.freebrowserdownload.utils.gon
import com.vidbrowserdownloader.freebrowserdownload.utils.visible
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.sdk.AdsManager
import com.vidbrowserdownloader.freebrowserdownload.ApiClient.YoutubeClient.Companion.getRetrofit
import com.vidbrowserdownloader.freebrowserdownload.ApiClient.YoutubeClient.Companion.getSuggationRetrofit
import com.vidbrowserdownloader.freebrowserdownload.ApiInterface.TrendingInterface
import com.vidbrowserdownloader.freebrowserdownload.Adapter.SearchAdapter
import com.vidbrowserdownloader.freebrowserdownload.Adapter.SuggestionAdapter
import com.vidbrowserdownloader.freebrowserdownload.Model.SearchModel.Client
import com.playerhdgallery.uplayerpro.model.SearchModel.ContentsItem
import com.vidbrowserdownloader.freebrowserdownload.Model.SearchModel.Request
import com.vidbrowserdownloader.freebrowserdownload.Model.SearchModel.SearchContext
import com.vidbrowserdownloader.freebrowserdownload.Model.SearchModel.SearchModel
import com.playerhdgallery.uplayerpro.model.SearchModel.SearchModellist
import com.vidbrowserdownloader.freebrowserdownload.Model.SearchModel.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search_Activity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var name: String
    private var search: String = ""
    lateinit var binding: YtactivitySearchBinding

    var list: ArrayList<ContentsItem> = arrayListOf()

    val list1: ArrayList<ContentsItem> = arrayListOf()

    lateinit var suggestionlist: List<List<Any>>

    val client = "youtube"
    val ds = "yt"
    val gl = "US"
    val xhr = "t"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = YtactivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {

            imgback.setOnClickListener {
                startActivity(Intent(this@Search_Activity, TrendingActivity::class.java))
                finish()
            }
            lntrending.setOnClickListener {
                startActivity(Intent(this@Search_Activity, TrendingActivity::class.java))
                finish()
            }
            btnYt.setOnClickListener {
                startActivity(Intent(this@Search_Activity, MainActivity2::class.java))
            }

            btnsearch.setOnClickListener {
                performSearch()
                suggestionRv.gon()
                searchRv.visible()
            }

            txtnews.setOnClickListener { tagSearch("Today News") }
            txtlovesong.setOnClickListener { tagSearch("Love Songs") }
            txtdance.setOnClickListener { tagSearch("Sexy Dance") }
            txtdeepwork.setOnClickListener { tagSearch("Deep Work") }
            txtnature.setOnClickListener { tagSearch("Nature") }
            txttiktok.setOnClickListener { tagSearch("Hot TikTok") }
            txtrelax.setOnClickListener { tagSearch("Relaxing") }
            txtchill.setOnClickListener { tagSearch("Chill") }
            edtSearch.setOnEditorActionListener { _, actionId, _ ->
                if (hasInternetConnect(this@Search_Activity)) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        performSearch()
                        suggestionRv.gon()
                        searchRv.visible()
                    }
                } else {
                    ("Internet connection error").tos(this@Search_Activity)
                }
                true
            }

            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    // This method is called before the text changes.
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // This method is called when the text changes.
                    val query = s.toString()

                    "count ==$count".log()
                    "start ==$start".log()
                    "before ==$before".log()
                    "s ==$s".log()
                    if (start == -1 || query.isEmpty()) {
                        handler.postDelayed({
                            taglayout.visible()
                            suggestionRv.gon()
                            "qurery is empty".log()
                        }, 500)
                    } else {
                        taglayout.gon()
                        suggestionRv.visible()
                        "qurery is not empty".log()
                    }
//                    suggestionRv.visible()
                    searchRv.gon()
                    suggationApiCall(query)
                }

                override fun afterTextChanged(s: Editable?) {
                    // This method is called after the text changes.
                }
            })
        }
    }

    fun tagSearch(tag: String) {
        binding.apply {
            edtSearch.setText(tag)
            binding.progress.visibility = View.VISIBLE
            searchApiCall(tag)
            val view: View? = this@Search_Activity.currentFocus
            if (view != null) {
                val inputMethodManager =
                    this@Search_Activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
            searchRv.visible()
            suggestionRv.gon()
        }
    }

    private fun performSearch() {
        name = binding.edtSearch.text.toString()

        if (name.isNotEmpty()) {
            binding.progress.visibility = View.VISIBLE
            searchApiCall(name)
        } else {
            "search is empty".tos(this@Search_Activity)
        }
        val view: View? = this@Search_Activity.currentFocus
        if (view != null) {
            val inputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun suggationApiCall(query: String) {

        val apiInterface = getSuggationRetrofit().create(TrendingInterface::class.java)
        apiInterface.suggationData(client, ds, gl, query, xhr)
            .enqueue(object : Callback<List<Any>> {
                override fun onResponse(
                    call: Call<List<Any>>, response: Response<List<Any>>
                ) {

                    if (response.isSuccessful) {
                        val list = response.body()!![1]

                        binding.suggestionRv.adapter = SuggestionAdapter(
                            this@Search_Activity, list as List<List<Any>>
                        ) { position ->
                            binding.progress.visible()
                            val item = list[position]
                            val dataitem = item[0].toString()
                            binding.edtSearch.setText(dataitem)
                            binding.searchRv.visible()
                            binding.suggestionRv.gon()
                            searchApiCall(dataitem)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Any>>, t: Throwable) {
                    "Faile".log()
                }
            })
    }

    private fun searchApiCall(query: String) {
        val searchmodel = SearchModel(
            query = query, context = SearchContext(
                request = Request(internalExperimentFlags = emptyList(), useSsl = true),
                client = Client(
                    hl = "en-GB",
                    gl = "IN",
                    clientName = "WEB",
                    originalUrl = "https://www.youtube.com",
                    clientVersion = "2.20230803.01.00",
                    platform = "DESKTOP"
                ),
                user = User(lockedSafetyMode = false)
            )
        )

        val apiInterface = getRetrofit().create(TrendingInterface::class.java)
        apiInterface.searchData(bodyModel = searchmodel)
            .enqueue(object : Callback<SearchModellist> {
                override fun onResponse(
                    call: Call<SearchModellist>, response: Response<SearchModellist>
                ) {
                    if (response.isSuccessful) {
                        binding.progress.gon()
                        list =
                            response.body()!!.contents!!.twoColumnSearchResultsRenderer!!.primaryContents!!.sectionListRenderer!!.contents!![0]!!.itemSectionRenderer!!.contents as ArrayList<ContentsItem>
                        list1.clear()

//                        val token =
//                            response.body()!!.contents!!.twoColumnSearchResultsRenderer!!.primaryContents!!.sectionListRenderer!!.contents!![0]!!.continuationItemRenderer!!.continuationEndpoint!!.continuationCommand!!.token
//
//
//                        val tokenlist = ArrayList<ContinuationItemsItem>()


                        list.forEach {

                            if (it.videoRenderer != null) {
//                                "here = ${it.videoRenderer}".log()
                                if (it.videoRenderer.viewCountText!!.runs == null) {
                                    if (it.videoRenderer.lengthText == null) {
                                        "video not available".log()
                                    } else {
//                                        if (it.videoRenderer.badges!![0] != null) {
//                                            if (it.videoRenderer.badges[0]!!.metadataBadgeRenderer!!.label == "LIVE") {

                                        list1.add(it)

//                                            val token1 = response.body()!!.contents!!.twoColumnSearchResultsRenderer!!.primaryContents!!.sectionListRenderer!!.contents!![0]!!.continuationItemRenderer!!.continuationEndpoint!!.continuationCommand!!.token

//                                            "new token == =$token1".log()

//                                            } else {
//                                                "is live".log()
//                                            }
//                                        }else{
//                                            "is null".log()
//                                        }
                                    }

                                } else {
                                    "videorender null".log()
                                }
                            }
                        }
                        binding.searchRv.adapter = SearchAdapter(this@Search_Activity, list1)
                    }
                }

                override fun onFailure(call: Call<SearchModellist>, t: Throwable) {
                    "Fail".log()
                }
            })
    }

    override fun onBackPressed() {
        AdsManager.getInstance().showOnbackPressAdExtra(this@Search_Activity) {
            startActivity(Intent(this@Search_Activity, TrendingActivity::class.java))
            finish()
        }
    }
}