package com.example.news.util

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context?.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    this ?: return
    Toast.makeText(this, text, duration).show()
}

fun TextView?.setTextOrGone(text: String?) {
    val textView = this ?: return
    if (text.isNullOrEmpty()) {
        textView.gone()
    } else {
        textView.visible()
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

fun View.setBottomViewVisibilityAnimation(visibility: Int) {
    if (this.visibility != visibility) {
        this.visibility = visibility
        try {
            val animation: Animation
            if (visibility == View.VISIBLE) {
                animation = AnimationUtils.makeInChildBottomAnimation(this.context)
            } else {
                animation = AnimationUtils.loadAnimation(this.context, R.anim.slide_down)
                animation.startTime = AnimationUtils.currentAnimationTimeMillis()
            }
            this.animation = animation
        } catch (e: Exception) {
            Log.e("view extension", "Exception$e")
        }
    }
}

fun RecyclerView.setAnimation(animationID: Int) {
    this.animation = AnimationUtils.loadAnimation(this.context, animationID)
}