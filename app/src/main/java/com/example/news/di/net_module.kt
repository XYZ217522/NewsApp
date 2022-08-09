package com.example.news.di

import com.example.news.networking.AuthInterceptor
import com.example.news.networking.provideNewsApi
import com.example.news.networking.provideOkHttpClient
import com.example.news.networking.provideRetrofit
import com.example.news.repository.NewsRepository
import org.koin.dsl.module

val networkModule = module {
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    factory { provideNewsApi(get()) }
}

val repoModule = module {
    factory { NewsRepository(get(), get()) }
}