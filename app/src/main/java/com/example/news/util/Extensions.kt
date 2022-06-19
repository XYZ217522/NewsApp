package com.example.news.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.example.news.model.ArticlesBean
import com.example.news.repository.NewsRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

/** 利用api回傳的總數，取得api全部totalPage */
fun Int.getTotalPage(): Int {
    val perSize = NewsRepository.PAGE_SIZE
    return if (this > perSize) ceil((this.toDouble() / perSize.toDouble())).toInt() else 1
}

/**
 * Format: yyyy-MM-dd'T'HH:mm:ss'Z'
 * 用來把日期由字串轉換成 Date
 */
@SuppressLint("SimpleDateFormat")
@Throws(ParseException::class)
fun String.formatStringToDate(): String? {
    return try {
        val localSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = localSimpleDateFormat.parse(this) ?: return null
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US)
        formatter.format(date)
    } catch (e: Exception) {
        Log.e("tag", "$e")
        null
    }
}

fun Activity?.hideKeyboard() {
    this ?: return
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.currentFocus?.let {
        inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}