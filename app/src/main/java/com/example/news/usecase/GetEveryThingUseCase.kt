package com.example.news.usecase

import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.repository.PagingParamByPage
import io.reactivex.Single

class GetEveryThingUseCase(private val repository: NewsRepository) {
    var pagingParam = PagingParamByPage.getEveryThingPagingParam()
    private var currentDomain = ""

    /** currentPage.default = 0 ，等取值成功後才改為1，避免失敗時有誤加 currentPage 的問題
    * */
    fun getEveryThing(domain: String): Single<NewsData> {

        if (currentDomain != domain) {
            currentDomain = domain
            pagingParam.reset()
        }
        return repository
            .getEverything(domain, pagingParam.currentPage + 1)
            .doOnSuccess {
                pagingParam.setTotalItem(it.totalResults)
                pagingParam.offsetNextPage()
            }
    }
}