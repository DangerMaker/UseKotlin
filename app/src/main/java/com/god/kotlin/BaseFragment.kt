package com.god.kotlin

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ez08.trade.exception.SessionLostException
import com.ez08.trade.net.Client
import com.ez08.trade.net.callback.StateListener

open class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    public fun dismissBusyDialog() {
        if (pDialog != null && pDialog!!.isShowing) {
            pDialog?.dismiss()
        }
    }

    private var pDialog: ProgressDialog? = null

    public fun showBusyDialog() {
        pDialog = ProgressDialog(context)
        pDialog.let {
            it?.setMessage("请稍候...")
            it?.setCancelable(true)
            it?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            it?.show()
        }
    }
}