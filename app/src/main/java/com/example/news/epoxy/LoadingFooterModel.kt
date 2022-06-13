package com.example.news.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.example.news.R

@EpoxyModelClass
abstract class LoadingFooterModel: EpoxyModel<Any>() {
    override fun getDefaultLayout() = R.layout.loadingview
}