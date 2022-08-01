package com.example.news.popularity.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterSingleTextBinding
import com.example.news.model.ArticlesBean

@EpoxyModelClass
abstract class HeadLineTextModel : BaseEpoxyModel<AdapterSingleTextBinding>() {

    @EpoxyAttribute
    var articlesBean: ArticlesBean? = null

    @EpoxyAttribute
    var listener: PopularityEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_single_text

    override fun AdapterSingleTextBinding.bind() {
        tvText.textSize = 14.0f
        ivArrow.visibility = View.VISIBLE
        fullWidthDivider.visibility = View.GONE
        divider.visibility = View.VISIBLE
        articlesBean?.let { article: ArticlesBean ->
            tvText.text = article.title ?: ""
            binding.root.setOnClickListener { listener?.onArticleClick(article) }
        }
    }
}