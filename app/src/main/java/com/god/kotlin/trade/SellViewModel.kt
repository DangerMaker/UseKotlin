package com.god.kotlin.trade

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.data.entity.TradeResultEntity
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class SellViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val stockEntity = MutableLiveData<TradeStockEntity>()
    val handStockList = MutableLiveData<MutableList<TradeHandEntity>>()
    val available = MutableLiveData<Avail>()
    val order = MutableLiveData<TradeResultEntity>()
    val tips = MutableLiveData<String>()

    val currentHQ = MutableLiveData<TradeStockEntity>()

    fun search(code: String) {
        repository.searchStock(code, object : OnResult<TradeStockEntity> {
            override fun onSucceed(response: TradeStockEntity) {
                stockEntity.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun getHandList(fundsId: String, count: Int = 100, offset: Int = 1) {
        repository.getHandStockList(fundsId, count, offset, object : OnResult<MutableList<TradeHandEntity>> {
            override fun onSucceed(response: MutableList<TradeHandEntity>) {
                handStockList.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun getAvailable( market: String,secuid: String,fundsId: String,code: String, price: Double, flag: Boolean) {
        repository.getAvailable( market,secuid,fundsId,
            code, price, flag, object : OnResult<Int> {
            override fun onSucceed(response: Int) {
                available.value = Avail(flag,response)
            }

            override fun onFailure(error: Error) {
                tips.value = error.szError
            }

        })
    }

    fun transaction(market: String, code: String,secuid: String,fundsId: String, price: Double, qty: Int, postFlag: String) {
        repository.transaction(market, code,secuid,fundsId, price, qty, postFlag, object : OnResult<TradeResultEntity> {
            override fun onSucceed(response: TradeResultEntity) {
                order.value = response
            }

            override fun onFailure(error: Error) {
                tips.value = error.szError
            }

        })
    }

    fun getHQ(market: String,code: String){
        repository.getHQ(market,code,object :OnResult<TradeStockEntity>{
            override fun onSucceed(response: TradeStockEntity) {
                currentHQ.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }
}