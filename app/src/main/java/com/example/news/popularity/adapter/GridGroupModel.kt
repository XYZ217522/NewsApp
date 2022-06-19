package com.example.news.popularity.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.news.R
import com.example.news.epoxy.KotlinEpoxyHolder

@EpoxyModelClass
abstract class GridGroupModel : EpoxyModelWithHolder<GridGroupModel.Holder>() {

//    @EpoxyAttribute
//    lateinit var adapter: GridCellAdapter

    @EpoxyAttribute
    lateinit var countryAdapter: GridCellAdapter

    @EpoxyAttribute
    lateinit var categoryAdapter: GridCellAdapter

//    @EpoxyAttribute
//    var title: String? = null

//    @JvmField
//    @EpoxyAttribute
//    var spanCount = 0

    override fun getDefaultLayout() = R.layout.adapter_grid_selector


    override fun bind(holder: Holder) {
        holder.rvCountry.swapAdapter(countryAdapter, false)
        holder.rvCategory.swapAdapter(categoryAdapter, false)
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        holder.rvCountry.swapAdapter(null, false)
        holder.rvCategory.swapAdapter(null, false)
    }

    class Holder : KotlinEpoxyHolder() {

        val rvCountry by bind<RecyclerView>(R.id.rv_country)
        val rvCategory by bind<RecyclerView>(R.id.rv_category)

        override fun bindView(itemView: View) {
            super.bindView(itemView)
            rvCountry.layoutManager = GridLayoutManager(this.view.context, 6)
            rvCategory.layoutManager = GridLayoutManager(this.view.context, 3)
        }
    }
}