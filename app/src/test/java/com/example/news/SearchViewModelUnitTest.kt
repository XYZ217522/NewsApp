package com.example.news

import androidx.lifecycle.Observer
import com.example.news.model.NewsData
import com.example.news.search.SearchViewModel
import com.example.news.sharepreferences.PreferenceConst
import com.example.news.sharepreferences.PreferenceConst.SEARCH_HISTORY
import com.example.news.sharepreferences.Preferences
import com.example.news.util.Event
import com.example.news.util.ViewStatus
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelUnitTest : BaseUnitTest() {

    @Mock
    lateinit var searchViewModel: SearchViewModel

    private val gson by lazy { Gson() }

    @Test
    fun getSearchHistory_TEST() {
        val mockHistoryList = listOf("a1", "b2", "c3")
        val mockPreferences = mockk<Preferences>()

        every { mockPreferences.getValue(any(), "") } returns gson.toJson(mockHistoryList)

        searchViewModel = SearchViewModel(repository, mockPreferences)
        val result = searchViewModel.historyLiveData.value
        assertEquals(mockHistoryList, result)
    }

    @Test
    fun search_TEST() {
        val mockHistoryList = listOf("a1", "b2", "c3")
        val mockSearchResult = NewsData.mock(1)
        val mockPreferences = mock(Preferences::class.java)
        val searchText = "NBA"
        val searchStartDay by lazy { LocalDate.now().minusMonths(1).toString() }

        `when`(mockPreferences.getValue(SEARCH_HISTORY, ""))
            .thenReturn(gson.toJson(mockHistoryList))

        `when`(repository.search(searchText, searchStartDay))
            .thenReturn(Single.just(mockSearchResult))

        searchViewModel = SearchViewModel(repository, mockPreferences)
        searchViewModel.search(searchText)

        val result1 = searchViewModel.historyLiveData.value
        assertEquals(mockHistoryList.size + 1, result1?.size)
        assertEquals(searchText, result1?.get(0))

        val result2 = searchViewModel.searchResultLiveData.value?.getContentIfNotHandled()
        assertEquals(mockSearchResult, result2)
        assertEquals(mockSearchResult.articles?.size, result2?.articles?.size)

    }
}
