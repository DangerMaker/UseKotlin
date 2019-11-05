package com.god.kotlin.util

import android.content.Context
import androidx.core.content.edit
import com.xuhao.didi.socket.common.interfaces.utils.TextUtils
import kotlinx.android.synthetic.main.trade_fragment_login.*

fun provideAccountSharedPref(context: Context?) =
    context?.applicationContext?.getSharedPreferences("login_account", Context.MODE_PRIVATE)

fun provideMobileSharedPref(context: Context?, market: String, account: String) =
    context?.applicationContext?.getSharedPreferences("$market&$account", Context.MODE_PRIVATE)

fun providePreSharedPref(context: Context?, user:String) =
    context?.applicationContext?.getSharedPreferences("$user&pre", Context.MODE_PRIVATE)


fun getMobile(context: Context?, market: String, account: String): String{
    val mobileStore = provideMobileSharedPref(context, market, account)
    var mobile: String? = null
    mobileStore?.apply {
        mobile = getString("mobile", "")
    }
    return if(TextUtils.isEmpty(mobile)){
        ""
    }else{
        mobile!!
    }
}

fun setMobile(context: Context?, market: String, account: String, mobile: String?) {
    if(mobile == null){
        return
    }

    val mobileStore = provideMobileSharedPref(context, market, account)
    mobileStore?.edit {
        putString("mobile", mobile)
    }
}
