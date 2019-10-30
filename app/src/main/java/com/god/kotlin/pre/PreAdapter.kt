package com.god.kotlin.pre

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.util.inflate

class PreAdapter(
    private val list: MutableList<PreData>
    , private val context: Context
    ,private val listView: ListView
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val stock = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_order1)

        with(rowView.findViewById<TextView>(R.id.pre_item_name)) {
            text = stock.name
        }

        with(rowView.findViewById<TextView>(R.id.pre_item_dict)) {
            text = stock.dict
        }

        with(rowView.findViewById<TextView>(R.id.pre_item_date)) {
            text = stock.date
        }

        with(rowView.findViewById<TextView>(R.id.pre_item_num)) {
            text = stock.qty.toString()
        }

        with(rowView.findViewById<TextView>(R.id.pre_item_price)) {
            text = stock.price.toString()
        }

        val checkBox = rowView.findViewById<ImageView>(R.id.pre_item_checkbox)
        checkBox.isPressed = listView.isItemChecked(position)

        return rowView
    }

    override fun getItem(position: Int): PreData {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}