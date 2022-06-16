package com.example.news.util

import android.annotation.SuppressLint
import android.util.Log
import com.example.news.repository.NewsRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

// 利用api回傳的總數，取得api全部totalPage
fun Int.getTotalPage(): Int {
    val perSize = NewsRepository.PAGE_SIZE
    return if (this > perSize) ceil((this.toDouble() / perSize.toDouble())).toInt() else 1
}

/**
 * Format: yyyy-MM-dd'T'HH:mm:ssZ
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
        Log.e("tag","$e")
        null
    }
}