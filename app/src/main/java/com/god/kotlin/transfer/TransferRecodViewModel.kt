package com.god.kotlin.transfer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Account
import com.god.kotlin.data.entity.TransferRecord
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class TransferRecodViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val recordList = MutableLiveData<MutableList<TransferRecord>>()

    fun query(fundid:String) {
        repository.queryTransferList(fundid,object : OnResult<MutableList<TransferRecord>> {
            override fun onSucceed(response: MutableList<TransferRecord>) {
                recordList.value = response
            }

            override fun onFailure(error: Error) {
            }
        })
    }
}