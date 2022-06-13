package com.example.news.model

data class NewsData(
    val status: String?,
    val totalResults: Int,
    val articles: MutableList<ArticlesBean>?
) {
    var currentPage: Int = 0
}

data class ArticlesBean(
    val source: SourceBean?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

data class SourceBean(val id: String?, val name: String?)