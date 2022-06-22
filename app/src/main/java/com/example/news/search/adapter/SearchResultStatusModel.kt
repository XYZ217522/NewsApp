package com.example.news.search.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder

@EpoxyModelClass
abstract class SearchResultStatusModel : EpoxyModelWithHolder<SearchResultStatusModel.Holder>() {

    @JvmField
    @EpoxyAttribute
    var isSortByDESC = false

    @EpoxyAttribute
    var listener: SearchEpoxyCallback? = null

    private val colorBlack by lazy { Color.parseColor("#000000") }
    private val colorBlue by lazy { Color.parseColor("#3296fb") }

    override fun getDefaultLayout() = R.layout.adapter_serch_result_status

    override fun bind(holder: Holder) {

        holder.ivPublishUp
            .setColorFilter(
                if (isSortByDESC) colorBlack else colorBlue,
                android.graphics.PorterDuff.Mode.SRC_ATOP
            )
        holder.ivPublishDown
            .setColorFilter(
                if (isSortByDESC) colorBlue else colorBlack,
                android.graphics.PorterDuff.Mode.SRC_ATOP
            )

        holder.tvPublishedDate.setOnClickListener { listener?.onDataSortClick(!isSortByDESC) }
    }

    class Holder : KotlinEpoxyHolder() {
        val tvPublishedDate by bind<TextView>(R.id.tv_published_date)
        val ivPublishUp by bind<ImageView>(R.id.iv_publish_up)
        val ivPublishDown by bind<ImageView>(R.id.iv_publish_down)
    }
}