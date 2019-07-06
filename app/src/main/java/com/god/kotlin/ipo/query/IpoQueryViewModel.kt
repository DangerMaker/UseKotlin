package com.god.kotlin.ipo.query

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.*
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class IpoQueryViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val peiHao = MutableLiveData<MutableList<PeiHao>>()
    val zhongQian = MutableLiveData<MutableList<ZhongQian>>()
    val daiJiao = MutableLiveData<MutableList<DaiJiao>>()

    fun queryPeiHao(fundid: String, count: Int, offset: Int, begin: String, end: String) {
        repository.queryPeiHaoList(begin, end, count, offset, object : OnResult<MutableList<PeiHao>> {
            override fun onSucceed(response: MutableList<PeiHao>) {
                peiHao.value = response
            }

            override fun onFailure(error: Error) {
            }
        })
    }

    fun queryZhongQian(fundid: String, count: Int, offset: Int, begin: String, end: String) {
        repository.queryZhongQianList(begin, end, count, offset, object : OnResult<MutableList<ZhongQian>> {
            override fun onSucceed(response: MutableList<ZhongQian>) {
                zhongQian.value = response
            }

            override fun onFailure(error: Error) {
            }
        })
    }


    fun queryDaiJiao() {
        repository.queryDaiJiaoList(object : OnResult<MutableList<DaiJiao>>{
            override fun onSucceed(response: MutableList<DaiJiao>) {
                daiJiao.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }





}