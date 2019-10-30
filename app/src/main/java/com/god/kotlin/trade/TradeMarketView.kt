package com.god.kotlin.trade

import android.content.Context
import android.content.DialogInterface
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
import com.ez08.trade.tools.YiChuangUtils
import com.god.kotlin.R
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.view_top.view.*
import kotlinx.android.synthetic.main.view_trade.*
import kotlinx.android.synthetic.main.view_trade.view.*
import kotlinx.android.synthetic.main.view_trade.view.available_num
import kotlinx.android.synthetic.main.view_trade.view.input_code
import kotlinx.android.synthetic.main.view_trade.view.level_view
import kotlinx.android.synthetic.main.view_trade.view.submit
import kotlinx.android.synthetic.main.view_trade.view.total_num
import kotlinx.android.synthetic.main.view_trade_market.view.*
import kotlinx.android.synthetic.main.view_trade_ratio.view.*

class TradeMarketView(context: Context?) : RelativeLayout(context), ITradeView {

    private var list = mutableListOf<TradeStockEntity.Dang>()
    private var adapter: LevelAdapter
    private lateinit var viewModel: SellViewModel
    private var direction: Boolean = true
    private var _data: TradeStockEntity? = null
    private var maxValue: Int = 0

    init {
        context!!.inflate(R.layout.view_trade_market, this)
        adapter = LevelAdapter(list, context)
        level_view.adapter = adapter

        input_code.setOnSearchListener {
            viewModel.search(it)
        }

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
            _data?.let {
                val user = UserHelper.getUserByMarket(it.market)
                val postFlag = getTagByQuoteName(if (direction) "0B" else "0S", quote_way.text.toString())
                if(TextUtils.isEmpty(postFlag)){
                    "请选择报价方式".toast(context)
                    return@setOnClickListener
                }

                val option = if (direction) "买入" else "卖出"

                DialogUtils.showTwoButtonDialog(
                    context, option + "交易确认", "确定$option",
                    "操作类型：" + option + "\n" +
                            "股票代码：" + it.stkcode + "  " + it.stkname + "\n" +
                            "委托数量：" + total_num.text.toIntOrZero() + "\n" +
                            "委托方式：" + quote_way.text.toString() +  postFlag + "\n" +
                            "股东代码：" + user.secuid
                ) { _, _ ->
                    viewModel.transaction(
                        it.market, it.stkcode, user.secuid, user.fundid,
                        it.fixprice, total_num.text.toInt(), postFlag
                    )
                }
            }
        }

    }

    override fun initData(flag: Boolean, vm: SellViewModel) {
        viewModel = vm
        direction = flag

        if (direction) {
            input_code.setBackgroundResource(R.drawable.trade_input_bg)
            total_num.setColor(R.color.trade_red)
            submit.setBackgroundResource(R.drawable.trade_red_corner_full)
            layout_quote.setBackgroundResource(R.drawable.trade_input_bg)
            submit.text = "立即买入"
        } else {
            input_code.setBackgroundResource(R.drawable.trade_input_bg_blue)
            total_num.setColor(R.color.trade_blue)
            layout_quote.setBackgroundResource(R.drawable.trade_input_bg_blue)
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
//            price.text = it.fixprice.format2()

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

