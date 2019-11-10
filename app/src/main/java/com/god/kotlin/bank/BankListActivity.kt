package com.god.kotlin.bank

import android.os.Bundle
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.transfer.TransferRecordActivity
import com.god.kotlin.util.startActivityExt
import com.god.kotlin.widget.TitleArrayAdapter
import kotlinx.android.synthetic.main.activity_transfer_menu.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class BankListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_menu)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "银行转账"

        with(transfer_list) {
            val arrayAdapter = TitleArrayAdapter(this@BankListActivity, R.layout.trade_holder_other, array)
            adapter = arrayAdapter
            setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> startActivityExt(TradeBank2SecurityActivity::class.java) { putExtra("type", 1) }
                    1 -> startActivityExt(TradeBank2SecurityActivity::class.java) { putExtra("type", 2) }
                    2 -> startActivityExt(TradeBank2SecurityActivity::class.java) { putExtra("type", 3) }
                    3 -> startActivityExt(TransferRecordActivity::class.java)
                    else -> throw Exception()
                }
            }
        }
    }
}


private val array: Array<String> = arrayOf("银行转证券", "证券转银行", "资金额度","转账查询")

