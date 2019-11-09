package com.god.kotlin.trade

import android.content.Context
import android.content.DialogInterface
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.view_trade_market.view.*
import kotlinx.android.synthetic.main.view_trade_trans.view.*
import kotlinx.android.synthetic.main.view_trade_trans.view.layout_quote
import kotlinx.android.synthetic.main.view_trade_trans.view.quote_way
import kotlinx.android.synthetic.main.view_trade_trans.view.total_num

class TradeTransView1(context: Context?) : AbsTradeView(context) {

    private var quoteType = "可转债转股"

    init {
        layout_quote.setOnClickListener {
            showSelectDialog(context, conversionType,
                DialogInterface.OnClickListener { _, which ->
                    quoteType = conversionType[which]
                    quote_way.text = quoteType


                })

        }
    }

    override fun inflate(): Int {
        return R.layout.view_trade_trans
    }

    override fun initData(flag: Boolean, vm: SellViewModel) {
        super.initData(flag, vm)
        if (flag) {
            layout_quote.setBackgroundResource(R.drawable.trade_input_bg)
        } else {
            layout_quote.setBackgroundResource(R.drawable.trade_input_bg_blue)
        }
    }

    override fun submit() {
        val postFlag = getTagByQuoteName("", quote_way.text.toString())

        DialogUtils.showTwoButtonDialog(
            context, option + "交易确认", "确定",
            "您确定要以股东代码${user!!.secuid}$quoteType'${_data!!.stkcode}${_data!!.stkname}'吗\n" +
                    "数量：${numView!!.text.toIntOrZero()}\r\n" +
                    "${quoteType}数量若大于最大可售，交易可能不会成功！"
        ) { _, _ ->
            viewModel.transaction(
                _data!!.market, _data!!.stkcode, user!!.secuid, user!!.fundid,
                _data!!.fixprice, numView!!.text.toIntOrZero(), postFlag
            )
        }
    }
}

