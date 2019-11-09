package com.god.kotlin.trade

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class HandViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val handStockList = MutableLiveData<MutableList<TradeHandEntity>>()

    fun getHandList(fundsId: String, count: Int = 100, offset: Int = 1) {
        repository.getHandStockList(fundsId, count, offset, object : OnResult<MutableList<TradeHandEntity>> {
            override fun onSucceed(response: MutableList<TradeHandEntity>) {
                handStockList.value = response
            }

            override fun onFailure(error: Error) {
            }
        })
    }
}