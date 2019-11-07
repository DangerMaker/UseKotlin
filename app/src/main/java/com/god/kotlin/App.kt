package com.god.kotlin

import android.app.Application
import com.ez08.trade.TradeInitalizer
import com.ez08.trade.net.Client
import com.god.kotlin.data.entity.User
import com.god.kotlin.user.UserHelper
import com.tencent.bugly.crashreport.CrashReport

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        TradeInitalizer.init()
        Client.getInstance().setOnUserListener {
            val userList = mutableListOf<User>()
            for (item in it){
                val user = User(item.name,item.market,item.fundid,item.custcert,item.secuid,item.custid)
                userList.add(user)
            }

            UserHelper.setUserList(userList)
        }
        CrashReport.initCrashReport(applicationContext, "55bec5db49", false)
    }
}