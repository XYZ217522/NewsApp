package com.example.news.home.adapter

import android.graphics.Color
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterSingleTextBinding

@EpoxyModelClass
abstract class DomainModel : BaseEpoxyModel<AdapterSingleTextBinding>() {

    @EpoxyAttribute
    var domain: String? = null

    @JvmField
    @EpoxyAttribute
    var isSelected: Boolean = false

    @EpoxyAttribute
    var listener: HomeEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_single_text

    override fun AdapterSingleTextBinding.bind() {
        val color = if (isSelected) Color.parseColor("#3296fb") else Color.BLACK
        tvText.text = domain
        tvText.setTextColor(color)
        binding.root.setOnClickListener { listener?.onDomainClick(domain!!) }
    }
}