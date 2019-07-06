package com.god.kotlin.ipo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Account
import com.god.kotlin.data.entity.NewStock
import com.god.kotlin.data.entity.Quota
import com.god.kotlin.data.entity.RiskLevel
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class IpoViewModel(private val repository: TradeRepository) :
    ViewModel() {

    var szQuota = 0
    var shQuota = 0
    var tips = MutableLiveData<String>()

    fun queryQuota(secuid: String) {
        repository.queryIpoQuota(secuid, object : OnResult<MutableList<Quota>> {
            override fun onSucceed(response: MutableList<Quota>) {
                for (i in response.indices) {
                    if (response[i].market == "0") {
                        szQuota = response[i].custquota
                    } else if (response[i].market == "1") {
                        shQuota = response[i].custquota
                    }
                }

                tips.value = "您今日的申购额度为沪市${shQuota}股，深市${szQuota}股"
            }

            override fun onFailure(error: Error) {
            }
        })
    }

    val newStockList = MutableLiveData<MutableList<NewStock>>()

    fun queryStockList() {
        repository.queryNewStockList(object : OnResult<MutableList<NewStock>> {
            override fun onSucceed(response: MutableList<NewStock>) {
                newStockList.value = response
            }

            override fun onFailure(error: Error) {
            }
        })
    }
}