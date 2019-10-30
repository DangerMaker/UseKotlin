package com.god.kotlin.trade.order

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.data.entity.Account
import com.god.kotlin.data.entity.Order
import com.god.kotlin.util.*

class OrderAdapter(
    private val list: MutableList<Order>
    , private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val order = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_entrust)

        val color = if (getResponseColor(order.bsflag) == 0) {
            R.color.trade_red
        } else{
            R.color.trade_blue
        }
        with(rowView.findViewById<TextView>(R.id.name)) {
            text = order.stkname
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.date)) {
            text = order.orderdate
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.time)) {
            text = getTime(order.opertime)
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.price)) {
            text = order.orderprice.format2()
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.order_num)) {
            text = order.orderqty.toString()
            setPriceColor(color)
        }
        with(rowView.findViewById<TextView>(R.id.final_num)) {
            text = order.matchqty.toString()
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.order_state)) {
            text = getResponseQuote(order.bsflag)
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.final_state)) {
            text = getOrderStatus(order.orderstatus)
            setPriceColor(color)
        }

        return rowView
    }

    override fun getItem(position: Int): Order {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}