package com.god.kotlin.ipo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import com.god.kotlin.R
import com.god.kotlin.data.entity.Account
import com.god.kotlin.data.entity.NewStock
import com.god.kotlin.util.getMarketType
import com.god.kotlin.util.inflate

class NewStockAdapter(
    private val list: MutableList<NewStock>
    , private val context: Context
    ,private val listView: ListView
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val stock = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_new_stock)

        with(rowView.findViewById<TextView>(R.id.stock_name)) {
            text = stock.stkname
        }

        with(rowView.findViewById<TextView>(R.id.stock_code)) {
            text = stock.stkcode
        }

        with(rowView.findViewById<TextView>(R.id.stock_available_num)) {
            text = stock.maxqty.toString()
        }
//
        val checkBox = rowView.findViewById<ImageView>(R.id.stock_checkbox)
        val flag = listView.isItemChecked(position)
        checkBox.isPressed = flag
        list[position].isSelect = flag
        return rowView
    }

    override fun getItem(position: Int): NewStock {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}