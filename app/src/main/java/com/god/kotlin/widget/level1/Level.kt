package com.god.kotlin.widget.level1

import com.god.kotlin.data.entity.TradeStockEntity

interface Level {

    fun setOnItemClickListener(listener: OnItemClickListener)

    fun setOpenPrice(price: Double)

    fun update(list: MutableList<TradeStockEntity.Dang>)

    interface OnItemClickListener {
        fun onClick(position: Int)
    }
}