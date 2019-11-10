package com.god.kotlin

import android.app.ProgressDialog
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

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