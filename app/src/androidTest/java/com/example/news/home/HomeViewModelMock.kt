package com.example.news.home

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.news.model.NewsData
import com.example.news.networking.AuthInterceptor
import com.example.news.networking.provideNewsApi
import com.example.news.networking.provideOkHttpClient
import com.example.news.networking.provideRetrofit
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.PreferenceConst
import com.example.news.sharepreferences.Preferences
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelMockTest : TestCase() {

//    @Mock
    lateinit var mHomeViewModel: HomeViewModel

//    private val context = mock(Application::class.java)
//    private val application = mock(Application::class.java)

//    private val retrofit = provideRetrofit(provideOkHttpClient(AuthInterceptor()))
//    private val newApi = provideNewsApi(retrofit)
//    private val preferences = Preferences(context)
//    private val preferences = mock(Preferences::class.java)
//    private val repository = NewsRepository(newApi, preferences)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val immediateScheduler = object : Scheduler() {
        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker({ it.run() }, false)
        }
    }

    @Before
    public override fun setUp() {
        super.setUp()
        RxJavaPlugins.setInitIoSchedulerHandler { immediateScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }

        val context = ApplicationProvider.getApplicationContext<Context>()
        val preferences = Preferences(context)
            .apply { remove(PreferenceConst.SELECT_DOMAIN) }

        val retrofit = provideRetrofit(provideOkHttpClient(AuthInterceptor()))

        val repository = NewsRepository(provideNewsApi(retrofit), preferences)

        mHomeViewModel = HomeViewModel(repository, preferences)
    }

    @Test
    fun test() {
//        val result1 = mHomeViewModel.titleLiveData.getOrAwaitValue()
//        println("result= $result1")
//        assert(result1 == HomeViewModel.DEFAULT_DOMAIN)
//        println("context=$context")
//        val viewModel = HomeViewModel(repository, preferences)

        val domain = "amazon.com"
        val page = 1
        val mockResponse = mock(NewsData::class.java)
        println("mockResponse = $mockResponse")
//        Mockito.doReturn(mock(NewsData::class.java)).`when`(repository)
        `when`(mHomeViewModel.repository.getEverything(domain, page)).then { Single.just(mockResponse) }

        // when sending request
        val result = mHomeViewModel.getEverythingByDomain(domain)

        // then verify result
        val expected = Result.success(mockResponse)
        assertEquals(expected, result)


//        val domain2 = "amazon.com"
//        mHomeViewModel.getEverythingByDomain(domain2)
//        Thread.sleep(1000)
//        val result2 = mHomeViewModel.titleLiveData.getOrAwaitValue()
//        println("result= $result2")
//        assert(result2 == domain2)
    }

    @Test
    fun test_newsData() {
//        val newsData =
//            mHomeViewModel.newsEverythingLiveData.getOrAwaitValue().getContentIfNotHandled()
//        println("newsData=$newsData")
//        assert(newsData?.status == "ok")
    }

    @Test
    fun test_viewStatus() {
//        val viewStatus =
//            mHomeViewModel.viewStatusLiveData.getOrAwaitValue().getContentIfNotHandled()
//        println("viewStatus=${viewStatus}")
//        assert(viewStatus is ViewStatus.Loading)
    }

}