package com.god.kotlin.trade.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Order
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class OrderViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val orderList = MutableLiveData<MutableList<Order>>()
    val result = MutableLiveData<String>()

    fun query(count: Int, offset: Int) {
        repository.queryOrderList(count, offset, object : OnResult<MutableList<Order>> {
            override fun onSucceed(response: MutableList<Order>) {
                orderList.value = response
            }

            override fun onFailure(error: Error) {
            }
        })
    }

    fun post(orderdate: String,
             fundid: String,
             ordersno: String,
             bsflag: String){
        repository.postOrder(orderdate,fundid,ordersno,bsflag,object :OnResult<String>{
            override fun onSucceed(response: String) {
                result.value = "操作成功"
            }

            override fun onFailure(error: Error) {
                result.value = "操作失败"
            }
        })
    }
}