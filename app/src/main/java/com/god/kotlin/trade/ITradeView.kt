package com.god.kotlin.trade

import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeStockEntity

interface ITradeView {

    fun initData(flag: Boolean,vm: SellViewModel)

    fun setStockCode(code: String)

    fun setAvailable(avail: Avail)

    fun setData(data: TradeStockEntity)

    fun updateHQ(data: TradeStockEntity)
}

interface ITradeAction {

    fun search(code: String)

    fun getAvailable(code: String, price: Double,bsflag: String)

    fun submit(code: String, price: Double, single: Int = 1, num: Int, bsflag: String)
}