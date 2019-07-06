package com.god.kotlin.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Account
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class AccountListViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val accountList = MutableLiveData<MutableList<Account>>()

    fun query() {
        repository.queryAccountList(object : OnResult<MutableList<Account>> {
            override fun onSucceed(response: MutableList<Account>) {
                accountList.value = response
            }

            override fun onFailure(error: Error) {
            }
        })
    }
}