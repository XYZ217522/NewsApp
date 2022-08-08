package com.example.news.home

import HomeViewState
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.model.NewsData
import com.example.news.sharepreferences.PreferenceConst.SELECT_DOMAIN
import com.example.news.sharepreferences.Preferences
import com.example.news.util.*
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subscribers.ResourceSubscriber

class HomeViewModel(
    private val getEveryThingUseCase: GetEveryThingUseCase,
    private val preferences: Preferences,
) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
        const val DEFAULT_DOMAIN = "apple.com"
    }

    private val compositeDisposable = CompositeDisposable()

    //state is used to describe a stage in a process (e.g. pending/dispatched).
    //status is used to describe an outcome of an operation (e.g. success/fail).
    // 若使用Event<T>，只會送一次，之後送null
    val newsEverythingLiveData = MutableLiveData<Event<NewsData>>()

    val titleLiveData = MutableLiveData<String>()
    val viewStatusLiveData = MutableLiveData<Event<HomeViewState>>()

    private var mCurrentDomain: String = preferences.getValue(SELECT_DOMAIN, DEFAULT_DOMAIN)

    fun onLoadMoreEveryThing() {
        Log.e(TAG, "onLoadMoreEveryThing")
        getEverythingByDomain(mCurrentDomain, resetPage = false, isFirst = false)
    }

    fun onStartRequestEveryThing() {
        Log.e(TAG, "onStartRequestEveryThing")
        getEverythingByDomain(requestDomain = mCurrentDomain, resetPage = true, isFirst = true)
    }

    fun onDomainClickRequestEveryThing(domain: String) {
        Log.e(TAG, "onDomainClickRequestEveryThing")
        getEverythingByDomain(domain, resetPage = true, isFirst = true)
    }

    private fun getEverythingByDomain(requestDomain: String, resetPage: Boolean = false, isFirst: Boolean) {
        Log.d(TAG, "getEverythingByDomain domain=$requestDomain")

        if (resetPage) getEveryThingUseCase.resetCurrentPage()

        if (isFirst) viewStatusLiveData.value = Event(HomeViewState.Loading)

        val isNotCurrentDomain = mCurrentDomain != requestDomain

        // 此情況直接回傳Flowable.empty()，不觸發onNext()
        val domainFlowable = Single
            .just(requestDomain)
            .cache()

        val getEverythingObservable = domainFlowable.flatMap {
            getEveryThingUseCase
                .getEveryThing(it)
                .doOnSubscribe { }
        }

        Single
            .concat(domainFlowable, getEverythingObservable)
            .compose(SwitchSchedulers.applyFlowableSchedulers())
            .subscribeWith(createGetEveryThingSubject(isNotCurrentDomain, !isFirst))
            .addTo(compositeDisposable)
    }

    private fun createGetEveryThingSubject(isNotCurrentDomain: Boolean, isFirst: Boolean): ResourceSubscriber<Any> {
        return object : ResourceSubscriber<Any>() {
            override fun onNext(response: Any) {
                Log.d(TAG, "onNext = $response")
                when (response) {
                    is String -> updateTitle(response)
                    is NewsData ->
                        if (response.isValid()) handleNewsData(response, isNotCurrentDomain)
                        else handleError("Articles Empty.", isFirst)
                }
            }

            override fun onError(t: Throwable?) {
                Log.e(TAG, "onError = $t")
                handleError(t.toString(), isFirst)
            }

            override fun onComplete() {}
        }
    }

    private fun updateTitle(domain: String) {
        Log.d("updateTitle", domain)
        mCurrentDomain = domain
        preferences.setValue(SELECT_DOMAIN, domain);
        titleLiveData.value = domain
    }

    private fun handleError(message: String?, isFirst: Boolean) {
        if (isFirst) {
            viewStatusLiveData.value = Event(HomeViewState.GetDataFail(message ?: "unknown error"))
        } else {
            viewStatusLiveData.value = Event(HomeViewState.GetLoadMoreDataFail)
        }
    }

    private fun handleNewsData(newsData: NewsData, isNotDefaultDomain: Boolean) {
        Log.d(TAG, "handleNewsData: ${newsData.isValid()}")
        newsEverythingLiveData.value = Event(newsData)
        viewStatusLiveData.value = Event(HomeViewState.GetDataSuccess(getEveryThingUseCase.mIsCanLoadMore))
        if (isNotDefaultDomain) viewStatusLiveData.value = Event(HomeViewState.ScrollToUp)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}