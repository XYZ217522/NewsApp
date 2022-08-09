package com.example.news.util.extensions

import androidx.lifecycle.MutableLiveData
import com.example.news.util.Event

inline fun <T> MutableLiveData<Event<MutableList<T>>>.lambdaMutableList(lambda: ((MutableList<T>) -> Unit)) {
    val currentArticles = (this.value?.peekContent() ?: mutableListOf())
    lambda.invoke(currentArticles)
    this.value = Event(currentArticles)
}

fun <T> MutableLiveData<Event<MutableList<T>>>.addAllMutableListBean(list: MutableList<T>?) {
    val mutableList = list ?: return
    val currentArticles = (this.value?.peekContent() ?: mutableListOf())
    currentArticles.addAll(mutableList)
    this.value = Event(currentArticles)
}

fun <T> MutableLiveData<Event<MutableList<T>>>.clearMutableList() {
    this.value = Event(mutableListOf())
}