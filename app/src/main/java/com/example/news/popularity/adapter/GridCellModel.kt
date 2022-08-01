package com.example.news.popularity.adapter

import android.graphics.Color
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.base.BaseEpoxyModel
import com.example.news.databinding.AdapterGridCellBinding

@EpoxyModelClass
abstract class GridCellModel : BaseEpoxyModel<AdapterGridCellBinding>() {

    @EpoxyAttribute
    var cellName: String = ""

    @JvmField
    @EpoxyAttribute
    var isCountryCell = false

    @JvmField
    @EpoxyAttribute
    var isSelected: Boolean = false

    @EpoxyAttribute
    var listener: PopularityEpoxyCallback? = null

    override fun getDefaultLayout() = R.layout.adapter_grid_cell

    override fun AdapterGridCellBinding.bind() {
        // 選中時改變顏色
        val timeColor = if (isSelected) Color.WHITE else Color.parseColor("#3296FB")
        tvLabel.setTextColor(timeColor)
        tvLabel.text = cellName

        val bg = if (isSelected) R.drawable.country_cell_selected_bg else R.drawable.country_cell_bg
        csRoot.apply {
            this.background = ContextCompat.getDrawable(context, bg)
            this.setOnClickListener {
                listener?.let {
                    if (isCountryCell) it.onCountryClick(cellName) else it.onCategoryClick(cellName)
                }
            }
        }
    }
}