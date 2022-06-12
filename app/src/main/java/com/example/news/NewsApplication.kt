package com.example.news

import androidx.multidex.MultiDexApplication
import com.example.news.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class NewsApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin {
            // declare used Android context
            androidContext(this@NewsApplication)
            // declare modules
            modules(appModule)
        }
    }
}