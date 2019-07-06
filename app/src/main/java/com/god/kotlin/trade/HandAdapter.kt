package com.god.kotlin.trade

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.util.format2
import com.god.kotlin.util.inflate
import com.god.kotlin.util.setPriceColor

class HandAdapter(
    private val list1: MutableList<TradeHandEntity>
    , private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.holder_hold_stock)

        val color = if (item.income <= 0) {
            R.color.trade_blue
        } else {
            R.color.trade_red
        }

        with(rowView.findViewById<TextView>(R.id.txt_name)) {
            text = item.stkname
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.txt_code)) {
            text = item.stkcode
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.profit_num)) {
            text = ((item.lastprice - item.costprice) / item.costprice * 100).format2()
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.profit_percent)) {
            text = item.stkname
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.stock_hand)) {
            text = item.stkbal.toString()
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.stock_available)) {
            text = item.stkavl.toString()
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.stock_prime)) {
            text = item.costprice.format2()
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.stock_last)) {
            text = item.lastprice.format2()
            setPriceColor(color)
        }

//            rowView.setOnClickListener { itemListener.onTaskClick(task) }

        return rowView
    }

    override fun getItem(position: Int): TradeHandEntity {
        return list1[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list1.size
    }
}