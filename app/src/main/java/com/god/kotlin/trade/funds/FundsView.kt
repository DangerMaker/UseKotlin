package com.god.kotlin.trade.funds

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.god.kotlin.R
import com.god.kotlin.data.entity.Funds
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.trade.ITradeView
import com.god.kotlin.trade.LevelAdapter
import com.god.kotlin.trade.SellViewModel
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.view_funds.view.*
import kotlinx.android.synthetic.main.view_top.view.*
import kotlinx.android.synthetic.main.view_trade.view.*
import kotlinx.android.synthetic.main.view_trade_ratio.view.*

class FundsView(context: Context?) : RelativeLayout(context) {

    private lateinit var viewModel: FundsViewModel
    var position = 0

    init {
        context!!.inflate(R.layout.view_funds, this)
        bizhong.setOnClickListener {
            showSelectDialog(context, itemKey, DialogInterface.OnClickListener { _, which ->
                if (position != which) {
                    position = which
                    flag_country.setImageDrawable(ContextCompat.getDrawable(context, itemPic[position]))
                    money_type.text = itemKey[position]
                    viewModel.query(position)
                }
            })
        }
    }

    fun initViewModel(vm: FundsViewModel) {
        viewModel = vm
    }

    fun setData(funds: Funds) {
        keyong.text = funds.fundavl.format2()
        zongzichan.text = funds.marketvalue.format2()
        shizhi.text = funds.stkvalue.format2()
        kequ.text = funds.fundfrz.format2()
    }
}

private val itemKey = arrayOf("人民币", "港元", "美元")
private val itemPic = arrayOf(R.drawable.china_3x, R.drawable.china_3x, R.drawable.usa_3x)
