package com.god.kotlin.transfer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.TransferRecord
import com.god.kotlin.profile.AccountListActivity
import com.god.kotlin.profile.AccountListViewModel
import com.god.kotlin.util.inflate
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.startActivityExt
import com.god.kotlin.widget.TitleArrayAdapter
import kotlinx.android.synthetic.main.activity_transfer_menu.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class TransferBankActivity : AppCompatActivity() {

    val list = mutableListOf<TransferRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_bank)

        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "银行转证券"



//        val viewModel = obtainViewModel(TransferRecodViewModel::class.java)
//        viewModel.recordList.observe(this, Observer {
//            list.clear()
//            list.addAll(it)
//            arrayAdapter.notifyDataSetChanged()
//        })
//
//        viewModel.query("fundid")
    }
}

