package com.example.news.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView


fun TextView?.setTextOrGone(text: String?) {
    val textView = this ?: return
    if (text.isNullOrEmpty()) {
        textView.visibility = View.GONE
    } else {
        textView.visibility = View.VISIBLE
        textView.text = text
    }
}

fun ImageView?.loadUrl(url: String?) {
    val imageView = this ?: return
    val imgUrl = if (url.isNullOrEmpty()) "" else url
    GlideApp.with(imageView.context)
        .load(imgUrl)
        .into(imageView)
}