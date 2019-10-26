package com.god.kotlin.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.ez08.trade.Constant
import com.ez08.trade.net.*
import com.ez08.trade.net.login.STradeGateLogin
import com.ez08.trade.net.login.STradeGateLoginA
import com.ez08.trade.net.verification.STradeVerificationCode
import com.ez08.trade.net.verification.STradeVerificationCodeA
import com.ez08.trade.tools.CommonUtils
import com.ez08.trade.tools.DialogUtils
import com.ez08.trade.tools.YCParser
import com.ez08.trade.ui.trade.entity.TradeEntrustEntity
import com.ez08.trade.user.UserHelper
import com.god.kotlin.data.entity.*
import com.god.kotlin.net.OnResult
import com.god.kotlin.util.async
import java.lang.Exception
import com.god.kotlin.net.Error
import com.god.kotlin.util.toDoubleOrZero
import com.god.kotlin.util.toIntOrZero
import com.xuhao.didi.socket.common.interfaces.utils.TextUtils
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
            if (gateLoginA.getbLoginSucc()) {
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
            } else {
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

    override fun queryFunds(fundsId: String, moneyType: Int, callback: OnResult<Funds>) {
        val body = "FUN=410502&TBL_IN=fundid,moneytype,remark;" + fundsId + "," +
                moneyType + ",;"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val funds = Funds(
                    result["fundavl"].toDoubleOrZero(),
                    result["fundbal"].toDoubleOrZero(),
                    result["marketvalue"].toDoubleOrZero(),
                    result["stkvalue"].toDoubleOrZero(),
                    result["fundfrz"].toDoubleOrZero()
                )
                callback.onSucceed(funds)
            } else {
                callback.onFailure(handleError(data))
            }
        }
//        async {
//            callback.onSucceed(Funds(1234.45, 13423.123, 234234.123, 523.1))
//        }
    }

    override fun queryOrderList(count: Int, offset: Int, callback: OnResult<MutableList<Order>>) {
        val body = "FUN=410415&TBL_IN=orderdate,fundid,secuid,stkcode,ordersno,qryflag,count,poststr;" +
                "" + "," +
                "" + "," +
                "" + "," +
                "" + "," +
                "" + "," +
                offset + "," +
                count + "," +
                ";"
        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val list = mutableListOf<Order>()
                val result = YCParser.parseArray(data)
                for (i in result.indices) {
                    val entity = Order()
                    entity.stkcode = result[i]["stkcode"]
                    entity.stkname = result[i]["stkname"]
                    entity.orderprice = result[i]["orderprice"].toDoubleOrZero()
                    entity.opertime = result[i]["opertime"]
                    entity.orderdate = result[i]["orderdate"]
                    entity.orderqty = result[i]["orderqty"].toIntOrZero()
                    entity.matchqty = result[i]["matchqty"].toIntOrZero()
                    entity.bsflag = result[i]["bsflag"]
                    entity.orderstatus = result[i]["orderstatus"]
                    entity.ordersno = result[i]["ordersno"]
                    entity.fundid = result[i]["fundid"]
                    list.add(entity)
                }
                callback.onSucceed(list)
            } else {
                callback.onFailure(handleError(data))
            }
        }
//        async {
//            val list = mutableListOf<Order>()
//            val order = Order()
//            order.stkname = "美的集团"
//            order.stkcode = "600500"
//            order.orderprice = 66.11
//            order.opertime = "101201"
//            order.orderdate = "20190101"
//            order.orderqty = 200
//            order.matchqty = 100
//            order.bsflag = "B"
//            order.orderstatus = "成功"
//            order.ordersno = "101010100"
//            order.fundid = "100100"
//            list.add(order)
//            list.add(order)
//            list.add(order)
//            callback.onSucceed(list)
//        }
    }

    override fun postOrder(
        orderdate: String,
        fundid: String,
        ordersno: String,
        bsflag: String,
        callback: OnResult<String>
    ) {
        val body = "FUN=410413&TBL_IN=orderdate,fundid,ordersno,bsflag;" +
                orderdate + "," +
                fundid + "," +
                ordersno + "," +
                bsflag +
                ";"
        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val ordersno = result["ordersno"].orEmpty()
                callback.onSucceed(ordersno)
            } else {
                callback.onFailure(handleError(data))
            }
        }
//        async {
//            callback.onSucceed("成功")
//        }
    }

    override fun queryInformation(callback: OnResult<Information>) {
        val body = "FUN=410321"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val custname = result["custname"].orEmpty()
                val sex = result["sex"].orEmpty()
                val addr = result["addr"].orEmpty()
                val idtype = result["idtype"].orEmpty()
                val idno = result["idno"].orEmpty()
                val postid = result["postid"].orEmpty()
                val email = result["email"].orEmpty()
                val telno = result["telno"].orEmpty()
                val information = Information(custname,sex,idtype,idno,telno,postid,email,addr)
                callback.onSucceed(information)
            }else{
                callback.onFailure(handleError(data))
            }
//        async {
//            val information = Information(
//                "刘海", "1", "身份证", "00080808080",
//                "12345667", "12000", "175208544@qq.com", "顶起"
//            )
//            callback.onSucceed(information)
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

        val body = ("FUN=410320&TBL_IN=idtype,idno,mobileno,postid,email,addr;"
                + idType + ","
                + idCard + ","
                + phone + ","
                + postCode + ","
                + email + ","
                + address
                + ";")

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val msgok = result["msgok"].orEmpty()
                callback.onSucceed(msgok)
            }else{
                callback.onFailure(handleError(data))
            }
        }
//        async {
//            callback.onSucceed("成功")
//        }
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
        secuid: String,
        fundsId: String,
        price: Double,
        qty: Int,
        postFlag: String,
        callback: OnResult<TradeResultEntity>
    ) {
        val body = "FUN=410411&TBL_IN=market,secuid,fundid,stkcode,bsflag,price,qty,ordergroup," +
                "bankcode,creditid,creditflag,remark,targetseat,promiseno,risksno,autoflag," +
                "enddate,linkman,linkway,linkmarket,linksecuid,sorttype,mergematchcode,mergematchdate" +
                //                "oldorderid,prodcode,pricetype,blackflag,dzsaletype,risksignsno" +
                ";" +
                market + "," +
                secuid + "," +
                fundsId + "," +
                code + "," +
                postFlag + "," +
                price + "," +
                qty + "," +
                "0" +
                //                "," + "," + "," + "," + ","
                "," + "," + "," + "," + "," +
                "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," +
                ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val entity = TradeResultEntity()
                val result = YCParser.parseObject(data)
                val ordersno = result["ordersno"]
                val orderid = result["orderid"]
                val ordergroup = result["ordergroup"]
                entity.ordergroup = ordergroup
                entity.orderid = orderid
                entity.ordersno = ordersno
                callback.onSucceed(entity)
            } else {
                callback.onFailure(handleError(data))
            }
        }
//        async {
//            val entity = TradeResultEntity()
//            entity.ordergroup = "1"
//            entity.orderid = "2"
//            entity.ordersno = "3"
//            callback.onSucceed(entity)
//        }
    }

    override fun getAvailable(
        market: String, secuid: String, fundsId: String,
        code: String, price: Double, flag: Boolean,
        callback: OnResult<Int>
    ) {
        val d: String = if (flag) {
            "B"
        } else {
            "S"
        }

        val body =
            "FUN=410410&TBL_IN=market,secuid,fundid,stkcode,bsflag,price,bankcode,hiqtyflag,creditid,creditflag,linkmarket,linksecuid,sorttype,dzsaletype,prodcode;" +
                    market + "," +
                    secuid + "," +
                    fundsId + "," +
                    code + "," +
                    d + "," +
                    price + "," + "," + "," + "," + "," + "," + "," + "," + "," +
                    ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val max = result["maxstkqty"].toIntOrZero()
                callback.onSucceed(max)
            } else {
                callback.onFailure(handleError(data))
            }
        }

//        async {
//            callback.onSucceed(1000)
//        }

    }

    override fun getHandStockList(
        fundsId: String,
        count: Int,
        offset: Int,
        callback: OnResult<MutableList<TradeHandEntity>>
    ) {
        val body = "FUN=410503&TBL_IN=market,fundid,secuid,stkcode,qryflag,count,poststr;" +
                "," + fundsId + "," + ",," + offset + "," + count + ",;"

        val list = mutableListOf<TradeHandEntity>()
        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseArray(data)
                for (i in result.indices) {
                    val entity = TradeHandEntity()
                    entity.stkcode = result[i]["stkcode"]
                    entity.stkname = result[i]["stkname"]
                    entity.market = result[i]["market"]
                    entity.stkbal = result[i]["stkbal"].toIntOrZero()
                    entity.stkavl = result[i]["stkavl"].toIntOrZero()
                    entity.costprice = result[i]["costprice"].toDoubleOrZero()
                    entity.mktval = result[i]["mktval"].toDoubleOrZero()
                    entity.income = result[i]["income"].toDoubleOrZero()
                    entity.lastprice = result[i]["lastprice"].toDoubleOrZero()
                    entity.moneyType = result[i]["moneytype"]
                    list.add(entity)
                }
                callback.onSucceed(list)
            } else {
                val error = Error()
                error.szError = data
                callback.onFailure(error)
            }
        }
//        async {
//            var mList = mutableListOf<TradeHandEntity>()
//            var entity = TradeHandEntity()
//            entity.stkname = "格力电器"
//            entity.stkcode = "000651"
//            entity.costprice = 12.49
//            entity.lastprice = 56.12
//            entity.lastprice = 56.12
//            entity.income = 10934.12
//            entity.stkbal = 1000
//            entity.stkavl = 300
//
//            mList.add(entity)
//            mList.add(entity)
//            mList.add(entity)
//            mList.add(entity)
//            mList.add(entity)
//            mList.add(entity)
//            callback.onSucceed(mList)
//        }
    }

    override fun searchStock(code: String, callback: OnResult<TradeStockEntity>) {

        val body = "FUN=410203&TBL_IN=market,stklevel,stkcode,poststr,rowcount,stktype;" +
                "" + "," +
                "" + "," +
                code + "," +
                "" + "," +
                "" + "," +
                ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val stockEntity = TradeStockEntity()

                if (result == null) {
                    val error = Error()
                    error.szError = "股票输入有误"
                    callback.onFailure(error)
                } else {
                    stockEntity.market = result["market"]
                    stockEntity.stkname = result["stkname"]
                    stockEntity.stkcode = result["stkcode"]
                    stockEntity.stopflag = result["stopflag"]
                    stockEntity.maxqty = result["maxqty"]
                    stockEntity.minqty = result["minqty"]
                    stockEntity.fixprice = result["fixprice"].toDoubleOrZero()
                    callback.onSucceed(stockEntity)
                }
            } else {
                val error = Error()
                error.szError = data
                callback.onFailure(error)
            }
        }
//        async {
//            val result = TradeStockEntity()
//            result.market = "0"
//            result.stkcode = "000651"
//            result.stkname = "格力电器"
//            result.fHigh = 54.80
//            result.fLow = 53.21
//            result.fOpen = 54.23
//            result.fLastClose = 53.31
//            result.fNewest = 52.10
//            result.fixprice = 52.18
//
//            val dang = TradeStockEntity.Dang()
//            dang.fOrder = 10
//            dang.fPrice = 10.88
//
//            result.ask = mutableListOf(dang, dang, dang, dang, dang)
//            result.bid = mutableListOf(dang, dang, dang, dang, dang)
//            callback.onSucceed(result)
//        }
    }

    private fun handleError(data: String): Error {
        val error = Error()
        error.szError = data
        Log.e("handleError", data)
        return error
    }

}
