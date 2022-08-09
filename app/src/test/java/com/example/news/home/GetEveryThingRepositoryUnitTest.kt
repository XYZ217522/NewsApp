package com.example.news.home

import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.usecase.GetEveryThingUseCase
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetEveryThingRepositoryUnitTest {

    @Mock
    lateinit var repository: NewsRepository

    lateinit var getEveryThingUseCase: GetEveryThingUseCase

    @Before
    fun setUp() {
        getEveryThingUseCase = GetEveryThingUseCase(repository)
    }

    @Test
    fun getEveryThingUseCase_Test_LoadMore() {
        val defaultDomain = "google.com"
        val changeNewDomain = "apple.com"

        `when`(repository.getEverything(defaultDomain, 1))
            .thenReturn(Single.just(NewsData.mockPage1()))

        `when`(repository.getEverything(defaultDomain, 2))
            .thenReturn(Single.just(NewsData.mockPage2()))

        val page1Result = getEveryThingUseCase.getEveryThing(defaultDomain).blockingGet()
        println(getEveryThingUseCase.pagingParam.isCanLoadMore())
        println(getEveryThingUseCase.pagingParam)
        println(getEveryThingUseCase.pagingParam.totalPage())
        Assert.assertEquals(getEveryThingUseCase.pagingParam.isCanLoadMore(), true)
        Assert.assertEquals(getEveryThingUseCase.pagingParam.currentPage, 2)

        val page2Result = getEveryThingUseCase.getEveryThing(defaultDomain).blockingGet()
        Assert.assertEquals(getEveryThingUseCase.pagingParam.isCanLoadMore(), false)
        Assert.assertEquals(getEveryThingUseCase.pagingParam.currentPage, 3)


        `when`(repository.getEverything(changeNewDomain, 1))
            .thenReturn(Single.just(NewsData.mock()))

        val newPage1Result = getEveryThingUseCase.getEveryThing(changeNewDomain).blockingGet()
        Assert.assertEquals(getEveryThingUseCase.pagingParam.isCanLoadMore(), false)
        Assert.assertEquals(getEveryThingUseCase.pagingParam.currentPage, 2)
    }


}