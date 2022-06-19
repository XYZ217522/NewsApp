package com.example.news.popularity.adapter

import com.example.news.epoxy.BaseNewsEpoxyCallback

interface PopularityEpoxyCallback : BaseNewsEpoxyCallback {

    fun onCountryClick(country: String)

    fun onCategoryClick(category: String)
}