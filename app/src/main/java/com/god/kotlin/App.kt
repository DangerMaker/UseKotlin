package com.god.kotlin

import android.app.Application
import com.ez08.trade.TradeInitalizer

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        TradeInitalizer.init()
    }
}