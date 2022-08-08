package com.example.news.model

import com.example.news.repository.NewsRepository
import kotlin.math.ceil

data class NewsData(
    val status: String?,
    val totalResults: Int,
    val articles: MutableList<ArticlesBean>?,
) : Valid {
    override fun isValid(): Boolean = !articles.isNullOrEmpty()

    companion object {
        /** 利用api回傳的總數，取得api全部totalPage */
        fun NewsData.totalPage(): Int {
            val perSize = NewsRepository.PAGE_SIZE
            return if (totalResults > perSize) ceil(totalResults.toDouble() / perSize.toDouble()).toInt() else 1
        }


        fun mock(totalResults: Int? = null): NewsData {
            val articles = mutableListOf<ArticlesBean>()
            for (i in 0..(totalResults ?: 1)) {
                ArticlesBean(
                    source = null,
                    author = "GeorgeHsu",
                    title = "This is a fake article #${i + 1}",
                    description = "",
                    url = "https://www.google.com",
                    urlToImage = "",
                    publishedAt = "",
                    content = ""
                ).let { articles.add(it) }
            }
            return NewsData(status = "ok", totalResults = totalResults ?: 1, articles = articles)
        }
    }
}

data class ArticlesBean(
    val source: SourceBean?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
)

data class SourceBean(val id: String?, val name: String?)

data class TopHeadlinesData(
    var firstGroup: MutableList<ArticlesBean> = mutableListOf(),
    var secondGroup: MutableList<ArticlesBean> = mutableListOf(),
    var thirdGroup: MutableList<ArticlesBean> = mutableListOf(),
    var fourthGroup: MutableList<ArticlesBean> = mutableListOf(),
    var others: MutableList<ArticlesBean> = mutableListOf(),
)

fun NewsData.topHeadLine(): TopHeadlinesData {
    val data = TopHeadlinesData()
    val articles = this.articles ?: return data
    val groupMaxCount = 10
    for (articlesBean in articles) {
        if (data.firstGroup.size < groupMaxCount) {
            data.firstGroup.add(articlesBean)
            continue
        }
        if (data.secondGroup.size < groupMaxCount) {
            data.secondGroup.add(articlesBean)
            continue
        }
        if (data.thirdGroup.size < groupMaxCount) {
            data.thirdGroup.add(articlesBean)
            continue
        }
        if (data.fourthGroup.size < groupMaxCount) {
            data.fourthGroup.add(articlesBean)
            continue
        }

        data.others.add(articlesBean)
    }
    return data
}

fun ArticlesBean.getExtraInfoText(): String {
    val sb = StringBuilder()
    this.author?.let { sb.append("author : $it") }
    this.source?.name?.let {
        if (sb.isNotEmpty()) sb.append(" , source : $it") else sb.append("source : $it")
    }
    return sb.toString()
}