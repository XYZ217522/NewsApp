package com.example.news.popularity.adapter

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.viewbinding.ViewBinding
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.base.ViewBindingHolder
import com.example.news.databinding.AdapterHorizontalScrollBinding

@EpoxyModelClass
abstract class HorizontalScrollModel : BaseEpoxyModel<AdapterHorizontalScrollBinding>() {

    @EpoxyAttribute
    lateinit var adapter: TopHeadlinesAdapter

    @EpoxyAttribute
    var title: String? = null

    override fun shouldSaveViewState(): Boolean = true

    override fun getDefaultLayout() = R.layout.adapter_horizontal_scroll

    override fun AdapterHorizontalScrollBinding.bind() {
        tvTitle.text = title ?: ""
        rvHorizontal.swapAdapter(adapter, false)
    }

    override fun AdapterHorizontalScrollBinding.unbind() {
        rvHorizontal.swapAdapter(null, false)
    }

    override fun AdapterHorizontalScrollBinding.onViewHolderBinding() {
        rvHorizontal.layoutManager = LinearLayoutManager(this.root.context, HORIZONTAL, false)
    }
}