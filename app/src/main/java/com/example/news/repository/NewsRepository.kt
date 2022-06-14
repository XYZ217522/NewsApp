package com.example.news.repository

import com.example.news.networking.NewsApi
import com.example.news.model.NewsData
import com.example.news.sharepreferences.Preferences
import io.reactivex.Single

open class NewsRepository(private val mNewsApi: NewsApi, private val mPreferences: Preferences) {

    companion object {
        const val PAGE_SIZE = 20 // The number of results to return per page.
    }

    fun getTopHeadlines(category: String, page: Int): Single<NewsData> {
        return mNewsApi
            .getTopHeadlines(category, page, PAGE_SIZE)
    }

    // apple.com
    fun getEverything(domains: String, page: Int): Single<NewsData> {
        return mNewsApi.getEverything(domains, page, PAGE_SIZE)
    }

    //  tesla , 2022-05-22, publishedAt
    fun search(query: String, from: String, sortBy: String): Single<NewsData> {
        return mNewsApi
            .search(query, from, sortBy)
    }

    // apple, 2022-05-22, 2022-05-23,
    fun searchPopularity(query: String, from: String, to: String): Single<NewsData> {
        return mNewsApi
            .searchPopularity(query, from, to)
    }
}