package com.example.news.epoxy

import com.example.news.model.ArticlesBean

interface BaseNewsEpoxyCallback {

    fun onArticleClick(articlesBean: ArticlesBean)
}