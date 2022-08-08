package com.example.news.home.adapter

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.example.news.R
import com.example.news.epoxy.LoadingFooterModel_
import com.example.news.epoxy.SimpleNewsModel
import com.example.news.epoxy.SimpleNewsModel_
import com.example.news.home.ArticleViewInfo
import com.example.news.model.ArticlesBean
import com.example.news.model.NewsData
import com.example.news.model.domainList

class HomeEpoxyController(private val mCallback: HomeEpoxyCallback) : EpoxyController() {

    companion object {
        const val TAG = "HomeEpoxyController"
    }

    var mSelectDomain = ""

    var isSelectDomainMode = false
        private set

    var mAlArticles: MutableList<ArticlesBean> = mutableListOf()
        set(value) {
            field.addAll(value)
            requestModelBuild()
        }

    var mIsLoadMore = false
        set(value) {
            field = value
            requestModelBuild()
        }

    @AutoModel
    lateinit var loadingLoadingFooterModel: LoadingFooterModel_

    override fun buildModels() {

        when (isSelectDomainMode) {
            true -> {
                domainList.forEachIndexed { index, domain ->
                    DomainModel_()
                        .id(domain + index)
                        .domain(domain)
                        .isSelected(mSelectDomain == domain)
                        .listener(mCallback)
                        .addTo(this)
                }
            }
            false -> {
                mAlArticles.forEachIndexed { index, articlesBean ->
                    SimpleNewsModel_()
                        .id(SimpleNewsModel::class.simpleName + index)
                        .articlesBean(articlesBean)
                        .listener(mCallback)
                        .addTo(this)
                }
                loadingLoadingFooterModel.addIf(mIsLoadMore, this)
            }
        }
    }

    fun clearNewsData() {
        mIsLoadMore = false
        isSelectDomainMode = false
        mAlArticles.clear()
        requestModelBuild()
    }

    fun changeDomainMode() {
        Log.d(TAG, "changeMode.")
        isSelectDomainMode = !isSelectDomainMode
        requestModelBuild()
    }

    fun getSelectDomainAnimation(): Int {
        Log.d(TAG, "changeMode.")
        return if (isSelectDomainMode) R.anim.slide_down else R.anim.slide_up
    }
}