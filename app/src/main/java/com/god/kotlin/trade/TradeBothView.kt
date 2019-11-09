package com.god.kotlin.trade

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.format2
import com.god.kotlin.util.getPriceColor
import com.god.kotlin.util.inflate
import com.god.kotlin.util.save100
import kotlinx.android.synthetic.main.view_top.view.*
import kotlinx.android.synthetic.main.view_trade.view.*
import kotlinx.android.synthetic.main.view_trade.view.input_code
import kotlinx.android.synthetic.main.view_trade.view.price
import kotlinx.android.synthetic.main.view_trade.view.submit
import kotlinx.android.synthetic.main.view_trade.view.total_num
import kotlinx.android.synthetic.main.view_trade_both.view.*
import kotlinx.android.synthetic.main.view_trade_ratio.view.*

class TradeBothView(context: Context?) : RelativeLayout(context), ITradeView {
    override fun getView(): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var list = mutableListOf<TradeStockEntity.Dang>()
    private lateinit var viewModel: SellViewModel
    private var direction: Boolean = true
    private lateinit var _data: TradeStockEntity
    private var buyMaxValue: Int = 0
    private var sellMaxValue: Int = 0

    init {
        context!!.inflate(R.layout.view_trade_both, this)

        input_code.setOnSearchListener {
            viewModel.search(it)
        }

        full.setOnClickListener {
            total_num.text = buyMaxValue.save100()
        }

        half.setOnClickListener {
            total_num.text = (buyMaxValue / 2).save100()
        }

        one_three.setOnClickListener {
            total_num.text = (buyMaxValue / 3).save100()
        }

        one_fourth.setOnClickListener {
            total_num.text = (buyMaxValue / 4).save100()
        }

        submit.setOnClickListener {
            val user = UserHelper.getUserByMarket(_data.market)
            val option = "买入"
            DialogUtils.showTwoButtonDialog(
                context, option + "交易确认", "确定$option",
                "操作类型：" + option + "\n" +
                        "股票代码：" + _data.stkcode + "  " + _data.stkname + "\n" +
                        "委托价格：" + price.text.toDouble() + "\n" +
                        "委托数量：" + total_num.text.toInt() + "\n" +
                        "委托方式：" + "限价委托" + "\n" +
                        "股东代码：" + user.secuid
            ) { _, _ ->
                viewModel.transaction(
                    _data.market, _data.stkcode, user.secuid, user.fundid,
                    price.text.toDouble(), total_num.text.toInt(), "0B"
                )
            }
        }

        submit1.setOnClickListener {
            val user = UserHelper.getUserByMarket(_data.market)
            val option = "卖出"
            DialogUtils.showTwoButtonDialog(
                context, option + "交易确认", "确定$option",
                "操作类型：" + option + "\n" +
                        "股票代码：" + _data.stkcode + "  " + _data.stkname + "\n" +
                        "委托价格：" + price1.text.toDouble() + "\n" +
                        "委托数量：" + total_num1.text.toInt() + "\n" +
                        "委托方式：" + "限价委托" + "\n" +
                        "股东代码：" + user.secuid
            ) { _, _ ->
                viewModel.transaction(
                    _data.market, _data.stkcode, user.secuid, user.fundid,
                    price.text.toDouble(), total_num.text.toInt(), "0S"
                )
            }
        }


    }

    override fun initData(flag: Boolean, vm: SellViewModel) {
        viewModel = vm

        input_code.setBackgroundResource(R.drawable.trade_gray_corner_border)

        price.setColor(R.color.trade_red)
        total_num.setColor(R.color.trade_red)
        submit.setBackgroundResource(R.drawable.trade_red_corner_full)
        submit.text = "立即买入"

        price1.setColor(R.color.trade_blue)
        total_num1.setColor(R.color.trade_blue)
        submit1.setBackgroundResource(R.drawable.trade_blue_corner_full)
        submit1.text = "立即卖出"

    }

    override fun setStockCode(code: String) {
        input_code.search(code)
    }

    override fun setAvailable(avail: Avail) {
        if (avail.direction) {
            buyMaxValue = avail.num
            available_num_buy.text = "可买${buyMaxValue}股"
        } else {
            sellMaxValue = avail.num
            available_num_sell.text = "可卖${sellMaxValue}股"
        }
    }

    override fun setData(data: TradeStockEntity) {
        val user = UserHelper.getUserByMarket(data.market)

        data.let {
            _data = data
            input_code.setData(it.stkcode, it.stkname)
            price.text = it.fixprice.format2()
            price1.text = it.fixprice.format2()
//
            viewModel.getAvailable(
                data.market, user.secuid, user.fundid,
                it.stkcode, it.fixprice, true
            )
            viewModel.getAvailable(
                data.market, user.secuid, user.fundid,
                it.stkcode, it.fixprice, false
            )
        }
    }

    override fun updateHQ(data: TradeStockEntity) {
        data.let {
            newest_price.text = it.fNewest.format2()
            newest_price.setTextColor(ContextCompat.getColorStateList(context, getPriceColor(it.fNewest, it.fOpen)))
            last_price.text = it.fLastClose.format2()
            limit_up_price.text = (it.fLastClose * 1.1f).format2()
            limit_up_price.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_red))
            limit_down_price.text = (it.fLastClose * 0.9f).format2()
            limit_down_price.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_green))

            list.clear()
            data.ask.reverse()
            list.addAll(data.ask)
            list.addAll(data.bid)

            level1_horizontal.setData1(data)

        }
    }

}

