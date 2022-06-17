package com.example.news.home.adapter

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder
import com.example.news.model.ArticlesBean
import com.example.news.util.formatStringToDate
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
            holder.tvPublished.setTextOrGone(article.publishedAt?.formatStringToDate())
            holder.tvExtraInfo.setTextOrGone(getExtraInfoText(article))
            holder.view.setOnClickListener { listener?.onArticleClick(article) }
        }
    }

    private fun getExtraInfoText(article: ArticlesBean): String {
        val sb = StringBuilder()
        article.author?.let { sb.append("author : $it") }
        article.source?.name?.let {
            if (sb.isNotEmpty()) sb.append(" , source : $it") else sb.append("source : $it")
        }
        return sb.toString()
    }

    class Holder : KotlinEpoxyHolder() {
        val ivNews by bind<ImageView>(R.id.iv_news)
        val tvTitle by bind<TextView>(R.id.tv_title)
        val tvPublished by bind<TextView>(R.id.tv_published)
        val tvExtraInfo by bind<TextView>(R.id.tv_extra_info)
    }
}