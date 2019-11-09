package com.god.kotlin.trade

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.god.kotlin.R
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.data.entity.User
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.format2
import com.god.kotlin.util.getPriceColor
import com.god.kotlin.util.inflate
import com.god.kotlin.util.toast
import com.god.kotlin.widget.RatioView
import com.god.kotlin.widget.level1.Level

abstract class AbsTradeView(context: Context?) : RelativeLayout(context), ITradeView {

    protected lateinit var viewModel: SellViewModel
    protected var list = mutableListOf<TradeStockEntity.Dang>()
    protected var user: User? = null
    protected var direction: Boolean = true
    protected var _data: TradeStockEntity? = null
    protected var maxValue: Int = 0
    protected var option = "买入"

    protected val levelView: Level
    protected var priceView: AdjustEditText? = null
    protected var numView: AdjustEditText? = null
    protected var availView: TextView? = null
    protected var inputView: InputCodeView? = null
    protected var ratioView: RatioView? = null
    protected var submitView: Button? = null
    protected var maxPrice: TextView? = null
    protected var minPrice: TextView? = null
    protected var lastPrice: TextView? = null
    protected var newestPrice: TextView? = null
    protected var maxPriceLayout: LinearLayout? = null
    protected var minPriceLayout: LinearLayout? = null


    abstract fun inflate(): Int
    abstract fun submit()

    init {
        context!!.inflate(inflate(), this)

        priceView = findViewById(R.id.price)
        inputView = findViewById(R.id.input_code)
        ratioView = findViewById(R.id.ratio_view)
        numView = findViewById(R.id.total_num)
        availView = findViewById(R.id.available_num)
        submitView = findViewById(R.id.submit)
        maxPrice = findViewById(R.id.limit_up_price)
        minPrice = findViewById(R.id.limit_down_price)
        lastPrice = findViewById(R.id.last_price)
        newestPrice = findViewById(R.id.newest_price)
        maxPriceLayout = findViewById(R.id.limit_up_layout)
        minPriceLayout = findViewById(R.id.limit_down_layout)

        val level: View = findViewById(R.id.level_view)
        levelView = (level as Level)
        levelView.setOnItemClickListener(object : Level.OnItemClickListener {
            override fun onClick(position: Int) {
                val prices = list[position]
                priceView?.text = prices.fPrice.format2()
            }
        })

        inputView?.setOnSearchListener {
            viewModel.search(it)
        }

        ratioView?.setOnMaxListener {
            numView?.text = it.toString()
        }

        submitView?.setOnClickListener {
            if (_data == null) {
                "无效的股票，请稍后再试".toast(context)
                return@setOnClickListener
            }

            if (user == null) {
                "您没有当前市场账户".toast(context)
                return@setOnClickListener
            }

            if(numView?.text == null){
                "请选择数量".toast(context)
                return@setOnClickListener
            }

           submit()
        }

    }

    override fun initData(flag: Boolean, vm: SellViewModel) {
        viewModel = vm
        direction = flag

        if (direction) {
            inputView?.setBackgroundResource(R.drawable.trade_input_bg)
            priceView?.setColor(R.color.trade_red)
            numView?.setColor(R.color.trade_red)
            submitView?.setBackgroundResource(R.drawable.trade_red_corner_full)
            submitView?.text = "立即买入"
            option = "买入"
        } else {
            inputView?.setBackgroundResource(R.drawable.trade_input_bg_blue)
            priceView?.setColor(R.color.trade_blue)
            numView?.setColor(R.color.trade_blue)
            submitView?.setBackgroundResource(R.drawable.trade_blue_corner_full)
            submitView?.text = "立即卖出"
            option = "卖出"
        }
    }

    override fun setStockCode(code: String) {
        inputView?.search(code)
    }

    override fun setAvailable(avail: Avail) {
        if (avail.direction == direction) {
            if (avail.direction) {
                maxValue = avail.num
                ratioView?.setMax(avail.num)
                availView?.text = "可买${maxValue}股"
            } else {
                maxValue = avail.num
                ratioView?.setMax(avail.num)
                availView?.text = "可卖${maxValue}股"
            }
        }
    }

    private var firstHq = false

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
            }

            minPriceLayout?.setOnClickListener {
                priceView?.text = minPrice?.text.toString()
            }

            user = UserHelper.getUserByMarket(_data?.market)
        }
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

                if(user == null){
                    return@let
                }

                viewModel.getAvailable(
                    _data!!.market, user!!.secuid, user!!.fundid,
                    _data!!.stkcode, it.fNewest, direction
                )
                firstHq = false
            }
        }
    }

    override fun getView(): View {
        return this
    }
}

