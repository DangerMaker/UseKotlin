package com.god.kotlin.trade

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.data.entity.TradeResultEntity
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class SellViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val stockEntity = MutableLiveData<TradeStockEntity>()
    val handStockList = MutableLiveData<MutableList<TradeHandEntity>>()
    val available = MutableLiveData<Int>()
    val order = MutableLiveData<TradeResultEntity>()

    fun search(code: String) {
        repository.searchStock(code, object : OnResult<TradeStockEntity> {
            override fun onSucceed(response: TradeStockEntity) {
                stockEntity.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun getHandList(fundsId: String, count: Int = 20, offset: Int = 0) {
        repository.getHandStockList(fundsId, count, offset, object : OnResult<MutableList<TradeHandEntity>> {
            override fun onSucceed(response: MutableList<TradeHandEntity>) {
                handStockList.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun getAvailable(code: String, price: Double, flag: String) {
        repository.getAvailable(code, price, flag, object : OnResult<Int> {
            override fun onSucceed(response: Int) {
                available.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun transaction(market: String, code: String, price: Double, qty: Int, postFlag: String) {
        repository.transaction(market, code, price, qty, postFlag, object : OnResult<TradeResultEntity> {
            override fun onSucceed(response: TradeResultEntity) {
                order.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }
}