package com.example.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.news.home.HomeViewModel
import com.example.news.home.HomeViewModel.Companion.DEFAULT_DOMAIN
import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.search.SearchViewModel
import com.example.news.sharepreferences.PreferenceConst.SELECT_DOMAIN
import com.example.news.sharepreferences.Preferences
import com.example.news.util.ViewStatus
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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
abstract class BaseUnitTest {

    @MockK
    lateinit var repository: NewsRepository

    @MockK
    lateinit var kRepository: NewsRepository

    /** 避免 LiveData 在 isMainThread 發生 NullPointException */
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
        kRepository = mockk()
    }

}
