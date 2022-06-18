package com.example.news.home.adapter

import com.example.news.epoxy.BaseNewsEpoxyCallback

interface HomeEpoxyCallback : BaseNewsEpoxyCallback {

    fun onDomainClick(domain: String)
}