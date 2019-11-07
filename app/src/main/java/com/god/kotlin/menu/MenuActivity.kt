package com.god.kotlin.menu

import android.os.Bundle
import com.ez08.trade.exception.LogoutException
import com.ez08.trade.net.Client
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.util.JumpActivity
import com.god.kotlin.util.addFragment
import com.god.kotlin.util.toast

class MenuActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        addFragment(TAG, R.id.container, true) {
            MenuFragment.newInstance()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissBusyDialog()
    }

    override fun onKickOUt() {
        "您被踢出".toast(this)
        Client.getInstance().logout()
        finish()
        JumpActivity.start(context, "登录")
    }

    override fun onDisConnect(e: Exception) {
        if (e is LogoutException) {
            finish()
            JumpActivity.start(context, "登录")
        }else if(e is SecurityException){
            Client.getInstance().logout()
            finish()
            JumpActivity.start(context, "登录")
        }
    }

}

private const val TAG = "MenuActivity_MenuFragment"