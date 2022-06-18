package com.example.news.search.adapter

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder

@EpoxyModelClass
abstract class HistoryTextModel : EpoxyModelWithHolder<HistoryTextModel.Holder>() {

    @EpoxyAttribute
    var historyText: String? = null

    @EpoxyAttribute
    var listener: SearchEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_single_text

    override fun bind(holder: Holder) {
        holder.tvDomain.apply {
            historyText?.let {
                this.text = it
                this.setOnClickListener { listener?.onHistoryTextClick(historyText!!) }
            }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val tvDomain by bind<TextView>(R.id.tv_text)
    }
}