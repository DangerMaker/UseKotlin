package com.god.kotlin

import android.os.Bundle
import com.ez08.trade.exception.LoginErrorException
import com.ez08.trade.exception.LogoutException
import com.ez08.trade.net.Client
import com.god.kotlin.login.LoginFragment
import com.god.kotlin.login.LoginViewModel
import com.god.kotlin.util.addFragment
import com.god.kotlin.util.obtainViewModel

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(TAG,R.id.container,true){
            LoginFragment.newInstance("compass")
        }

        Client.getInstance().connect()
    }

    override fun onExchange() {
        obtainViewModel().getVerificationCode(30,15)

    }

    override fun onDisConnect(e: Exception) {
        if(e is LoginErrorException) {
            Client.getInstance().connect()
        }else if(e is LogoutException){
            Client.getInstance().connect()
        }
    }

    fun obtainViewModel(): LoginViewModel = obtainViewModel(LoginViewModel::class.java)

}

private const val TAG = "MainActivity_LoginFragment"
