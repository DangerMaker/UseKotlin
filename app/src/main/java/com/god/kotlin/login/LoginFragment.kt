package com.god.kotlin.login

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ez08.trade.net.Client
import com.god.kotlin.BaseActivity
import com.god.kotlin.MainActivity
import com.god.kotlin.R
import com.god.kotlin.menu.MenuActivity
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.trade_fragment_login.*

class LoginFragment : Fragment(), TradePhoneCheckWindows.OnPhoneCheckListener {

    private lateinit var serverName: String
    private var sharePref: SharedPreferences? = null
    private lateinit var viewModel: LoginViewModel
    private lateinit var phoneCheckWindows: TradePhoneCheckWindows

    private val items = arrayOf("资金账户", "深A", "沪Ａ", "深Ｂ", "沪Ｂ", "沪港通", "股转Ａ", "股转Ｂ", "开放式基金", "深港通")
    private val itemsCode = arrayOf("Z", "0", "1", "2", "3", "5", "6", "7", "J", "S")

    companion object {
        fun newInstance(serverName: String): LoginFragment {

            return LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(SERVER_ARG, serverName)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.trade_fragment_login, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        serverName = arguments?.getString(SERVER_ARG) ?: return

        sharePref = provideAccountSharedPref(context)
        sharePref?.apply {
            auto_input?.isChecked = getBoolean(SHARE_PREF_AUTO_INPUT, false)
            username_edit?.setText(getString(SHARE_PREF_ACCOUNT, ""))
            password_edit?.setText(getString(SHARE_PREF_PASSWORD, ""))
        }

        check_code.inputType = EditorInfo.TYPE_CLASS_PHONE

        auto_input.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked)
                sharePref?.edit {
                    clear()
                }
        }

        safe_code_iv.setOnClickListener {
            if (Client.getInstance().state == Client.STATE.EXCHANGE ||
                Client.getInstance().state == Client.STATE.DISCONNECT
            ) {
                Client.getInstance().connect()
            }
        }

        viewModel = (activity as MainActivity).obtainViewModel()

        viewModel.success.observe(this, Observer {
            //            login_loading.visibility = View.GONE
            (activity as BaseActivity).dismissBusyDialog()
            if (it) {
                startActivity(Intent(context, MenuActivity::class.java))
                activity?.finish()
            } else {

            }
        })

        viewModel.checkSuccess.observe(this, Observer {
            setMobile(context, defaultItem, username_edit.text.toString(), phoneNum)
            login(checkNum)
        })

        viewModel.bitmap.observe(this, Observer {
            if (it != null) {
                safe_code_iv.setImageBitmap(it)
            }
        })

        viewModel.tips.observe(this, Observer {
            it.toast(context)
        })

        account_layout.setOnClickListener {
            showPickDialog()
        }

        login_btn.setOnClickListener {
            //判断有没有手机号
            phoneNum = getMobile(context, defaultItem, username_edit.text.toString())
            if (TextUtils.isEmpty(phoneNum)) {
                //弹窗
                phoneCheckWindows.showPopupWindow(it)
            } else {
                login(check_code.text.toString())
            }
        }

        phoneCheckWindows = TradePhoneCheckWindows(activity)
        phoneCheckWindows.setPhoneCheckListener(this)
    }

    private fun login(checkCode: String) {
        var isChecked = auto_input.isChecked
        if (isChecked)
            sharePref?.edit {
                putBoolean(SHARE_PREF_AUTO_INPUT, isChecked)
                putString(SHARE_PREF_ACCOUNT, username_edit.text?.toString())
                putString(SHARE_PREF_PASSWORD, password_edit.text?.toString())
            }

        (activity as BaseActivity).showBusyDialog()
        viewModel.login(defaultItem,
            username_edit.text.toString(), password_edit.text.toString(), checkCode,
            getInfo(context!!, phoneNum)
        )
    }

    private var defaultItem = "Z"
    private fun showPickDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setItems(items) { _, which ->
            account_type.text = items[which]
            defaultItem = itemsCode[which]
        }
        builder.setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    private lateinit var phoneNum: String
    override fun onSendNum(phoneNum: String) {
        this.phoneNum = phoneNum
        viewModel.sendSMS(phoneNum)
    }

    private lateinit var checkNum: String
    override fun onCheckNum(phoneNum: String, code: String) {
        this.checkNum = code
        viewModel.checkSMS(phoneNum, code)
    }

}

private const val SERVER_ARG = "login_server_arg"
private const val SHARE_PREF_ACCOUNT = "account"
private const val SHARE_PREF_PASSWORD = "password"
private const val SHARE_PREF_AUTO_INPUT = "auto_input"
