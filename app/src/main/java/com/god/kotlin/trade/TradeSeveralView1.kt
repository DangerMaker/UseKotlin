package com.god.kotlin.trade

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R
import com.god.kotlin.util.toIntOrZero
import com.god.kotlin.util.toast
import kotlinx.android.synthetic.main.view_trade_several.view.*

class TradeSeveralView1(context: Context?) : AbsTradeView(context) {

    override fun inflate(): Int {
        return R.layout.view_trade_several
    }

    override fun initData(flag: Boolean, vm: SellViewModel) {
        super.initData(flag, vm)
        if (flag) {
            single_num.setColor(R.color.trade_red)
        } else {
            single_num.setColor(R.color.trade_blue)
        }
    }

    override fun submit() {
        if (TextUtils.isEmpty(single_num.text) || single_num.text.toIntOrZero() == 0) {
            "请输入有效数量".toast(context)
            return
        }

        if (single_num.text.toIntOrZero() > total_num.text.toIntOrZero()) {
            "总委托数量不得小于单笔数量".toast(context)
            return
        }

        val single = single_num.text.toIntOrZero()
        var times: Int
        var remainder: Int

        times = total_num.text.toIntOrZero() / single
        remainder = total_num.text.toIntOrZero() % single

        DialogUtils.showTwoButtonDialog(
            context, option + "交易确认", "确定$option",
            "操作类型：" + option + "\n" +
                    "股票代码：" + _data!!.stkcode + "  " + _data!!.stkname + "\n" +
                    "委托价格：" + priceView!!.text.toDouble() + "\n" +
                    "委托数量：" + numView!!.text.toInt() + "\n" +
                    "单笔数量：" + single + "\n" +
                    "委托方式：" + quoteType + "\n" +
                    "股东代码：" + user!!.secuid) { _, _ ->
            viewModel.transactionSeveral(
                _data!!.market, _data!!.stkcode, user!!.secuid, user!!.fundid,
                priceView!!.text.toDouble(), single, postFlag, times, remainder
            )
        }
    }
}

