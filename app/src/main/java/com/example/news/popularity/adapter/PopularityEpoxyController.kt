package com.example.news.popularity.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.style.TextAppearanceSpan
import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.example.news.R
import com.example.news.epoxy.LoadingFooterModel_
import com.example.news.epoxy.SimpleNewsModel
import com.example.news.epoxy.SingleTextModel_
import com.example.news.epoxy.simpleNews
import com.example.news.model.*
import com.example.news.popularity.PopularityViewModel.Companion.MAX_POPULARITY_PAGE
import com.example.news.util.createSpannableString
import com.example.news.util.dp
import com.example.news.util.getTotalPage

class PopularityEpoxyController(
    private val mCallback: PopularityEpoxyCallback,
) : EpoxyController() {

    companion object {
        private const val TAG = "PopularityEpoxyController"
        private const val NEWS_HEADLINES = "NEWS HEADLINES"
        private const val TITLE_PREFIX = "Popularity"
        private const val TITLE = " NEWS"
        const val COUNTRY = "Country"
        const val CATEGORY = "Category"
    }

    private var mTopHeadlinesData: TopHeadlinesData? = null
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
    lateinit var gridGroupModel: GridGroupModel_

    @AutoModel
    lateinit var singleTextModel: SingleTextModel_

    private val countryPair by lazy { Pair(COUNTRY, countryList) }
    private val categoryPair by lazy { Pair(CATEGORY, categoryList) }
    private val color by lazy { ColorStateList.valueOf(Color.parseColor("#3296fb")) }
    private val titleSpan by lazy { TextAppearanceSpan(null, Typeface.BOLD, 17.dp(), color, null) }

    override fun buildModels() {

        if (isSelectMode) {
            gridGroupModel
                .countryAdapter(GridCellAdapter(countryPair, mSelectCountry, mCallback))
                .categoryAdapter(GridCellAdapter(categoryPair, mSelectCategory, mCallback))
                .addTo(this)
            return
        }

        /** TopHeadLine */
        mTopHeadlinesData?.let {
            if (it.firstGroup.isNotEmpty()) {
                horizontalScroll {
                    id(HorizontalScrollModel::class.java.simpleName, "firstGroup")
                    adapter(getTopHeadlinesAdapter(it.firstGroup, 1, true))
                    title("$NEWS_HEADLINES 1~10")
                }
            }
            if (it.secondGroup.isNotEmpty()) {
                horizontalScroll {
                    id(HorizontalScrollModel::class.java.simpleName, "secondGroup")
                    adapter(getTopHeadlinesAdapter(it.secondGroup, 11))
                    title("$NEWS_HEADLINES 11~20")
                }
            }
            if (it.thirdGroup.isNotEmpty()) {
                horizontalScroll {
                    id(HorizontalScrollModel::class.java.simpleName, "thirdGroup")
                    adapter(getTopHeadlinesAdapter(it.thirdGroup, 21))
                    title("$NEWS_HEADLINES 21~30")
                }
            }

            if (it.fourthGroup.isNotEmpty()) {
                horizontalScroll {
                    id(HorizontalScrollModel::class.java.simpleName, "fourthGroup")
                    adapter(getTopHeadlinesAdapter(it.thirdGroup, 31))
                    title("NEWS HEADLINES 31~40")
                }
            }

            if (it.others.isNotEmpty()) {
                it.others.forEachIndexed { index, articlesBean ->
                    headLineText {
                        id(HeadLineTextModel::class.java.simpleName + index)
                        articlesBean(articlesBean)
                        listener(mCallback)
                    }
                }
            }
        }

        /** newsData */
        mPopularityData?.let {
            val articles = it.articles ?: emptyList()

            singleTextModel
                .spannableString(titleSpan.createSpannableString(Pair(TITLE_PREFIX, TITLE)))
                .textColor(Color.parseColor("#3296fb"))
                .addIf(articles.isNotEmpty(), this)

            articles.forEachIndexed { index, articlesBean ->
                simpleNews {
                    id(SimpleNewsModel::class.java.simpleName + index)
                    articlesBean(articlesBean)
                    listener(mCallback)
                }
            }

            loadingLoadingFooterModel.addIf(checkIsLoading(articles), this)
        }


    }

    private fun getTopHeadlinesAdapter(
        articles: List<ArticlesBean>,
        startNumber: Int,
        isBigType: Boolean = false,
    ): TopHeadlinesAdapter = TopHeadlinesAdapter(articles, startNumber, isBigType, mCallback)


    private fun checkIsLoading(articles: List<ArticlesBean>): Boolean {
        if (articles.isEmpty()) return true
        return mPopularityData?.let {
            val totalPage = it.totalResults.getTotalPage()
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
        mTopHeadlinesData = null
        mPopularityData = null
        isSelectMode = false
        requestModelBuild()
    }

    fun changeMode(): Int {
        Log.d(TAG, "changeMode.")
        isSelectMode = !isSelectMode
        requestModelBuild()
        return if (isSelectMode) R.anim.slide_down else R.anim.slide_up
    }

    fun setCountryAndCategory(pair: Pair<String, String>) {
        mSelectCountry = pair.first
        mSelectCategory = pair.second
    }

    fun setTopHeadLineData(topHeadlinesData: TopHeadlinesData) {
        Log.d(TAG, "setTopHeadLineData = $topHeadlinesData")
        mTopHeadlinesData = topHeadlinesData
        requestModelBuild()
    }

    fun resetHorizontalScrollModel() {
        val modelCountBuiltSoFar = modelCountBuiltSoFar
        for (i in 0 until modelCountBuiltSoFar) {
            val model = adapter.getModelAtPosition(0)
            if (model is HorizontalScrollModel) {
                //todo  epoxyModel.restoreOriginalPosition()
            }
        }
    }

}