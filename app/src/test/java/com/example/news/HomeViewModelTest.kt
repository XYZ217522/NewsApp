package com.example.news

import android.media.metrics.Event
import com.example.news.home.HomeViewModel
import com.example.news.home.HomeViewModel.Companion.DEFAULT_DOMAIN
import com.example.news.model.NewsData
import com.example.news.repository.PagingParamByPage
import com.example.news.sharepreferences.PreferenceConst.SELECT_DOMAIN
import com.example.news.sharepreferences.Preferences
import com.example.news.usecase.GetEveryThingUseCase
import com.example.news.util.ViewStatus
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest : BaseUnitTest() {

    @Mock
    lateinit var useCase: GetEveryThingUseCase

    @Mock
    lateinit var preferences: Preferences

    lateinit var viewModel: HomeViewModel


    @Before
    fun setUp() {
        /** construct 建構 preferences 參數，需要在建構之前就寫好when,return*/
        `when`(preferences.getValue(SELECT_DOMAIN, DEFAULT_DOMAIN))
            .thenReturn("apple.com")
        viewModel = HomeViewModel(useCase, preferences)
    }

    @Test
    fun getNewsEverythingLiveData_Is_Done() {
        val testDomain = "apple.com"
        `when`(useCase.getEveryThing(testDomain))
            .thenReturn(Single.just(NewsData.mock()))

        `when`(useCase.pagingParam)
            .thenReturn(PagingParamByPage(30, 1, 20).apply { setTotalItem(30) })

        viewModel.onStartRequestEveryThing()

        Assert.assertEquals(
            viewModel.mViewStatusLiveData.value?.peekContent(),
            HomeViewState.GetDataSuccess(true)
        )

        Assert.assertTrue(viewModel.mArticlesLiveData.value?.peekContent()?.isNotEmpty() == true)
    }

    @Test
    fun getTitleLiveData_Is_Done() {
    }

    @Test
    fun getViewStatusLiveData_Is_Done() {
    }

    @Test
    fun onLoadMoreEveryThing_Is_Done() {
    }

    @Test
    fun onStartRequestEveryThing_Is_Done() {
    }

    @Test
    fun onDomainClickRequestEveryThing_Is_Done() {
    }
}