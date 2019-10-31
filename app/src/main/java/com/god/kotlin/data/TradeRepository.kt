package com.god.kotlin.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.ez08.trade.net.Client
import com.ez08.trade.net.NetUtil
import com.ez08.trade.net.hq.STradeHQQuery
import com.ez08.trade.net.hq.STradeHQQueryA
import com.ez08.trade.net.login.STradeGateLogin
import com.ez08.trade.net.login.STradeGateLoginA
import com.ez08.trade.net.verification.STradeVerificationCode
import com.ez08.trade.net.verification.STradeVerificationCodeA
import com.ez08.trade.tools.YCParser
import com.god.kotlin.data.entity.*
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult
import com.god.kotlin.util.getMarketByTag
import com.god.kotlin.util.toDoubleOrZero
import com.god.kotlin.util.toIntOrZero
import java.util.*


class TradeRepository : TradeDataSource {

    //获取验证码
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
                callback.onFailure(handleError("获取验证码失败"))
            }
        }
    }

    //登录
    override fun login(
        userType: String, userId: String, password: String, checkCode: String, verifiCodeId: String,
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

            val gateLoginA = STradeGateLoginA(data.headBytes, data.bodyBytes, Client.getInstance().aesKey)
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
                callback.onFailure(handleError(gateLoginA.getSzErrMsg()))
            }
        }
    }

    //session登录
    override fun loginSession(
        userType: String, userId: String, password: String, sessionId: String, strNet2: String,
        callback: OnResult<MutableList<User>>
    ) {
        val tradeGateLogin = STradeGateLogin()
        tradeGateLogin.setBody(Client.strUserType, Client.userId, Client.password, Client.sessionId, Client.strNet2)
        Client.getInstance().send(tradeGateLogin) { success, data ->
            val gateLoginA = STradeGateLoginA(data.headBytes, data.bodyBytes, Client.getInstance().aesKey)
            if (!gateLoginA.getbLoginSucc()) {
                callback.onFailure(handleError(gateLoginA.getSzErrMsg()))
            } else {
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
            }
        }
    }

    //新股配号
    override fun queryPeiHaoList(
        begin: String, end: String, count: Int, offset: Int,
        callback: OnResult<MutableList<PeiHao>>
    ) {
        val body = "FUN=411518&TBL_IN=strdate,enddate,fundid,stkcode,secuid,qryflag,count,poststr;" +
                begin + "," +
                end + "," +
                "" + "," +
                "" + "," +
                "" + "," +
                "0" + "," +
                "100" + "," +
                ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseArray(data)
                val list = mutableListOf<PeiHao>()
                for (i in result.indices) {
                    val stkcode = result[i]["stkcode"].orEmpty()
                    val stkname = result[i]["stkname"].orEmpty()
                    val bizdate = result[i]["bizdate"].orEmpty()
                    val mateno = result[i]["mateno"].orEmpty()
                    val matchqty = result[i]["matchqty"].toIntOrZero()
                    val entity = PeiHao(stkcode,stkname,bizdate,mateno,matchqty)
                    list.add(entity)
                }
                callback.onSucceed(list)
            }else{
                callback.onFailure(handleError(data))
            }
        }
    }

    //新股中签
    override fun queryZhongQianList(
        begin: String, end: String, count: Int, offset: Int,
        callback: OnResult<MutableList<ZhongQian>>
    ) {
        val body = "FUN=411560&TBL_IN=secuid,market,stkcode,issuetype,begindate,enddate,count,poststr;" +
                "" + "," +
                "" + "," +
                "" + "," +
                "" + "," +
                begin + "," +
                end + "," +
                "100" + "," +
                ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseArray(data)
                val list = mutableListOf<ZhongQian>()

                for (i in result.indices) {
                    val stkname = result[i]["stkname"].orEmpty()
                    val matchdate = result[i]["matchdate"].orEmpty()
                    val hitqty = result[i]["hitqty"].toIntOrZero()
                    val status = result[i]["status"].orEmpty()
                    val entity = ZhongQian(stkname,hitqty,matchdate,status)
                    list.add(entity)
                }
                callback.onSucceed(list)
            }else{
                callback.onFailure(handleError(data))
            }
        }
    }

    //新股代缴款
    override fun queryDaiJiaoList(callback: OnResult<MutableList<DaiJiao>>) {
        val body = "FUN=411547&TBL_IN=secuid,market,stkcode,issuetype;" + "," + "," + "," + ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseArray(data)
                val list = mutableListOf<DaiJiao>()
                for (i in result.indices) {
                    val stkname = result[i]["stkname"].orEmpty()
                    val matchdate = result[i]["matchdate"].orEmpty()
                    val hitqty = result[i]["hitqty"].toIntOrZero()
                    val stkcode = result[i]["stkcode"].orEmpty()
                    val market = result[i]["market"].orEmpty()
                    val entity = DaiJiao(stkname,stkcode,matchdate,market,hitqty)
                    list.add(entity)
                }
                callback.onSucceed(list)
            }else{
                callback.onFailure(handleError(data))
            }
        }
    }

    //获取上市深市新股额度
    override fun queryIpoQuota(secuid: String, callback: OnResult<MutableList<Quota>>) {
        val body = "FUN=410610&TBL_IN=market,secuid,orgid,count,posstr;" +
                "" + "," +
                secuid + "," +
                "" + "," +
                "100" + "," +
                ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                Log.e("queryIpoQuota",data)

                val result = YCParser.parseArray(data)
                val list = mutableListOf<Quota>()
                for (i in result.indices) {
                    val market = result[i]["market"].orEmpty()
                    val custquota = result[i]["custquota"].toIntOrZero()
                    val receivedate = result[i]["receivedate"].orEmpty()
                    val entity = Quota(market,custquota,receivedate)
                    list.add(entity)
                }
                callback.onSucceed(list)
            }else{
                callback.onFailure(handleError(data))
            }
        }
    }

    //获取新股列表
    override fun queryNewStockList(callback: OnResult<MutableList<NewStock>>) {
        val body = "FUN=411549&TBL_IN=market,stkcode,issuedate;" +
                "" + "," +
                "" + "," +
                ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseArray(data)
                val list = mutableListOf<NewStock>()
                for (i in result.indices) {
                    val stkcode = result[i]["stkcode"].orEmpty()
                    val stkname = result[i]["stkname"].orEmpty()
                    val linkstk = result[i]["linkstk"].orEmpty()
                    val maxqty = result[i]["maxqty"].toIntOrZero()
                    val minqty = result[i]["minqty"].toIntOrZero()
                    val market = result[i]["market"].orEmpty()
                    val entity = NewStock(stkcode,stkname,linkstk,minqty,maxqty,market,false)
                    list.add(entity)
                }
                callback.onSucceed(list)
            }else{
                callback.onFailure(handleError(data))
            }
        }
    }

    //查询当日成交
    override fun queryTodayDeal(
        fundid: String, count: Int, offset: Int,
        callback: OnResult<MutableList<Deal>>
    ) {
        val body =
            "FUN=410512&TBL_IN=fundid,market,secuid,stkcode,ordersno,bankcode,qryflag,count,poststr,qryoperway;" +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "1" + "," +
                    "100" + "," +
                    "" + "," +
                    ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseArray(data)
                val list = mutableListOf<Deal>()
                for (i in result.indices) {
                    val stkcode = result[i]["stkcode"].orEmpty()
                    val stkname = result[i]["stkname"].orEmpty()
                    val matchtime = result[i]["matchtime"].orEmpty()
                    val matchprice = result[i]["matchprice"].toDoubleOrZero()
                    val matchqty = result[i]["matchqty"].toIntOrZero()
                    val trddate = result[i]["trddate"].orEmpty()
                    val bsFlag = result[i]["bsflag"].orEmpty()
                    val entity = Deal(trddate, matchtime, matchprice, matchqty, stkname, stkcode, bsFlag)
                    list.add(entity)
                }
                callback.onSucceed(list)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //查询历史成交
    override fun queryHistoryDeal(
        begin: String, end: String, fundid: String, count: Int, offset: Int,
        callback: OnResult<MutableList<Deal>>
    ) {
        val body = "FUN=411513&TBL_IN=strdate,enddate,fundid,market,secuid,stkcode,bankcode,qryflag,count,poststr;" +
                begin + "," +
                end + "," +
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
                val result = YCParser.parseArray(data)
                val list = mutableListOf<Deal>()
                for (i in result.indices) {
                    val stkcode = result[i]["stkcode"].orEmpty()
                    val stkname = result[i]["stkname"].orEmpty()
                    val matchtime = result[i]["matchtime"].orEmpty()
                    val matchprice = result[i]["matchprice"].toDoubleOrZero()
                    val matchqty = result[i]["matchqty"].toIntOrZero()
                    val trddate = result[i]["trddate"].orEmpty()
                    val bsFlag = result[i]["bsflag"].orEmpty()
                    val entity = Deal(trddate, matchtime, matchprice, matchqty, stkname, stkcode, bsFlag)
                    list.add(entity)
                }
                callback.onSucceed(list)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //查询当日委托
    override fun queryTodayOrderList(fundid: String, count: Int, offset: Int, callback: OnResult<MutableList<Order>>) {
        val body =
            "FUN=410510&TBL_IN=market,fundid,secuid,stkcode,ordersno,Ordergroup,bankcode,qryflag,count,poststr,extsno,qryoperway;" +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    offset + "," +
                    count + "," +
                    "" + "," +
                    "" + "," +
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
                    list.add(entity)
                }
                callback.onSucceed(list)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //查询历史委托
    override fun queryHistoryOrderList(
        begin: String, end: String, fundid: String, count: Int, offset: Int,
        callback: OnResult<MutableList<Order>>
    ) {
        val body =
            "FUN=411511&TBL_IN=strdate,enddate,fundid,market,secuid,stkcode,ordersno,Ordergroup,bankcode,qryflag,count,poststr,extsno,qryoperway;" +
                    begin + "," +
                    end + "," +
                    "" + "," + //fundid
                    "" + "," +
                    "" + "," + //secuid
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    "" + "," +
                    offset + "," +
                    count + "," +
                    "" + "," +
                    "" + "," +
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
                    list.add(entity)
                }
                callback.onSucceed(list)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //查询转账记录
    override fun queryTransferList(fundid: String, callback: OnResult<MutableList<TransferRecord>>) {
        val body = "FUN=410608&TBL_IN=fundid,moneytype,sno,extsno,qryoperway;" +
                fundid + "," +
                "" + "," +
                "" + "," +
                "" + "," +
                ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val list = mutableListOf<TransferRecord>()
                val result = YCParser.parseArray(data)
                for (i in result.indices) {
                    val operdate = result[i]["operdate"].orEmpty()
                    val opertime = result[i]["opertime"].orEmpty()
                    val status = result[i]["status"].orEmpty()
                    val fundeffect = result[i]["fundeffect"].orEmpty()
                    val entity = TransferRecord(operdate, opertime, fundeffect, status)
                    list.add(entity)
                }
                callback.onSucceed(list)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //修改密码
    override fun postPwd(pwd: String, callback: OnResult<String>) {
        val body = "FUN=410302&TBL_IN=newpwd;$pwd;"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val msg = result["msgok"].orEmpty()
                callback.onSucceed(msg)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //修改资金密码
    override fun postFundsPwd(pwd: String, oldPwd: String, callback: OnResult<String>) {
        val body = "FUN=410303&TBL_IN=fundid,oldfundpwd,newfundpwd;" +
                "" + "," +
                oldPwd + "," +
                pwd + ";"

        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val msg = result["msgok"].orEmpty()
                callback.onSucceed(msg)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //查询资金概括
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
    }

    //查询委托列表
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
    }

    //修改委托
    override fun postOrder(
        orderdate: String, fundid: String, ordersno: String, bsflag: String,
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
    }

    //查询个人资料
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
                val information = Information(custname, sex, idtype, idno, telno, postid, email, addr)
                callback.onSucceed(information)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //修改个人资料
    override fun postInformation(
        idType: String, idCard: String, phone: String, postCode: String, email: String, address: String,
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
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //查询用户账户列表
    override fun queryAccountList(callback: OnResult<MutableList<Account>>) {
        val body = "FUN=410501&TBL_IN=fundid,market,secuid,qryflag,count,poststr;,,,1,10,;"
        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseArray(data)
                val list = mutableListOf<Account>()
                for (i in result.indices) {
                    val custid = result[i]["custid"].orEmpty()
                    val market = result[i]["market"].orEmpty()
                    val secuid = result[i]["secuid"].orEmpty()
                    val name = result[i]["name"].orEmpty()
                    val fundid = result[i]["fundid"].orEmpty()
                    val entity = Account(custid, market, secuid, name)
                    list.add(entity)
                }
                callback.onSucceed(list)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //查询风险等级
    override fun queryRiskLevel(custid: String, callback: OnResult<RiskLevel>) {
        val body = "FUN=99000120&TBL_IN=ANS_TYPE,USER_CODE;0,$custid;"
        Client.getInstance().sendBiz(body) { success, data ->
            if (success) {
                val result = YCParser.parseObject(data)
                val score = result["SURVEY_SCORE"].orEmpty()
                val type = result["RATING_LVL_NAME"].orEmpty()
                val riskLevel = RiskLevel(type,score)
                callback.onSucceed(riskLevel)
            } else {
                callback.onFailure(handleError(data))
            }
        }
    }

    //发送交易请求
    override fun transaction(
        market: String, code: String, secuid: String, fundsId: String, price: Double, qty: Int, postFlag: String,
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
    }

    //获取可买可卖最大值
    override fun getAvailable(
        market: String, secuid: String, fundsId: String, code: String, price: Double, flag: Boolean,
        callback: OnResult<Int>
    ) {

        val d = if(flag) "B" else "S"

        val body = "FUN=410410&TBL_IN=market,secuid,fundid,stkcode,bsflag,price,bankcode,hiqtyflag,creditid,creditflag,linkmarket,linksecuid,sorttype,dzsaletype,prodcode;" +
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
    }

    //获取持仓数据
    override fun getHandStockList(
        fundsId: String, count: Int, offset: Int,
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
    }

    //搜索股票
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
    }


    override fun getHQ(market: String,code: String,callback :OnResult<TradeStockEntity>){
        Client.getInstance().send(STradeHQQuery(getMarketByTag(market), code)) { success, data ->
            if (success) {
                val queryA = STradeHQQueryA(data.headBytes, data.bodyBytes, Client.getInstance().aesKey)

                val entity = TradeStockEntity()
                entity.fOpen = queryA.fOpen.toDouble()
                entity.fLastClose = queryA.fLastClose.toDouble()
                entity.fHigh = queryA.fHigh.toDouble()
                entity.fLow = queryA.fLow.toDouble()
                entity.fNewest = queryA.fNewest.toDouble()

                val askItems = queryA.ask
                val list1 = ArrayList<TradeStockEntity.Dang>()
                for (i in askItems.indices) {
                    val dang = TradeStockEntity.Dang()
                    dang.fOrder = askItems[i].fOrder.toInt()
                    dang.fPrice = askItems[i].fPrice.toDouble()
                    list1.add(dang)
                }
                entity.ask = list1
                val bidItems = queryA.bid
                val list2 = ArrayList<TradeStockEntity.Dang>()
                for (i in bidItems.indices) {
                    val dang = TradeStockEntity.Dang()
                    dang.fOrder = bidItems[i].fOrder.toInt()
                    dang.fPrice = bidItems[i].fPrice.toDouble()
                    list2.add(dang)
                }
                entity.bid = list2
                callback.onSucceed(entity)
            }else{

            }
        }

    }

    private fun handleError(data: String): Error {
        val error = Error()
        error.szError = data
        return error
    }

}
