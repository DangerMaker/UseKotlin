package com.god.kotlin

import android.app.Application
import com.ez08.trade.SocketInitializer

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        SocketInitializer.getInstance(applicationContext)
    }
}