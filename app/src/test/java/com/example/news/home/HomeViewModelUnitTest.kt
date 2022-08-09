package com.example.news.home//package com.example.news
//
//import com.example.news.home.HomeViewModel
//import com.example.news.home.HomeViewModel.Companion.DEFAULT_DOMAIN
//import com.example.news.model.NewsData
//import com.example.news.sharepreferences.PreferenceConst.SELECT_DOMAIN
//import com.example.news.sharepreferences.Preferences
//import com.example.news.util.ViewStatus
//import io.reactivex.Single
//import org.junit.Assert
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.Mockito.`when`
//import org.mockito.Mockito.mock
//import org.mockito.junit.MockitoJUnitRunner
//
//@RunWith(MockitoJUnitRunner::class)
//class HomeViewModelUnitTest : BaseUnitTest() {
//
//    @Mock
//    lateinit var newsViewModel: HomeViewModel
//
//    @Test
//    fun repository_TEST() {
//        val testDomain = "google.com"
//        val testPage = 1
//
//        `when`(repository.getEverything(testDomain, testPage))
//            .thenReturn(Single.just(NewsData.mock()))
//
//        val result = repository.getEverything(testDomain, testPage).blockingGet()
//        println("getEverything_TEST result = $result")
//        Mockito.verify(repository).getEverything(testDomain, testPage)
//        Assert.assertEquals(NewsData.mock(), result)
//    }
//
//    @Test
//    fun viewModel_TEST() {
//        val testDomain = "google.com"
//        val testPage = 1
//
//        val mockNewsData = NewsData.mock()
//        val mockPreferences = mock(Preferences::class.java)
//        `when`(repository.getEverything(testDomain, testPage))
//            .thenReturn(Single.just(mockNewsData))
//        `when`(mockPreferences.getValue(SELECT_DOMAIN, DEFAULT_DOMAIN))
//            .thenReturn(testDomain)
//
//        newsViewModel = HomeViewModel(repository, mockPreferences)
//
//        val title = newsViewModel.titleLiveData.value
//        println("viewModel_TEST title = $title")
//        Assert.assertEquals(title, testDomain)
//
//        val result = newsViewModel.newsEverythingLiveData.value?.getContentIfNotHandled()
//        println("viewModel_TEST result = $result")
//        Assert.assertEquals(NewsData.mock(), result)
//
//        val viewState = newsViewModel.viewStatusLiveData.value?.getContentIfNotHandled()
//        println("viewModel_TEST viewState = $viewState")
//        Assert.assertTrue(viewState is ViewStatus.ScrollToUp)
//    }
//
//    @Test
//    fun loadMore_TEST() {
//        val testDomain = "google.com"
//        var testPage = 1
//
//        val mockNewsData = NewsData.mock(21)
//        val mockPreferences = mock(Preferences::class.java)
//        `when`(repository.getEverything(testDomain, testPage))
//            .thenReturn(Single.just(mockNewsData))
//        `when`(mockPreferences.getValue(SELECT_DOMAIN, DEFAULT_DOMAIN))
//            .thenReturn(testDomain)
//        newsViewModel = HomeViewModel(repository, mockPreferences)
//
//        // mock loadMore load page = 2
//        testPage = 2
//        `when`(repository.getEverything(testDomain, testPage))
//            .thenReturn(Single.just(mockNewsData))
//
//        newsViewModel.loadMore(false)
//
//        val viewState = newsViewModel.viewStatusLiveData.value?.getContentIfNotHandled()
//        println("loadMore_TEST viewState = $viewState")
//        Assert.assertTrue(viewState is ViewStatus.GetDataSuccess)
//    }
//}
