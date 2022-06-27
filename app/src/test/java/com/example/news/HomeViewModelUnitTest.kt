package com.example.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.news.home.HomeViewModel
import com.example.news.home.HomeViewModel.Companion.DEFAULT_DOMAIN
import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.PreferenceConst.SELECT_DOMAIN
import com.example.news.sharepreferences.Preferences
import com.example.news.util.ViewStatus
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelUnitTest {

    @Mock
    lateinit var repository: NewsRepository

    @Mock
    lateinit var newsViewModel: HomeViewModel


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val immediateScheduler = object : Scheduler() {
        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker({ it.run() }, false)
        }
    }

    @Before
    fun init() {
        RxJavaPlugins.setInitIoSchedulerHandler { immediateScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }
        repository = mock(NewsRepository::class.java)
    }

    @Test
    fun repository_TEST() {
        val testDomain = "google.com"
        val testPage = 1

        `when`(repository.getEverything(testDomain, testPage))
            .thenReturn(Single.just(NewsData.mock()))

        val result = repository.getEverything(testDomain, testPage).blockingGet()
        println("getEverything_TEST result = $result")
        Mockito.verify(repository).getEverything(testDomain, testPage)
        Assert.assertEquals(NewsData.mock(), result)
    }

    @Test
    fun viewModel_TEST() {
        val testDomain = "google.com"
        val testPage = 1

        val mockNewsData = NewsData.mock()
        val mockPreferences = mock(Preferences::class.java)
        `when`(repository.getEverything(testDomain, testPage))
            .thenReturn(Single.just(mockNewsData))
        `when`(mockPreferences.getValue(SELECT_DOMAIN, DEFAULT_DOMAIN))
            .thenReturn(testDomain)

        newsViewModel = HomeViewModel(repository, mockPreferences)

        val title = newsViewModel.titleLiveData.value
        println("viewModel_TEST title = $title")
        Assert.assertEquals(title, testDomain)

        val result = newsViewModel.newsEverythingLiveData.value?.getContentIfNotHandled()
        println("viewModel_TEST result = $result")
        Assert.assertEquals(NewsData.mock(), result)

        val viewState = newsViewModel.viewStatusLiveData.value?.getContentIfNotHandled()
        println("viewModel_TEST viewState = $viewState")
        Assert.assertTrue(viewState is ViewStatus.ScrollToUp)
    }

    @Test
    fun loadMore_TEST() {
        val testDomain = "google.com"
        var testPage = 1

        val mockNewsData = NewsData.mock(21)
        val mockPreferences = mock(Preferences::class.java)
        `when`(repository.getEverything(testDomain, testPage))
            .thenReturn(Single.just(mockNewsData))
        `when`(mockPreferences.getValue(SELECT_DOMAIN, DEFAULT_DOMAIN))
            .thenReturn(testDomain)
        newsViewModel = HomeViewModel(repository, mockPreferences)

        // mock loadMore load page = 2
        `when`(repository.getEverything(testDomain, 2))
            .thenReturn(Single.just(mockNewsData))

        newsViewModel.loadMore(false)

        val viewState = newsViewModel.viewStatusLiveData.value?.getContentIfNotHandled()
        println("loadMore_TEST viewState = $viewState")
        Assert.assertTrue(viewState is ViewStatus.GetDataSuccess)
    }
}
