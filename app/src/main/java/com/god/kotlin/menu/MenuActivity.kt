package com.god.kotlin.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.ez08.trade.net.Client
import com.ez08.trade.net.NetUtil
import com.ez08.trade.net.login.STradeGateLogin
import com.ez08.trade.net.login.STradeGateLoginA
import com.ez08.trade.tools.CommonUtils
import com.ez08.trade.ui.TradeMenuActivity
import com.ez08.trade.user.TradeUser
import com.ez08.trade.user.UserHelper
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.util.JumpActivity
import com.god.kotlin.util.addFragment
import com.god.kotlin.util.toast
import com.xuhao.didi.socket.client.impl.exceptions.ManuallyDisconnectException
import java.util.ArrayList
import javax.security.auth.login.LoginException

class MenuActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        addFragment(TAG, R.id.container, true) {
            MenuFragment.newInstance()
        }

    }

    override fun onResume() {
        super.onResume()
        connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissBusyDialog()
    }

    override fun onKickOUt() {
        "您被踢出".toast(this)
        Client.getInstance().logout()
    }

    private fun connect(){
        if (Client.getInstance().state != Client.STATE.LOGIN) {
            showBusyDialog()
            if (Client.getInstance().state == Client.STATE.DISCONNECT) {
                Client.getInstance().connect()
            }
        }
    }

    override fun onDisConnect(e: Exception) {
        if (e is LoginException) {
            connect()
        }
    }

    override fun onExchange() {
        if (Client.sessionId == null) {
            dismissBusyDialog()
            JumpActivity.start(context, "登录")
            finish()
        } else {
            setLoginSessionPackage()
        }
    }

    private fun setLoginSessionPackage() {
        val tradeGateLogin = STradeGateLogin()
        tradeGateLogin.setBody(Client.strUserType, Client.userId, Client.password, Client.sessionId, Client.strNet2)
        Client.getInstance().send(tradeGateLogin) { success, data ->
            dismissBusyDialog()
            val gateLoginA =
                STradeGateLoginA(data.headBytes, data.bodyBytes, Client.getInstance().aesKey)
            if (!gateLoginA.getbLoginSucc()) {
                CommonUtils.show(this, gateLoginA.getSzErrMsg())
                Client.getInstance().shutDown()
                JumpActivity.start(this, "登录")
            } else {
                val list = ArrayList<TradeUser>()
                for (i in gateLoginA.list.indices) {
                    val user = TradeUser()
                    user.market = NetUtil.byteToStr(gateLoginA.list[i].sz_market)
                    user.name = NetUtil.byteToStr(gateLoginA.list[i].sz_name)
                    user.fundid = gateLoginA.list[i].n64_fundid.toString() + ""
                    user.custcert = NetUtil.byteToStr(gateLoginA.list[i].sz_custcert)
                    user.custid = gateLoginA.list[i].n64_custid.toString() + ""
                    user.secuid = NetUtil.byteToStr(gateLoginA.list[i].sz_secuid)
                    list.add(user)
                }

                UserHelper.setUserList(list)
            }
        }
    }
}

private const val TAG = "MenuActivity_MenuFragment"