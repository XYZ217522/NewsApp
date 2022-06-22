package com.example.news.search.adapter

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.example.news.epoxy.SimpleNewsModel
import com.example.news.epoxy.simpleNews
import com.example.news.model.NewsData

class SearchEpoxyController(private val mCallback: SearchEpoxyCallback) : EpoxyController() {

    companion object {
        const val TAG = "SearchEpoxyController"
    }

    private var mHistoryList: List<String> = emptyList()
    private var mNewsData: NewsData? = null
    private var isHistoryListMode = true

    @AutoModel
    lateinit var historyFooterModel: HistoryFooterModel_

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

        // todo add search total count ,filter button by publish date
        val articles = mNewsData?.articles ?: emptyList()
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