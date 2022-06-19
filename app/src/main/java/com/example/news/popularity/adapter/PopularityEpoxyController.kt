package com.example.news.popularity.adapter

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.example.news.R
import com.example.news.epoxy.LoadingFooterModel_
import com.example.news.epoxy.SimpleNewsModel_
import com.example.news.home.adapter.HomeEpoxyController
import com.example.news.model.*
import com.example.news.popularity.PopularityViewModel.Companion.MAX_POPULARITY_PAGE
import com.example.news.util.getTotalPage

class PopularityEpoxyController(
    private val mCallback: PopularityEpoxyCallback,
) : EpoxyController() {

    companion object {
        private const val TAG = "PopularityEpoxyController"
        const val COUNTRY = "Country"
        const val CATEGORY = "Category"
    }

    private var mTopHeadLineData: TopHeadLineData? = null
    private var mPopularityData: NewsData? = null
    var isSelectMode = false

    var mSelectCountry = ""
        set(value) {
            field = value
            requestModelBuild()
        }
    var mSelectCategory = ""
        set(value) {
            field = value
            requestModelBuild()
        }

    @AutoModel
    lateinit var loadingLoadingFooterModel: LoadingFooterModel_

    @AutoModel
    lateinit var countryGridGroupModel: GridGroupModel_

    @AutoModel
    lateinit var gridGroupModel: GridGroupModel_

    private val countryPair by lazy { Pair(COUNTRY, countryList) }
    private val categoryPair by lazy { Pair(CATEGORY, categoryList) }

    override fun buildModels() {

        if (isSelectMode) {
            gridGroupModel
                .countryAdapter(getAdapter(countryPair, mSelectCountry))
                .categoryAdapter(getAdapter(categoryPair, mSelectCategory))
                .addTo(this)
            return
        }

        /** TopHeadLine */
        mTopHeadLineData?.let {
            if (it.firstGroup.isNotEmpty()) {
                HoriScrollModel_()
                    .id(HoriScrollModel::class.java.simpleName + "firstGroup")
                    .adapter(getTopHeadLineAdapter(it.firstGroup, 1, true))
                    .title("Top Head Line Top1~10")
                    .addTo(this)
            }
            if (it.secondGroup.isNotEmpty()) {
                HoriScrollModel_()
                    .id(HoriScrollModel::class.java.simpleName + "secondGroup")
                    .adapter(getTopHeadLineAdapter(it.secondGroup, 11))
                    .title("Top Head Line Top11~20")
                    .addTo(this)
            }
            if (it.thirdGroup.isNotEmpty()) {
                HoriScrollModel_()
                    .id(HoriScrollModel::class.java.simpleName + "thirdGroup")
                    .adapter(getTopHeadLineAdapter(it.thirdGroup, 21))
                    .title("Top Head Line Top21~30")
                    .addTo(this)
            }
            if (it.fourthGroup.isNotEmpty()) {
                HoriScrollModel_()
                    .id(HoriScrollModel::class.java.simpleName + "fourthGroup")
                    .adapter(getTopHeadLineAdapter(it.thirdGroup, 31))
                    .title("Top Head Line Top31~40")
                    .addTo(this)
            }

            if (it.others.isNotEmpty()) {
                // single text model
            }
        }

        /** newsData */
        val articles = mPopularityData?.articles ?: emptyList()
        articles.forEachIndexed { index, articlesBean ->
            SimpleNewsModel_()
                .id(index)
                .articlesBean(articlesBean)
                .listener(mCallback)
                .addTo(this)
        }

        loadingLoadingFooterModel.addIf(checkIsLoading(articles), this)
    }

    private fun getAdapter(pair: Pair<String, List<String>>, selectItem: String): GridCellAdapter {
        return GridCellAdapter(pair, selectItem, mCallback)
    }

    private fun getTopHeadLineAdapter(
        articles: List<ArticlesBean>,
        startNumber: Int,
        isBigType: Boolean = false,
    ): TopHeadLineAdapter = TopHeadLineAdapter(articles, startNumber, isBigType, mCallback)


    private fun checkIsLoading(articles: List<ArticlesBean>): Boolean {
        if (articles.isEmpty()) return true
        return mPopularityData?.let {
            val totalPage = it.totalResults.getTotalPage()
            Log.d(HomeEpoxyController.TAG, "totalPage=$totalPage")
            it.currentPage < totalPage && it.currentPage < MAX_POPULARITY_PAGE
        } ?: true
    }

    fun setPopularityData(newsData: NewsData) {
        Log.d(TAG, "setPopularityData:${newsData.currentPage}")
        mPopularityData?.let {
            it.currentPage = newsData.currentPage
            it.articles?.addAll(newsData.articles ?: emptyList())
        } ?: run { mPopularityData = newsData }
        requestModelBuild()
    }

    fun clearNewsData() {
        mTopHeadLineData = null
        mPopularityData = null
        isSelectMode = false
        requestModelBuild()
    }

    fun changeMode(): Int {
        Log.d(HomeEpoxyController.TAG, "changeMode.")
        isSelectMode = !isSelectMode
        requestModelBuild()
        return if (isSelectMode) R.anim.slide_down else R.anim.slide_up
    }

    fun setCountryAndCategory(pair: Pair<String, String>) {
        mSelectCountry = pair.first
        mSelectCategory = pair.second
    }

    fun setTopHeadLineData(topHeadLineData: TopHeadLineData) {
        Log.d(TAG,"setTopHeadLineData = $topHeadLineData")
        mTopHeadLineData = topHeadLineData
        requestModelBuild()
    }
}