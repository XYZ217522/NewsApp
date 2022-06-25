package com.example.news.search

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.news.getOrAwaitValue
import com.example.news.networking.NewsApi
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.PreferenceConst
import com.example.news.sharepreferences.Preferences
import com.example.news.util.ViewStatus
import com.google.gson.Gson
import enqueueResponse
import getImmediateScheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.TestCase.assertEquals
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
class SearchViewModelTest {

    private val immediateScheduler by lazy { getImmediateScheduler() }

    private lateinit var mockServer: MockWebServer
    private lateinit var mRepository: NewsRepository
    private lateinit var mPreferences: Preferences

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        RxJavaPlugins.setInitIoSchedulerHandler { immediateScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }

        mockServer = MockWebServer()
        val mockService = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NewsApi::class.java)

        val context = ApplicationProvider.getApplicationContext<Context>()
        mPreferences = Preferences(context)
            .apply { remove(PreferenceConst.SEARCH_HISTORY) }

        mRepository = NewsRepository(mockService, mPreferences)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }


    @Test
    fun getHistoryLiveData_test() {
        val mockSearchList = listOf("A", "B", "C", "D")
        mPreferences.setValue(PreferenceConst.SEARCH_HISTORY, Gson().toJson(mockSearchList))

        val searchViewModel = SearchViewModel(mRepository, mPreferences)
        val result = searchViewModel.historyLiveData.getOrAwaitValue()
        println(result)

        assert(result.isNotEmpty())
        assertEquals(mockSearchList.size, result.size)
    }

    @Test
    fun search_test() {
        this.javaClass.enqueueResponse(mockServer, "search_tesla.json")
        val searchText = "tesla"
        val searchViewModel = SearchViewModel(mRepository, mPreferences)
        searchViewModel.search(searchText)

        val history = searchViewModel.historyLiveData.getOrAwaitValue()
        assert(history.isNotEmpty())
        assertEquals(1, history.size)
        assertEquals(history[0], searchText)

        val state = searchViewModel.viewStatusLiveData.getOrAwaitValue().getContentIfNotHandled()
        assert(state is ViewStatus.ScrollToUp)

        val result = searchViewModel.searchResultLiveData.getOrAwaitValue().getContentIfNotHandled()
        assert(!result?.articles.isNullOrEmpty())
    }

    @Test
    fun search_empty_test() {
        this.javaClass.enqueueResponse(mockServer, "empty_result.json")

        val searchText = "GG"
        val searchViewModel = SearchViewModel(mRepository, mPreferences)
        searchViewModel.search(searchText)

        val history = searchViewModel.historyLiveData.getOrAwaitValue()
        assert(history.isNotEmpty())
        assertEquals(1, history.size)
        assertEquals(history[0], searchText)

        val state = searchViewModel.viewStatusLiveData.getOrAwaitValue().getContentIfNotHandled()
        assert(state is ViewStatus.ShowDialog)

        val result = searchViewModel.searchResultLiveData.getOrAwaitValue().getContentIfNotHandled()
        assert(result?.articles?.isEmpty() == true)
    }
}