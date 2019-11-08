package com.god.kotlin

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ez08.trade.exception.SessionLostException
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

            override fun disconnect(e: Exception) {
                onDisConnect(e)
            }
        }

        Client.getInstance().registerListener(stateListener)
    }

    override fun onDestroy() {
        dismissBusyDialog()
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
        finish()
    }

    protected open fun onDisConnect(e :Exception){
        if(e is SessionLostException){
            finish()
        }
    }

    public fun dismissBusyDialog() {
        if (pDialog != null && pDialog!!.isShowing) {
            pDialog?.dismiss()
        }
    }

    private var pDialog: ProgressDialog? = null

    public fun showBusyDialog() {
        pDialog = ProgressDialog(this)
        pDialog.let {
            it?.setMessage("请稍候...")
            it?.setCancelable(true)
            it?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            it?.show()
        }
    }

}