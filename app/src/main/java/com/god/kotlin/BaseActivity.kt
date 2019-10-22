package com.god.kotlin

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ez08.trade.net.Client
import com.ez08.trade.net.callback.StateListener

open class BaseActivity : AppCompatActivity() {

    protected lateinit var context: Context
    private lateinit var stateListener: StateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        stateListener = object :StateListener{
            override fun connect() {
                onConnect()
            }

            override fun exchange() {
                onExchange()
            }

            override fun login() {
                onLogin()
            }

            override fun kickOut() {
                onKickOUt()
            }

            override fun disconnect() {
                onDisConnect()
            }
        }

        Client.getInstance().registerListener(stateListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        Client.getInstance().unRegisterListener(stateListener)
    }

    protected open fun onConnect(){

    }

    protected open fun onExchange(){

    }

    protected open fun onLogin(){

    }

    protected open fun onKickOUt(){

    }

    protected open fun onDisConnect(){

    }

    protected fun dismissBusyDialog() {
        if (pDialog != null) {
            pDialog!!.dismiss()
        }
    }

    private var pDialog: ProgressDialog? = null

    protected fun showBusyDialog() {
        pDialog = ProgressDialog(this)
        pDialog!!.setMessage("请稍候...")
        pDialog!!.setCancelable(true)
        pDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            pDialog!!.show()
        } else {
            runOnUiThread { pDialog!!.show() }
        }
    }
}