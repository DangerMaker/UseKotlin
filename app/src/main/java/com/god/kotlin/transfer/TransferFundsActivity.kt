package com.god.kotlin.transfer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.god.kotlin.R
import com.god.kotlin.data.entity.TransferRecord
import kotlinx.android.synthetic.main.toolbar_normal.*

class TransferFundsActivity : AppCompatActivity() {

    val list = mutableListOf<TransferRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_funds)

        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "资金余额"



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

