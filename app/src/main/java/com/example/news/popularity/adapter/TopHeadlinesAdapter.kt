package com.example.news.popularity.adapter

import com.airbnb.epoxy.EpoxyAdapter
import com.example.news.model.ArticlesBean

class TopHeadlinesAdapter(
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
            TopHeadLineModel_()
                .id(TAG + start)
                .rank(start)
                .articlesBean(it)
                .isBigType(isBigType)
                .listener(listener)
                .let { addModel(it) }
            start++
        }
    }
}