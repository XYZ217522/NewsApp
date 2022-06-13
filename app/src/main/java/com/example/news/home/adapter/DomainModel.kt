package com.example.news.home.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.model.ArticlesBean
import com.example.news.util.KotlinEpoxyHolder
import com.example.news.util.loadUrl
import com.example.news.util.setTextOrGone

@EpoxyModelClass
abstract class DomainModel : EpoxyModelWithHolder<DomainModel.Holder>() {

    @EpoxyAttribute
    var domain: String? = null

    @JvmField
    @EpoxyAttribute
    var isSelected = false

    @EpoxyAttribute
    var listener: HomeEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_single_text

    override fun bind(holder: Holder) {
        holder.tvDomain.apply {
            domain?.let {
                this.text = it
                this.setTextColor(if (isSelected) Color.YELLOW else Color.BLACK)
                this.setOnClickListener { listener?.onDomainClick(domain!!) }
            }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val tvDomain by bind<TextView>(R.id.tv_domain)
    }
}