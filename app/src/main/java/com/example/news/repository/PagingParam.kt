package com.example.news.repository

import kotlin.math.ceil

data class PagingParamByItem(val offsetItem: Int = 0, val limit: Int = 10)

data class PagingParamByPage(
    private var _totalItem: Int,
    private var _offsetPage: Int = 1,
    private val _perPageSize: Int = 20
) {
    companion object {
        const val GET_EVERYTHING_PAGE_SIZE = 20

        fun getEveryThingPagingParam(): PagingParamByPage =
            PagingParamByPage(_totalItem = 0, _offsetPage = 1, _perPageSize = GET_EVERYTHING_PAGE_SIZE)
    }

    val offsetPage: Int
        get() = _offsetPage

    /** 利用api回傳的總數，取得api全部totalPage */
    private fun totalPage(): Int {
        return if (_totalItem > _perPageSize) ceil(_totalItem.toDouble() / _perPageSize.toDouble()).toInt() else 1
    }

    fun offsetNextPage() {
        _offsetPage += 1
    }

    /**
     * 根據總數才有辦法計算，總共有幾頁
     * */
    fun setTotalItem(totalItem: Int) {
        this._totalItem = totalItem
    }

    fun isCanLoadMore() = totalPage() > _offsetPage

    fun reset() {
        _totalItem = 0
        _offsetPage = 1
    }

}