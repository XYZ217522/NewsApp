package com.example.news.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.news.networking.NewsApi
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.PreferenceConst
import com.example.news.sharepreferences.Preferences
import enqueueResponse
import getImmediateScheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.TestCase
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class ApiTest : TestCase() {

    lateinit var mockService: NewsApi
    lateinit var mockServer: MockWebServer
    lateinit var mRepository: NewsRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

//    @get:Rule
//    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val immediateScheduler by lazy { getImmediateScheduler() }

    @Before
    public override fun setUp() {
        super.setUp()
        RxJavaPlugins.setInitIoSchedulerHandler { immediateScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }

        mockServer = MockWebServer()
        mockService = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NewsApi::class.java)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val preferences = Preferences(context)
            .apply { remove(PreferenceConst.SELECT_DOMAIN) }

        mRepository = NewsRepository(mockService, preferences)
//        mHomeViewModel = HomeViewModel(mRepository, preferences)
    }

    @After
    public override fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun getEverythingTest() {
        this.javaClass.enqueueResponse(mockServer, "everything_appleCom.json")
//        val response = mockService.getEverything("apple.com", 1, 20)
        val response = mRepository.getEverything("apple.com", 1)
        val newsData = response.blockingGet()
        assertEquals("ok", newsData?.status)
        assertEquals(83, newsData?.totalResults)
        assertEquals(20, newsData?.articles?.size)
    }

    @Test
    fun searchTest() {
        this.javaClass.enqueueResponse(mockServer, "search_tesla.json")
        val response = mRepository.search("tesla", "2022-05-25")
        val newsData = response.blockingGet()
        assertEquals("ok", newsData?.status)
        assertEquals(50, newsData?.articles?.size)
    }

    @Test
    fun topHeadlinesTest() {
        this.javaClass.enqueueResponse(mockServer, "topHeadlines_tw_sports.json")
        val response = mRepository.getTopHeadlines("tw", "sports")
        val newsData = response.blockingGet()
        assertEquals("ok", newsData?.status)
        assertEquals(70, newsData?.totalResults)
        assertEquals(50, newsData?.articles?.size)
    }
}