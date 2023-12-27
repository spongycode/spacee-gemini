package com.spongycode.spaceegemini

import android.app.Application

class App : Application() {

    companion object {
        @Volatile
        private var INSTANCE: App? = null

        fun getInstance(): App = INSTANCE ?: synchronized(this) {
            INSTANCE ?: App().also { INSTANCE = it }
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}