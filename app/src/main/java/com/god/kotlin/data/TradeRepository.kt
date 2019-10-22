package com.god.kotlin.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.ez08.trade.net.*
import com.ez08.trade.net.login.STradeGateLogin
import com.ez08.trade.net.login.STradeGateLoginA
import com.ez08.trade.net.verification.STradeVerificationCode
import com.ez08.trade.net.verification.STradeVerificationCodeA
import com.god.kotlin.data.entity.*
import com.god.kotlin.net.OnResult
import com.god.kotlin.util.async
import java.lang.Exception
import com.god.kotlin.net.Error
import java.util.ArrayList


class TradeRepository : TradeDataSource {

    override fun getVerificationCode(width: Int, height: Int, callback: OnResult<Bitmap>) {
        Client.getInstance().send(STradeVerificationCode(width, height)) { success, data ->
            try {
                val resp = STradeVerificationCodeA(
                    data.headBytes,
                    data.bodyBytes,
                    Client.getInstance().aesKey
                )
                val picReal = resp.getPic()
                val decodedByte = BitmapFactory.decodeByteArray(picReal, 0, picReal.size)
                callback.onSucceed(decodedByte)
            } catch (e: Exception) {
                val error = Error()
                error.dwReqId = "0"
                error.dwErrorCode = "0"
                error.szError = "获取验证码失败"
                callback.onFailure(error)
            }
        }
    }

    override fun login(
        userType: String,
        userId: String,
        password: String,
        checkCode: String,
        verifiCodeId: String,
        callback: OnResult<MutableList<User>>
    ) {
        val tradeGateLogin = STradeGateLogin()
        var str2 = "|ZNZ|ANDROID"
        tradeGateLogin.setBody(userType, userId, password, checkCode, str2)
        Client.getInstance().send(tradeGateLogin) { success, data ->
            Client.strNet2 = str2
            Client.strUserType = userType
            Client.userId = userId
            Client.password = password

            val gateLoginA =
                STradeGateLoginA(data.headBytes, data.bodyBytes, Client.getInstance().aesKey)
            if(gateLoginA.getbLoginSucc()){
                val list = ArrayList<User>()
                for (i in gateLoginA.list.indices) {
                    val user = User(
                        NetUtil.byteToStr(gateLoginA.list[i].sz_name),
                        NetUtil.byteToStr(gateLoginA.list[i].sz_market),
                        gateLoginA.list[i].n64_fundid.toString() + "",
                        NetUtil.byteToStr(gateLoginA.list[i].sz_custcert),
                        NetUtil.byteToStr(gateLoginA.list[i].sz_secuid),
                        gateLoginA.list[i].n64_custid.toString()
                        )
                    list.add(user)
                }
                callback.onSucceed(list)
            }else{
                val error = Error()
                error.dwReqId = "0"
                error.dwErrorCode = "0"
                error.szError = gateLoginA.getSzErrMsg()
                callback.onFailure(error)
            }
        }
//        async {
//            var user = User("李磊", "Z", "00002000001", "secret", "secuid", "cusid")
//            var user1 = User("李磊", "0", "00002000001", "secret1", "secuid1", "cusid1")
//            val list = mutableListOf<User>()
//            list.add(user)
//            list.add(user1)
//            callback.onSucceed(list)
//        }
    }

    override fun queryPeiHaoList(
        begin: String,
        end: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<PeiHao>>
    ) {
        async {
            val list = mutableListOf<PeiHao>()
            val peiHao = PeiHao("600600", "潍柴动力", "20120808", "1232312", 1000)
            list.add(peiHao)
            list.add(peiHao)
            list.add(peiHao)
            callback.onSucceed(list)
        }
    }

    override fun queryZhongQianList(
        begin: String,
        end: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<ZhongQian>>
    ) {
        async {
            val list = mutableListOf<ZhongQian>()
            val zhongQian = ZhongQian("600600", 1000, "20120808", "0")
            list.add(zhongQian)
            list.add(zhongQian)
            list.add(zhongQian)
            callback.onSucceed(list)
        }
    }

    override fun queryDaiJiaoList(callback: OnResult<MutableList<DaiJiao>>) {
        async {
            val list = mutableListOf<DaiJiao>()
            val daiJiao = DaiJiao("万科A", "600600", "20120808", "0", 20000)
            list.add(daiJiao)
            list.add(daiJiao)
            list.add(daiJiao)
            callback.onSucceed(list)
        }
    }

    override fun queryIpoQuota(secuid: String, callback: OnResult<MutableList<Quota>>) {
        async {
            val list = mutableListOf<Quota>()
            val quota = Quota("0", 1000, "20120808")
            val quota1 = Quota("1", 1000, "20120808")
            list.add(quota)
            list.add(quota1)
            callback.onSucceed(list)
        }
    }

    override fun queryNewStockList(callback: OnResult<MutableList<NewStock>>) {
        async {
            val list = mutableListOf<NewStock>()
            val newStock = NewStock("600600", "美的集团", "123", 1000, 1000)
            list.add(newStock)
            list.add(newStock)
            list.add(newStock)
            list.add(newStock)
            callback.onSucceed(list)
        }
    }

    override fun queryTodayDeal(fundid: String, count: Int, offset: Int, callback: OnResult<MutableList<Deal>>) {
        async {
            val list = mutableListOf<Deal>()
            val deal = Deal("20190101", "101201", 66.11, 100, "没底集团", "500500", "B")
            list.add(deal)
            list.add(deal)
            list.add(deal)
            list.add(deal)
            list.add(deal)
            callback.onSucceed(list)
        }
    }

    override fun queryHistoryDeal(
        begin: String,
        end: String,
        fundid: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<Deal>>
    ) {
        async {
            val list = mutableListOf<Deal>()
            val deal = Deal("20190101", "101001", 66.11, 100, "没底集团", "500500", "B")
            list.add(deal)
            list.add(deal)
            list.add(deal)
            list.add(deal)
            list.add(deal)
            callback.onSucceed(list)
        }
    }

    override fun queryOrderList1(fundid: String, count: Int, offset: Int, callback: OnResult<MutableList<Order>>) {
        async {
            val list = mutableListOf<Order>()
            val order = Order()
            order.stkname = "美的集团"
            order.stkcode = "600500"
            order.orderprice = 66.11
            order.opertime = "101212"
            order.orderdate = "20190101"
            order.orderqty = 200
            order.matchqty = 100
            order.bsflag = "B"
            order.orderstatus = "成功"
            order.ordersno = "101010100"
            order.fundid = "100100"
            list.add(order)
            list.add(order)
            list.add(order)
            callback.onSucceed(list)
        }
    }

    override fun queryHistoryOrderList(
        begin: String,
        end: String,
        fundid: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<Order>>
    ) {
        async {
            val list = mutableListOf<Order>()
            val order = Order()
            order.stkname = "美的集团"
            order.stkcode = "600500"
            order.orderprice = 66.11
            order.opertime = "121212"
            order.orderdate = "20190101"
            order.orderqty = 200
            order.matchqty = 100
            order.bsflag = "B"
            order.orderstatus = "成功"
            order.ordersno = "101010100"
            order.fundid = "100100"
            list.add(order)
            list.add(order)
            list.add(order)
            callback.onSucceed(list)
        }
    }

    override fun queryTransferList(fundid: String, callback: OnResult<MutableList<TransferRecord>>) {
        async {
            val list = mutableListOf<TransferRecord>()
            val record1 = TransferRecord("2020-12-02", "101221", "100000.00", "0")
            list.add(record1)
            list.add(record1)
            list.add(record1)
            callback.onSucceed(list)
        }
    }


    override fun postPwd(pwd: String, callback: OnResult<String>) {
        async {
            callback.onSucceed("成功")
        }
    }

    override fun postFundsPwd(pwd: String, oldPwd: String, callback: OnResult<String>) {
        async {
            callback.onSucceed("成功")
        }
    }

    override fun queryFunds(moneyType: Int, callback: OnResult<Funds>) {
        async {
            callback.onSucceed(Funds(1234.45, 13423.123, 234234.123, 523.1))
        }
    }

    override fun queryOrderList(count: Int, offset: Int, callback: OnResult<MutableList<Order>>) {
        async {
            val list = mutableListOf<Order>()
            val order = Order()
            order.stkname = "美的集团"
            order.stkcode = "600500"
            order.orderprice = 66.11
            order.opertime = "101201"
            order.orderdate = "20190101"
            order.orderqty = 200
            order.matchqty = 100
            order.bsflag = "B"
            order.orderstatus = "成功"
            order.ordersno = "101010100"
            order.fundid = "100100"
            list.add(order)
            list.add(order)
            list.add(order)
            callback.onSucceed(list)
        }
    }

    override fun postOrder(
        orderdate: String,
        fundid: String,
        ordersno: String,
        bsflag: String,
        callback: OnResult<String>
    ) {
        async {
            callback.onSucceed("成功")
        }
    }

    override fun queryInformation(callback: OnResult<Information>) {
        async {
            val information = Information(
                "刘海", "1", "身份证", "00080808080",
                "12345667", "12000", "175208544@qq.com", "顶起"
            )
            callback.onSucceed(information)
        }
    }

    override fun postInformation(
        idType: String,
        idCard: String,
        phone: String,
        postCode: String,
        email: String,
        address: String,
        callback: OnResult<String>
    ) {

        async {
            callback.onSucceed("成功")
        }
    }

    override fun queryAccountList(callback: OnResult<MutableList<Account>>) {
        async {
            val list = mutableListOf<Account>()
            list.add(Account("custid123", "0", "secuid123", "李冬梅"))
            list.add(Account("custid223", "1", "secuid223", "李冬梅"))
            list.add(Account("custid323", "2", "secuid323", "李冬梅"))
            Log.e("TradeRepository", "queryAccountList")
            callback.onSucceed(list)
        }
    }

    override fun queryRiskLevel(custid: String, callback: OnResult<RiskLevel>) {
        async {
            var entity = RiskLevel("C4", "69")
            callback.onSucceed(entity)
        }
    }

    override fun transaction(
        market: String,
        code: String,
        price: Double,
        qty: Int,
        postFlag: String,
        callback: OnResult<TradeResultEntity>
    ) {
        async {
            val entity = TradeResultEntity()
            entity.ordergroup = "1"
            entity.orderid = "2"
            entity.ordersno = "3"
            callback.onSucceed(entity)
        }
    }

    override fun getAvailable(
        code: String, price: Double, flag: String,
        callback: OnResult<Int>
    ) {
        async {
            callback.onSucceed(1000)
        }

    }

    override fun getHandStockList(
        fundsId: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<TradeHandEntity>>
    ) {
        async {
            var mList = mutableListOf<TradeHandEntity>()
            var entity = TradeHandEntity()
            entity.stkname = "格力电器"
            entity.stkcode = "000651"
            entity.costprice = 12.49
            entity.lastprice = 56.12
            entity.lastprice = 56.12
            entity.income = 10934.12
            entity.stkbal = 1000
            entity.stkavl = 300

            mList.add(entity)
            mList.add(entity)
            mList.add(entity)
            mList.add(entity)
            mList.add(entity)
            mList.add(entity)
            callback.onSucceed(mList)
        }

    }

    override fun searchStock(code: String, callback: OnResult<TradeStockEntity>) {
        async {
            val result = TradeStockEntity()
            result.market = "0"
            result.stkcode = "000651"
            result.stkname = "格力电器"
            result.fHigh = 54.80
            result.fLow = 53.21
            result.fOpen = 54.23
            result.fLastClose = 53.31
            result.fNewest = 52.10
            result.fixprice = 52.18

            val dang = TradeStockEntity.Dang()
            dang.fOrder = 10
            dang.fPrice = 10.88

            result.ask = mutableListOf(dang, dang, dang, dang, dang)
            result.bid = mutableListOf(dang, dang, dang, dang, dang)
            callback.onSucceed(result)
        }
    }


}
