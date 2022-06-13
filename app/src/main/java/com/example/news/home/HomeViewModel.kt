package com.example.news.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.model.NewsData
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.Preferences
import com.example.news.util.SwitchSchedulers
import com.example.news.util.getTotalPage
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subscribers.ResourceSubscriber

class HomeViewModel(
    private val repository: NewsRepository,
    private val preferences: Preferences,
) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
        const val SP_SELECT_DOMAIN = "SP_SELECT_DOMAIN"
        const val DEFAULT_DOMAIN = "apple.com"
    }

    private val compositeDisposable = CompositeDisposable()

    private var mCurrentPage = 1
    private var mTotalResults = 0
    private var mCurrentDomain: String? = null

    val newsEverythingLiveData = MutableLiveData<NewsData>()
    val titleLiveData = MutableLiveData<String>()

//    init {
//        subscribe()
//    }

    fun subscribe() {
        Log.d(TAG, "subscribe.")
        getEverythingByDomain()
    }

    fun getEverythingByDomain(requestDomain: String? = null) {
        Log.d(TAG, "getEverythingByDomain domain=$requestDomain")
        val domainFlowable = if (mCurrentDomain != null && mCurrentDomain == requestDomain) {
            Flowable.empty() // 此情況直接回傳Flowable.empty()，不觸發onNext()
        } else {
            getDomainFlowable(requestDomain ?: mCurrentDomain).cache()
        }

        val getEverythingObservable = domainFlowable
            .defaultIfEmpty(mCurrentDomain ?: "")
            .single(mCurrentDomain ?: "")
            .flatMap { repository.getEverything(it, mCurrentPage) }
            .map { run { it.currentPage = mCurrentPage; it } }
            .toFlowable()

        Flowable.concat(domainFlowable, getEverythingObservable)
            .compose(SwitchSchedulers.applyFlowableSchedulers())
            .subscribeWith(object : ResourceSubscriber<Any>() {
                override fun onStart() {
                    request(1)
                }

                override fun onNext(t: Any?) {
                    Log.d(TAG, "onNext = $t")
                    request(1)
                    when (t) {
                        is String -> titleLiveData.value = t
                        is NewsData -> {
                            mTotalResults = t.totalResults
                            newsEverythingLiveData.value = t
                        }
                    }
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "onError = $t")
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete.")
                }

            }).addTo(compositeDisposable)

    }

    private fun getDomainFlowable(requestDomain: String? = null): Flowable<String> {
        return Flowable
            .just(requestDomain ?: preferences.getValue(SP_SELECT_DOMAIN, DEFAULT_DOMAIN))
            .doOnNext { mCurrentDomain = run { preferences.setValue(SP_SELECT_DOMAIN, it);it } }
    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }

    fun loadMore() {
        Log.d(TAG, "loadMore，mCurrentPage=$mCurrentPage")
        val nextPage = mCurrentPage + 1
        val totalPage = mTotalResults.getTotalPage()
        if (nextPage <= totalPage) {
            Log.d(TAG, "loadMore，fetch next page data!")
            mCurrentPage++
            getEverythingByDomain(mCurrentDomain)
        }
    }
}