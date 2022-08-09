package com.example.news.search.adapter

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterSingleTextBinding
import com.example.news.epoxy.KotlinEpoxyHolder

@EpoxyModelClass
abstract class HistoryTextModel : BaseEpoxyModel<AdapterSingleTextBinding>() {

    @EpoxyAttribute
    var historyText: String? = null

    @EpoxyAttribute
    var listener: SearchEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_single_text

    override fun AdapterSingleTextBinding.bind() {
        tvText.apply {
            historyText?.let {
                this.text = it
                binding.root.setOnClickListener { listener?.onHistoryTextClick(historyText!!) }
            }
        }
    }
}