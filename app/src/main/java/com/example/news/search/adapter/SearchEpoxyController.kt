package com.example.news.search.adapter

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.example.news.epoxy.SimpleNewsModel
import com.example.news.epoxy.simpleNews
import com.example.news.model.NewsData
import com.example.news.util.dateToStamp

class SearchEpoxyController(private val mCallback: SearchEpoxyCallback) : EpoxyController() {

    companion object {
        const val TAG = "SearchEpoxyController"
    }

    var isSortByDESC: Boolean = false
        set(value) {
            field = value
            requestModelBuild()
        }

    private var mHistoryList: List<String> = emptyList()
    private var mNewsData: NewsData? = null
    private var isHistoryListMode = true

    @AutoModel
    lateinit var historyFooterModel: HistoryFooterModel_

    @AutoModel
    lateinit var searchResultStatusModel: SearchResultStatusModel_

    override fun buildModels() {

        if (isHistoryListMode) {
            mHistoryList.forEachIndexed { index, text ->
                historyText {
                    id(HistoryTextModel::class.simpleName + index)
                    historyText(text)
                    listener(mCallback)
                }
            }

            historyFooterModel.listener(mCallback).addIf(mHistoryList.isNotEmpty(), this)
            return
        }

        var articles = mNewsData?.articles ?: emptyList()

        searchResultStatusModel
            .isSortByDESC(isSortByDESC).listener(mCallback).addIf(articles.isNotEmpty(), this)

        // sort articles
        articles = if (isSortByDESC) {
            articles.sortedByDescending { it.publishedAt?.dateToStamp() }
        } else {
            articles.sortedBy { it.publishedAt?.dateToStamp() }
        }
        Log.d(TAG, "articles=$articles")
        articles.forEachIndexed { index, articlesBean ->
            simpleNews {
                id(SimpleNewsModel::class.simpleName + index)
                articlesBean(articlesBean)
                listener(mCallback)
            }
        }
    }

    fun setHistoryData(historyList: List<String>) {
        Log.d(TAG, "setHistoryData:${historyList}")
        mHistoryList = historyList
        requestModelBuild()
    }

    fun setNewsData(newsData: NewsData) {
        Log.d(TAG, "setNewsData:${newsData}")
        mNewsData = newsData
        isHistoryListMode = false
        requestModelBuild()
    }

    fun changeMode(isHistory: Boolean) {
        isHistoryListMode = isHistory
        requestModelBuild()
    }
}