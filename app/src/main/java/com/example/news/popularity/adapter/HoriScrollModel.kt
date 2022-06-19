package com.example.news.popularity.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder
import com.example.news.model.ArticlesBean

@EpoxyModelClass
abstract class HoriScrollModel : EpoxyModelWithHolder<HoriScrollModel.Holder>() {

    @EpoxyAttribute
    lateinit var adapter: TopHeadLineAdapter

    @EpoxyAttribute
    var title: String? = null

    override fun getDefaultLayout() = R.layout.adapter_horizontal_scroll


    override fun bind(holder: Holder) {
        holder.tvTitle.text = title ?: ""
        holder.rvHorizontal.swapAdapter(adapter, false)
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        holder.rvHorizontal.swapAdapter(null, false)
    }

    override fun shouldSaveViewState(): Boolean = true

    class Holder : KotlinEpoxyHolder() {
        val tvTitle by bind<TextView>(R.id.tv_title)
        val rvHorizontal by bind<RecyclerView>(R.id.rv_horizontal)

        override fun bindView(itemView: View) {
            super.bindView(itemView)
            rvHorizontal.layoutManager = LinearLayoutManager(itemView.context, HORIZONTAL, false)
        }
    }
}