package com.example.news.di

import com.example.news.sharepreferences.Preferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val prefModule = module {
    single { Preferences(androidContext()) }
}

val appModule = listOf(networkModule, viewModelModule, prefModule, repoModule, useCaseModule)