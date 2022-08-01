package com.example.news.epoxy

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterSimpleNewsBinding
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
        articlesBean?.let { article: ArticlesBean ->
            ivNews.loadUrl(article.urlToImage)
            tvTitle.setTextOrGone(article.title)
            tvPublished.setTextOrGone(article.publishedAt?.formatStringToDate())
            tvExtraInfo.setTextOrGone(article.getExtraInfoText())
            binding.root.setOnClickListener { listener?.onArticleClick(article) }
        }
    }


}