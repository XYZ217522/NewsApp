package com.example.news.popularity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.model.NewsData
import com.example.news.model.TopHeadlinesData
import com.example.news.model.topHeadLine
import com.example.news.repository.NewsRepository
import com.example.news.sharepreferences.PreferenceConst.SELECT_CATEGORY
import com.example.news.sharepreferences.PreferenceConst.SELECT_COUNTRY
import com.example.news.sharepreferences.Preferences
import com.example.news.util.Event
import com.example.news.util.SwitchSchedulers
import com.example.news.util.ViewStatus
import com.example.news.util.getTotalPage
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subscribers.ResourceSubscriber

class PopularityViewModel(
    private val repository: NewsRepository,
    private val preferences: Preferences,
) : ViewModel() {

    companion object {
        private const val TAG = "PopularityViewModel"
        private const val DEFAULT_COUNTRY = "tw"
        private const val DEFAULT_CATEGORY = "entertainment"
        const val MAX_POPULARITY_PAGE = 3
        val DEFAULT_PAIR = Pair(DEFAULT_COUNTRY, DEFAULT_CATEGORY)
    }

    private val compositeDisposable = CompositeDisposable()

    private var mCurrentPage = 1
    private var mTotalResults = 0
    private var mCountry: String? = null
    private var mCategory: String? = null

    // 若使用Event<T>，只會送一次，之後送null
    val topHeadLineLiveData = MutableLiveData<Event<TopHeadlinesData>>()
    val popularityLiveData = MutableLiveData<Event<NewsData>>()
    val titleLiveData = MutableLiveData<Pair<String, String>>()
    val viewStatusLiveData = MutableLiveData<Event<ViewStatus>>()

    init {
        Log.d(TAG, "init.")
        viewStatusLiveData.value = Event(ViewStatus.Loading)
        fetchDataByApi()
    }

    private fun fetchDataByApi(country: String? = null, category: String? = null) {

        val titleFlowable = getTitleFlowable(country, category).cache()
        val topHeadlinesFlowable = titleFlowable
            .defaultSinglePair()
            .flatMap { repository.getTopHeadlines(it.first, it.second) }
            .map { it.topHeadLine() }
            .toFlowable()
        val popularityFlowable = getPopularitySingle(titleFlowable).toFlowable()

        Flowable
            .concat(titleFlowable, topHeadlinesFlowable, popularityFlowable)
            .compose(SwitchSchedulers.applyFlowableSchedulers())
            .subscribeWith(object : ResourceSubscriber<Any>() {
                override fun onStart() = request(1)
                override fun onNext(t: Any) {
                    Log.d(TAG, "onNext = $t")
                    request(1)
                    when (t) {
                        is Pair<*, *> -> titleLiveData.value = t as Pair<String, String>
                        is TopHeadlinesData -> {
                            topHeadLineLiveData.value = Event(t)
                            viewStatusLiveData.value = Event(ViewStatus.GetDataSuccess)
                        }
                        is NewsData -> t.handleNewsData()
                    }
                }

                override fun onError(t: Throwable?) {
                    Log.e(TAG, t.toString())
                    viewStatusLiveData.value = errorEvent(t)
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete.")
                }
            })
            .addTo(compositeDisposable)
    }

    private fun NewsData.handleNewsData() {
        if (this.currentPage == 1 && this.articles.isNullOrEmpty()) {
            viewStatusLiveData.value = Event(ViewStatus.ShowDialog("Articles Empty."))
            return
        }

        mTotalResults = this.totalResults
        popularityLiveData.value = Event(this)
    }

    private fun getTitleFlowable(
        selectCountry: String? = null,
        selectCategory: String? = null,
    ): Flowable<Pair<String, String>> {
        var country = saveSp(SELECT_COUNTRY, selectCountry) ?: mCountry
        var category = saveSp(SELECT_CATEGORY, selectCategory) ?: mCategory
        country = (country ?: preferences.getValue(SELECT_COUNTRY, DEFAULT_COUNTRY)).also {
            mCountry = it
        }
        category = category ?: preferences.getValue(SELECT_CATEGORY, DEFAULT_CATEGORY).also {
            mCategory = it
        }
        return Flowable.just(Pair(country, category))
    }

    private fun getPopularitySingle(titleFlowable: Flowable<Pair<String, String>>? = null): Single<NewsData> {
        val single = titleFlowable?.defaultSinglePair() ?: getTitleFlowable().single(DEFAULT_PAIR)
        return single
            .flatMap { repository.searchPopularity(it.second, mCurrentPage, it.first) }
            .map { it.apply { currentPage = mCurrentPage } }
    }

    private fun Flowable<Pair<String, String>>?.defaultSinglePair(): Single<Pair<String, String>> {
        return this?.single(DEFAULT_PAIR) ?: Single.just(DEFAULT_PAIR)
    }

    private fun saveSp(key: String, value: String?): String? {
        value ?: return null
        preferences.setValue(key, value)
        return value
    }

    private fun errorEvent(throwable: Throwable?): Event<ViewStatus> {
        return Event(ViewStatus.ShowDialog(throwable?.toString() ?: "unknown error."))
    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }

    fun loadMore(selectMode: Boolean) {
        Log.d(TAG, "loadMore，mCurrentPage=$mCurrentPage")
        if (selectMode) run { Log.d(TAG, "is selectMode can't loadMore."); return }
        val nextPage = mCurrentPage + 1
        val totalPage = mTotalResults.getTotalPage()
        if (nextPage <= totalPage && nextPage <= MAX_POPULARITY_PAGE) {
            Log.d(TAG, "loadMore，fetch next page data!")
            mCurrentPage++
            getPopularitySingle()
                .compose(SwitchSchedulers.applySingleSchedulers())
                .subscribe(
                    { popularityLiveData.value = Event(it) },
                    { Log.e(TAG, "error=$it");viewStatusLiveData.value = errorEvent(it) }
                )
                .addTo(compositeDisposable)
        }
    }

    private fun resetDataPage() {
        mCurrentPage = 1
        mTotalResults = 0
    }

    fun checkValueAfterFetchData(selectCountry: String, selectCategory: String): Boolean {
        val pairData = titleLiveData.value ?: return true // not happen
        if (pairData.first == selectCountry && pairData.second == selectCategory) {
            return false
        }
        resetDataPage()
        viewStatusLiveData.value = Event(ViewStatus.Loading)
        fetchDataByApi(selectCountry, selectCategory)
        return true
    }

    fun restoreTitlePairData() {
        val pairData = titleLiveData.value ?: return
        titleLiveData.value = pairData
    }
}