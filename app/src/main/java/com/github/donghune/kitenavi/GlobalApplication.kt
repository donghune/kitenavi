package com.github.donghune.kitenavi

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: Context

        fun getInstance(): Context {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
    }
}
