package com.example.news.popularity.adapter

import com.airbnb.epoxy.EpoxyAdapter
import com.example.news.model.ArticlesBean
import com.example.news.popularity.BigTopHeadLineModel_

class TopHeadLineAdapter(
    articles: List<ArticlesBean>,
    startNumber: Int,
    isBigType: Boolean,
    listener: PopularityEpoxyCallback,
) : EpoxyAdapter() {

    companion object {
        private const val TAG = "TopHeadLineAdapter"
    }

    init {
        enableDiffing()

        var start = startNumber
        articles.forEach { it: ArticlesBean ->
            BigTopHeadLineModel_()
                .id(TAG + start)
                .rank(start)
                .articlesBean(it)
                .listener(listener)
                .let { addModel(it) }
            start++
        }
    }
}