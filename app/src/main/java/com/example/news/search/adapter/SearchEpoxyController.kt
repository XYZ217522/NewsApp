package com.example.news.search.adapter

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.example.news.epoxy.LoadingFooterModel_
import com.example.news.epoxy.SimpleNewsModel
import com.example.news.epoxy.simpleNews
import com.example.news.model.NewsData

class SearchEpoxyController(private val mCallback: SearchEpoxyCallback) : EpoxyController() {

    companion object {
        const val TAG = "SearchEpoxyController"
    }

    private var historyList: List<String> = emptyList()
    private var mNewsData: NewsData? = null
    private var isHistoryListMode = true

    @AutoModel
    lateinit var historyFooterModel: HistoryFooterModel_

    @AutoModel
    lateinit var loadingLoadingFooterModel: LoadingFooterModel_

    override fun buildModels() {

        if (isHistoryListMode) {
            historyList.forEachIndexed { index, text ->
                historyText {
                    id(HistoryTextModel::class.simpleName + index)
                    historyText(text)
                    listener(mCallback)
                }
            }

            historyFooterModel.listener(mCallback).addIf(historyList.isNotEmpty(), this)
            return
        }

        val articles = mNewsData?.articles ?: emptyList()
        Log.d(TAG, "articles=$articles")
        articles.forEachIndexed { index, articlesBean ->
            simpleNews {
                id(SimpleNewsModel::class.simpleName + index)
                articlesBean(articlesBean)
                listener(mCallback)
            }
        }

        loadingLoadingFooterModel.addIf(articles.isEmpty(), this)
    }

    fun setHistoryData(historyList: List<String>) {
        Log.d(TAG, "setHistoryData:${historyList}")
        this.historyList = historyList
        requestModelBuild()
    }

    fun setNewsData(newsData: NewsData) {
        Log.d(TAG, "setNewsData:${newsData}")
        mNewsData?.let {
            it.currentPage = newsData.currentPage
            it.articles?.addAll(newsData.articles ?: emptyList())
        } ?: run { mNewsData = newsData }
        requestModelBuild()
    }

    fun clearNewsData() {
        mNewsData = null
        changeMode(false)
    }

    fun changeMode(isHistory: Boolean? = null) {
        isHistoryListMode = isHistory ?: !isHistoryListMode
        requestModelBuild()
    }
}