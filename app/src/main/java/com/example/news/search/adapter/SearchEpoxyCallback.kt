package com.example.news.search.adapter

import com.example.news.epoxy.BaseNewsEpoxyCallback

interface SearchEpoxyCallback : BaseNewsEpoxyCallback {

    fun onHistoryTextClick(historyText: String)

    fun clearHistory()
}