package com.god.kotlin.login

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.User
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult
import com.god.kotlin.user.UserHelper

class LoginViewModel(private val repository: TradeRepository) : ViewModel() {

    var success = MutableLiveData<Boolean>()
    var bitmap = MutableLiveData<Bitmap>()
    var tips = MutableLiveData<String>()

    fun login(userType: String, userId: String, password: String, checkCode: String, verifiCodeId: String) {

        repository.login(userType, userId, password, checkCode, verifiCodeId, object : OnResult<MutableList<User>> {

            override fun onSucceed(response: MutableList<User>) {
                success.value = true
                UserHelper.setUserList(response)
            }

            override fun onFailure(error: Error) {
                success.value = false
                tips.value = error.szError
            }

        })

    }

    fun getVerificationCode(width: Int, height: Int) {
        repository.getVerificationCode(width,height,object :OnResult<Bitmap>{
            override fun onSucceed(response: Bitmap) {
                bitmap.value = response
            }

            override fun onFailure(error: Error) {
                tips.value = error.szError
            }

        })
    }
}