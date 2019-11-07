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
    var checkSuccess = MutableLiveData<Boolean>()

    fun login(userType: String, userId: String, password: String, checkCode: String, strNet2: String) {

//        repository.login(userType, userId, password, checkCode, strNet2, object : OnResult<MutableList<User>> {
//
//            override fun onSucceed(response: MutableList<User>) {
//                success.value = true
//                UserHelper.setUserList(response)
//            }
//
//            override fun onFailure(error: Error) {
//                success.value = false
//                tips.value = error.szError
//            }
//
//        })

        repository.login1(userType, userId, password, checkCode, strNet2, object : OnResult<Boolean> {

            override fun onSucceed(response: Boolean) {
                success.value = true
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

    fun sendSMS(phone:String){
        repository.sendSms(phone,object : OnResult<String>{
            override fun onSucceed(response: String) {
                tips.value = response
            }

            override fun onFailure(error: Error) {
                tips.value = error.szError
            }

        })
    }

    fun checkSMS(phone: String,checkCode: String){
        repository.checkSms(phone,checkCode,object :OnResult<String> {
            override fun onSucceed(response: String) {
                checkSuccess.value = true
            }

            override fun onFailure(error: Error) {
                tips.value = error.szError
            }
        })
    }
}