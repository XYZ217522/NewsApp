package com.example.news.popularity.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder
import com.example.news.model.ArticlesBean

@EpoxyModelClass
abstract class HeadLineTextModel : EpoxyModelWithHolder<HeadLineTextModel.Holder>() {

    @EpoxyAttribute
    var articlesBean: ArticlesBean? = null

    @EpoxyAttribute
    var listener: PopularityEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_single_text

    override fun bind(holder: Holder) {
        articlesBean?.let { article: ArticlesBean ->
            holder.tvHeadline.text = article.title ?: ""
            holder.view.setOnClickListener { listener?.onArticleClick(article) }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val tvHeadline by bind<TextView>(R.id.tv_text)
        private val ivArrow by bind<ImageView>(R.id.iv_arrow)
        private val fullWidthDivider by bind<View>(R.id.full_width_divider)
        private val divider by bind<View>(R.id.divider)

        override fun bindView(itemView: View) {
            super.bindView(itemView)
            tvHeadline.textSize = 14.0f
            ivArrow.visibility = View.VISIBLE
            fullWidthDivider.visibility = View.GONE
            divider.visibility = View.VISIBLE
        }
    }
}