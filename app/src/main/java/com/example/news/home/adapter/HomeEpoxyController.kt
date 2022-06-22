package com.example.news.home.adapter

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.example.news.R
import com.example.news.epoxy.LoadingFooterModel_
import com.example.news.epoxy.simpleNews
import com.example.news.model.ArticlesBean
import com.example.news.model.NewsData
import com.example.news.model.domainList
import com.example.news.util.getTotalPage

class HomeEpoxyController(private val mCallback: HomeEpoxyCallback) : EpoxyController() {

    companion object {
        const val TAG = "HomeEpoxyController"
    }

    private var mNewsData: NewsData? = null
    var isSelectDomainMode = false
    var mSelectDomain = ""

    @AutoModel
    lateinit var loadingLoadingFooterModel: LoadingFooterModel_

    override fun buildModels() {

        if (isSelectDomainMode) {
            domainList.forEachIndexed { index, domain ->
                domain {
                    id(domain + index)
                    domain(domain)
                    isSelected(mSelectDomain == domain)
                    listener(mCallback)
                }
            }
            return
        }

        mNewsData?.let {
            val articles = mNewsData?.articles ?: emptyList()
            articles.forEachIndexed { index, articlesBean ->
                simpleNews {
                    id(index)
                    articlesBean(articlesBean)
                    listener(mCallback)
                }
            }

            loadingLoadingFooterModel.addIf(checkIsLoading(articles), this)
        }
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
        Log.d(TAG,"setNewsData:${newsData.currentPage}")
        mNewsData?.let {
            it.currentPage = newsData.currentPage
            it.articles?.addAll(newsData.articles ?: emptyList())
        } ?: run { mNewsData = newsData }
        requestModelBuild()
    }

    fun clearNewsData() {
        mNewsData = null
        isSelectDomainMode = false
        requestModelBuild()
    }

    fun changeMode(): Int {
        Log.d(TAG, "changeMode.")
        isSelectDomainMode = !isSelectDomainMode
        requestModelBuild()
        return if (isSelectDomainMode) R.anim.slide_down else R.anim.slide_up
    }
}