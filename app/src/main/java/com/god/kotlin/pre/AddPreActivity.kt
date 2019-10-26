package com.god.kotlin.pre

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.trade.SellViewModel
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.toolbar_normal.*
import kotlinx.android.synthetic.main.trade_activity_new_order.*
import kotlinx.android.synthetic.main.view_top.*
import java.text.SimpleDateFormat
import java.util.*

class AddPreActivity : AppCompatActivity() {

    private lateinit var viewModel: SellViewModel
    private lateinit var context: Context
    private var _data: TradeStockEntity? = null
    private var quoteValue = "0B"
    private var direction = "0B"
    private var quoteWay = "限价委托"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trade_activity_new_order)

        toolbar_title.text = "新建预埋单"
        toolbar_back.setOnClickListener { finish() }

        context = this
        viewModel = obtainViewModel(SellViewModel::class.java)
        pre_quote_way.text = quoteWay

        pre_input_code.setOnSearchListener {
            viewModel.search(it)
        }

        pre_buy_flag.setOnClickListener {
            _data?.let {
                val select = arrayOf("买入", "卖出")
                showSelectDialog(context, select,
                    DialogInterface.OnClickListener { _, which ->
                        pre_quote_way.text = select[which]
                        direction = if (select[which] == "买入") "0B" else "0S"
                    })
            } ?: "请输入代码".toast(context)
        }

        pre_quote_way.setOnClickListener {
            _data?.let {
                val select: Array<String> = when (it.market) {
                    "0" -> szQuoteType
                    "1" -> shQuoteType
                    else -> arrayOf()
                }
                showSelectDialog(context, select,
                    DialogInterface.OnClickListener { _, which ->
                        pre_quote_way.text = select[which]
                        quoteValue = getTagByQuoteName(direction, select[which])
                    })
            } ?: "请输入代码".toast(context)

        }

        pre_add_submit.setOnClickListener {
            _data?.let {
                val dateFormat = SimpleDateFormat("yyyyMMdd")
                val date = dateFormat.format(Date())

                val orderEntity = PreData(
                    it.market, it.stkcode, it.stkname, quoteValue,
                    pre_price.text.toString().toDouble(),
                    pre_num.text.toString().toInt(), direction, date
                )

                val intent = Intent()
                intent.putExtra("entity", orderEntity)
//                intent.putExtra("position", position)
                setResult(2, intent)
                finish()
            }
        }

        viewModel.available.observe(this, Observer {
            pre_num.setText(it.toString())
        })

        viewModel.stockEntity.observe(this, Observer {
            _data = it
            pre_input_code.setData(it.stkcode, it.stkname)
            pre_price.setText(it.fixprice.format2())

//            viewModel.getAvailable(it.stkcode, it.fixprice, direction)

            newest_price.text = it.fNewest.format2()
            newest_price.setTextColor(ContextCompat.getColorStateList(context, getPriceColor(it.fNewest, it.fOpen)))
            last_price.text = it.fLastClose.format2()
            limit_up_price.text = (it.fLastClose * 1.1f).format2()
            limit_up_price.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_red))
            limit_down_price.text = (it.fLastClose * 0.9f).format2()
            limit_down_price.setTextColor(ContextCompat.getColorStateList(context, R.color.trade_green))
        })

    }
}