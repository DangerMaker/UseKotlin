package com.god.kotlin.change

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.showSelectDialog
import com.god.kotlin.util.toast
import kotlinx.android.synthetic.main.activity_change_pwd.*
import kotlinx.android.synthetic.main.activity_information_change.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class ChangePwdActivity : AppCompatActivity() {

    private var type = 0 // 0 交易密码 1资金密码
    private var array: Array<String> = arrayOf("交易密码", "资金密码")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pwd)

        toolbar_title.text = "修改密码"
        toolbar_back.setOnClickListener { finish() }
        changeState()
        select_type.setOnClickListener {
            showSelectDialog(this, array, DialogInterface.OnClickListener { _, which ->
                if (type != which) {
                    type = which
                    changeState()
                }
            })
        }

        val viewModel = obtainViewModel(PasswordViewModel::class.java)
        pwd_submit.setOnClickListener {
            val oldPwdValue = pwd_origin.text.toString()
            val newPwdValue = pwd_new.text.toString()
            val repeatPwdValue = pwd_repeat.text.toString()
            if (repeatPwdValue == newPwdValue) {
                "两次输入密码不一致".toast(this)
            }

            if (type == 0) {
                viewModel.postPwd(newPwdValue)
            } else {
                viewModel.postFundsPwd(newPwdValue, oldPwdValue)
            }
        }

        viewModel.result.observe(this, Observer {
            it.toast(this)
        })
    }

    private fun changeState() {
        if (type == 0) {
            pwd_type.text = "交易密码"
            pwd_title1.text = "当前交易密码"
            pwd_title2.text = "新交易密码　"
            pwd_title3.text = "确认交易密码"
        } else {
            pwd_type.text = "资金密码"
            pwd_title1.text = "当前资金密码"
            pwd_title2.text = "新资金密码　"
            pwd_title3.text = "确认资金密码"
        }
    }
}