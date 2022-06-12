package com.example.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.news.home.NewsHomeFragment

//import org.koin.android.ext.android.inject

class NewsActivity : AppCompatActivity() {

//    private val preferences: Preferences by inject()
//    private val exampleFragment: WeatherFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        NewsHomeFragment.newInstance().let {
            supportFragmentManager.beginTransaction().replace(R.id.root, it, "HOME").commit()
        }

    }
}