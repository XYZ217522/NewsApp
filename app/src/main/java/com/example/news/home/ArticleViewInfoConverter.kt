package com.example.news.home

import com.example.news.model.ArticlesBean
import com.example.news.model.NewsData
import com.example.news.model.getExtraInfoText
import com.example.news.util.formatStringToDate

object ArticleViewInfoConverter {

    fun convertArticleViewInfo(articlesBean: ArticlesBean): ArticleViewInfo {
        return ArticleViewInfo(
            articlesBean.urlToImage ?: "",
            articlesBean.title ?: "",
            articlesBean.publishedAt?.formatStringToDate() ?: "",
            articlesBean.getExtraInfoText(),
            articlesBean.url ?: ""
        )
    }

}