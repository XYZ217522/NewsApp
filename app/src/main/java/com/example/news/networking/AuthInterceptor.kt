package com.example.news.networking

import android.util.Log
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import com.example.news.BuildConfig


class AuthInterceptor : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
        private const val API_KEY = BuildConfig.API_KEY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        req = req
            .newBuilder()
            .url(req.url())
            .headers(Headers.of(mapOf(Pair("X-Api-Key", API_KEY)))).build()
        val response = chain.proceed(req)
        if (response.code() != 200) {
            throw Exception("response = ${response.body()?.string() ?: "UNKNOW ERROR"}")
        }
        return response
    }
}