package com.god.kotlin.login

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.god.kotlin.R
import com.god.kotlin.menu.MenuActivity
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.provideAccountSharedPref
import kotlinx.android.synthetic.main.trade_fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var serverName: String
    private var sharePref: SharedPreferences? = null

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

        auto_input.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked)
                sharePref?.edit {
                    clear()
                }

        }

        val viewModel = obtainViewModel(LoginViewModel::class.java)

        viewModel.success.observe(this, Observer {
            login_loading.visibility = View.GONE
            if (it) {
                startActivity(Intent(context, MenuActivity::class.java))
            }else{

            }
        })

        login_btn.setOnClickListener {
            var isChecked = auto_input.isChecked
            if (isChecked)
                sharePref?.edit {
                    putBoolean(SHARE_PREF_AUTO_INPUT, isChecked)
                    putString(SHARE_PREF_ACCOUNT, username_edit.text?.toString())
                    putString(SHARE_PREF_PASSWORD, password_edit.text?.toString())
                }

            login_loading.visibility = View.VISIBLE
            viewModel.lggin(
                "Z", "userid", "password", "checkCode",
                "veriId"
            )

        }

    }
}

private const val SERVER_ARG = "login_server_arg"
private const val SHARE_PREF_ACCOUNT = "account"
private const val SHARE_PREF_PASSWORD = "password"
private const val SHARE_PREF_AUTO_INPUT = "auto_input"
