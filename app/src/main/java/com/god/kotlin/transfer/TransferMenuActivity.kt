package com.god.kotlin.transfer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.god.kotlin.R
import com.god.kotlin.profile.AccountListActivity
import com.god.kotlin.util.startActivityExt
import com.god.kotlin.widget.TitleArrayAdapter
import kotlinx.android.synthetic.main.activity_transfer_menu.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class TransferMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_menu)

        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "银行转账"

        with(transfer_list) {
            val arrayAdapter = TitleArrayAdapter(this@TransferMenuActivity, R.layout.trade_holder_other, array)
            adapter = arrayAdapter
            setOnItemClickListener{_,_,position,_->
                when(position){
                    0 -> startActivityExt(TransferBankActivity::class.java)
                    1 -> startActivityExt(TransferBrokerActivity::class.java)
                    2 -> startActivityExt(TransferFundsActivity::class.java)
                    3 -> startActivityExt(TransferRecordActivity::class.java)
                    else -> throw Exception()
                }
            }
        }
    }
}



private val array: Array<String> = arrayOf("银行转证券", "证券转银行", "资金余额", "转账查询")