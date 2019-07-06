package com.god.kotlin.ipo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.god.kotlin.R
import com.god.kotlin.ipo.query.QueryIpoActivity
import com.god.kotlin.query.QueryTradeActivity
import com.god.kotlin.util.Constant
import com.god.kotlin.util.startActivityExt
import com.god.kotlin.widget.TitleArrayAdapter
import kotlinx.android.synthetic.main.activity_transfer_menu.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class IpoMenuActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_menu)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "新股申购"

        with(transfer_list) {
            val arrayAdapter = TitleArrayAdapter(this@IpoMenuActivity, R.layout.trade_holder_other, array)
            adapter = arrayAdapter
            setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> startActivityExt(QueryIpoActivity::class.java) { putExtra(Constant.QUERY_TYPE, 0) }
                    1 -> startActivityExt(QueryIpoActivity::class.java) { putExtra(Constant.QUERY_TYPE, 1) }
                    2 -> startActivityExt(QueryIpoActivity::class.java) { putExtra(Constant.QUERY_TYPE, 2) }
                    else -> throw Exception()
                }
            }
        }
    }
}


private val array: Array<String> = arrayOf("配号查询", "中签查询", "新股申购代缴款查询")