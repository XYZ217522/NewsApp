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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
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

    private var mSearchText: String = ""

    private val mGson by lazy { Gson() }
    private val mTypeToken = object : TypeToken<List<String>>() {}.type
    private val mSearchStartDay by lazy { LocalDate.now().minusMonths(1).toString() }

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

    private fun validation(searchText: String): Boolean {
        val result = searchText != mSearchText
        return result.apply {
            val viewStatus = if (result) ViewStatus.Loading else ViewStatus.GetDataSuccess
            viewStatusLiveData.value = Event(viewStatus)
        }
    }

    private fun getToSaveList(searchText: String): List<String> {
        val historyList = historyLiveData.value ?: emptyList()
        val removeLast = historyList.size > MAX_RECORD_COUNTS
        return historyList.toMutableList()
            .apply { add(0, searchText) }
            .apply { if (removeLast) removeLast() }.distinct().toList()
            .apply { historyLiveData.postValue(this) }
    }

    fun search(searchText: String) {
        Log.d(TAG, "search:$searchText")
        Flowable
            .just(searchText)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { validation(searchText) }
            .observeOn(Schedulers.io())
            .map { getToSaveList(it) }
            .map { preferences.setValue(SEARCH_HISTORY, mGson.toJson(it)).run { searchText } }
            .flatMap { repository.search(it, mSearchStartDay).toFlowable() }
//            .map { it.apply { this.currentPage = 1 }.also { mSearchText = searchText } }
            .compose(SwitchSchedulers.applyFlowableSchedulers())
            .subscribe(
                {
                    searchResultLiveData.value = Event(it)
                    if (it.articles.isNullOrEmpty()) {
                        viewStatusLiveData.value = errorEvent(Exception("articles Empty "))
                    } else {
                        viewStatusLiveData.value = Event(ViewStatus.GetDataSuccess)
                        viewStatusLiveData.value = Event(ViewStatus.ScrollToUp)
                    }
                },
                {
                    Log.e(TAG, "error=$it")
                    viewStatusLiveData.value = errorEvent(it)
                }
            )
            .addTo(compositeDisposable)
    }

    fun clearHistory() {
        Log.d(TAG, "clearHistory.")
        preferences.remove(SEARCH_HISTORY)
        historyLiveData.value = emptyList()
    }

}
