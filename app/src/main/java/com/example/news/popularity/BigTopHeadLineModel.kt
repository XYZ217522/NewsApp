package com.example.news.popularity

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder
import com.example.news.model.ArticlesBean
import com.example.news.model.getExtraInfoText
import com.example.news.popularity.adapter.PopularityEpoxyCallback
import com.example.news.util.formatStringToDate
import com.example.news.util.loadUrl
import com.example.news.util.setTextOrGone

@EpoxyModelClass
abstract class BigTopHeadLineModel : EpoxyModelWithHolder<BigTopHeadLineModel.Holder>() {

    @EpoxyAttribute
    var articlesBean: ArticlesBean? = null

    @EpoxyAttribute
    var listener: PopularityEpoxyCallback? = null

    @JvmField
    @EpoxyAttribute
    var rank: Int = 0

    override fun getDefaultLayout() = R.layout.adapter_big_tophead

    override fun bind(holder: Holder) {
        articlesBean?.let { article: ArticlesBean ->
            holder.ivNews.loadUrl(article.urlToImage)
            holder.tvTitle.setTextOrGone(article.title)
            holder.tvPublished.setTextOrGone(article.publishedAt?.formatStringToDate())
            holder.tvExtraInfo.setTextOrGone(article.getExtraInfoText())
            holder.tvRank.text = "$rank"
            holder.view.setOnClickListener { listener?.onArticleClick(article) }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val ivNews by bind<ImageView>(R.id.iv_news)
        val tvTitle by bind<TextView>(R.id.tv_title)
        val tvRank by bind<TextView>(R.id.tv_rank)
        val tvPublished by bind<TextView>(R.id.tv_published)
        val tvExtraInfo by bind<TextView>(R.id.tv_extra_info)
    }
}