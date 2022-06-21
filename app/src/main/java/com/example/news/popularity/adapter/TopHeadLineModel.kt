package com.example.news.popularity.adapter

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder
import com.example.news.model.ArticlesBean
import com.example.news.model.getExtraInfoText
import com.example.news.util.formatStringToDate
import com.example.news.util.gone
import com.example.news.util.loadUrl
import com.example.news.util.setTextOrGone

@EpoxyModelClass
abstract class TopHeadLineModel : EpoxyModelWithHolder<TopHeadLineModel.Holder>() {

    @EpoxyAttribute
    var articlesBean: ArticlesBean? = null

    @EpoxyAttribute
    var listener: PopularityEpoxyCallback? = null

    @JvmField
    @EpoxyAttribute
    var rank: Int = 0

    @JvmField
    @EpoxyAttribute
    var isBigType = true

    override fun getDefaultLayout(): Int {
        return if (isBigType) R.layout.adapter_big_headlines else R.layout.adapter_small_headlines
    }

    override fun bind(holder: Holder) {
        articlesBean?.let { article: ArticlesBean ->
            holder.ivNews.loadUrl(article.urlToImage)
            holder.tvTitle.setTextOrGone(article.title)
            holder.tvExtraInfo.setTextOrGone(article.getExtraInfoText())
            holder.tvRank.text = "$rank"
            holder.tvPublished.apply {
                if (isBigType) setTextOrGone(article.publishedAt?.formatStringToDate()) else gone()
            }
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