package com.example.githubuserfinal.data.local

import android.content.Context

internal class SettingPreference (context: Context){
    companion object{
        private const val PREFS_NAME = "setting-pref"
        private const val NOTIFY = "notify"
    }

    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setNotify(value: Boolean){
        val editor = pref.edit()
        editor.putBoolean(NOTIFY, value)
        editor.apply()
    }

    fun getNotify() = pref.getBoolean(NOTIFY, false)
}