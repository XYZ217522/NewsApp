package com.example.news.sharepreferences

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    private val preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun getStringValue(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun <T> getValue(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> getStringValue(key, defaultValue) as T
            is Int -> preferences.getInt(key, defaultValue) as T
            is Boolean -> preferences.getBoolean(key, defaultValue) as T
            is Long -> preferences.getLong(key, defaultValue) as T
            is Float -> preferences.getFloat(key, defaultValue) as T
            else -> defaultValue
        }
    }

    fun <T> setValue(key: String, value: T) {
        val edit = preferences.edit()
        when (value) {
            is String -> edit.putString(key, value).apply()
            is Int -> edit.putInt(key, value).apply()
            is Boolean -> edit.putBoolean(key, value).apply()
            is Long -> edit.putLong(key, value).apply()
            is Float -> edit.putFloat(key, value).apply()
        }
    }

}