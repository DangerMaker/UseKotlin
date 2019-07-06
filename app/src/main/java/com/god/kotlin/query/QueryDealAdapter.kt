package com.god.kotlin.query

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.data.entity.Deal
import com.god.kotlin.data.entity.TransferRecord
import com.god.kotlin.util.*

class QueryDealAdapter(
    private val list: MutableList<Deal>
    , private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val deal = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_deal)

        val color = if (deal.bsFlag == "B") {
            R.color.trade_red
        } else {
            R.color.trade_blue
        }

        with(rowView.findViewById<TextView>(R.id.deal_date)) {
            text = deal.trddate
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.deal_time)) {
            text = getTime(deal.matchtime)
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.deal_name)) {
            text = deal.stkname
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.deal_code)) {
            text = deal.stkcode
            setPriceColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.deal_price)) {
            text = deal.matchprice.format2()
            setPriceColor(color)

        }

        with(rowView.findViewById<TextView>(R.id.deal_num)) {
            text = deal.matchqty.toString()
            setPriceColor(color)
        }

        return rowView
    }

    override fun getItem(position: Int): Deal {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}