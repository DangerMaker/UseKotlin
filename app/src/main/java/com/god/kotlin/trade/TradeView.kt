package com.god.kotlin.trade

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.view_top.view.*
import kotlinx.android.synthetic.main.view_trade.*
import kotlinx.android.synthetic.main.view_trade.view.*
import kotlinx.android.synthetic.main.view_trade_ratio.view.*

class TradeView(context: Context?) : RelativeLayout(context), ITradeView {

    private var list = mutableListOf<TradeStockEntity.Dang>()
    private var adapter: LevelAdapter
    private lateinit var viewModel: SellViewModel
    private var direction: Boolean = true
    private lateinit var _data: TradeStockEntity
    private var maxValue: Int = 0

    init {
        context!!.inflate(R.layout.view_trade, this)
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
            viewModel.transaction(
                "market", _data.stkcode,
                price.text.toDouble(), total_num.text.toInt(), if(direction) "0B" else "0S"
            )
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
    }

    override fun setAvailable(max: Int) {
        maxValue = max
        if(direction) {
            available_num.text = "可买${max}股"
        }else{
            available_num.text = "可卖${max}股"
        }
    }

    override fun setData(data: TradeStockEntity) {
        data.let {
            _data = data
            input_code.setData(it.stkcode, it.stkname)
            price.text = it.fixprice.format2()

            viewModel.getAvailable(it.stkcode, it.fixprice,  if(direction) "0B" else "0S")

            newest_price.text = it.fNewest.format2()
            newest_price.setTextColor(ContextCompat.getColorStateList(context, getPriceColor(it.fNewest, it.fOpen)))
            last_price.text = it.fLastClose.format2()
            limit_up_price.text = (it.fLastClose * 1.1f).format2()
            limit_up_price.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_red))
            limit_down_price.text = (it.fLastClose * 0.9f).format2()
            limit_down_price.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_green))

            list.clear()
            list.addAll(data.bid)
            list.addAll(data.ask)
            adapter.notifyDataSetChanged()
        }
    }
}

