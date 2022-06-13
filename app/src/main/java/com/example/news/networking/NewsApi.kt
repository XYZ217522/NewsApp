package com.example.news.networking

import com.example.news.model.NewsData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @category Find sources that display news of this category.
 * Possible options: business entertainment general health science sports technology. Default: all categories.
 *
 * @language Find sources that display news in a specific language.
 * Possible options: ar de en es fr he it nl no pt ru sv ud zh. Default: all languages.
 *
 * @country Find sources that display news in a specific country.
 * Possible options: ae ar at au be bg br ca ch cn co cu cz de eg fr gb gr hk hu id ie il in it jp
 * kr lt lv ma mx my ng nl no nz ph pl pt ro rs ru sa se sg si sk th tr tw ua us ve za
 * Default: all countries.
 */
interface NewsApi {

    /**
     * Top business headlines in the US right now
     */
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("category") category: String, // e.g. business
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("country") country: String = "tw", // e.g. us
    ): Single<NewsData>

    /**
     * All articles published by the Wall Street Journal in the last 6 months, sorted by recent first
     */
    @GET("everything")
    fun getEverything(
        @Query("domains") domains: String,// e.g. wsj.com
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): Single<NewsData>

    /**
     * All articles about Tesla from the last month, sorted by recent first
     */
    @GET("everything")
    fun search(
        @Query("q") q: String, // e.g. tesla
        @Query("from") from: String, // e.g. 2022-05-22
        @Query("sortBy") sortBy: String, // e.g. publishedAt
    ): Single<NewsData>

    /**
     * All articles about Tesla from the last month, sorted by recent first
     */
    @GET("everything")
    fun searchPopularity(
        @Query("q") q: String, // e.g. apple
        @Query("from") from: String, // e.g. 2022-05-22
        @Query("to") to: String, // e.g. 2022-05-23
        @Query("sortBy") sortBy: String = "popularity", // e.g. popularity
    ): Single<NewsData>
}
