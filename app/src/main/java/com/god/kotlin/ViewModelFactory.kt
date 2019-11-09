package com.god.kotlin

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.god.kotlin.login.LoginViewModel
import com.god.kotlin.change.InformationViewModel
import com.god.kotlin.change.PasswordViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.ipo.IpoViewModel
import com.god.kotlin.ipo.query.IpoQueryViewModel
import com.god.kotlin.profile.AccountListViewModel
import com.god.kotlin.profile.RiskQueryViewModel
import com.god.kotlin.query.QueryViewModel
import com.god.kotlin.trade.HandViewModel
import com.god.kotlin.trade.SellViewModel
import com.god.kotlin.trade.TradeViewModel
import com.god.kotlin.trade.funds.FundsViewModel
import com.god.kotlin.trade.order.OrderViewModel
import com.god.kotlin.transfer.TransferRecodViewModel

class ViewModelFactory private constructor(
    private val tasksRepository: TradeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(tasksRepository)
                isAssignableFrom(SellViewModel::class.java) ->
                    SellViewModel(tasksRepository)
                isAssignableFrom(RiskQueryViewModel::class.java) ->
                    RiskQueryViewModel(tasksRepository)
                isAssignableFrom(AccountListViewModel::class.java) ->
                    AccountListViewModel(tasksRepository)
                isAssignableFrom(InformationViewModel::class.java) ->
                    InformationViewModel(tasksRepository)
                isAssignableFrom(OrderViewModel::class.java) ->
                    OrderViewModel(tasksRepository)
                isAssignableFrom(FundsViewModel::class.java) ->
                    FundsViewModel(tasksRepository)
                isAssignableFrom(PasswordViewModel::class.java) ->
                    PasswordViewModel(tasksRepository)
                isAssignableFrom(TransferRecodViewModel::class.java) ->
                    TransferRecodViewModel(tasksRepository)
                isAssignableFrom(QueryViewModel::class.java) ->
                    QueryViewModel(tasksRepository)
                isAssignableFrom(IpoViewModel::class.java) ->
                    IpoViewModel(tasksRepository)
                isAssignableFrom(IpoQueryViewModel::class.java) ->
                    IpoQueryViewModel(tasksRepository)
                isAssignableFrom(HandViewModel::class.java) ->
                    HandViewModel(tasksRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    TradeRepository())
                    .also { INSTANCE = it }
            }


         fun destroyInstance() {
            INSTANCE = null
        }
    }
}