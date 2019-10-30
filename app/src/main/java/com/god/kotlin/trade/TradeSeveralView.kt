package com.god.kotlin.trade

import android.content.Context
import android.util.Log
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.view_top.view.*
import kotlinx.android.synthetic.main.view_trade.view.input_code
import kotlinx.android.synthetic.main.view_trade.view.price
import kotlinx.android.synthetic.main.view_trade.view.submit
import kotlinx.android.synthetic.main.view_trade.view.total_num
import kotlinx.android.synthetic.main.view_trade_ratio.view.*
import kotlinx.android.synthetic.main.view_trade_several.view.*
import kotlinx.android.synthetic.main.view_trade_several.view.level_view
import kotlinx.android.synthetic.main.view_trade_several.view.available_num

class TradeSeveralView(context: Context?) : RelativeLayout(context), ITradeView {
    private var list = mutableListOf<TradeStockEntity.Dang>()
    private lateinit var viewModel: SellViewModel
    private var adapter: LevelAdapter

    private var direction: Boolean = true
    private lateinit var _data: TradeStockEntity
    private var maxValue: Int = 0

    init {
        context!!.inflate(R.layout.view_trade_several, this)
        adapter = LevelAdapter(list, context)
        level_view.adapter = adapter

        input_code.setOnSearchListener {
            viewModel.search(it)
        }

        full.setOnClickListener {
            total_num.text = maxValue.save100()
        }

        half.setOnClickListener {
            total_num.text = (maxValue / 2).save100()
        }

        one_three.setOnClickListener {
            total_num.text = (maxValue / 3).save100()
        }

        one_fourth.setOnClickListener {
            total_num.text = (maxValue / 4).save100()
        }

        submit.setOnClickListener {
            val user = UserHelper.getUserByMarket(_data.market)
            val option = if (direction) "买入" else "卖出"
            var singleNum = single_num.text.toIntOrZero()
            var times = 1
            if (singleNum == 0) {
                times = 1
                singleNum = total_num.text.toIntOrZero()
            } else {
                times = total_num.text.toIntOrZero() / singleNum
            }

            DialogUtils.showTwoButtonDialog(
                context, option + "交易确认", "确定$option",
                "操作类型：" + option + "\n" +
                        "股票代码：" + _data.stkcode + "  " + _data.stkname + "\n" +
                        "委托价格：" + price.text.toDouble() + "\n" +
                        "委托数量：" + total_num.text.toInt() + "\n" +
                        "单笔数量：" + singleNum + "\n" +
                        "委托方式：" + "限价委托" + "\n" +
                        "股东代码：" + user.secuid
            ) { _, _ ->
                for (i in 0 until times) {
                    viewModel.transaction(
                        _data.market, _data.stkcode, user.secuid, user.fundid,
                        price.text.toDouble(), singleNum, if (direction) "0B" else "0S"
                    )
                }
            }
        }

    }

    override fun initData(flag: Boolean, vm: SellViewModel) {
        Log.e("TradeView", "initData")
        viewModel = vm
        direction = flag

        if (direction) {
            input_code.setBackgroundResource(R.drawable.trade_input_bg)
            price.setColor(R.color.trade_red)
            total_num.setColor(R.color.trade_red)
            single_num.setColor(R.color.trade_red)
            submit.setBackgroundResource(R.drawable.trade_red_corner_full)
            submit.text = "立即买入"
        } else {
            input_code.setBackgroundResource(R.drawable.trade_input_bg_blue)
            price.setColor(R.color.trade_blue)
            single_num.setColor(R.color.trade_blue)
            total_num.setColor(R.color.trade_blue)
            submit.setBackgroundResource(R.drawable.trade_blue_corner_full)
            submit.text = "立即卖出"
        }

    }

    override fun setStockCode(code: String) {
        input_code.search(code)
    }

    override fun setAvailable(avail: Avail) {
        if (avail.direction == direction) {
            if (avail.direction) {
                maxValue = avail.num
                available_num.text = "可买${maxValue}股"
            } else {
                maxValue = avail.num
                available_num.text = "可卖${maxValue}股"
            }
        }
    }

    override fun setData(data: TradeStockEntity) {
        val user = UserHelper.getUserByMarket(data.market)

        data.let {
            _data = data
            input_code.setData(it.stkcode, it.stkname)
            price.text = it.fixprice.format2()
//
            viewModel.getAvailable(
                data.market, user.secuid, user.fundid,
                it.stkcode, it.fixprice, direction
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
            adapter.setOpenPrice(it.fOpen)
            adapter.notifyDataSetChanged()

        }
    }

}

