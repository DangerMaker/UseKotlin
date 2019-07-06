package com.god.kotlin.profile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.Account
import com.god.kotlin.util.inflate
import com.god.kotlin.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_account_list.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class AccountListActivity : AppCompatActivity() {

    private val list = mutableListOf<Account>()
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_list)
        Log.e("AccountListActivity","onCreate")

        toolbar_title.text = "股东资料"
        toolbar_back.setOnClickListener { finish() }

        with(account_list) {
            accountAdapter = AccountAdapter(list,context)
            account_list.addHeaderView(inflate(R.layout.trade_holder_shareholders_title))
            adapter = accountAdapter
        }

        val viewModel = obtainViewModel(AccountListViewModel::class.java)
        viewModel.accountList.observe(this, Observer {
            list.clear()
            list.addAll(it)
            accountAdapter.notifyDataSetChanged()
            Log.e("AccountListActivity","notifyDataSetChanged")
        })

        viewModel.query()
    }

    override fun onResume() {
        super.onResume()
        Log.e("AccountListActivity","onResume")

    }
}