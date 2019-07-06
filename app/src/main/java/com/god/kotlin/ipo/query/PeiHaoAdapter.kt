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
import com.god.kotlin.util.*
import com.god.kotlin.widget.SingleLineAutoResizeTextView

class PeiHaoAdapter(
    private val list: MutableList<PeiHao>
    , private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val peiHao = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_ph)

        with(rowView.findViewById<TextView>(R.id.peihao_name)) {
            text = peiHao.stkname
        }

        with(rowView.findViewById<TextView>(R.id.peihao_code)) {
            text = peiHao.stkcode
        }

        with(rowView.findViewById<TextView>(R.id.peihao_date)) {
            text = peiHao.bizdate
        }

        with(rowView.findViewById<SingleLineAutoResizeTextView>(R.id.peihao_no)) {
            setTextContent(peiHao.mateno)
        }

        with(rowView.findViewById<TextView>(R.id.peihao_num)) {
            text = peiHao.matchqty.toString()
        }

        return rowView
    }

    override fun getItem(position: Int): PeiHao {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}