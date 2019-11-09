package com.god.kotlin.trade

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeResultEntity
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class TradeViewModel(private val repository: TradeRepository) :
    ViewModel() ,IPresenter{

    val stockEntity = MutableLiveData<TradeStockEntity>()
    val available = MutableLiveData<Int>()
    val deal = MutableLiveData<TradeResultEntity>()
    val tips = MutableLiveData<String>()

    override fun search(code: String) {
        repository.searchStock(code, object : OnResult<TradeStockEntity> {
            override fun onSucceed(response: TradeStockEntity) {
                stockEntity.value = response
            }

            override fun onFailure(error: Error) {

            }

        })
    }

    override fun getAvailable(market: String, secuid: String, fundsId: String, code: String, price: Double, flag: Boolean) {
        repository.getAvailable(market, secuid, fundsId, code, price, flag, object : OnResult<Int> {
                override fun onSucceed(response: Int) {
                    available.value = response
                }

                override fun onFailure(error: Error) {
                    tips.value = error.szError
                }
            })
    }

    override  fun submit(market: String, code: String, secuid: String, fundsId: String, price: Double, qty: Int, postFlag: String) {
        repository.transaction(
            market, code, secuid, fundsId, price, qty, postFlag, object : OnResult<TradeResultEntity> {
                override fun onSucceed(response: TradeResultEntity) {
                    deal.value = response
                }

                override fun onFailure(error: Error) {
                    tips.value = error.szError
                }
            })
    }
}