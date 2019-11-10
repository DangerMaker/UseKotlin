package com.god.kotlin.trade

import android.content.Context
import android.text.TextUtils
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
import com.god.kotlin.util.*
import com.god.kotlin.widget.RatioView
import com.god.kotlin.widget.level1.Level
import kotlinx.android.synthetic.main.view_trade_market.view.*

abstract class AbsTradeView(context: Context?) : RelativeLayout(context), ITradeView {

    protected lateinit var viewModel: SellViewModel
    protected var list = mutableListOf<TradeStockEntity.Dang>()
    protected var user: User? = null
    protected var direction: Boolean = true
    protected var _data: TradeStockEntity? = null
    protected var option = "买入"
    protected var quoteType = "限价委托"
    protected var postFlag = ""

    protected val levelView: Level
    protected var priceView: AdjustEditText? = null
    protected var priceView1: AdjustEditText? = null
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
        priceView1 = findViewById(R.id.price1)
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
                priceView1?.text = prices.fPrice.format2()
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

            if (numView?.text == null) {
                "请选择数量".toast(context)
                return@setOnClickListener
            }

            postFlag = getFlagByQuoteName(direction, quoteType)
            if(TextUtils.isEmpty(postFlag)){
                "请选择正确的报价方式".toast(context)
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
            if (avail.direction) {
                ratioView?.setMax(avail.num)
                availView?.text = "最大可买${ avail.num}股"
            } else {
                ratioView?.setMax(avail.num)
                availView?.text = "最大可卖${avail.num}股"
            }
    }

    protected var firstHq = false

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
                firstHq = false
            }
        }
    }


    override fun getView(): View {
        return this
    }
}

