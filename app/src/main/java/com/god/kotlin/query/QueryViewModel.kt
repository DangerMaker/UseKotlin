package com.god.kotlin.query

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Deal
import com.god.kotlin.data.entity.Order
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class QueryViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val orderList = MutableLiveData<MutableList<Order>>()
    val dealList = MutableLiveData<MutableList<Deal>>()
    val loading = MutableLiveData<Boolean>()


    fun queryOrder(fundid: String, count: Int, offset: Int, begin: String = "", end: String = "") {
        loading.value = true
        if (TextUtils.isEmpty(begin)) {
            repository.queryTodayOrderList(fundid, count, offset, object : OnResult<MutableList<Order>> {
                override fun onSucceed(response: MutableList<Order>) {
                    orderList.value = response
                    loading.value = false
                }

                override fun onFailure(error: Error) {
                    loading.value = false
                }
            })
        } else {
            repository.queryHistoryOrderList(begin, end, fundid, count, offset, object : OnResult<MutableList<Order>> {
                override fun onSucceed(response: MutableList<Order>) {
                    orderList.value = response
                    loading.value = false
                }

                override fun onFailure(error: Error) {
                    loading.value = false
                }
            })
        }
    }


    fun queryDeal(fundid: String, count: Int, offset: Int, begin: String = "", end: String = "") {
        loading.value = true
        if (TextUtils.isEmpty(begin)) {
            repository.queryTodayDeal(fundid, count, offset, object : OnResult<MutableList<Deal>> {
                override fun onSucceed(response: MutableList<Deal>) {
                    dealList.value = response
                    loading.value = false
                }

                override fun onFailure(error: Error) {
                    loading.value = false
                }
            })
        } else {
            repository.queryHistoryDeal(
                begin,
                end,
                fundid,
                count,
                offset,
                object : OnResult<MutableList<Deal>> {
                    override fun onSucceed(response: MutableList<Deal>) {
                        dealList.value = response
                        loading.value = false
                    }

                    override fun onFailure(error: Error) {
                        loading.value = false
                    }
                })
        }
    }

}