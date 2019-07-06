package com.god.kotlin.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.RiskLevel
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class RiskQueryViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val level = MutableLiveData<RiskLevel>()

    fun query(custid: String) {
        repository.queryRiskLevel(custid, object : OnResult<RiskLevel> {
            override fun onSucceed(response: RiskLevel) {
                level.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }
}