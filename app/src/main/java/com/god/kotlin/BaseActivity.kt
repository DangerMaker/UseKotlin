package com.god.kotlin

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    protected var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
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