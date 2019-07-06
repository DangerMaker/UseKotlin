package com.god.kotlin.ipo.query

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.data.entity.DaiJiao
import com.god.kotlin.util.getMarketType
import com.god.kotlin.util.inflate

class DaiJiaoAdapter(
    private val list: MutableList<DaiJiao>
    , private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_daijiao)

        with(rowView.findViewById<TextView>(R.id.daijiao_name)) {
            text = item.stkname
        }

        with(rowView.findViewById<TextView>(R.id.daijiao_code)) {
            text = item.stkcode
        }

        with(rowView.findViewById<TextView>(R.id.daijiao_date)) {
            text = item.matchdate
        }

        with(rowView.findViewById<TextView>(R.id.daijiao_market)) {
            text = getMarketType(item.market)
        }

        with(rowView.findViewById<TextView>(R.id.daijiao_hit)) {
            text = item.hitqty.toString()
        }

        return rowView
    }

    override fun getItem(position: Int): DaiJiao {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}