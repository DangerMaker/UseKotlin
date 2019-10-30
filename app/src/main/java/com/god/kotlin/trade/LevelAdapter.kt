package com.god.kotlin.trade

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.util.format2
import com.god.kotlin.util.getPriceColor
import com.god.kotlin.util.inflate

class LevelAdapter(
    private val list: MutableList<TradeStockEntity.Dang>
    , private val context: Context
) : BaseAdapter() {

    private var fOpen = 0.0
    fun setOpenPrice(fOpen:Double){
        this.fOpen = fOpen
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val level = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_item_five)

        val color = ContextCompat.getColorStateList(context, getPriceColor(level.fPrice, fOpen))

        //todo  findViewById 耗时
        with(rowView.findViewById<TextView>(R.id.level_name)) {
            text = LEVEL_NAMES[position]
        }

        with(rowView.findViewById<TextView>(R.id.level_price)) {
            text = level.fPrice.format2()
            setTextColor(color)
        }

        with(rowView.findViewById<TextView>(R.id.level_quantity)) {
            text = (level.fOrder/100).toString()
        }
        return rowView
    }

    override fun getItem(position: Int): TradeStockEntity.Dang {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}

private val LEVEL_NAMES = arrayOf("卖5", "卖4", "卖3", "卖2", "卖1", "买1", "买2", "买3", "买4", "买5")