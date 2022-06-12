package com.example.news.sharepreferences

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

//
//    private fun storeFragmentContent(content: String) {
//        preferences.edit().putString(fragmentContentKey, content).apply()
//    }
//
//    fun getFragmentContent(): String? {
//        return preferences.getString(fragmentContentKey, "")
//    }


}