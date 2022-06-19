package com.example.news.di

import com.example.news.home.HomeViewModel
import com.example.news.networking.AuthInterceptor
import com.example.news.networking.provideNewsApi
import com.example.news.networking.provideOkHttpClient
import com.example.news.networking.provideRetrofit
import com.example.news.popularity.PopularityViewModel
import com.example.news.repository.NewsRepository
import com.example.news.search.SearchViewModel
import com.example.news.sharepreferences.Preferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    factory { provideNewsApi(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { PopularityViewModel(get(), get()) }
}

val repoModule = module {
    factory { NewsRepository(get(), get()) }
}

val prefModule = module {
    single { Preferences(androidContext()) }
}

val appModule = listOf(networkModule, viewModelModule, prefModule, repoModule)