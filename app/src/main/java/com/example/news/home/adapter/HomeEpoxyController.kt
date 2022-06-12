package com.example.news.home.adapter

import com.airbnb.epoxy.EpoxyController
import com.example.news.model.NewsData

class HomeEpoxyController : EpoxyController() {

    var mNewsData: NewsData? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        val articles = mNewsData?.articles ?: return
        articles.forEachIndexed { index, articlesBean ->
            SimpleNewsModel_()
                .id(index)
                .articlesBean(articlesBean)
                .addTo(this)
        }
    }

}