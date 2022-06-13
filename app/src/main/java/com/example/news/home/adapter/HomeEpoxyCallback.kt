package com.example.news.home.adapter

import com.example.news.model.ArticlesBean

interface HomeEpoxyCallback {

    fun onDomainClick(domain: String)

    fun onArticleClick(articlesBean: ArticlesBean)
}