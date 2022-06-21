package com.example.news.search.adapter

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder

@EpoxyModelClass
abstract class HistoryFooterModel : EpoxyModelWithHolder<HistoryFooterModel.Holder>() {

    @EpoxyAttribute
    var listener: SearchEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_history_footer

    override fun bind(holder: Holder) {
        holder.tvHistoryFooter.setOnClickListener { listener?.onClearHistoryClick() }
    }

    class Holder : KotlinEpoxyHolder() {
        val tvHistoryFooter by bind<TextView>(R.id.tv_history_footer)
    }
}