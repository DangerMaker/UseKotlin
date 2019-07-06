package com.god.kotlin.change

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class PasswordViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val result = MutableLiveData<String>()

    fun postPwd(pwd: String) {
        repository.postPwd(pwd, object : OnResult<String> {
            override fun onSucceed(response: String) {
                result.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun postFundsPwd(pwd: String, oldPwd: String) {
        repository.postFundsPwd(pwd,oldPwd,object :OnResult<String>{
            override fun onSucceed(response: String) {
                result.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }
}