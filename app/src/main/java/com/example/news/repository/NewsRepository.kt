package com.example.news.repository

import android.util.Log
import com.example.news.networking.NewsApi
import com.example.news.model.NewsData
import com.example.news.sharepreferences.Preferences
import io.reactivex.Single

open class NewsRepository(private val mNewsApi: NewsApi, private val mPreferences: Preferences) {

    companion object {
        const val TAG = "NewsRepository"
        const val PAGE_SIZE = 20 // The number of results to return per page.
    }

    fun getTopHeadlines(category: String, page: Int): Single<NewsData> {
        return mNewsApi
            .getTopHeadlines(category, page, PAGE_SIZE)
    }

    // apple.com
    fun getEverything(domains: String, page: Int): Single<NewsData> {
        Log.d(TAG, "getEverything,domain:$domains,page:$page")
        return mNewsApi.getEverything(domains, page, PAGE_SIZE)
    }

    //  tesla , 2022-05-22
    fun search(query: String, from: String): Single<NewsData> {
        Log.d(TAG, "search,query:$query,from:$from")
        return mNewsApi.search(query, from)
    }

    // apple, 2022-05-22, 2022-05-23,
    fun searchPopularity(query: String, from: String): Single<NewsData> {
        return mNewsApi.searchPopularity(query, from)
    }
}