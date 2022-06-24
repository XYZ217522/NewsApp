package com.example.news

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class NewsTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, NewsTest::class.java.name, context)
    }
}