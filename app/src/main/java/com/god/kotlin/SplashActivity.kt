package com.god.kotlin

import android.content.Intent
import android.os.Bundle
import com.ez08.trade.net.Client
import com.god.kotlin.menu.MenuActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        button2.setOnClickListener {
            if(Client.getInstance().state == Client.STATE.LOGIN) {
                //连接通路状态
                startActivity(Intent(context, MenuActivity::class.java))
            }else{
                if(Client.sessionId == null){
                    //LoginActivity 做重连
                    startActivity(Intent(context, MainActivity::class.java))
                }else{
                    //MenuActivity 做重连
                    startActivity(Intent(context, MenuActivity::class.java))
                }
            }
        }
    }
}