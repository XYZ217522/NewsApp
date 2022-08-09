package com.example.news.usecase

import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.repository.PagingParamByPage
import io.reactivex.Single

class GetEveryThingUseCase(private val repository: NewsRepository) {
    var pagingParam = PagingParamByPage.getEveryThingPagingParam()

    fun getEveryThing(domain: String): Single<NewsData> {
        return repository
            .getEverything(domain, pagingParam.offsetPage)
            .doOnSuccess {
                pagingParam.setTotalItem(it.totalResults)
                pagingParam.offsetNextPage()
            }
    }

    fun resetCurrentPage() {
        pagingParam.reset()
    }
}