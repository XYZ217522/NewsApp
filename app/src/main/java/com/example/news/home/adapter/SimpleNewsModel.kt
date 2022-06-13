package com.example.news.home.adapter

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.model.ArticlesBean
import com.example.news.util.KotlinEpoxyHolder
import com.example.news.util.loadUrl
import com.example.news.util.setTextOrGone

@EpoxyModelClass
abstract class SimpleNewsModel : EpoxyModelWithHolder<SimpleNewsModel.Holder>() {

    @EpoxyAttribute
    var articlesBean: ArticlesBean? = null

    @EpoxyAttribute
    var listener: HomeEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_simple_news

    override fun bind(holder: Holder) {
        articlesBean?.let { article: ArticlesBean ->
            holder.ivNews.loadUrl(article.urlToImage)
            holder.tvTitle.setTextOrGone(article.title)
            holder.tvPublished.setTextOrGone(article.publishedAt)
            holder.view.setOnClickListener { listener?.onArticleClick(article) }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val ivNews by bind<ImageView>(R.id.iv_news)
        val tvTitle by bind<TextView>(R.id.tv_title)
        val tvPublished by bind<TextView>(R.id.tv_published)
    }
}