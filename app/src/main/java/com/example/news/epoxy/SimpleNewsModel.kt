package com.example.news.epoxy

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterSimpleNewsBinding
import com.example.news.home.ArticleViewInfo
import com.example.news.home.ArticleViewInfoConverter
import com.example.news.model.ArticlesBean
import com.example.news.model.getExtraInfoText
import com.example.news.util.formatStringToDate
import com.example.news.util.loadUrl
import com.example.news.util.setTextOrGone

@EpoxyModelClass
abstract class SimpleNewsModel : BaseEpoxyModel<AdapterSimpleNewsBinding>() {

    @EpoxyAttribute
    var articlesBean: ArticlesBean? = null

    @EpoxyAttribute
    var listener: BaseNewsEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_simple_news

    override fun AdapterSimpleNewsBinding.bind() {
        articlesBean?.let { articlesBean ->
            val articleViewInfo = ArticleViewInfoConverter.convertArticleViewInfo(articlesBean)
            ivNews.loadUrl(articleViewInfo.imageUrl)
            tvTitle.setTextOrGone(articleViewInfo.title)
            tvPublished.setTextOrGone(articleViewInfo.publishTime)
            tvExtraInfo.setTextOrGone(articleViewInfo.extraInfo)
            binding.root.setOnClickListener { listener?.onArticleClick(articlesBean) }
        }
    }
}