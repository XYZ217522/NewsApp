package com.example.news.repository

import com.example.news.networking.NewsApi
import com.example.news.model.NewsData
import com.example.news.sharepreferences.Preferences
import io.reactivex.Single

open class NewsRepository(private val mNewsApi: NewsApi, private val mPreferences: Preferences) {

    // techcrunch , us , business
    fun getTopHeadlines(sources: String, country: String, category: String): Single<NewsData> {
        return mNewsApi
            .getTopHeadlines(sources, country, category)
    }

    // wsj.com
    fun getEverything(domains: String): Single<NewsData> = mNewsApi.getEverything(domains)

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