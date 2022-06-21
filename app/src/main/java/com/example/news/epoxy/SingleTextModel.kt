package com.example.news.epoxy

import android.graphics.Color
import android.text.SpannableString
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R

@EpoxyModelClass
abstract class SingleTextModel : EpoxyModelWithHolder<SingleTextModel.Holder>() {

    @EpoxyAttribute
    var spannableString: SpannableString? = null

    @JvmField
    @EpoxyAttribute
    var textColor = Color.parseColor("#0066ff")

    @JvmField
    @EpoxyAttribute
    var backgroundColor = Color.parseColor("#ffffff")

    override fun getDefaultLayout(): Int = R.layout.adapter_one_text

    override fun bind(holder: Holder) {
        holder.tvTitle.setTextColor(textColor)
        holder.tvTitle.text = spannableString ?: ""
        holder.view.setBackgroundColor(backgroundColor)
    }

    class Holder : KotlinEpoxyHolder() {
        val tvTitle by bind<TextView>(R.id.tv_text)
    }
}