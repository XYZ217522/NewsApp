package com.example.news.search.adapter

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterHistoryFooterBinding
import com.example.news.epoxy.KotlinEpoxyHolder

@EpoxyModelClass
abstract class HistoryFooterModel : BaseEpoxyModel<AdapterHistoryFooterBinding>() {

    @EpoxyAttribute
    var listener: SearchEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_history_footer

    override fun AdapterHistoryFooterBinding.bind() {
        tvHistoryFooter.setOnClickListener { listener?.onClearHistoryClick() }
    }
}