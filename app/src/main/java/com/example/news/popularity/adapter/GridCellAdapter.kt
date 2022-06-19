package com.example.news.popularity.adapter

import com.airbnb.epoxy.EpoxyAdapter
import com.example.news.popularity.adapter.PopularityEpoxyController.Companion.COUNTRY

class GridCellAdapter(
    gridItems: Pair<String, List<String>>,
    selectCellName:String,
    listener: PopularityEpoxyCallback,
) : EpoxyAdapter() {

    init {
        enableDiffing()
        val items = gridItems.second
        for (i in items.indices) {
            GridCellModel_()
                .id("GridCellModel_$i")
                .cellName(items[i])
                .isCountryCell(gridItems.first == COUNTRY)
                .isSelected(selectCellName.isNotEmpty() && items[i] == selectCellName)
                .listener(listener)
                .let { addModel(it) }
        }
    }


}