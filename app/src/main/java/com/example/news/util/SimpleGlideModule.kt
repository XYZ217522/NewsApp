package com.example.news.util

import android.content.Context
import android.os.Build
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.example.news.R

@GlideModule
class SimpleGlideModule : AppGlideModule() {

    //4.x Glide預設為ARGB_8888，必須重新設置為RGB_565
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val format: RequestOptions = RequestOptions().format(DecodeFormat.PREFER_RGB_565)
            .placeholder(R.drawable.place_holder)
        if (Build.VERSION.SDK_INT < 26) {
            builder.setDefaultRequestOptions(format)
        } else {
            builder.setDefaultRequestOptions(format.disallowHardwareConfig())
        }
        builder.setLogLevel(Log.ERROR)
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize
        builder.setMemoryCache(LruResourceCache((defaultMemoryCacheSize / 3).toLong()))
        builder.setBitmapPool(LruBitmapPool((defaultBitmapPoolSize / 3).toLong()))
    }
}