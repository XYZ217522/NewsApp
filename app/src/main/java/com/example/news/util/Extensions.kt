package com.example.news.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import com.example.news.R
import com.example.news.model.ArticlesBean
import com.example.news.repository.NewsRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

fun Int.dp(): Int {
    val metrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()

}

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

fun String?.dateToStamp(locale: Locale = Locale.US): Long {
    this ?: return 0L
    return try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale)
        simpleDateFormat.parse(this)?.time ?: 0L
    } catch (e: Exception) {
        Log.e("tag", "$e")
        0L
    }
}

fun TextAppearanceSpan.createSpannableString(pair: Pair<String, String>): SpannableString {
    val span = this
    val frontText = pair.first
    val backText = pair.second
    val sFullText = frontText + backText
    val indexStart = frontText.length
    val indexEnd = sFullText.length

    return SpannableString(sFullText)
        .apply {
            setSpan(span, indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
}

fun Activity?.hideKeyboard() {
    this ?: return
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.currentFocus?.let {
        inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}