package com.example.news.popularity.adapter

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterGridSelectorBinding

@EpoxyModelClass
abstract class GridGroupModel : BaseEpoxyModel<AdapterGridSelectorBinding>() {

    @EpoxyAttribute
    lateinit var countryAdapter: GridCellAdapter

    @EpoxyAttribute
    lateinit var categoryAdapter: GridCellAdapter

    override fun getDefaultLayout() = R.layout.adapter_grid_selector


    override fun AdapterGridSelectorBinding.bind() {
        rvCountry.swapAdapter(countryAdapter, false)
        rvCategory.swapAdapter(categoryAdapter, false)
    }

    override fun AdapterGridSelectorBinding.unbind() {
        rvCountry.swapAdapter(null, false)
        rvCategory.swapAdapter(null, false)
    }
}