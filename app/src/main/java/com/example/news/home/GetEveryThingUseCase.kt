package com.example.news.home

import com.example.news.model.NewsData
import com.example.news.model.NewsData.Companion.totalPage
import com.example.news.repository.NewsRepository
import io.reactivex.Single

class GetEveryThingUseCase(private val repository: NewsRepository) {
    private var mCurrentPage = 1

    var mIsCanLoadMore = false
        private set

    fun getEveryThing(domain: String): Single<NewsData> {
        return repository
            .getEverything(domain, mCurrentPage)
            .doOnSuccess { mCurrentPage += 1 }
            .doOnSuccess { mIsCanLoadMore = it.totalPage() > mCurrentPage }
    }

    fun resetCurrentPage() {
        mCurrentPage = 1
    }
}