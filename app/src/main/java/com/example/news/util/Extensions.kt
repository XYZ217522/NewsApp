package com.example.news.util

import com.example.news.repository.NewsRepository
import kotlin.math.ceil

// 利用api回傳的總數，取得api全部totalPage
fun Int.getTotalPage(): Int {
    val perSize = NewsRepository.PAGE_SIZE
    return if (this > perSize) ceil((this.toDouble() / perSize.toDouble())).toInt() else 1
}