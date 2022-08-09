package com.example.news.epoxy

import android.graphics.Color
import android.text.SpannableString
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterOneTextBinding
import com.example.news.databinding.AdapterSingleTextBinding

@EpoxyModelClass
abstract class SingleTextModel : BaseEpoxyModel<AdapterOneTextBinding>() {

    @EpoxyAttribute
    var spannableString: SpannableString? = null

    @JvmField
    @EpoxyAttribute
    var textColor = Color.parseColor("#0066ff")

    @JvmField
    @EpoxyAttribute
    var backgroundColor = Color.parseColor("#ffffff")

    override fun getDefaultLayout(): Int = R.layout.adapter_one_text

    override fun AdapterOneTextBinding.bind() {
        tvText.setTextColor(textColor)
        tvText.text = spannableString ?: ""
        binding.root.setBackgroundColor(backgroundColor)

    }
}