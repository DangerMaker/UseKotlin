package com.god.kotlin.query

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.transfer.TransferBankActivity
import com.god.kotlin.transfer.TransferBrokerActivity
import com.god.kotlin.transfer.TransferFundsActivity
import com.god.kotlin.transfer.TransferRecordActivity
import com.god.kotlin.util.Constant
import com.god.kotlin.util.startActivityExt
import com.god.kotlin.widget.TitleArrayAdapter
import kotlinx.android.synthetic.main.activity_transfer_menu.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class QueryMenuActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_menu)

        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "查询"

        with(transfer_list) {
            val arrayAdapter = TitleArrayAdapter(this@QueryMenuActivity, R.layout.trade_holder_other, array)
            adapter = arrayAdapter
            setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> startActivityExt(QueryTradeActivity::class.java) { putExtra(Constant.QUERY_TYPE, 0) }
                    1 -> startActivityExt(QueryTradeActivity::class.java) { putExtra(Constant.QUERY_TYPE, 1) }
                    2 -> startActivityExt(QueryTradeActivity::class.java) { putExtra(Constant.QUERY_TYPE, 2) }
                    3 -> startActivityExt(QueryTradeActivity::class.java) { putExtra(Constant.QUERY_TYPE, 3) }
                    else -> throw Exception()
                }
            }
        }
    }
}


private val array: Array<String> = arrayOf("当日成交", "当日委托", "历史成交", "历史委托")