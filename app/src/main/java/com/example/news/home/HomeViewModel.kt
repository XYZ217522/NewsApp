package com.example.news.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.Preferences
import com.example.news.util.SwitchSchedulers
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subscribers.ResourceSubscriber

class HomeViewModel(
    private val repository: NewsRepository,
    private val preferences: Preferences
) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
        const val SP_SELECT_DOMAIN = "SP_SELECT_DOMAIN"
    }

    private val compositeDisposable = CompositeDisposable()
    val newsEverythingLiveData = MutableLiveData<NewsData>()

//    init {
//        subscribe()
//    }

    fun subscribe() {
        Log.d(TAG, "subscribe.")
        getEverythingByDomain()
    }

    fun getEverythingByDomain(domain: String? = null) {
        Log.d(TAG, "getEverythingByDomain domain=$domain")
//        Observable
//            .just(domain ?: preferences.getValue(SP_SELECT_DOMAIN, "youtube.com"))
//            .doOnNext { preferences.setValue(SP_SELECT_DOMAIN, it) }
//            .singleOrError()
//            .flatMap { repository.getEverything(it) }
//            .compose(SwitchSchedulers.applySingleSchedulers())
//            .subscribe(
//                { newsData -> newsEverythingLiveData.value = newsData },
//                { e -> Log.e(TAG, e.toString()) }
//            ).addTo(compositeDisposable)

        val domainFlowable = Flowable
            .just(domain ?: preferences.getValue(SP_SELECT_DOMAIN, "youtube.com"))
            .doOnNext { preferences.setValue(SP_SELECT_DOMAIN, it) }
            .cache()
        val getEverythingObservable = domainFlowable
            .singleOrError()
            .flatMap { repository.getEverything(it) }
            .toFlowable()
        Flowable.concat(domainFlowable, getEverythingObservable)
            .compose(SwitchSchedulers.applyFlowableSchedulers())
            .subscribeWith(object : ResourceSubscriber<Any>() {
                override fun onStart() {
                    request(1)
                }

                override fun onNext(t: Any?) {
                    Log.d(TAG,"onNext = $t")
                    request(1)
                    when (t) {
                        is String -> Log.d(TAG,"domain = $t")
                        is NewsData -> newsEverythingLiveData.value = t
                    }
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG,"onError = $t")
                }

                override fun onComplete() {
                    Log.d(TAG,"onComplete.")
                }

            }).addTo(compositeDisposable)

    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }
}