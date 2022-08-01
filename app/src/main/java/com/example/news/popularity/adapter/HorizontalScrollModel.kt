package com.example.news.popularity.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterHorizontalScrollBinding

@EpoxyModelClass
abstract class HorizontalScrollModel : BaseEpoxyModel<AdapterHorizontalScrollBinding>() {

    @EpoxyAttribute
    lateinit var adapter: TopHeadlinesAdapter

    @EpoxyAttribute
    var title: String? = null

    override fun getDefaultLayout() = R.layout.adapter_horizontal_scroll

    override fun AdapterHorizontalScrollBinding.bind() {
        rvHorizontal.layoutManager = LinearLayoutManager(binding.root.context, HORIZONTAL, false)
        tvTitle.text = title ?: ""
        rvHorizontal.swapAdapter(adapter, false)
    }

    override fun AdapterHorizontalScrollBinding.unbind() {
        rvHorizontal.swapAdapter(null, false)
    }
}