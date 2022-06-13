package com.example.news.networking

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    companion object {
        const val API_KEY = "e62ad6aae08b44d8be55ebb439d15d54"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val url = req.url().newBuilder().addQueryParameter("apiKey", API_KEY).build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}