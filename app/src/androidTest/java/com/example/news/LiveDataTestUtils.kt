package com.example.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitValue(): T {
    var data: T? = null
    val countDownLatch = CountDownLatch(1)

    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            data = t
            removeObserver(this)
            countDownLatch.countDown()

        }
    }

    this.observeForever(observer)

    try {
        if (!countDownLatch.await(2, TimeUnit.SECONDS)) {
            throw TimeoutException("Live data doesn't have data")
        }
    } finally {
        this.removeObserver(observer)
    }

    return data as T
}
