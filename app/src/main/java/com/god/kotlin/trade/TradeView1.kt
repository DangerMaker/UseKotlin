package com.god.kotlin.trade

import android.content.Context
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R

class TradeView1(context: Context?) : AbsTradeView(context) {

    override fun inflate(): Int {
       return R.layout.view_trade
    }

    override fun submit() {
        DialogUtils.showTwoButtonDialog(
            context, option + "交易确认", "确定$option",
            "操作类型：" + option + "\n" +
                    "股票代码：" + _data!!.stkcode + "  " + _data!!.stkname + "\n" +
                    "委托价格：" + priceView!!.text.toDouble() + "\n" +
                    "委托数量：" + numView!!.text.toInt() + "\n" +
                    "委托方式：" + quoteType + "\n" +
                    "股东代码：" + user!!.secuid) { _, _ ->

            viewModel.transaction(
                _data!!.market, _data!!.stkcode, user!!.secuid, user!!.fundid,
                priceView!!.text.toDouble(), numView!!.text.toInt(), postFlag
            )
        }
    }
}

