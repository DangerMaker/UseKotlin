package com.god.kotlin.trade

import android.content.Context
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.R
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.view_trade_both.view.*

class TradeBothView1(context: Context?) : AbsTradeView(context) {

    init {
        ratio_view1.setOnMaxListener {
            total_num1.text = it.toString()
        }

        submit1.setOnClickListener {
            if (_data == null) {
                "无效的股票，请稍后再试".toast(context)
                return@setOnClickListener
            }

            if (user == null) {
                "您没有当前市场账户".toast(context)
                return@setOnClickListener
            }

            if (total_num1.text == null) {
                "请选择数量".toast(context)
                return@setOnClickListener
            }

            postFlag = getFlagByQuoteName(false, quoteType)
            if(TextUtils.isEmpty(postFlag)){
                "请选择正确的报价方式".toast(context)
                return@setOnClickListener
            }

            DialogUtils.showTwoButtonDialog(
                context,  "卖出交易确认", "确定卖出",
                "操作类型：" + "卖出" + "\n" +
                        "股票代码：" + _data!!.stkcode + "  " + _data!!.stkname + "\n" +
                        "委托价格：" + price1!!.text.toDouble() + "\n" +
                        "委托数量：" + total_num1!!.text.toInt() + "\n" +
                        "委托方式：" + quoteType + "\n" +
                        "股东代码：" + user!!.secuid) { _, _ ->

                viewModel.transaction(
                    _data!!.market, _data!!.stkcode, user!!.secuid, user!!.fundid,
                    price1!!.text.toDouble(), total_num1!!.text.toInt(), postFlag
                )
            }
        }
    }

    override fun initData(flag: Boolean, vm: SellViewModel) {
        super.initData(flag, vm)
        inputView?.setBackgroundResource(R.drawable.trade_input_corner_border)

    }

    override fun setAvailable(avail: Avail) {
        if (avail.direction) {
            ratioView?.setMax(avail.num)
            availView?.text = "最大可买${ avail.num}股"
        } else {
            ratio_view1.setMax(avail.num)
            available_num_sell.text = "最大可卖${avail.num}股"
        }
    }

    override fun setData(data: TradeStockEntity) {
        data.let {
            _data = it
            firstHq = true

            inputView?.setData(it.stkcode, it.stkname)
            maxPrice?.text = it.maxrisevalue.format2()
            maxPrice?.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_red))
            minPrice?.text = it.maxdownvalue.format2()
            minPrice?.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_green))

            maxPriceLayout?.setOnClickListener {
                priceView?.text = maxPrice?.text.toString()
                price1?.text = maxPrice?.text.toString()
            }

            minPriceLayout?.setOnClickListener {
                priceView?.text = minPrice?.text.toString()
                price1?.text = minPrice?.text.toString()
            }

            user = UserHelper.getUserByMarket(_data?.market)
        }
    }

    override fun inflate(): Int {
       return R.layout.view_trade_both
    }

    override fun updateHQ(data: TradeStockEntity) {
        data.let {
            newestPrice?.text = it.fNewest.format2()
            newestPrice?.setTextColor(ContextCompat.getColorStateList(context, getPriceColor(it.fNewest, it.fOpen)))
            lastPrice?.text = it.fLastClose.format2()
            priceView?.text = it.fNewest.format2()

            list.clear()
            val temp = mutableListOf<TradeStockEntity.Dang>()
            temp.addAll(data.ask)
            temp.reverse()
            list.addAll(temp)
            list.addAll(data.bid)

            levelView.setOpenPrice(it.fOpen)
            levelView.update(list)

            if (firstHq) {
                priceView?.text = newestPrice?.text.toString()
                price1.text = newestPrice?.text.toString()

                if (user == null) {
                    return@let
                }

                val price = when (quoteType) {
                    "限价委托" -> {
                        it.fNewest
                    }
                    in szQuoteType -> {
                        if(direction){
                            _data?.maxrisevalue?:0.0
                        }else{
                            _data?.maxdownvalue?:0.0
                        }
                    }
                    else -> {
                        _data?.fixprice?:0.0
                    }
                }

                viewModel.getAvailable(
                    _data!!.market, user!!.secuid, user!!.fundid,
                    _data!!.stkcode, price, direction
                )

                viewModel.getAvailable(
                    _data!!.market, user!!.secuid, user!!.fundid,
                    _data!!.stkcode, price, false
                )
                firstHq = false
            }
        }
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

