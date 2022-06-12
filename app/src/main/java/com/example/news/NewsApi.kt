package com.example.news

import com.example.news.model.NewsData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    /**
     * Top business headlines in the US right now
     */
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("sources") sources: String, // e.g. techcrunch
        @Query("us") country: String, // e.g. us
        @Query("category") category: String // e.g. business
    ): Single<NewsData>

    /**
     * All articles published by the Wall Street Journal in the last 6 months, sorted by recent first
     */
    @GET("everything")
    fun getEverything(
        @Query("domains") domains: String // e.g. wsj.com
    ): Single<NewsData>

    /**
     * All articles about Tesla from the last month, sorted by recent first
     */
    @GET("everything")
    fun search(
        @Query("q") q: String, // e.g. tesla
        @Query("from") from: String, // e.g. 2022-05-22
        @Query("sortBy") sortBy: String // e.g. publishedAt
    ): Single<NewsData>

    /**
     * All articles about Tesla from the last month, sorted by recent first
     */
    @GET("everything")
    fun searchPopularity(
        @Query("q") q: String, // e.g. apple
        @Query("from") from: String, // e.g. 2022-05-22
        @Query("to") to: String, // e.g. 2022-05-23
        @Query("sortBy") sortBy: String = "popularity" // e.g. popularity
    ): Single<NewsData>
}
