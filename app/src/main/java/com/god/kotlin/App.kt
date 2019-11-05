package com.god.kotlin

import android.app.Application
import com.ez08.trade.TradeInitalizer
import com.tencent.bugly.crashreport.CrashReport

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        TradeInitalizer.init()
        CrashReport.initCrashReport(applicationContext, "55bec5db49", false)
    }
}