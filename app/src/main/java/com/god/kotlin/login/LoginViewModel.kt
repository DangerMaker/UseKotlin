package com.god.kotlin.login

import androidx.core.graphics.scaleMatrix
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.User
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class LoginViewModel(private val repository: TradeRepository) : ViewModel() {

    var success = MutableLiveData<Boolean>()

    fun lggin(userType: String, userId: String, password: String, checkCode: String, verifiCodeId: String) {

        repository.login(userType,userId,password,checkCode,verifiCodeId,object : OnResult<MutableList<User>>{

            override fun onSucceed(response: MutableList<User>) {
                success.value = true
            }

            override fun onFailure(error: Error) {
                success.value = false
            }

        })

    }
}