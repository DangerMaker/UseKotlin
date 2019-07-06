package com.god.kotlin.change

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Information
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class InformationViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val information = MutableLiveData<Information>()
    val result = MutableLiveData<String>()

    fun query() {
        repository.queryInformation(object : OnResult<Information> {
            override fun onSucceed(response: Information) {
                information.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun post(
        idType: String,
        idCard: String,
        phone: String,
        postCode: String,
        email: String,
        address: String
    ) {
        repository.postInformation(idType,idCard,phone, postCode, email, address, object : OnResult<String>{
            override fun onSucceed(response: String) {
                result.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }
}