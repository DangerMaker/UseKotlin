package com.god.kotlin.trade

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.view_trade_market.view.*

class TradeMarketView1(context: Context?) : AbsTradeView(context) {

    private var select: Array<String> = szQuoteType

    init {
        quoteType = select[0]
        quote_way.text = quoteType

        layout_quote.setOnClickListener {
            showSelectDialog(context, select,
                DialogInterface.OnClickListener { _, which ->
                    quoteType = select[which]
                    quote_way.text = quoteType
                })
        }
    }

    override fun inflate(): Int {
        return R.layout.view_trade_market
    }

    override fun setData(data: TradeStockEntity) {
        super.setData(data)
        select = when (data.market) {
            "0" -> szQuoteType
            "1" -> shQuoteType
            else -> arrayOf()
        }
        quoteType = select[0]
        quote_way.text = quoteType
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
        DialogUtils.showTwoButtonDialog(
            context, option + "交易确认", "确定$option",
            "操作类型：" + option + "\n" +
                    "股票代码：" + _data!!.stkcode + "  " + _data!!.stkname + "\n" +
                    "委托数量：" + numView!!.text.toIntOrZero() + "\n" +
                    "委托方式：" + quoteType + "\n" +
                    "股东代码：" + user!!.secuid
        ) { _, _ ->
            viewModel.transaction(
                _data!!.market, _data!!.stkcode, user!!.secuid, user!!.fundid,
                0.0, numView!!.text.toInt(), postFlag
            )
        }
    }
}

