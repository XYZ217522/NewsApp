package com.example.news.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.news.getOrAwaitValue
import com.example.news.networking.AuthInterceptor
import com.example.news.networking.provideNewsApi
import com.example.news.networking.provideOkHttpClient
import com.example.news.networking.provideRetrofit
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.PreferenceConst
import com.example.news.sharepreferences.Preferences
import com.example.news.util.ViewStatus
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeViewModelJUnit4Test : TestCase() {

    private lateinit var mHomeViewModel: HomeViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()

        val context = ApplicationProvider.getApplicationContext<Context>()
        val preferences = Preferences(context)
            .apply { remove(PreferenceConst.SELECT_DOMAIN) }

        val retrofit = provideRetrofit(provideOkHttpClient(AuthInterceptor()))

        val repository = NewsRepository(provideNewsApi(retrofit), preferences)

        mHomeViewModel = HomeViewModel(repository, preferences)
    }

    @Test
    fun test_title() {
//        val result1 = mHomeViewModel.titleLiveData.getOrAwaitValue()
//        println("result= $result1")
//        assert(result1 == HomeViewModel.DEFAULT_DOMAIN)

        val domain2 = "amazon.com"
        mHomeViewModel.getEverythingByDomain(domain2)
        Thread.sleep(1000)
        val result2 = mHomeViewModel.titleLiveData.getOrAwaitValue()
        println("result= $result2")
        assert(result2 == domain2)
    }

    @Test
    fun test_newsData() {
        val newsData = mHomeViewModel.newsEverythingLiveData.getOrAwaitValue().getContentIfNotHandled()
        println("newsData=$newsData")
        assert(newsData?.status == "ok")
    }

    @Test
    fun test_viewStatus() {
        val viewStatus = mHomeViewModel.viewStatusLiveData.getOrAwaitValue().getContentIfNotHandled()
        println("viewStatus=${viewStatus}")
        assert(viewStatus is ViewStatus.Loading)
    }

}