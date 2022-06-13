package com.example.news.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(
    private val mLinearLayoutManager: LinearLayoutManager,
) : RecyclerView.OnScrollListener() {

    // The total number of items in the dataset after the last load
    private var previousTotal = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // The minimum amount of items to have below your current scroll position before loading more.
    private val visibleThreshold = 3

    private var currentPage = 1

    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0


    fun resetPage() {
        currentPage = 1
        totalItemCount = mLinearLayoutManager.itemCount
        previousTotal = totalItemCount
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = mLinearLayoutManager.itemCount
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

        if (previousTotal > totalItemCount) {
            previousTotal = 0
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) { // End has been reached
            // Do something
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }

        if (firstVisibleItem == 0) {
            onDismissScrollTopBtn()
        } else {
            onShowScrollTopBtn()
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            onScrollIdle(firstVisibleItem)
        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            onScrolling()
        }
    }

    abstract fun onLoadMore(currentPage: Int)
    open fun onShowScrollTopBtn() {}
    open fun onDismissScrollTopBtn() {}
    open fun onScrolling() {}
    open fun onScrollIdle(firstVisibleItem: Int) {}

}