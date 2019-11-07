package com.god.kotlin.ipo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.*
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class IpoViewModel(private val repository: TradeRepository) :
    ViewModel() {

    var szQuota = 0
    var shQuota = 0
    var tips = MutableLiveData<String>()
    var result = MutableLiveData<String>()

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

    private var total: Int = 0
    private var temp = 0
    private var collection: String = ""
    fun transaction(secuid: String, fundsId: String, list: MutableList<NewStock>) {
        total = list.size
        temp = 0
        collection = ""
        for (stock in list) {
            repository.transaction(
                stock.market, stock.stkcode, secuid, fundsId, 0.0, stock.maxqty, "0D",
                object : OnResult<TradeResultEntity> {
                    override fun onSucceed(response: TradeResultEntity) {
                        collect("[" + stock.stkname + "]" + "   申购成功" + "\n")
                    }

                    override fun onFailure(error: Error) {
                        collect(
                            "[" + stock.stkname + "]" + "   申购失败" + "\n" +
                                    "原因：" + error.szError + "\n"
                        )
                    }
                })
        }
    }

    fun collect(str: String) {
        if (temp < total) {
            collection += str
            temp += 1
            if (temp == total) {
                result.value = collection
            }
        }
    }
}