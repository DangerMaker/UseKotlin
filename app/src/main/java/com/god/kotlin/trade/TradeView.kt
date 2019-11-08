package com.god.kotlin.trade

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.*
import com.god.kotlin.widget.level1.Level
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.view_top.view.*
import kotlinx.android.synthetic.main.view_trade.*
import kotlinx.android.synthetic.main.view_trade.view.*
import kotlinx.android.synthetic.main.view_trade_ratio.view.*
import java.util.*

class TradeView(context: Context?) : RelativeLayout(context), ITradeView {
    private var list = mutableListOf<TradeStockEntity.Dang>()
    //    private var adapter: LevelAdapter
    private lateinit var viewModel: SellViewModel
    private var direction: Boolean = true
    private var _data: TradeStockEntity? = null
    private var maxValue: Int = 0
    private val levelView: Level

    init {
        context!!.inflate(R.layout.view_trade, this)
//        adapter = LevelAdapter(list, context)
//        level_view.adapter = adapter
//        level_view.setOnItemClickListener { parent, view, position, id ->
//            val prices = list[position]
//            price.text = prices.fPrice.format2()
//        }
        levelView = (level_view as Level)
        levelView.setOnItemClickListener(object : Level.OnItemClickListener {
            override fun onClick(position: Int) {
                if(position > list.size){
                    return
                }
                val prices = list[position]
                price.text = prices.fPrice.format2()
            }
        })

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
            if (_data == null) {
                "无效的股票，请稍后再试".toast(context)
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(price.text)) {
                "请输入有效价格".toast(context)
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(total_num.text)) {
                "请输入有效数量".toast(context)
                return@setOnClickListener
            }


            val user = UserHelper.getUserByMarket(_data!!.market)
            val option = if (direction) "买入" else "卖出"

            DialogUtils.showTwoButtonDialog(
                context, option + "交易确认", "确定$option",
                "操作类型：" + option + "\n" +
                        "股票代码：" + _data!!.stkcode + "  " + _data!!.stkname + "\n" +
                        "委托价格：" + price.text.toDouble() + "\n" +
                        "委托数量：" + total_num.text.toInt() + "\n" +
                        "委托方式：" + "限价委托" + "\n" +
                        "股东代码：" + user.secuid
            ) { _, _ ->
                viewModel.transaction(
                    _data!!.market, _data!!.stkcode, user.secuid, user.fundid,
                    price.text.toDouble(), total_num.text.toInt(), if (direction) "0B" else "0S"
                )
            }
        }

    }

    override fun initData(flag: Boolean, vm: SellViewModel) {
        viewModel = vm
        direction = flag

        if (direction) {
            input_code.setBackgroundResource(R.drawable.trade_input_bg)
            price.setColor(R.color.trade_red)
            total_num.setColor(R.color.trade_red)
            submit.setBackgroundResource(R.drawable.trade_red_corner_full)
            submit.text = "立即买入"
        } else {
            input_code.setBackgroundResource(R.drawable.trade_input_bg_blue)
            price.setColor(R.color.trade_blue)
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


    private var firstHq = false

    override fun setData(data: TradeStockEntity) {
        data.let {
            _data = it
            input_code.setData(it.stkcode, it.stkname)

            limit_up_price.text = it.maxrisevalue.format2()
            limit_up_price.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_red))
            limit_down_price.text = it.maxdownvalue.format2()
            limit_down_price.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_green))

            limit_up_layout.setOnClickListener {
                price.text = limit_up_price.text.toString()
            }

            limit_down_layout.setOnClickListener {
                price.text = limit_down_price.text.toString()
            }

            firstHq = true
        }
    }

    override fun updateHQ(data: TradeStockEntity) {
        data.let {
            newest_price.text = it.fNewest.format2()
            newest_price.setTextColor(ContextCompat.getColorStateList(context, getPriceColor(it.fNewest, it.fOpen)))
            last_price.text = it.fLastClose.format2()
            price.text = it.fNewest.format2()

            list.clear()

            val temp = mutableListOf<TradeStockEntity.Dang>()
            temp.addAll(data.ask)
            temp.reverse()

            list.addAll(temp)
            list.addAll(data.bid)

//            adapter.setOpenPrice(it.fOpen)
//            adapter.notifyDataSetChanged()
            levelView.setOpenPrice(it.fOpen)
            levelView.update(list)

            if (firstHq) {
                price.text = newest_price.text.toString()
                val user = UserHelper.getUserByMarket(_data!!.market)
                viewModel.getAvailable(
                    _data!!.market, user.secuid, user.fundid,
                    _data!!.stkcode, it.fNewest, direction
                )
                firstHq = false
            }
        }
    }

}

