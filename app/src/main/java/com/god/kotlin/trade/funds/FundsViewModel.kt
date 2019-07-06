package com.god.kotlin.trade.funds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Funds
import com.god.kotlin.data.entity.RiskLevel
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class FundsViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val funds = MutableLiveData<Funds>()

    fun query(type: Int) {
        repository.queryFunds(type, object : OnResult<Funds> {
            override fun onSucceed(response: Funds) {
                funds.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }
}