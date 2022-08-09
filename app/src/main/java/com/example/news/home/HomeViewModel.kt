package com.example.news.home

import HomeViewState
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.model.ArticlesBean
import com.example.news.model.NewsData
import com.example.news.sharepreferences.PreferenceConst.SELECT_DOMAIN
import com.example.news.sharepreferences.Preferences
import com.example.news.usecase.GetEveryThingUseCase
import com.example.news.util.*
import com.example.news.util.extensions.clearMutableList
import com.example.news.util.extensions.addAllMutableListBean
import com.example.news.util.extensions.lambdaMutableList
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subscribers.ResourceSubscriber

/**
 * 設計原則，
 * 在 ViewModel 李避免使用 activity or context，應該只將 resourceid 傳給 view
 * 建議：https://medium.com/androiddevelopers/locale-changes-and-the-androidviewmodel-antipattern-84eb677660d9
 *
 *
 * */
class HomeViewModel(
    private val getEveryThingUseCase: GetEveryThingUseCase,
    private val preferences: Preferences,
) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
        const val DEFAULT_DOMAIN = "apple.com"
    }

    private val compositeDisposable = CompositeDisposable()

    private var mCurrentDomain: String = preferences.getValue(SELECT_DOMAIN, DEFAULT_DOMAIN)

    val mTitleLiveData = MutableLiveData<String>()
    val mViewStatusLiveData = MutableLiveData<Event<HomeViewState>>()

    //state is used to describe a stage in a process (e.g. pending/dispatched).
    //status is used to describe an outcome of an operation (e.g. success/fail).
    // 若使用 Event<T> ，只會送一次，之後送null
    val mNewsDataLiveData = MutableLiveData<Event<NewsData>>()

    /* 由 ViewModel 掌管所有的資料，應只有一個區域掌管 Domain 的資料，Epoxy 仍被當為 View  */
    /* 當上游改變(newsEveryThingLiveData)時，會自動轉接到 articlesLiveData 讓分工更為細緻 */
    val mArticlesLiveData = MutableLiveData(Event(mutableListOf<ArticlesBean>()))

    fun onLoadMoreEveryThing() {
        Log.e(TAG, "onLoadMoreEveryThing")
        getEverythingByDomain(requestDomain = mCurrentDomain, isFirst = false)
    }

    fun onStartRequestEveryThing() {
        Log.e(TAG, "onStartRequestEveryThing")
        getEverythingByDomain(requestDomain = mCurrentDomain, isFirst = true)
    }

    fun onDomainClickRequestEveryThing(domain: String) {
        Log.e(TAG, "onDomainClickRequestEveryThing")
        mArticlesLiveData.lambdaMutableList { it.clear() }
        getEverythingByDomain(requestDomain = domain, isFirst = true)
    }

    /**
     * requestDomain 有二種可能性
     * (1) requestDomain 跟 currentDomain 相同(要維持)、不相同(以 requestDomain 為主)
     * (2) resetPage 為 true 時 useCase ０
     * (3) isFirst 為 true 時，
     * */
    private fun getEverythingByDomain(requestDomain: String, isFirst: Boolean) {
        Log.d(TAG, "getEverythingByDomain domain=$requestDomain")

        if (isFirst) mViewStatusLiveData.value = Event(HomeViewState.Loading)

        val isNotCurrentDomain = mCurrentDomain != requestDomain

        // 此情況直接回傳Flowable.empty()，不觸發onNext()
        val domainFlowable = Single
            .just(requestDomain)
            .cache()

        val getEverythingObservable = domainFlowable.flatMap {
            getEveryThingUseCase.getEveryThing(it)
        }

        Single
            .concat(domainFlowable, getEverythingObservable)
            .compose(SwitchSchedulers.applyFlowableSchedulers())
            .subscribeWith(createGetEveryThingSubject(isNotCurrentDomain, isFirst))
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
        mTitleLiveData.value = domain
    }

    private fun handleError(message: String?, isFirst: Boolean) {
        if (isFirst) {
            mViewStatusLiveData.value = Event(HomeViewState.GetDataFail(message ?: "unknown error"))
        } else {
            mViewStatusLiveData.value = Event(HomeViewState.GetLoadMoreDataFail)
        }
    }

    private fun handleNewsData(newsData: NewsData, isNotDefaultDomain: Boolean) {
        Log.d(TAG, "handleNewsData: ${newsData.isValid()}")
        mNewsDataLiveData.value = Event(newsData)
        mArticlesLiveData.lambdaMutableList { it.addAll(newsData.articles ?: mutableListOf()) }
        mViewStatusLiveData.value =
            Event(HomeViewState.GetDataSuccess(getEveryThingUseCase.pagingParam.isCanLoadMore()))
        if (isNotDefaultDomain) mViewStatusLiveData.value = Event(HomeViewState.ScrollToUp)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}