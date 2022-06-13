package com.example.news.home.adapter

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.example.news.epoxy.LoadingFooterModel_
import com.example.news.model.ArticlesBean
import com.example.news.model.NewsData
import com.example.news.util.getTotalPage

class HomeEpoxyController : EpoxyController() {

    companion object {
        const val TAG = "HomeEpoxyController"
    }

    private var mNewsData: NewsData? = null

    @AutoModel
    lateinit var loadingLoadingFooterModel: LoadingFooterModel_

    override fun buildModels() {

        val articles = mNewsData?.articles ?: emptyList()
        articles.forEachIndexed { index, articlesBean ->
            SimpleNewsModel_()
                .id(index)
                .articlesBean(articlesBean)
                .addTo(this)
        }

        loadingLoadingFooterModel.addIf(checkIsLoading(articles), this)
    }

    private fun checkIsLoading(articles: List<ArticlesBean>): Boolean {
        if (articles.isEmpty()) return true
        return mNewsData?.let {
            val totalPage = it.totalResults.getTotalPage()
            Log.d(TAG, "totalPage=$totalPage")
            it.currentPage < totalPage
        } ?: true
    }

    fun setNewsData(newsData: NewsData) {
        mNewsData?.let {
            it.currentPage = newsData.currentPage
            it.articles?.addAll(newsData.articles ?: emptyList())
        } ?: run { mNewsData = newsData }
        requestModelBuild()
    }

}