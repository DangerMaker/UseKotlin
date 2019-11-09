package com.god.kotlin.trade

import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeResultEntity
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

interface IView {

    fun setPresenter(flag: Boolean, presenter: IPresenter)

    fun setStockCode(code: String)

    fun setAvailable(avail: Avail)

    fun setData(data: TradeStockEntity)

    fun updateHQ(data: TradeStockEntity)
}

interface IPresenter {

    fun search(code: String)

    fun getAvailable(market: String, secuid: String, fundsId: String, code: String, price: Double, flag: Boolean)

    fun submit(market: String, code: String, secuid: String, fundsId: String, price: Double, qty: Int, postFlag: String)
}
