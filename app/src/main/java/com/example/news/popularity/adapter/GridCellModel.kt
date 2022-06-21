package com.example.news.popularity.adapter

import android.graphics.Color
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder

@EpoxyModelClass
abstract class GridCellModel : EpoxyModelWithHolder<GridCellModel.Holder>() {

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

    override fun bind(holder: Holder) {

        val context = holder.view.context ?: return

        // 選中時改變顏色
        val timeColor = if (isSelected) Color.WHITE else Color.parseColor("#3296FB")
        holder.tvLabel.setTextColor(timeColor)
        holder.tvLabel.text = cellName

        val bg = if (isSelected) R.drawable.country_cell_selected_bg else R.drawable.country_cell_bg
        holder.csBackground.apply {
            this.background = ContextCompat.getDrawable(context, bg)
            this.setOnClickListener {
                listener?.let {
                    if (isCountryCell) it.onCountryClick(cellName) else it.onCategoryClick(cellName)
                }
            }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val csBackground by bind<ConstraintLayout>(R.id.cs_root)
        val tvLabel by bind<TextView>(R.id.tv_label)
    }
}