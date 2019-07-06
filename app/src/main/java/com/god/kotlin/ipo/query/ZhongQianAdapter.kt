package com.god.kotlin.ipo.query

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.data.entity.Deal
import com.god.kotlin.data.entity.PeiHao
import com.god.kotlin.data.entity.TransferRecord
import com.god.kotlin.data.entity.ZhongQian
import com.god.kotlin.util.*
import com.god.kotlin.widget.SingleLineAutoResizeTextView

class ZhongQianAdapter(
    private val list: MutableList<ZhongQian>
    , private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_zq)

        with(rowView.findViewById<TextView>(R.id.zq_name)) {
            text = item.stkname
        }

        with(rowView.findViewById<TextView>(R.id.zq_date)) {
            text = item.matchdate
        }

        with(rowView.findViewById<SingleLineAutoResizeTextView>(R.id.zq_num)) {
            setTextContent(item.hitqty.toString())
        }

        with(rowView.findViewById<TextView>(R.id.zq_status)) {
            text = getZhongqianStatus(item.status)
        }

        return rowView
    }

    override fun getItem(position: Int): ZhongQian {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}