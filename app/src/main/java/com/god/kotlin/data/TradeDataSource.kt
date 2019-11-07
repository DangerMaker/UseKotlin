package com.god.kotlin.data

import android.graphics.Bitmap
import com.god.kotlin.data.entity.*
import com.god.kotlin.net.OnResult

interface TradeDataSource {

    fun getVerificationCode(
        width: Int,
        height: Int,
        callback: OnResult<Bitmap>
    )

    fun sendSms(phone: String,callback: OnResult<String>)

    fun checkSms(phone: String,code: String, callback: OnResult<String>)

    fun login(
        userType: String, userId: String, password: String,
        checkCode: String, strNet2: String,
        callback: OnResult<MutableList<User>>
    )

    fun login1(
        userType: String, userId: String, password: String,
        checkCode: String, strNet2: String,
        callback: OnResult<Boolean>
    )

    fun loginSession(
        userType: String, userId: String, password: String,
        sessionId: String, strNet2: String,
        callback: OnResult<MutableList<User>>
    )


    fun searchStock(code: String, callback: OnResult<TradeStockEntity>)

    fun getHandStockList(
        fundsId: String, count: Int, offset: Int,
        callback: OnResult<MutableList<TradeHandEntity>>
    )

    fun getAvailable(
        market: String,secuid: String,fundsId: String,
        code: String, price: Double, flag: Boolean, callback: OnResult<Int>
    )

    fun transaction(
        market: String,
        secuid: String,
        fundsId: String,
        code: String,
        price: Double,
        qty: Int,
        postFlag: String,
        callback: OnResult<TradeResultEntity>
    )

    fun queryRiskLevel(custid: String, callback: OnResult<RiskLevel>)

    fun queryAccountList(callback: OnResult<MutableList<Account>>)

    fun queryInformation(callback: OnResult<Information>)

    fun postInformation(
        idType: String, idCard: String, phone: String, postCode: String,
        email: String, address: String, callback: OnResult<String>
    )

    fun queryOrderList(count: Int, offset: Int, callback: OnResult<MutableList<Order>>)

    fun postOrder(orderdate: String, fundid: String, ordersno: String, bsflag: String, callback: OnResult<String>)

    fun queryFunds(fundsId: String,moneyType: Int, callback: OnResult<Funds>)

    fun postPwd(pwd: String, callback: OnResult<String>)

    fun postFundsPwd(pwd: String, oldPwd: String, callback: OnResult<String>)

    fun queryTransferList(fundid: String, callback: OnResult<MutableList<TransferRecord>>)

    fun queryTodayDeal(fundid: String, count: Int, offset: Int, callback: OnResult<MutableList<Deal>>)

    fun queryHistoryDeal(
        begin: String,
        end: String,
        fundid: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<Deal>>
    )

    fun queryTodayOrderList(fundid: String, count: Int, offset: Int, callback: OnResult<MutableList<Order>>)

    fun queryHistoryOrderList(
        begin: String,
        end: String,
        fundid: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<Order>>
    )

    fun queryIpoQuota(secuid: String, callback: OnResult<MutableList<Quota>>)

    fun queryNewStockList(callback: OnResult<MutableList<NewStock>>)

    fun queryPeiHaoList(
        begin: String,
        end: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<PeiHao>>
    )

    fun queryZhongQianList(
        begin: String,
        end: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<ZhongQian>>
    )

    fun queryDaiJiaoList(callback: OnResult<MutableList<DaiJiao>>)

    fun getHQ(market: String,code: String,callback :OnResult<TradeStockEntity>)

}