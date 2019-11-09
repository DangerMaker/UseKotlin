package com.god.kotlin.trade

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.view_trade_market.view.*

class TradeMarketView1(context: Context?) : AbsTradeView(context) {

    init {
        layout_quote.setOnClickListener {
            _data?.let {
                val select: Array<String> = when (it.market) {
                    "0" -> szQuoteType
                    "1" -> shQuoteType
                    else -> arrayOf()
                }
                showSelectDialog(context, select,
                    DialogInterface.OnClickListener { _, which -> quote_way.text = select[which] })
            } ?: "请输入代码".toast(context)
        }
    }

    override fun inflate(): Int {
        return R.layout.view_trade_market
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
        val postFlag = getTagByQuoteName(if (direction) "0B" else "0S", quote_way.text.toString())

        if (TextUtils.isEmpty(postFlag)) {
            "请选择报价方式".toast(context)
            return
        }

        _data.let {
            DialogUtils.showTwoButtonDialog(
                context, option + "交易确认", "确定$option",
                "操作类型：" + option + "\n" +
                        "股票代码：" + it!!.stkcode + "  " + it!!.stkname + "\n" +
                        "委托数量：" + numView!!.text.toIntOrZero() + "\n" +
                        "委托方式：" + quote_way.text.toString() + postFlag + "\n" +
                        "股东代码：" + user!!.secuid
            ) { _, _ ->
                viewModel.transaction(
                    it.market, it.stkcode, user!!.secuid, user!!.fundid,
                    it.fixprice, numView!!.text.toInt(), postFlag
                )
            }
        }
    }
}

