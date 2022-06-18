package com.example.news.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.PreferenceConst.SEARCH_HISTORY
import com.example.news.sharepreferences.Preferences
import com.example.news.util.Event
import com.example.news.util.SwitchSchedulers
import com.example.news.util.ViewStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.time.LocalDate


class SearchViewModel(
    private val repository: NewsRepository,
    private val preferences: Preferences,
) : ViewModel() {

    companion object {
        const val TAG = "SearchViewModel"
        const val MAX_RECORD_COUNTS = 20
    }

    private val compositeDisposable = CompositeDisposable()

    private val mGson by lazy { Gson() }
    private val mTypeToken = object : TypeToken<List<String>>() {}.type
    private val mFirstDayOfMonth by lazy { LocalDate.now().withDayOfMonth(1).toString() }

    val historyLiveData = MutableLiveData<List<String>>()
    val searchResultLiveData = MutableLiveData<Event<NewsData>>()
    val viewStatusLiveData = MutableLiveData<Event<ViewStatus>>()

    init {
        Log.d(TAG, "init.")
        getSearchHistory()
    }

    private fun getSearchHistory() {
        Flowable
            .just(preferences.getValue(SEARCH_HISTORY, ""))
            .filter { it.isNotEmpty() }
            .map { mGson.fromJson<List<String>>(it, mTypeToken) }
            .single(emptyList())
            .compose(SwitchSchedulers.applySingleSchedulers())
            .subscribe(
                { historyLiveData.value = it },
                { Log.e(TAG, "error=$it");viewStatusLiveData.value = errorEvent(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun errorEvent(throwable: Throwable?): Event<ViewStatus> {
        return Event(ViewStatus.ShowDialog(throwable?.toString() ?: "unknown error."))
    }

    private fun getToSaveList(searchText: String): List<String> {
        var historyList = historyLiveData.value ?: emptyList()
        historyList = if (historyList.size <= MAX_RECORD_COUNTS) {
            historyList.toMutableList().apply { add(0, searchText) }
        } else {
            historyList.toMutableList().apply { add(0, searchText);removeLast() }
        }
        historyList = historyList.distinct().toList()
        historyLiveData.value = historyList
        return historyList
    }


    fun search(searchText: String) {
        Log.d(TAG, "search:$searchText")
        Flowable
            .just(getToSaveList(searchText))
            .map { preferences.setValue(SEARCH_HISTORY, mGson.toJson(it));searchText }
            .single(searchText)
            .flatMap { repository.search(it, mFirstDayOfMonth) }
            .map { it.currentPage = 1;it }
            .compose(SwitchSchedulers.applySingleSchedulers())
            .subscribe(
                {
                    searchResultLiveData.value = Event(it)
                    viewStatusLiveData.value = Event(ViewStatus.GetDataSuccess)
                },
                { Log.e(TAG, "error=$it");viewStatusLiveData.value = errorEvent(it) }
            )
            .addTo(compositeDisposable)
    }

    fun clearHistory() {
        preferences.remove(SEARCH_HISTORY)
        historyLiveData.value = emptyList()
    }

}
