package com.example.news.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.util.SwitchSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class HomeViewModel(private val repository: NewsRepository) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val compositeDisposable = CompositeDisposable()
    val newsEverythingLiveData = MutableLiveData<NewsData>()

//    init {
//        subscribe()
//    }

     fun subscribe() {
        Log.d(TAG, "subscribe.")
        repository.getEverything("apple.com") //apple.com
            .compose(SwitchSchedulers.applySingleSchedulers())
            .subscribe(
                { newsData -> newsEverythingLiveData.value = newsData },
                { e -> Log.e(TAG, e.toString()) }
            ).addTo(compositeDisposable)
    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }
}