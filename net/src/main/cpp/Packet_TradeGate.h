//
//  Packet_TradeGate.h
//  XFrame
//
//  Created by guangming xu on 2019/3/25.
//  Copyright © 2019年 guangguang. All rights reserved.
//

#ifndef Packet_TradeGate_h
#define Packet_TradeGate_h

#include "Packet_TradeBase.h"
#include "cBase64.h"
#include "Packet_Tools.h"

#pragma pack(push,1)
#pragma warning( disable : 4200 )

struct STradeGateHead
{
    WORD            wPid;
    union{
        DWORD    dwBodyLen;
        DWORD    dwBodySize;
    };
    DWORD            dwIP;
    DWORD            dwThreadIndex;
    DWORD            dwSocketId;
    DWORD            dwReqId;        //dwReqId>0
};


//数据字典定义

//1、交易市场定义
#define    MARKET_SHENA        '0'    //深A
#define    MARKET_HUA            '1'    //沪A
#define    MARKET_SHENB        '2'    //深B
#define    MARKET_HUB            '3'    //沪B
#define MARKET_HUGANGTONG    '5' //沪港通
#define    MARKET_SANBANA        '6'    //三板A
#define    MARKET_SANBANB        '7'    //三板B
#define    MARKET_NONTRADEBOND    'A'    //非交易所债券
#define    MARKET_LOF            'J'    //开放式基金
#define    MARKET_SHENGANGTONG    'S' //深港通

//2、货币各类
#define MONEYTYPE_RMB        '0' //人民币
#define MONEYTYPE_HKD        '1' //港币
#define MONEYTYPE_USD        '2' //美元

//3、委托状态

#define ORDERSTATUS_UNREPORT      '0'        //未报    0    未报
#define ORDERSTATUS_REPORTING      '1'        //1    正报
#define ORDERSTATUS_REPORTED      '2'        //2    已报
#define    ORDERSTATUS_YIBAODAICHE      '3'        //已报待撤
#define ORDERSTATUS_BUCHENGDAICHE '4'        //4    部成待撤
#define ORDERSTATUS_BUCHE          '5'        //5    部撤
#define ORDERSTATUS_CANCELED      '6'        //6    已撤
#define ORDERSTATUS_BUCHENG          '7'        //7    部成
#define ORDERSTATUS_YICHENG          '8'        //8    已成
#define ORDERSTATUS_FEIDAN          '9'        //9    废单
#define ORDERSTATUS_DAIBAO          'A'        //A    待报
#define ORDERSTATUS_ZHENGBAO      'B'        //正报
#define ORDERSTATUS_YISHOULI      'C'        //C    已受理
#define ORDERSTATUS_YIQUEREN      'D'        //D    已确认


//4、买卖类别定义
#define CATEGORY_BUY                "B"        //B:买入
#define CATEGORY_SELL                "S"        //S:卖出
#define    CATEGORY_CONV2SHARES        "G"     //G:转股
#define CATEGORY_SELLBACK            "H"        //回售
/*    3基金申购
 4基金赎回
 5基金认购
 7行权
 a对方买入
 b本方买入
 c即时买入
 d五档买入
 e全额买入
 f对方卖出
 g本方卖出
 h即时卖出
 i五档卖出
 j全额卖出
 q转限买入
 r转限卖出
 1E意向买
 1F意向卖
 1G定价买
 1H定价卖
 1I确认买
 1J确认卖
 82 ETF实物申购
 84 ETF实物赎回
 85基金拆分
 86基金合并
 89 ETF实物冲账
 90跨境跨市场ETF申购
 91跨境跨市场ETF赎回
 1K 报价回购买入
 1N 报价回购入质
 1O 报价回购出质
 0n质押入质
 0p质押出质
 1R 柜台意向买
 1S 柜台意向卖

 1T 柜台大额做市买
 1U 柜台大额做市卖
 1V 柜台小额做市买
 1W 柜台小额做市卖
 1X 柜台成交确认买
 1Y 柜台成交确认卖
 1c 柜台定位转入
 1d 柜台定位转出
 WB国债预发行买
 WS国债预发行卖
 1m 盘后定价大宗买
 1n 盘后定价大宗卖
 10 自主行权
 2e LOF认购
 2f LOF申购
 2g LOF赎回
 1w 上证LOF分拆
 1l上证LOF合并
 */

//证券类型定义
enum SecurityType
{
    SECURITY_STOCK = 0,   //股票
    SECURITY_BOND,         //债券
    SECURITY_FUND         //基金
};

//查询标志定义
#define QUERY_FLAG_FORWARD '1'
#define QUERY_FLAG_BEHIND '0'

//客户性质： '0' 普通客户 '1' 客户代理人 '2' 经纪人
#define TRADE_CUSTPROP_0                ('0')        //普通客户
#define TRADE_CUSTPROP_1                ('1')        //客户代理人
#define TRADE_CUSTPROP_2                ('2')        //经纪人


//常用公共定义
#define BOND_PARVALUE            100                    //债券面值

//字段长度统一定义
#define        ORGID_LEN            5
#define        MONEYTYPE_LEN        2
#define        SECUID_LEN            11
#define        STKNAME_LEN            9
#define        STKCODE_LEN            9
#define        MARKET_LEN            2
#define        POSTSTR_LEN            33                            //定位串长度
#define        REMARK_LEN            66                            //备注长度
#define        QRYFLAG_LEN            2                            //查询方向长度



//
#define PID_TRADE_GATE__BIZFUN__FUN                "FUN"
#define PID_TRADE_GATE__BIZFUN__TBLIN            "TBL_IN"
#define PID_TRADE_GATE__BIZFUN__TBLOUT            "TBL_OUT"
#define PID_TRADE_GATE__BIZFUN__TBLFIX            "TBL_FIX"

#define PID_TRADE_GATE__BIZFUN__LOGIN_SUCC        "LOGIN_SUCC"
#define PID_TRADE_GATE__BIZFUN__LOGIN_ERRMSG    "LOGIN_ERRMSG"

//
#define PID_TRADE_GATE__BIZFUN__TBLERR            "TBL_ERR"
#define PID_TRADE_GATE__BIZFUN__TBLERR_REQID    "errReqId"
#define PID_TRADE_GATE__BIZFUN__TBLERR_CODE        "errCode"
#define PID_TRADE_GATE__BIZFUN__TBLERR_MSG        "errMsg"

//
#define PID_TRADE_GATE__BIZFUN__LOG_TIME        "log_time"
#define PID_TRADE_GATE__BIZFUN__LOG_ACCESS        "log_access"
#define PID_TRADE_GATE__BIZFUN__LOG_REQSEQ        "log_reqSeq"
#define PID_TRADE_GATE__BIZFUN__LOG_REQTIME        "log_reqTime"
#define PID_TRADE_GATE__BIZFUN__LOG_REQFUN        "log_reqFun"
#define PID_TRADE_GATE__BIZFUN__LOG_REQCID        "log_reqCid"
#define PID_TRADE_GATE__BIZFUN__LOG_ANSTIME        "log_ansTime"
#define PID_TRADE_GATE__BIZFUN__LOG_ANSRET        "log_ansRet"

#define PID_TRADE_GATE__PUBINFO__PUBLICID        "PUBLICID"






#define PID_TRADE_GATE__OK            (PID_TRADE_GATE_BASE+0)
struct STradeGateOK
{
    DWORD    dwReserved[4];
};

//在线用户全量包
#define PID_TRADE_GATE__USER_LIST        (PID_TRADE_GATE_BASE+1)
struct STradeGateUserItem
{
    DWORD            dwSocketId;
};
struct STradeGateUserList
{
    DWORD                dwThreadIndex;
    DWORD                dwThreadCount;
    DWORD                dwCount;
    STradeGateUserItem    pItem[0];
};


//在线用户增量包
#define PID_TRADE_GATE__USER_ONLINE        (PID_TRADE_GATE_BASE+2)
struct    STradeGateUserOnline
{
    DWORD            dwThreadIndex;
    DWORD            dwSocketId;
};

//在线用户增量包
#define PID_TRADE_GATE__USER_OFFLINE    (PID_TRADE_GATE_BASE+3)
typedef STradeGateUserOnline    STradeGateUserOffline;

//文本内容
#define PID_TRADE_GATE__TEXT_INFO        (PID_TRADE_GATE_BASE+4)
struct STradeGateTextInfo
{
    DWORD            dwTextLen;    //含'\0'
    char            szText[0];
};
//文本格式：Key1=Value1&Key2=Value2
//Key和Value内容需要编码


#define PID_TRADE_GATE__ERROR                    (PID_TRADE_GATE_BASE+9)                                //零表示出错了，非零为正确的解析包
struct STradeGateError
{
    DWORD    dwReqId;                    //一个socket仅支持同时存在一个未完成的请求
    DWORD    dwErrorCode;
    char    szError[0];
    std::string toJSON(JNIEnv* env,const char* content)
    {
        cJSON *json = cJSON_CreateObject();
        //
        cJSON_AddNumberToObject(json,"dwReqId",(int)dwReqId);
        cJSON_AddNumberToObject(json,"dwErrorCode",(int)dwErrorCode);
        cJSON_AddStringToObject(json,"szError",NewCodedString(env,content,"GB2312",strlen(content)));
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};





struct STradeGateUserInfo
{
    __int64        n64_custid;                    //    客户代码    custid    int    登陆后送，登陆客户代码
    char        sz_custorgid[ORGID_LEN];    //    客户机构    custorgid     char(4)    登陆后送，客户所属机构
    char        sz_trdpwd[33];                //    交易密码    trdpwd    char(32)    交易密码
    char        sz_netaddr[271];            //    操作站点    netaddr    char(270)    网卡地址或电话号码, 必须送，不可以为空
    char        sz_orgid[5];                //    操作机构    orgid     char(4)    操作地机构代码, 必须送，不可以为空 修改为 登录后送 系统缩写不超过4个字
    char        sz_custcert[129];            //    客户证书    custcert    char(128)    客户证书，登陆时送空串,登陆后获得，后续请求传递
    char        sz_netaddr2[256];            //    站点扩位    netaddr2    Char(255)    操作站点扩位，接收MAC地址和PC硬盘序列号/手机号
};

//用户登陆（410301）
#define PID_TRADE_GATE__LOGIN                    (PID_TRADE_GATE_BASE+10)
struct STradeGateLogin
{
    STradeGateUserInfo        userinfo;            //用户信息，必送
#define INPUTTYPE_FUND        'Z'                    //'Z'表示以资金帐户登陆  -登陆标识为资金帐户
#define INPUTTYPE_OTHER        'O'                    //其他表示以股东代码登陆  -登陆标识为对应市场的股东代码
    char                    sz_inputtype[2];    //登陆类型    inputtype    char(1)    Y    见备注
    char                    sz_inputid[65];        //登陆标识    inputid    char(64)    Y    见备注
    char                    sz_market[2];        //市场标识    以股东代码登陆时，为对应的市场代码（这个字段文档里没有，是根据文档里的备注信息猜来的）
    BYTE                    btMD5_of_Client[16];
    char                    szVerificationId[21];
    char                    szVerificationCode[9];
    char                    szReserved[21];

};
struct STradeGateLoginAItem
{
#define CUSTPROP_GENERAL '0'            //普通客户
#define CUSTPROP_AGENT     '1'            //客户代理人
#define    CUSTPROP_BROKER     '2'            //经纪人

    char        sz_custprop[2];            //        客户性质    custprop    char(1)
    char        sz_market[MARKET_LEN];    //        交易市场    market    char(1)

    char        sz_secuid[SECUID_LEN];    //        股东代码    secuid    char(10)
    char        sz_name[17];            //        股东姓名    name    char(16)
    __int64        n64_fundid;                //        缺省资金帐户fundid    Int64
    __int64        n64_custid;                //        客户代码    custid    Int64    登录后送，客户端要保存，以后的业务要带这个东西
    char        sz_custname[17];        //        客户姓名    custname    char(16)
    char        sz_orgid[ORGID_LEN];    //        机构编码    orgid    char(4)      登录后送，客户端要保存，以后的业务要带这个东西

    char        sz_timeoutflag[2];        //        延时属性    timeoutflag    char(1)    1延时0不延时
    char        sz_authlevel[2];        //        认证方式/级别    authlevel    char(1)    安全级别
    int            n_pwderrtimes;            //        登陆错误次数    pwderrtimes    int
    char        sz_singleflag[2];        //        客户标志        singleflag    char(1)    0 个人 1 机构
    char        sz_checkpwdflag[2];        //        密码有效标志    checkpwdflag    char(1)    0 正常 1 过期 2 未修改

    char        sz_custcert[129];        //        客户证书    custcert    char(128)，登录成功后返回的会话信息，以后做委托或查询时传入作为入参  登陆时送空串,登陆后获得，后续请求传递  登录成功后返回的会话信息，以后做委托或查询时传入作为入参
    char        sz_tokenlen[9];            //        登录时输入的动态令牌长度    tokenlen    char(8)    如为普通方式，则是0
    char        sz_lastlogindate[9];    //        最近登录日期    lastlogindate    char(8)
    char        sz_lastlogintime[9];    //        最近登录时间    lastlogintime    char(8)
    char        sz_lastloginip[65];        //        最近登录IP    lastloginip    char(64)
    char        sz_lastloginmac[33];    //        最近登录MAC    lastloginmac    char(32)
    char        sz_inputtype[2];        //        登陆类型    inputtype    char(1)
    char        sz_inputid[65];            //        登陆标识    inputid    char(64)
    char        sz_tokenenddate[9];        //        客户动态令牌结束日期    tokenenddate    char(8)
    char        sz_bindflag[2];            //        硬件绑定信息标识    bindflag    char(1)    'N'未绑定 '1'已绑定已验证通过  '0'验证绑定信息失败
    cJSON* toJSONObject(JNIEnv* env)
    {
        cJSON *json = cJSON_CreateObject();
        //
        cJSON_AddStringToObject(json,"sz_custprop",NewCodedString(env,sz_custprop,"GB2312",strlen(sz_custprop)));
        cJSON_AddStringToObject(json,"sz_market",NewCodedString(env,sz_market,"GB2312",strlen(sz_market)));
        cJSON_AddStringToObject(json,"sz_secuid",NewCodedString(env,sz_secuid,"GB2312",strlen(sz_secuid)));
        cJSON_AddStringToObject(json,"sz_name",NewCodedString(env,sz_name,"GB2312",strlen(sz_name)));
        cJSON_AddNumberToObject(json,"n64_fundid",n64_fundid);
        cJSON_AddNumberToObject(json,"n64_custid",n64_custid);
        //
        cJSON_AddStringToObject(json,"sz_custname",NewCodedString(env,sz_custname,"GB2312",strlen(sz_custname)));
        cJSON_AddStringToObject(json,"sz_orgid",NewCodedString(env,sz_orgid,"GB2312",strlen(sz_orgid)));
        cJSON_AddStringToObject(json,"sz_timeoutflag",NewCodedString(env,sz_timeoutflag,"GB2312",strlen(sz_timeoutflag)));
        cJSON_AddStringToObject(json,"sz_authlevel",NewCodedString(env,sz_authlevel,"GB2312",strlen(sz_authlevel)));
        cJSON_AddNumberToObject(json,"n_pwderrtimes",n_pwderrtimes);

        cJSON_AddStringToObject(json,"sz_singleflag",NewCodedString(env,sz_singleflag,"GB2312",strlen(sz_singleflag)));
        cJSON_AddStringToObject(json,"sz_checkpwdflag",NewCodedString(env,sz_checkpwdflag,"GB2312",strlen(sz_checkpwdflag)));
        cJSON_AddStringToObject(json,"sz_custcert",NewCodedString(env,sz_custcert,"GB2312",strlen(sz_custcert)));
        cJSON_AddStringToObject(json,"sz_tokenlen",NewCodedString(env,sz_tokenlen,"GB2312",strlen(sz_tokenlen)));
        cJSON_AddStringToObject(json,"sz_lastlogindate",NewCodedString(env,sz_lastlogindate,"GB2312",strlen(sz_lastlogindate)));
        cJSON_AddStringToObject(json,"sz_lastlogintime",NewCodedString(env,sz_lastlogintime,"GB2312",strlen(sz_lastlogintime)));
        cJSON_AddStringToObject(json,"sz_lastloginip",NewCodedString(env,sz_lastloginip,"GB2312",strlen(sz_lastloginip)));
        cJSON_AddStringToObject(json,"sz_lastloginmac",NewCodedString(env,sz_lastloginmac,"GB2312",strlen(sz_lastloginmac)));
        cJSON_AddStringToObject(json,"sz_inputtype",NewCodedString(env,sz_inputtype,"GB2312",strlen(sz_inputtype)));
        cJSON_AddStringToObject(json,"sz_inputid",NewCodedString(env,sz_inputid,"GB2312",strlen(sz_inputid)));
        cJSON_AddStringToObject(json,"sz_tokenenddate",NewCodedString(env,sz_tokenenddate,"GB2312",strlen(sz_tokenenddate)));
        cJSON_AddStringToObject(json,"sz_bindflag",NewCodedString(env,sz_bindflag,"GB2312",strlen(sz_bindflag)));
        return json;
    }
};

struct STradeGateLoginA
{
    BOOLEAN        bLoginSucc;        //登陆是否成功
    union
    {
        struct{//bLoginSucc=TRUE
            DWORD                    dwCount;
            STradeGateLoginAItem    pItem[0];
        };
        struct{//bLoginSucc=FALSE, dwLen包含'\0'
            DWORD                    dwLen;
            char                    szErrMsg[0];    //end with '\0'
        };
    };

    std::string toJSON(JNIEnv* env,const char* errMsg,STradeGateLoginAItem * lists)
    {
        cJSON *json = cJSON_CreateObject();
        //
        cJSON_AddNumberToObject(json,"bLoginSucc",(int)bLoginSucc);
        cJSON_AddNumberToObject(json,"dwCount",dwCount);
        cJSON_AddNumberToObject(json,"dwLen",dwLen);
        int base64len = (strlen(errMsg)/3+1)*4+1;
        //char *basestr = new char[base64len];
        //base64_encode((const char *)errMsg,(const long)strlen(errMsg),basestr);
        cJSON_AddStringToObject(json,"szErrMsg",NewCodedString(env,errMsg,"GB2312",strlen(errMsg)));
        if(lists != NULL && sizeof(lists)>0)
        {
            cJSON *jsonarray = cJSON_CreateArray();
            for(int i=0;i<sizeof(lists);i++)
            {
                cJSON_AddItemToArray(jsonarray,lists[i].toJSONObject(env));
            }
            cJSON_AddItemToObject(json,"items",jsonarray);
        }
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};



//////////////////////////////////////////////////////////////////////////
//业务功能号定义
#define    FUNCTIONID_LOGIN                    "410301"                //用户登录

//功能号定义
#define    FUNCTIONID_SYSSTATE                    "410232"                //查询系统当前状态
#define FUNCTIONID_SECURITYINFO                "410203"                //查询证券信息
#define FUNCTIONID_DICTINFO                    "410220"                //查询字典信息
#define FUNCTIONID_MODIFYCUSTINFO            "410320"                //修改用户资料
#define FUNCTIONID_CUSTOMERINFO                "410321"                //客户资料查询
#define FUNCTIONID_CUSTOMERRIGHT            "410332"                //客户权限查询
#define FUNCTIONID_LASTPRICEQUERY            "410409"                //股份余额及最新价查询
#define FUNCTIONID_MAXTRADENUM                "410410"                //最大可交易数量
#define FUNCTIONID_ORDER                    "410411"                //委托买卖业务
#define FUNCTIONID_RESET_COSTPRICE            "410450"                //重置成本
#define FUNCTIONID_FUNDSQUERY                "410502"                //资金查询
#define FUNCTIONID_HOLDQUERY                "410504"                //股份汇总查询
#define FUNCTIONID_CURRDAYORDERQUERY        "410510"                //当日委托明细查询
#define FUNCTIONID_HISORDERQUERY            "411511"                //历史委托明细查询
#define FUNCTIONID_CURRDAYDEALQUERY            "410512"                //当日成交明细查询
#define FUNCTIONID_HISDEALQUERY                "411513"                //历史成交明细查询
#define FUNCTIONID_CANCEL                    "410413"                //委托撤单
#define FUNCTIONID_STATEMENTQUERY            "411525"                //资金流水
#define    FUNCTIONID_SHAREHOLDERQUERY            "410501"                //股东列表
#define FUNCTIONID_MODIFY_TRADEPWD            "410302"                //修改交易密码
#define FUNCTIONID_MODIFY_FUNDPWD            "410303"                //修改资金密码
#define FUNCTIONID_FUND_MAXDRAW                "410529"                //资金可取数查询

#define FUNCTIONID_DealAggregate            "410516"                //当日成交汇总查询
#define FUNCTIONID_FUNDSFLOWS                "410523"                //当日资金流水查询

#define FUNCTIONID_REPURCHASEDAYS            "410462"                //资金占用天数查询
#define FUNCTIONID_UNEXPIREDREPURCHASE        "410545"                //未到期回购查询
#define FUNCTIONID_CUSTOMERMSG                "410568"                //客户消息查询
#define FUNCTIONID_STANDARDTICKETQUERY        "410574"                //标准券汇总查询

#define FUNCTIONID_DELIVERY_QUERY            "411520"                //交割单查询
//场外基金功能号定义
#define FUNCTIONID_FUNDCOMPANYQUERY            "417416"                //基金公司查询
#define FUNCTIONID_FUNDPURCHASE                "410801"                //基金申购认购
#define FUNCTIONID_FUNDREDEEM                "410803"                //基金赎回
#define FUNCTIONID_FUNDCANCEL                "410804"                //基金撤销
#define FUNCTIONID_FUNDCONVERT                "410805"                //基金转换业务(410805)
#define FUNCTIONID_SETSHARESTYPE            "410806"                //基金分红方式设置
#define FUNCTIONID_FUNDHOLDQUERY            "410809"                //基金的持仓信息
#define FUNCTIONID_FUNDQUOTATIONQUERY        "410810"                //基金行情查询
#define FUNCTIONID_ACCOUNTINFO                "410812"                //基金账号信息查询
#define    FUNCTIONID_CURRDAYTRADE                "410815"                //当日交易查询，即当日委托明细
#define FUNCTIONID_OPENACCOUNT                "410821"                //基金开户，包含账单寄送方式
#define FUNCTIONID_AIPSET                    "410847"                //基金定时定额账户设置
#define FUNCTIONID_AIPCANCEL                "410848"                //基金定时定额账户撤销
#define    FUNCTIONID_FUNDAIPQUERY                "410849"                //定时定额帐户查询(410849)  基金定投
#define FUNCTIONID_RESERVATIONCANCEL        "410862"                //基金预约委托撤消业务
#define FUNCTIONID_RESERVATIONTRADE            "410863"                //基金预约交易查询        对应冷静期查询
#define FUNCTIONID_RISKTOLERANCE            "410867"                //查询投资人风险承受能力结果
#define FUNCTIONID_SIGNECONTRACT            "410886"                //客户电子合同计划产品约定设置功能  客户电子合同签约
#define FUNCTIONID_ECONTRACT                "410887"                //客户电子合同签约情况查询
#define FUNCTIONID_ECFINANCING_SINGLSTATUS    "410888"                //客户电子合同理财计划产品签约情况查询
#define FUNCTIONID_ECFINANCING_VERIFY_QUERY "410889"                //客户电子合同理财计划签约核对情况查询
#define FUNCTIONID_FINANCING_FUNDINFO        "410890"                //理财基金信息查询(410890)
#define FUNCTIONID_UNSIGN_FINANCEINGPRODUCT "410895"                //客户未签署合同理财产品查询 (410895)
#define FUNCTIONID_ECTEXT                    "410896"                //电子合同文本查询
#define    FUNCTIONID_CUSTOMERRISKLEVEL        "99000120"                //客户适当评测结果，客户风险级别411015
#define FUNCTIONID_HISTORYORDERQUERY        "411808"                //基金历史 委托
#define FUNCTIONID_CUSTOMERADEQUACY            "411015"                //客户适当性信息查询
#define FUNCTIONID_RISKSIGN                    "411018"                //场外基金风险提示书签署流水
#define    FUNCTIONID_HISTORYDEALQUERY            "411811"                //基金历史成交查询
//网络投票
#define FUNCTIONID_VOTE_MEETING                "440001"                //网络投票股东大会信息查询
#define FUNCTIONID_VOTE_PROPOSAL            "440002"                //网络投票议案信息查询
#define FUNCTIONID_VOTE_RESULT                "440003"                //网络投票结果查询
#define FUNCTIONID_VOTE                        "440101"                //网络投票

//风险评测
#define FUNCTIONID_RISK_QUESTION_QUERY        "99000100"                //风险测评试题信息查询
#define FUNCTIONID_RISK_QUESTION_SUBMIT        "99000394"                //客户风险测评批量答题

//银证转账
#define FUNCTIONID_BANKTRANS_QUERY            "410608"                //银证转帐查询
#define FUNCTIONID_TRANSBANKINFO            "410211"                //转帐银行业务信息
#define FUNCTIONID_TRANSBANK_ACCOUNT_QUERY    "410601"                //获取开通转帐(银行)账号
#define FUNCTIONID_FUNDBALANCE                "410606"                //查询账户余额
#define FUNCTIONID_BANK_TRANSACTION            "410605"                //银证转帐

//新股申购
#define FUNCTIONID_NEWGOODSLIST_QUERY        "411549"                //当日新股清单查询
#define FUNCTIONID_NEWGOODSQUOTA_QUERY        "410610"                //外围新股申购市值额度表查询
#define FUNCTIONID_NEWGOODS_SUCCESS            "411547"                //市值配售中签查询
#define FUNCTIONID_NEWGOODS_HISSUCCESS        "411560"                //历史中签查询
#define FUNCTIONID_NEWGOODS_MATCHNUM        "411518"                //配号查询
#define FUNCTIONID_NEWGOODS_DEALNO_SET        "411548"                //市值配售交收顺序修改


//    统一的业务包
#define PID_TRADE_GATE__BIZFUN                (PID_TRADE_GATE_BASE+15)
struct STradeGateBizFun
{
    STradeGateUserInfo        userinfo;                //    用户信息，必送

    DWORD                    dwContentLen;            //非空时含'\0'
    DWORD                    reserve[4];

    char                    szContent[0];            //见 BizFun字符串例子
};

struct STradeGateBizFunA
{
    DWORD                    dwContentLen;
    DWORD                    reserve[4];

    char                    szContent[0];            //见 BizFun字符串例子
    //
    std::string toJSON(JNIEnv* env,const char* content)
    {
        cJSON *json = cJSON_CreateObject();
        //
        cJSON_AddNumberToObject(json,"dwContentLen",(int)dwContentLen);
        cJSON_AddStringToObject(json,"content",NewCodedString(env,content,"GB2312",strlen(content)));
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};


//    获取公共信息包 用和 PID_TRADE_GATE__BIZFUN 一样的结构
#define PID_TRADE_GATE__PUBLIC_INFO            (PID_TRADE_GATE_BASE+16)

#define OTCFUND_LIST_FILE            "PUB0001"
//BIZ字符串样例
/*BizFun字符串例子
 入参:
 PUBLICID=PUB0001&TBL_IN=filename,md5,position;otcfundlist.ini,,0;

 出参,单集合
 PUBLICID=PUB0001
 &TBL_OUT=filename,filecontent,position,totalsize; //totalsize编码前的文件长度
 otcfundlist.ini,xxxxxx,6;
 */

//////////////////////////////////////////////////////////////////////////



//资金查询 (410502)
#define PID_TRADE_GATE__FUNDS_QUERY            (PID_TRADE_GATE_BASE+20)
/*struct STradeGateFundsQuery
 {
 STradeGateUserInfo        userinfo;                        //    用户信息，必送

 __int64                    n64_fundid;                        //    资金账号    fundid        int    N    不送查询全部
 char                    sz_moneytype[MONEYTYPE_LEN];    //    货币        moneytype    char(1)    N    不送查询全部
 char                    sz_remark[7];                    //    备注        remark        char(6)    N
 };

 struct STradeGateFundsQueryAItem
 {
 __int64                    n64_custid;                    //    客户代码    custid            int
 __int64                    n64_fundid;                    //    资金账户    fundid            int
 char                    sz_orgid[ORGID_LEN];        //    机构编码    orgid            char(4)
 char                    sz_moneytype[MONEYTYPE_LEN];//    货币        moneytype    char(1)
 double                    db_fundbal;                    //    资金余额    fundbal    numeric(15,2)  用double刚刚能表示下
 double                    db_fundavl;                    //    资金可用金额    fundavl    numeric(15,2)
 double                    db_marketvalue;                //    资产总值    marketvalue    numeric(15,2)
 double                    db_fund;                    //    资金资产    Fund    numeric(15,2)
 double                    db_stkvalue;                //    市值    stkvalue    numeric(15,2)
 int                        n_fundseq;                    //    主资金标志    fundseq    int
 double                    db_fundloan;                //    融资总金额    fundloan    numeric(15,2)
 double                    db_fundbuy;                    //    买入冻结    fundbuy    numeric(15,2)
 double                    db_fundsale;                //    卖出解冻    fundsale    numeric(15,2)
 double                    db_fundfrz;                    //    冻结总金额    fundfrz    numeric(15,2)
 double                    db_fundlastbal;                //    昨日余额    fundlastbal    numeric(15,2)
 };
 struct STradeGateFundsQueryA
 {
 DWORD                        dwCount;
 STradeGateFundsQueryAItem    pItem[0];
 };*/

//外围资金帐户资金情况查询(410505)
#define PID_TRADE_GATE__EXTERNAL_FUNDS_QUERY    (PID_TRADE_GATE_BASE+21)

/*struct    SExternalFundsQuery
 {
 STradeGateUserInfo                userinfo;                                //用户信息
 __int64                            n64_fundid;                                //资金账号    fundid    int    N    不送查询全部
 char                            sz_moneytype[MONEYTYPE_LEN];            //货币    moneytype    char(1)    N    不送查询全部
 };


 struct    SExternalFundsQueryAItem
 {
 __int64                            n64_custid;                                //客户代码    custid    int
 char                            sz_custname[17];                        //客户姓名    custname    CHAR(16)
 __int64                            n64_fundid;                                //资金账户    fundid    int
 char                            sz_orgid[ORGID_LEN];                    //机构编码    orgid    char(4)
 char                            sz_moneytype[MONEYTYPE_LEN];            //货币    moneytype    char(1)
 double                            db_fundavl;                                //资金可用    fundavl    numeric(19,2)
 double                            db_fundbal;                                //资金余额    fundbal    numeric(19,2)
 };


 struct    SExternalFundsQueryA
 {
 DWORD        dwCount;
 SExternalFundsQueryAItem        pItem[0];
 };
 */

//股份汇总查询(410504)
#define PID_TRADE_GATE__HOLD_QUERY                (PID_TRADE_GATE_BASE+30)
// struct STradeGateHoldQuery
// {
//     STradeGateUserInfo            userinfo;                //    用户信息，必送
//
//
//     char                        sz_market[MARKET_LEN];    //     交易市场    market    char(1)        N    不送查询全部
//     char                        sz_secuid[SECUID_LEN];    //     股东代码    secuid    char(10)    N    不送查询全部
//     char                        sz_stkcode[9];            //     证券代码    stkcode    char(8)        N    不送查询全部
//     __int64                        n64_fundid;                //     资金帐户    fundid    __int64        N    不送查询全部
// };
//
// struct STradeGateHoldQueryAItem
// {
//     __int64                        n64_custid;                            //客户代码    custid    int
//     char                        sz_market[MARKET_LEN];                //交易市场    market    char(1)
//     char                        sz_stkname[STKNAME_LEN];            //证券名称    stkname    char(8)
//     char                        sz_stkcode[STKCODE_LEN];            //证券代码    stkcode    char(8)
//     char                        sz_moneytype[MONEYTYPE_LEN];        //货币    moneytype    char(1)
//     int                            n_stkbal;                            //股份余额    stkbal    int
//     int                            n_stkavl;                            //股份可用    stkavl    int
//     double                        db_buycost;                            //当前成本    buycost    numeric(15,2)
//     double                        db_costprice;                        //成本价格    costprice    numeric(9,3)
//     double                        db_mktval;                            //市值    mktval    numeric(15,2)
//     double                        db_income;                            //盈亏    income    numeric(15,2)
//     char                        sz_mtkcalflag[2];                    //市值计算标识    mtkcalflag    char(1)
//     int                            n_stkqty;                            //当前拥股数    stkqty    int
//     double                        db_lastprice;                        //最新价格    lastprice    numeric(9,3)
//     char                        sz_stktype[2];                        //证券类型    stktype    char(1)
//     double                        db_proincome;                        //参考盈亏    proincome    numeric(15,2)
//     double                        db_profitcost;                        //参考成本    profitcost    numeric(15,2)
//     double                        db_profitprice;                        //参考成本价    profitprice    numeric(15,2)
// };
// struct STradeGateHoldQueryA
// {
//     DWORD                        dwCount;
//     STradeGateHoldQueryAItem    pItem[0];
// };

//当日委托查询(410510)
#define PID_TRADE_GATE__ORDER_QUERY                (PID_TRADE_GATE_BASE+40)
// struct STradeGateOrderQuery
// {
//     STradeGateUserInfo            userinfo;                //    用户信息，必送
//     char                        sz_market[MARKET_LEN];    //     交易市场        market            char(1)        N    不送查询全部
//     __int64                        n64_fundid;                //     资金帐户    fundid    int    N
//     char                        sz_secuid[SECUID_LEN];    //     股东代码        secuid            char(10)    N    不送查询全部
//     char                        sz_stkcode[STKCODE_LEN];//     证券代码        stkcode            char(8)        N    不送查询全部
//     int                            n_ordersno;                //     委托序号    ordersno    int    N    不送查询全部
//     int                            n_Ordergroup;            //     委托批号    Ordergroup    Int    N    不送查询全部
//     char                        sz_bankcode[5];            //     外部银行        bankcode        char(4)        N    三方交易时送
//     char                        sz_qryflag[QRYFLAG_LEN];//     查询方向        qryflag            char(1)     Y    向下/向上查询方向
//     int                            n_count;                //     请求行数    count    int     Y    每次取的行数
//     char                        sz_poststr[POSTSTR_LEN];//     定位串            poststr            char(32)    Y    第一次填空
//     char                        sz_extsno[33];            //     外部流水号        extsno            char(32)    N
//     char                        sz_qryoperway[2];        //     委托渠道        qryoperway        char(1)        N
// };
//
// struct STradeGateOrderQueryAItem
// {
//     char                        sz_poststr[POSTSTR_LEN];    //     定位串            poststr            char(32)
//     int                            n_orderdate;                //     委托日期        orderdate        int
//     int                            n_ordersno;                    //     委托序号        ordersno        int
//     int                            n_Ordergroup;                //     委托批号        Ordergroup        Int
//     __int64                        n64_custid;                    //     客户代码        custid            int
//     char                        sz_custname[17];            //     客户姓名        custname        char(16)
//     __int64                        n64_fundid;                    //     资金账户        fundid            int
//     char                        sz_moneytype[MONEYTYPE_LEN];//     货币            moneytype        char(1)
//     char                        sz_orgid[ORGID_LEN];        //     机构编码        orgid            char(4)
//     char                        sz_secuid[SECUID_LEN];        //     股东代码        secuid            char(10)
//     char                        sz_bsflag[3];                //     买卖类别        bsflag            char(2)    为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                        sz_orderid[11];                //     申报合同序号    orderid            char(10)
//     int                            n_reporttime;                //     报盘时间        reporttime        int
//     int                            n_opertime;                    //     委托时间        opertime        int    格式为HHMMSS
//     char                        sz_market[MARKET_LEN];        //     交易市场        market            char(1)
//     char                        sz_stkcode[STKCODE_LEN];    //     证券名称        stkcode            char(8)
//     char                        sz_stkname[STKNAME_LEN];    //     证券代码        stkname            char(8)
//     char                        sz_prodcode[13];            //     产品编码        prodcode        char(12)
//     char                        sz_prodname[65];            //     产品名称        prodname        char(64)
//     double                        db_orderprice;                //     委托价格        orderprice        numeric(9,3)
//     int                            n_orderqty;                    //     委托数量        orderqty        int
//     double                        db_orderfrzamt;                //     冻结金额        orderfrzamt        numeric(15,2)
//     int                            n_matchqty;                    //     成交数量        matchqty        int
//     double                        db_matchamt;                //     成交金额        matchamt        numeric(15,2)
//     int                            n_cancelqty;                //     撤单数量        cancelqty        int
//     char                        sz_orderstatus[2];            //     委托状态        orderstatus        char(1)
//     char                        sz_seat[7];                    //     交易席位        seat            char(6)
//     char                        sz_cancelflag[2];            //     撤单标识        cancelflag        char(1)
//     int                            n_operdate;                    //     操作日期        operdate        Int
//     double                        db_bondintr;                //     债券应计利息    bondintr        numeric(12,8)    报价回购业务记录提前终止收益率
//     char                        sz_operway[2];                //     委托渠道        operway            char(1)
//     char                        sz_remark[65];                //     备注信息        remark            char(64)
// };
// struct STradeGateOrderQueryA
// {
//     DWORD                        dwCount;
//     STradeGateOrderQueryAItem    pItem[0];
// };

//当日成交查询(410512)
#define PID_TRADE_GATE__DEAL_QUERY                (PID_TRADE_GATE_BASE+50)
// struct STradeGateDealQuery
// {
//     STradeGateUserInfo            userinfo;                //    用户信息，必送
//     __int64                        n64_fundid;                //     资金帐户        fundid            Char(16)    N    不送查询全部
//     char                        sz_market[MARKET_LEN];    //    交易市场        market            char(1)        N    不送查询全部
//     char                        sz_secuid[SECUID_LEN];    //     股东代码        secuid            char(10)    N    不送查询全部
//     char                        sz_stkcode[STKCODE_LEN];//     证券代码        stkcode            char(8)        N    不送查询全部
//     char                        n_ordersno;                //     委托序号    ordersno    int    N    不送查询全部
//     char                        sz_bankcode[5];            //     外部银行        bankcode        char(4)        N
//     char                        sz_qryflag[QRYFLAG_LEN];//     查询方向        qryflag            char(1)     Y    向下/向上查询方向
//     int                            n_count;                //     请求行数    count    int     Y    每次取的行数
//     char                        sz_poststr[POSTSTR_LEN];//     定位串            poststr            char(32)    Y    第一次填空
//     char                        sz_qryoperway[2];        //     委托渠道        qryoperway    char(1)        N
// };
//
// struct STradeGateDealQueryAItem
// {
//     char                    sz_poststr[POSTSTR_LEN];    //     定位串            poststr            char(32)    查询返回定位值
//     int                        n_trddate;                    //     成交日期        trddate            int
//     char                    sz_secuid[SECUID_LEN];        //     股东代码        secuid            char(10)
//     char                    sz_bsflag[3];                //     买卖类别        bsflag            char(2)    为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     int                        n_ordersno;                    //     委托序号        ordersno        int
//     char                    sz_orderid[11];                //     申报合同序号    orderid            char(10)
//     char                    sz_market[MARKET_LEN];        //     交易市场        market            char(1)
//     char                    sz_stkname[STKNAME_LEN];    //     证券代码        stkname            char(8)
//     char                    sz_stkcode[STKCODE_LEN];    //     证券名称        stkcode            char(8)
//     char                    sz_prodcode[13];            //     产品编码        prodcode        char(12)
//     char                    sz_prodname[65];            //     产品名称        prodname        char(64)
//     int                        n_matchtime;                //     成交时间        matchtime        int    不足8位时前补0，格式为HHMMSSZZ
//     char                    sz_matchcode[21];            //     成交序号        matchcode        int
//     double                    db_matchprice;                //     成交价格        matchprice        numeric(9,3)
//     int                        n_matchqty;                    //     成交数量        matchqty        int
//     double                    db_matchamt;                //     成交金额        matchamt        numeric(15,2)
//     char                    sz_matchtype[2];            //     成交类型        matchtype        char(1)    '0'普通成交,'1'撤单成交,'2'废单,'3'内部撤单,'4'撤单废单
//     int                        n_orderqty;                    //     委托数量        orderqty        int
//     double                    db_orderprice;                //     委托价格        orderprice        numeric(9,3)
//     double                    db_bondintr;                //     债券应计利息    bondintr        numeric(12,8)    报价回购业务记录提前终止收益率
// };
//
// struct STradeGateDealQueryA
// {
//     DWORD                        dwCount;
//     STradeGateDealQueryAItem    pItem[0];
// };

//委托买卖业务(410411)
#define PID_TRADE_GATE__ORDER                (PID_TRADE_GATE_BASE+60)
/*struct STradeGateOrder
 {
 STradeGateUserInfo            userinfo;                        //    用户信息，必送
 char                        sz_market[MARKET_LEN];            //    交易市场    market    char(1)    Y
 char                        sz_secuid[SECUID_LEN];            //     股东代码        secuid            char(10)    Y
 __int64                        n64_fundid;                        //     资金账户        fundid            Char(16)    Y
 char                        sz_stkcode[STKCODE_LEN];        //     证券代码        stkcode            char(8)    Y
 char                        sz_bsflag[3];                    //     买卖类别        bsflag            char(2)    Y
 double                        db_price;                        //     价格            price            numeric(9,3)    Y

 int                            n_qty;                            //     数量    qty    int    Y
 int                            n_ordergroup;                    //     委托批号    ordergroup    int    Y    0,
 char                        sz_bankcode[5];                    //     外部银行        bankcode        char(4)    N    三方交易时送
 char                        sz_creditid[2];                    //     信用产品标识    creditid        Char(1)    N
 char                        sz_creditflag[2];                //     特殊委托类型    creditflag        Char(1)    N
 char                        sz_remark[65];                    //     备注信息        remark            char(64)    N
 char                        sz_targetseat[7];                //     对方席位        targetseat        Char(6)    N    Bsflag为1I,1J,1X,1Y时必需填写
 char                        sz_promiseno[9];                //     约定号            promiseno        Char(8)    N
 int                            n_risksno;                        //     风险调查流水号    risksno            Int        N    调用410567业务获取
 char                        sz_autoflag[2];                    //     自动展期标志    autoflag        Char(1)    N    报价回购买入业务才送，控制晚上交收生成的合约能否自动展期   0 不自动展期,1 自动展期
 int                            n_enddate;                        //     展期终止日期    enddate            int    N    当autoflag设置为1时才送终止日期   当bsflag为1R,1S时，送"失效日期"
 char                        sz_linkman[13];                    //     联系人            linkman            Char(12)    N    联系人,深圳综合协议意向和定价委托要输入,柜台债券意向委托要输入
 char                        sz_linkway[31];                    //     联系方式        linkway            Char(30)    N    联系方式,深圳综合协议意向和定价委托要输入,柜台债券意向委托要输入
 char                        sz_linkmarket[MARKET_LEN];        //     关联市场        linkmarket        Char(1)    N    ETF实物申赎时送
 char                        sz_linksecuid[SECUID_LEN];        //     关联股东        linksecuid        Char(10)    N    ETF实物申赎时送
 char                        sz_sorttype[4];                    //     品种类别        sorttype        char(3)    N    约定购回初始交易时送
 int                            n_mergematchcode;                //     合并管理的初始交易的成交编号    mergematchcode    int    N
 int                            n_mergematchdate;                //     合并管理的初始交易的成交日期    mergematchdate    int    N
 char                        sz_oldorderid[25];                //     原合同序号        oldorderid        char(24)    N    当bsflag为1X,1Y，且是意向转成交时必须填
 char                        sz_prodcode[13];                //     产品编码        prodcode        Char(12)    N    6位的证券代码 + 3位购回天数 + 3位期号（报价回购使用）
 char                        sz_pricetype[2];                //     报价类型        pricetype        Char(1)    N    盘后定价大宗交易报价类型    1：当日收盘价,    2：加权平均价

 char                        sz_blackflag[2];                //是否允许购买黑名单证券    blackflag    Char(1)    N    1：允许购买 0：不允许购买，默认为0
 char                        sz_dzsaletype[2];                //    减持类型    dzsaletype    Char(1)    N    大宗交易股份减持类型。0：非特定股份减持1：特定股份减持

 __int64                        n64_risksignsno;                //风险揭示签署流水号    risksignsno    int64    N    调用411017获取

 };
 struct STradeGateOrderA
 {
 int                        n_ordersno;                            //     委托序号        ordersno        int            返回给股民
 char                    sz_orderid[11];                        //     合同序号        orderid            char(10)    返回给股民
 int                        n_ordergroup;                        //     委托批号        ordergroup        int            返回给股民
 };

 */

//委托撤单(410413)
#define PID_TRADE_GATE__CANCEL            (PID_TRADE_GATE_BASE+70)
// struct STradeGateCancel
// {
//     STradeGateUserInfo        userinfo;                    //    用户信息，必送
//     int                        n_orderdate;                //     委托日期    orderdate    int    Y
//     __int64                    n64_fundid;                    //     资金帐户    fundid    int    Y
//     int                        n_ordersno;                    //     委托序号    ordersno    int    Y    委托返回的
//     char                    sz_bsflag[3];                //     买卖类别    bsflag    char(2)    N    柜台债券意向委托撤单要求输入(2位)     1R 柜台意向买,1S 柜台意向卖
// };
// struct STradeGateCancelA
// {
//     char                    sz_msgok[33];                //     msgok            成功信息    char(32)
//     char                    sz_cancel_status[2];        //     cancel_status    内部撤单标志    char(1)    1:内部撤单,    非1:普通撤单
//     int                        n_ordersno;                    //     ordersno        撤单委托序号    int
// };


//证券信息查询(410203)
#define PID_TRADE_GATE__STOCK_QUERY        (PID_TRADE_GATE_BASE+80)
// struct STradeGateStockQuery
// {
//     STradeGateUserInfo            userinfo;                                    //    用户信息，必送
//     char                        sz_market[MARKET_LEN];                        //     交易市场        market            char(1)    N    不输入查全部
//     char                        sz_stklevel[2];                                //     证券级别        stklevel        char(1)    N    见字典项Ezqjb定义,其中'Z'表示为交易所标示退市证券类别;不输入查全部
//     char                        sz_stkcode[STKCODE_LEN];                    //     证券代码        stkcode            char(8)    N    不输入查全部
//     char                        sz_poststr[POSTSTR_LEN];                    //  定位串            poststr    char(32)    N        第一次不用输入，只有证券代码未输入的时候需要输入
//     int                            n_rowcount;                                    //  查询行数        rowcount    int    N    默认为100行，输入大于100也是输出100行
//     char                        sz_stktype[2];                                //    证券类别        stktype    char(1)    N    不输入查全部。字典项Ezqlb
// };
// struct STradeGateStockQueryAItem
// {
//     char                        sz_poststr[POSTSTR_LEN];                    //  定位串            poststr    char(32)    N        只有证券代码未输入的时候需要输入
//     char                        sz_market[MARKET_LEN];                        //     交易市场        market            char(1)
//     char                        sz_moneytype[2];                            //     货币            moneytype        char(1)
//     char                        sz_stkname[STKNAME_LEN];                    //     证券名称        stkname            char(8)
//     char                        sz_stkcode[STKCODE_LEN];                    //     证券代码        stkcode            char(8)
//     char                        sz_stktype[2];                                //     证券类别        stktype            char（1）
//     int                            n_priceunit;                                //     价位            priceunit        int        1:代表0.001元，10:代表0.01元
//     double                        db_maxrisevalue;                            //     涨停价格        maxrisevalue    numeric(9,3)
//     double                        db_maxdownvalue;                            //     跌停价格        maxdownvalue    numeric(9,3)
//     char                        sz_stopflag[2];                                //     停牌标志        stopflag        char(1)
//     char                        sz_mtkcalflag[2];                            //     市值计算标识    mtkcalflag        char(1)
//     double                        db_bondintr;                                //     债券应计利息    bondintr        numeric(15,8)
//     int                            n_maxqty;                                    //     最高数量        maxqty            dtint
//     int                            n_minqty;                                    //     最低数量        minqty            dtint
//     int                            n_buyunit;                                    //     买入最小单位    buyunit            dtint
//     int                            n_saleunit;                                    //     卖出最小单位    saleunit        dtint
//     char                        sz_stkstatus[2];                            //     证券状态        stkstatus        char(1)
//     char                        sz_stklevel[2];                                //     证券级别        stklevel        char(1)
//     char                        sz_trdid[2];                                //     交易类型        trdid            char(1)
//     int                            n_quitdate;                                    //     退市证券交易截止日期    quitdate    dtint
//     double                        db_fixprice;                                //    系统定价    fixprice    numeric(9,3)
//     char                        sz_priceflag[2];                            //    委托价格标志    priceflag    char(1)    '0' 客户委托价 '1' 系统定价,忽略用户委托价 '2' 系统定价,检查用户委托价
//     char                        sz_memotext[129];                            //     退市证券提示信息    memotext    char(128)
// };
// struct STradeGateStockQueryA
// {
//     DWORD                        dwCount;
//     STradeGateStockQueryAItem    pItem[0];
// };

//订阅成交回报     从起始点开始，如果推送的成交比起点大，收到查询一次
#define PID_TRADE_GATE__DEAL_SUBSCRIBE        (PID_TRADE_GATE_BASE+100)
// struct STradeGateDealSubscribe
// {
//     char chPos[32];                                                            //     定位串            poststr            char(32)    查询返回定位值
// };
// struct STradeGateDealSubscribeA
// {
//     BOOL    bSucessed;//?
// };

//推送的成交回报
#define PID_TRADE_GATE__DEAL_PUSH        (PID_TRADE_GATE_BASE+110)
// typedef STradeGateDealQueryAItem    STradeGateDealPushAItem;
// struct STradeGateDealPushA
// {
//     DWORD                        dwCount;
//     STradeGateDealPushAItem        pItem[0];
// };


//历史委托明细查询(411511)
#define PID_TRADE_GATE__ORDER_HISTORY        (PID_TRADE_GATE_BASE+120)
// struct STradeGateOrderHistory
// {
//     STradeGateUserInfo        userinfo;                                        //    用户信息，必送
//
//     int                        n_strdate;                                        // 起始日期    strdate    int    Y
//     int                        n_enddate;                                        // 终止日期    enddate    int    Y
//
//     __int64                    n64_fundid;                                        // 资金帐户            fundid            Char(16)    Y
//     char                    sz_market[MARKET_LEN];                            // 交易市场            market            char(1)        Y    送空或送空格查询全部
//     char                    sz_secuid[SECUID_LEN];                            // 股东代码            secuid            char(10)    Y    送空查询全部
//     char                    sz_stkcode[STKCODE_LEN];                        // 证券代码            stkcode            char(8)        Y    送空查询全部
//     int                        n_ordersno;                                        // 委托序号    ordersno    int    Y    送小于等于0查询全部
//     int                        n_Ordergroup;                                    // 委托批号    Ordergroup    Int    Y    送小于等于0查询全部
//
//     char                    sz_bankcode[5];                                    // 外部银行            bankcode        char(4)        Y    三方交易时送，送空查询全部
//     char                    sz_qryflag[QRYFLAG_LEN];                        // 查询方向            qryflag            char(1)     Y    向下/向上查询方向
//     int                        n_count;                                        //请求行数    count    int     Y    每次取的行数
//     char                    sz_poststr[POSTSTR_LEN];                        // 定位串            poststr            char(32)    Y    第一次填空
//     char                    sz_extsno[33];                                    // 外部流水号        extsno            char(32)    N
//     char                    sz_qryoperway[2];                                // 委托渠道            qryoperway        char(1)        N
// };
// struct STradeGateOrderHistoryAItem
// {
//     char                    sz_poststr[POSTSTR_LEN];                        //     定位串            poststr            char(32)    查询返回定位值
//     int                        n_orderdate;                                    //     委托日期        orderdate        int
//     __int64                    n64_custid;                                        //     客户代码        custid            int
//     char                    sz_custname[17];                                //     客户姓名        custname        char(16)
//     __int64                    n64_fundid;                                        //     资金账户        fundid            int
//     char                    sz_moneytype[2];                                //     货币            moneytype        char(1)
//     char                    sz_orgid[ORGID_LEN];                            //     机构编码        orgid            char(4)
//     char                    sz_secuid[SECUID_LEN];                            //     股东代码        secuid            char(10)
//     char                    sz_bsflag[3];                                    //     买卖类别        bsflag            char(2)    为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                    sz_orderid[11];                                    //     申报合同序号    orderid            char(10)
//     int                        n_Ordergroup;                                    //     委托批号        Ordergroup        Int
//     int                        n_reporttime;                                    //     报盘时间        reporttime        int
//     int                        n_opertime;                                        //     委托时间        opertime        int    不足8位时前补0，格式为HHMMSSZZ
//     char                    sz_market[MARKET_LEN];                            //     交易市场        market            char(1)
//     char                    sz_stkcode[STKCODE_LEN];                        //     证券名称        stkcode            char(8)
//     char                    sz_stkname[STKNAME_LEN];                        //     证券代码        stkname            char(8)
//     char                    sz_prodcode[13];                                //     产品编码        prodcode        char(12)
//     double                    db_orderprice;                                    //     委托价格        orderprice        numeric(9,3)
//     int                        n_orderqty;                                        //     委托数量        orderqty        int
//     double                    db_orderfrzamt;                                 //     冻结金额        orderfrzamt        numeric(15,2)
//     int                        n_matchqty;                                        //     成交数量        matchqty        int
//     double                    db_matchamt;                                    //     成交金额        matchamt        numeric(15,2)
//     int                        n_cancelqty;                                    //     撤单数量        cancelqty        int
//     char                    sz_cancelflag[2];                                //     撤单标志        cancelflag        char(1)
//     char                    sz_orderstatus[2];                                //     委托状态        orderstatus        char(1)
//     char                    sz_seat[7];                                        //     交易席位        seat            char(6)
//     double                    db_matchprice;                                    //     成交价格        matchprice        numeric(15,2)
//     int                        n_operdate;                                        //     操作日期        operdate        Int
//     double                    db_bondintr;                                    //     债券应计利息    bondintr        numeric(12,8)    报价回购业务记录提前终止收益率
//     char                    sz_operway[2];                                    //     操作方式        operway            char(1)
//     //char                    sz_qryoperway[2];                                //     委托渠道        qryoperway            char(1)
//     char                    sz_remark[65];                                    //     备注信息        remark            char(64)
//
// };
// struct STradeGateOrderHistoryA
// {
//     DWORD                        dwCount;
//     STradeGateOrderHistoryAItem    pItem[0];
// };

//历史成交明细查询(411513)
#define PID_TRADE_GATE__DEAL_HISTORY        (PID_TRADE_GATE_BASE+130)
// struct STradeGateDealHistory
// {
//     STradeGateUserInfo            userinfo;                                    //    用户信息，必送
//     int                            n_strdate;                                    //     起始日期        strdate            int    Y
//     int                            n_enddate;                                    //     终止日期        enddate            int    Y
//     __int64                        n64_fundid;                                    //     资金帐户        fundid            Char(16)    N    不送查询全部
//     char                        sz_market[MARKET_LEN];                        //     交易市场        market            char(1)        N    不送查询全部
//     char                        sz_secuid[SECUID_LEN];                        //     股东代码        secuid            char(10)    N    不送查询全部
//     char                        sz_stkcode[STKCODE_LEN];                    //     证券代码            stkcode            char(8)        N    不送查询全部
//     char                        sz_bankcode[5];                                //     外部银行        bankcode        char(4)        N
//     char                        sz_qryflag[QRYFLAG_LEN];                    //     查询方向        qryflag            char(1)     Y    向下/向上查询方向
//     int                            n_count;                                    //     请求行数        count                int    Y    每次取的行数
//     char                        sz_poststr[POSTSTR_LEN];                    //     定位串          poststr            char(32)    Y    第一次填空
// };
// struct STradeGateDealHistoryAItem
// {
//     char                    sz_poststr[POSTSTR_LEN];                        //     定位串            poststr            char(32)    查询返回定位值
//     int                        n_bizdate;                                        //     交收日期        bizdate            int
//     int                        n_cleardate;                                    //     成交日期        cleardate        int
//     char                    sz_secuid[SECUID_LEN];                            //     股东代码        secuid            char(10)
//     char                    sz_bsflag[3];                                    //     买卖类别        bsflag            char(2)    为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                    sz_orderid[11];                                    //     申报合同序号    orderid            char(10)
//     int                        n_ordersno;                                        //     委托序号        ordersno        int
//     char                    sz_market[MARKET_LEN];                            //     交易市场        market            char(1)
//     char                    sz_stkcode[STKCODE_LEN];                        //     证券名称        stkcode            char(8)
//     char                    sz_stkname[STKNAME_LEN];                        //     证券代码        stkname            char(8)
//     char                    sz_prodcode[13];                                //     产品编码        prodcode        char(12)
//     int                        n_matchtime;                                    //     成交时间        matchtime        int    格式为HHMMSS
//     char                    sz_matchcode[21];                                //     成交序号        matchcode        char(20)
//     double                    db_matchprice;                                    //     成交价格        matchprice        numeric(9,3)
//     int                        n_matchqty;                                        //     成交数量        matchqty        int
//     double                    db_matchamt;                                    //     成交金额        matchamt        numeric(15,2)
//     int                        n_orderqty;                                        //     委托数量        orderqty        int
//     double                    db_orderprice;                                    //     委托价格        orderprice        numeric(9,3)
//     int                        n_stkbal;                                        //     股份本次余额    stkbal            int
//     double                    db_fee_jsxf;                                    //     净手续费        fee_jsxf        numeric(15,2)
//     double                    db_fee_sxf;                                        //     手续费            fee_sxf            numeric(15,2)
//     double                    db_fee_yhs;                                        //     印花税            fee_yhs            numeric(15,2)
//     double                    db_fee_ghf;                                        //     过户费            fee_ghf            numeric(15,2)
//     double                    db_fee_qsf;                                        //     清算费            fee_qsf            numeric(15,2)
//     double                    db_fee_jygf;                                    //     交易规费        fee_jygf        numeric(15,2)
//     double                    db_fee_jsf;                                        //     经手费            fee_jsf            numeric(15,2)
//     double                    db_fee_zgf;                                        //     证管费            fee_zgf            numeric(15,2)
//     double                    db_fee_qtf;                                        //     其他费            fee_qtf            numeric(15,2)
//     double                    db_feefront;                                    //     前台费用        feefront        numeric(15,2)
//     double                    db_fundeffect;                                    //     资金发生数        fundeffect        numeric(15,2)
//     double                    db_fundbal;                                        //     使用金额        fundbal            numeric(15,2)
//     double                    db_bondintr;                                    //     债券应计利息    bondintr        numeric(12,8)    报价回购业务记录提前终止收益率
// };
// struct STradeGateDealHistoryA
// {
//     DWORD                        dwCount;
//     STradeGateDealHistoryAItem    pItem[0];
// };
//

//未到期回购查询(410545)
#define PID_TRADE_GATE__UNEXPIREDREPURCHASE_HISTORY        (PID_TRADE_GATE_BASE+140)

// struct STradeGateUnexpiredRepurchaseQuery
// {
//     STradeGateUserInfo            userinfo;                                    //    用户信息，必送
//     char                        sz_orgid[ORGID_LEN];                        // 机构编码    orgid    char(4)    Y
//     __int64                        n64_fundid;                                    // 资金帐户    fundid    int    Y
//     char                        sz_market[MARKET_LEN];                        // 交易市场    market    char(1)    N    不送查询全部
//     char                        sz_secuid[SECUID_LEN];                        // 股东代码    secuid    char(10)    N    不送查询全部
//     char                        sz_stkcode[STKCODE_LEN];                    // 证券代码    stkcode    char(8)    N    不送查询全部
//     char                        sz_datetype[2];                                // 日期类型    datetype    char(1)    N    不送查询全部// 0-    按委托日期    // 1-    1按购回日期
//     int                            n_begindate;                                // 开始日期    begindate    int    N    不送查询全部
//     int                            n_enddate;                                    // 结束日期    enddate    int    N    不送查询全部
//     int                            n_count;                                    // 请求行数    count    int     Y    每次取的行数
//     char                        sz_poststr[POSTSTR_LEN];                    // 定位串      poststr    char(32)    Y    第一次填空
// };
//
// struct STradeGateUnexpiredRepurchaseQueryAItem
// {
//     char                        sz_poststr[POSTSTR_LEN];                    // 定位串      poststr    char(32)
//     char                        sz_orgid[5];                                // 机构编码    orgid    char(4)
//     __int64                        n64_fundid;                                    // 资金账号    fundid    int
//     __int64                        n64_custid;                                    // 客户代码    custid    int
//     char                        sz_custname[17];                            // 客户姓名    custname    char(16)
//     char                        sz_moneytype[2];                            // 货币    moneytype    char(1)
//     char                        sz_market[MARKET_LEN];                        // 交易市场    market    char(1)
//     char                        sz_stkname[STKNAME_LEN];                    // 证券代码    stkname    char(8)
//     char                        sz_stkcode[STKCODE_LEN];                    // 证券名称    stkcode    char(8)
//     char                        sz_seat[7];                                    // 交易席位    seat    char(6)
//     char                        sz_secuid[SECUID_LEN];                        // 股东代码    secuid    char(10)
//     int                            n_orderdate;                                // 委托日期    orderdate    int
//     int                            n_matchdate;                                // 购回日期    matchdate    int
//     char                        sz_orderid[11];                                // 合同序号    orderid    char(10)
//     char                        sz_matchcode[21];                            // 成交号码    matchcode    char
//     double                        db_orderprice;                                // 委托价格    orderprice    numeric(9,3)
//     int                            n_orderqty;                                    // 委托数量    orderqty    int
//     double                        db_matchprice;                                // 成交价格    matchprice    numeric(9,3)
//     int                            n_matchqty;                                    // 成交数量    matchqty    int
//     double                        db_matchamt;                                // 成交金额    matchamt    numeric(15,2)
//     double                        db_clearedamt;                                // 购回金额    clearedamt    numeric(15,2)
//     char                        sz_brhid[5];                                // 机构分支    brhid    char(4)
//     char                        sz_stktype[2];                                // 证券类别    stktype    char(1)
//     char                        sz_bsflag[3];                                // 买卖类别    bsflag    char(2)
//     char                        sz_trdid[2];                                // 交易类型    trdid    char(1)
//     char                        sz_busitype[5];                                // 业务类别    busitype    char(4)
//     char                        sz_operway[2];                                // 操作方式    operway    char(1)
//     char                        sz_status[2];                                // 状态    status    char(1)
//     double                        db_fee_sxf;                                    // 购回交易佣金    fee_sxf    numeric(12,2)
//     char                        sz_breakflag[2];                            // 违约标志    breakflag    char(1)
//     double                        db_discratio;                                // 折扣比例    discratio    numeric(12,8)
// };
//
// struct STradeGateUnexpiredRepurchaseQueryA
// {
//     DWORD                        dwCount;
//     STradeGateUnexpiredRepurchaseQueryAItem pItem[0];
// };

/**************************************************************************************
 //银证业务数据包定义
 ****************************************************************************************/

//银证转帐(410605)
#define PID_TRADE_GATE__STKBANK_TRANSACTION        (PID_TRADE_GATE_BASE+150)

// struct SStockBankTransaction
// {
//     STradeGateUserInfo            userinfo;                                    //    用户信息，必送
//
//     __int64                        n64_fundid;                                    //资金账号    fundid    int    Y
//     char                        sz_moneytype[MONEYTYPE_LEN];                // 货币    moneytype    char(1)        货币代码    moneytype    char(1)    Y
//     char                        sz_fundpwd[33];                                //资金密码    fundpwd    char(32)    N    密码需要加密
//     char                        sz_bankcode[5];                                //银行代码    bankcode    char(4)    Y
//     char                        sz_bankpwd[33];                                //银行密码    bankpwd    char(32)    N    明文
#define TRANSTYPE_BANKTOSTK        '1'
#define TRANSTYPE_STKTOBANK        '2'
//     char                        sz_banktrantype[2];                            //转帐类型    banktrantype    char(1)    Y    1：银行转证券        2：证券转银行
//     double                        db_tranamt;                                    //转帐金额    tranamt    numeric(15,2)    Y     //单位: 元
#define PWDFLAG_UNCHECK        '0'
#define PWDFLAG_CHECK            '1'
//     char                        sz_pwdflag[2];                                //资金密码校验标志    pwdflag    char(1)    N    对于证券转银行,0表示不校验资金密码,1表示校验资金密码,不送默认为1
//     char                        sz_extsno[33];                                //外部流水号    extsno    char(32)    N    外部流水号
// };
//
//
// struct SStockBankTransactionA
// {
//     int                            n_sno;                                            //委托序号    sno    int
//     int                            n_syserrid;                                        //错误代码    syserrid    int
//     char                        sz_errmsg[65];                                    //错误信息    errmsg    char(64)
// };

//转帐银行业务信息(410211) 功能代码    410211  功能描述    银行业务信息
#define PID_TRADE_GATE__TRANSFERBANK_BUSIINFO_QUERY  (PID_TRADE_GATE_BASE+151)

/*一定需要机构编码,为各分支处理各自的银行接口
 发起方： '0' 证券发起 '1' 银行发起 '2' 双方发起
 转帐方式：1－银行转证券   2－证券转银行  3－查证券余额  4－查银行余额
 5－冲银行转证券  6－冲证券转银行  7－开户  8－销户  M－转帐调整
 资金密码校验： 0 不校验 1 校验资金密码 2 校验交易密码 3 券商发起检验 4 银行发起检验
 银行密码校验： 0 不校验 1 校验
 银行帐号校验： 0 不校验 1 校验
 证件校验标志： 0 不校验 1 校验
 */
// struct    STransferBankBusiInfoQuery
// {
//     STradeGateUserInfo                userinfo;                                    //用户信息
//     char                            sz_moneytype[MONEYTYPE_LEN];                //货币代码    moneytype    char(1)    N    不输查询所有币种转帐银行
//     char                            sz_bankcode[5];                                //银行代码    bankcode    char(4)    N    不输查询机构全部转帐银行
// };
//
// struct    STransferBankBusiInfoQueryAItem
// {
//     char                            sz_bankcode[5];                                //银行代码    bankcode    char(4)
//     char                            sz_sourcetype[2];                            //发起方    sourcetype    char(1)
//     char                            sz_banktrantype[2];                            //转帐方式    banktrantype    char(1)
//     char                            sz_fundpwdflag[2];                            //资金密码校验    fundpwdflag    char(1)
//     char                            sz_bankpwdflag[2];                            //银行密码校验    bankpwdflag    char(1)
//     char                            sz_checkbankid[2];                            //银行帐号校验    checkbankid    char(1)
//     char                            sz_checkidno[2];                            //证件校验标志    checkidno    char(1)
//     char                            sz_orgid[ORGID_LEN];                        //机构编码    orgid    char(4)
//     char                            sz_moneytype[MONEYTYPE_LEN];                //货币代码    moneytype    char(1)
//     char                            sz_status[2];                                //银行状态    status    char(1)
// };
//
//
// struct    STransferBankBusiInfoQueryA
// {
//     DWORD        dwCount;
//     STransferBankBusiInfoQueryAItem        pItem[0];
// };
//

//存管及转帐银行业务信息(410213)功能代码    410213功能描述    银证通转帐银行业务信息
#define PID_TRADE_GATE__STORETRANSFERBANK_BUSIINFO_QUERY  (PID_TRADE_GATE_BASE+152)

/*
 一定需要机构编码,为各分支处理各自的银行接口
 发起方： '0' 证券发起 '1' 银行发起  '2' 双方发起
 校验密码校验：0 不校验 1 校验
 证件校验标志：0 不校验 1 校验

 */
// struct    SStoreTransferBankInfoQuery
// {
//     STradeGateUserInfo                userinfo;                                    //用户信息
//     char                            sz_bankcode[5];                                //银行代码    bankcode    char(4)    N    不输查询机构全部银证通银行
// };
//
// struct    SStoreTransferBankInfoQueryAItem
// {
//     char                            sz_bankcode[5];                                //银行代码    bankcode    char(4)
//     int                                n_banktrdid;                                //银行交易类型    banktrdid    int
//     char                            sz_trdpwdflag[2];                            //交易密码校验    trdpwdflag    char(1)
//     char                            sz_ctrlflag[2];                                //证件校验标志    ctrlflag    char(1)
//     char                            sz_status[2];                                //银行状态    status    char(1)
// };
//
//
// struct    SStoreTransferBankInfoQueryA
// {
//     DWORD        dwCount;
//     SStoreTransferBankInfoQueryAItem        pItem[0];
// };
//
//
//获取开通转帐(银行)账号(410601)功能代码    410601功能描述    查询转帐银行帐户

#define PID_TRADE_GATE__TRANSFERBANK_ACCOUNT_QUERY  (PID_TRADE_GATE_BASE+153)

/*
 备注信息    sourcetype: '0' 证券发起 '1' 银行发起 '2' 双方发起
 由于发起方向和转帐方式关联，所以目前系统只取了'银行转证券'作为判别发起方向的标识
 获取开通外

 */
// struct    STransferBankAccountQuery
// {
//     STradeGateUserInfo                userinfo;                                    //用户信息
//     char                            sz_bankcode[5];                                //银行代码    bankcode    char(4)    N    不送查询全部
//     char                            sz_moneytype[MONEYTYPE_LEN];                //货币代码    moneytype    char(1)    N    不送查询全部
//     __int64                            n64_fundid;                                    //资金帐户    fundid    int    N
// };
//
// struct    STransferBankAccountQueryAItem
// {
//     __int64                            n64_custid;                                //客户代码    custid    int
//     char                            sz_orgid[ORGID_LEN];                                //机构编码    orgid    char(4)
//     char                            sz_bankcode[5];                                //银行代码    bankcode    char(4)
//     char                            sz_bankname[33];                                //银行名称    bankname    char(32)
//     char                            sz_moneytype[MONEYTYPE_LEN];                                //货币代码    moneytype    char(1)
//     char                            sz_bankid[33];                                //银行帐户    bankid    char(32)
//     char                            sz_bankid_src[33];                                //开户银行帐号    bankid_src    char(32)
//     __int64                            n64_fundid;                                //资金帐号    fundid    int
//     char                            sz_linkflag[2];                                //转帐标识    linkflag    char(1)
//     char                            sz_sourcetype[2];                                //发起方向    sourcetype    char(1)
// };
//
//
// struct    STransferBankAccountQueryA
// {
//     DWORD        dwCount;
//     STransferBankAccountQueryAItem        pItem[0];
// };

//获取开通外部(银行)账号(410602)    功能代码    410602        功能描述    查询外部银行帐户
#define PID_TRADE_GATE__EXTERNALBANK_ACCOUNT_QUERY  (PID_TRADE_GATE_BASE+154)

// struct    SExternalBankAccountQuery
// {
//     STradeGateUserInfo                userinfo;                                    //用户信息
//     char                            sz_bankcode[5];                                //银行代码    bankcode    char(4)    N    不送查询全部
//     char                            sz_moneytype[MONEYTYPE_LEN];                //货币代码    moneytype    char(1)    N    不送查询全部
//     __int64                            n64_fundid;                                    //资金帐号    fundid    int    N
// };
//
// struct    SExternalBankAccountQueryAItem
// {
//     __int64                            n64_custid;                                        //客户代码    custid    int
//     char                            sz_orgid[ORGID_LEN];                        //机构编码    orgid    char(4)
//     char                            sz_bankcode[5];                                //银行代码    bankcode    char(4)
//     char                            sz_bankid[33];                                //银行帐户    bankid    char(32)
//     char                            sz_subbankid[33];                            //银行子帐户    subbankid    char(32)
//     __int64                            n64_fundid;                                        //资金帐号    fundid    int
// };
//
//
// struct    SExternalBankAccountQueryA
// {
//     DWORD        dwCount;
//     SExternalBankAccountQueryAItem        pItem[0];
// };

//查询账户余额(410606)
#define PID_TRADE_GATE__FUNDBALANCE_QUERY        (PID_TRADE_GATE_BASE+160)
// struct SFundBalanceQuery
// {
//     STradeGateUserInfo            userinfo;                                    //    用户信息，必送
//
//     __int64                        n64_fundid;                                    //资金账号    fundid    int    Y
//     char                        sz_moneytype[MONEYTYPE_LEN];                //货币代码    moneytype    char(1)    Y
//     char                        sz_fundpwd[33];                                //资金密码    fundpwd    char(32)    N    密码需要加密
//     char                        sz_bankcode[5];                                //银行代码    bankcode    char(4)    Y
//     char                        sz_bankpwd[33];                                //银行密码    bankpwd    char(32)    N    明文
//
// };

// struct SFundBalanceQueryA
// {
//     int                            n_sno;                                            //委托序号    sno    int
//     char                        sz_errmsg[65];                                    //错误信息    errmsg    char(64)
//     int                            n_syserrid;                                        //错误代码    syserrid    int
//     double                        db_fundeffect;                                    //银行余额    fundeffect    numeric(15,2)        单位：元
//
// };


//银证转帐查询(410608)
#define PID_TRADE_GATE__BANKSTKTRANS_QUERY        (PID_TRADE_GATE_BASE+170)

// struct SBankStkTransQuery
// {
//     STradeGateUserInfo            userinfo;                                    //    用户信息，必送
//
//     __int64                        n64_fundid;                                    //资金账号    fundid    int    Y
//     char                        sz_moneytype[MONEYTYPE_LEN];                //货币    moneytype    char(1)        货币代码    moneytype    char(1)    Y
//     int                            n_sno;                                        //委托序号    sno    int        N    不送查询全部
//     char                        sz_extsno[33];                                //外部流水号    extsno    char(32)    N    外部流水号    不送查询全部
//     char                        sz_qryoperway[2];                            //委托方式    qryoperway    char(1)    N    不送查询全部
//
// };
//
// struct SBankStkTransQueryAItem
// {
//     int                            n_operdate;                                    //转帐日期    operdate    int
//     int                            n_opertime;                                    //转帐时间    opertime    int    格式为HHMMSS
//     __int64                        n64_fundid;                                    //资金账号    fundid    int
//     char                        sz_moneytype[MONEYTYPE_LEN];                //货币    moneytype    char(1)
//     __int64                        n64_custid;                                    // 客户代码    custid    int
//
//     char                        sz_bankcode[5];                                //银行代码    bankcode    char(4)
//     char                        sz_banktranid[2];                            //业务类型    banktranid    char(1)    参看数据字典banktranid
//     int                            n_sno;                                        //合同序号    sno    int
//     double                        db_fundeffect;                                //委托金额    fundeffect    numeric(15,2)
//     double                        db_fundbal;                                    //余额    fundbal    numeric(15,2)
//     char                        sz_remark[33];                                //备注信息    remark    char(32)
//     char                        sz_status[2];                                //处理结果    status    char(1)
//     char                        sz_sourcetype[2];                            //发起方向    sourcetype    char(1)
//     char                        sz_bankmsgid[17];                            //外部信息代码    bankmsgid    char(16)
//     char                        sz_bankmsg[65];                                //外部信息内容    bankmsg      char(64)
//     char                        sz_errormsg[65];                            //系统错误信息    errormsg    char(64)
//     int                            n_syserrid;                                    //系统错误代码    syserrid    Int
//     char                        sz_extsno[33];                                //外部流水号    extsno    char(32)
//     char                        sz_operway[2];                                //委托方式    operway    char(1)    参看数据字典
//
// };
//
// struct SBankStkTransQueryA
// {
//     DWORD            dwCount;
//     SBankStkTransQueryAItem pItem[0];
// };
//



/*****************************************************************************************
 新股业务
 ********************************************************************************************/
//issuetype 定义

#define ISSUETYPE_APPLY            '0'
#define ISSUETYPE_RATION        '1'

//当日新股清单查询(411549)    功能代码    411549    功能描述    当日新股清单查询
#define PID_TRADE_GATE__NEWSTOCK_LIST        (PID_TRADE_GATE_BASE+200)
//请求数据    域名称    标识    类型及长度    必要    描述

// struct SNewStkListQuery
// {
//     STradeGateUserInfo            userinfo;                                    //    用户信息，必送
//
//     char                        sz_market[MARKET_LEN];                        // 交易市场    market    char(1)    N    不送查全部
//     char                        sz_stkcode[STKCODE_LEN];                    // 申购代码    stkcode    Char(8)    N    不送查全部
//     int                            n_issuedate;                                // 申购日期    issuedate    int    N    不送查全部
// };
//
// struct SNewStkListQueryAItem
// {
//     char                        sz_market[MARKET_LEN];                        //交易市场    market    Char(1)
//     char                        sz_stkcode[STKCODE_LEN];                    //申购代码    stkcode    Char(8)
//     char                        sz_stkname[STKNAME_LEN];                    //申购代码名称    stkname    Char(16)
//     char                        sz_linkstk[STKCODE_LEN];                    //正股代码    linkstk    Char(8)
//     char                        sz_inssuetype[2];                            //发行方式    issuetype    char(1)    0、申购    1、配售
//     int                            n_issuedate;                                //申购日期    issuedate    int
//     double                        db_fixprice;                                //申购价格    fixprice    numeric(9,3)
//     int                            n_buyunit;                                    //申购单位    buyunit    Int
//     int                            n_minqty;                                    //申购最小数量    minqty    Int
//     int                            n_maxqty;                                    //申购最大数量    maxqty    Int
// #define ALLOWANCES_UNCHECK            '0'
// #define ALLOWANCES_CHECK            '1'
//     char                        sz_chkmktvalueflag[2];                        //是否校验额度标志    chkmktvalueflag    Char(1)    0:不校验1：校验
//     char                        sz_stktype[2];                                //证券类别    stktype    Char(1)    见字典项Ezqlb
// };
//
// struct SNewStkListQueryA
// {
//     DWORD            dwCount;
//     SNewStkListQueryAItem pItem[0];
// };

//外围新股申购市值额度表查询(410610)            功能代码    410610   功能描述    新股申购客户市值额度查询
#define PID_TRADE_GATE__QUOTA_QUERY        (PID_TRADE_GATE_BASE+201)

// struct SQuotaQuery
// {
//     STradeGateUserInfo            userinfo;
//     char                        sz_market[MARKET_LEN];                        //交易市场    market    char(1)    N    不送查全部
//     char                        sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)    N    不送查全部
//     char                        sz_orgid[ORGID_LEN];                        //机构编码    orgid    char(4)    N    不送查全部
//     int                            n_count;                                    //请求行数    count    int    Y        每次取的行数
//     char                        sz_posstr[65];                                //定位串    posstr    char(64)    Y    第一次填空
// };
//
// struct SQuotaQueryAItem
// {
//     char                        sz_posstr[65];                                //定位串    posstr    char(64)
//     int                            n_serverid;                                    //节点编号    serverid    int
//     int                            n_dbfrec;                                    //序号    dbfrec    int
//     __int64                        n64_custid;                                    //客户代码    custid    Int64
//     char                        sz_orgid[ORGID_LEN];                        //机构编码    orgid    Char(4)
//     char                        sz_market[MARKET_LEN];                        //交易市场    market    Char(1)
//     char                        sz_secuid[SECUID_LEN];                        //股东代码    secuid    Char(10)
//     __int64                        n64_custquota;                                //客户市值额度    custquota    Int64
//     int                            n_receivedate;                                //接收日期    receivedate    Int
//     char                        sz_remark[REMARK_LEN];                        //备注    remark    Char(32)
// };
//
// struct SQuotaQueryA
// {
//     DWORD            dwCount;
//     SQuotaQueryAItem pItem[0];
// };
//
//配号查询(411518)功能代码    411518    功能描述    新股配号查询
#define PID_TRADE_GATE__MATCHNUM_QUERY    (PID_TRADE_GATE_BASE+202)

// struct SNStkMatchNumQuery
// {
//     STradeGateUserInfo            userinfo;
//     int                n_strdate;                                                //起始日期    strdate    int    Y
//     int                n_enddate;                                                //终止日期    enddate    int    Y
//     __int64            n64_fundid;                                                //资金帐户    fundid    int    N
//     char            sz_stkcode[STKCODE_LEN];                                //证券代码    stkcode    char(8)    N    不送查询全部
//     char            sz_secuid[SECUID_LEN];                                    //股东代码    secuid    char(10)    N    不送查询全部
//     char            sz_qryflag[QRYFLAG_LEN];                                //查询方向    qryflag    char(1)     Y    向下/向上查询方向
//     int                n_count;                                                //请求行数    count    int     Y    每次取的行数
//     char            poststr[POSTSTR_LEN];                                    //定位串    poststr    char(32)    Y    第一次填空
// };
//
// struct SNStkMatchNumQueryAItem
// {
//     char            sz_poststr[POSTSTR_LEN];                                //位串      poststr    char(32)    查询返回定位值
//     int                n_bizdate;                                                //配号日期    bizdate    int
//     char            sz_market[MARKET_LEN];                                    //交易市场    market    char(1)
//     char            sz_secuid[SECUID_LEN];                                    //股东代码    secuid    char(10)
//     char            sz_stkname[STKNAME_LEN];                                //证券名称    stkname    char(16)
//     char            sz_stkcode[STKCODE_LEN];                                //证券代码    stkcode    char(8)
//     int                n_matchqty;                                                //配号数量    matchqty    int
//     char            sz_mateno[33];                                            //申购配号    mateno    char(32)
//     char            sz_orderid[11];                                            //委托号    orderid    char(10)
//     int                n_orderdate;                                            //委托日期    orderdate    int
//     char            mateerrmsg[128];                                        //备注    mateerrmsg    char(128)
// };
//
// struct SNStkMatchNumQueryA
// {
//     DWORD dwCount;
//     SNStkMatchNumQueryAItem pItem[0];
// };
//
//市值配售中签查询(411547)    功能代码    411547    功能描述    客户从外围查询市值中签数据(新股申购、市值配售)
#define PID_TRADE_GATE__NSTKHIT_QUERY   (PID_TRADE_GATE_BASE+203)

// struct    SNStkHitQuery
// {
//     STradeGateUserInfo                userinfo;                                    //用户信息
//     char                            sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)    N
//     char                            sz_market[MARKET_LEN];                        //交易市场    market    char(1)    N
//     char                            sz_stkcode[STKCODE_LEN];                    //证券代码    stkcode    char(8)    N
//     char                            sz_issuetype[2];                            //发行方式    issuetype    char(1)    N    ‘0’申购‘1’配售
// };
//
// struct    SNStkHitQueryAItem
// {
//     __int64                            n64_custid;                                    //客户代码    custid    Int64
//     char                            sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)
//     char                            sz_market[MARKET_LEN];                        //交易市场    market    char(1)
//     char                            sz_stkcode[STKCODE_LEN];                    //证券代码    stkcode    char(8)
//     char                            sz_stkname[STKNAME_LEN];                    //证券名称    stkname    char(16)
//     int                                n_orderdate;                                //委托日期    orderdate    int
//     int                                n_matchdate;                                //中签日期    matchdate    Int
//     int                                n_hitqty;                                    //中签数量    hitqty    Int
//     int                                n_giveupqty;                                //放弃数量    giveupqty    Int
//     double                            db_matchprice;                                //价格    matchprice    numeric(9,3)
//     double                            db_hitamt;                                    //中签金额    hitamt    numeric(19,2)
//     int                                n_payqty;                                    //已缴款数量    payqty    Int
//     double                            db_payamt;                                    //已缴款金额    payamt    numeric(19,2)
//     double                            db_frzamt;                                    //预冻结金额    frzamt    numeric(19,2)
//     int                                n_clearsno;                                    //交收顺序    clearsno    int
//     char                            sz_issuetype[2];                            //发行方式    issuetype    char(1)
//     char                            sz_status[2];                                //流程状态    status    char(1)         ‘0’新股中签‘1’中签缴款‘2’中签确认
// };
//
//
// struct    SNStkHitQueryA
// {
//     DWORD                    dwCount;
//     SNStkHitQueryAItem        pItem[0];
// };

//中签历史查询(411560)功能代码    411560功能描述    客户从外围查询市值中签数据(新股申购、市值配售)
#define PID_TRADE_GATE__NSTKHISTORYHIT_QUERY   (PID_TRADE_GATE_BASE+204)

// struct    SNStkHistoryHitQuery
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)    N
//     char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)    N
//     char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)    N
//     char                            sz_issuetype[2];                        //发行方式    issuetype    char(1)    N    ‘0’申购‘1’配售
//     int                                n_begindate;                            //中签开始日期    begindate    Int    Y    ‘0’申购‘1’配售
//     int                                n_enddate;                                //中签结束日期    enddate    Int    Y    ‘0’申购‘1’配售
//     int                                n_count;                                //请求行数    count    int     N    每次取的行数，不送默认为0
//     char                            sz_poststr[POSTSTR_LEN];                //定位串    poststr    char(32)    N    第一次填空，不送默认为空
// };
//
// struct    SNStkHistoryHitQueryAItem
// {
//     char                            sz_poststr[POSTSTR_LEN];                                //定位串      poststr    char(32)        查询返回定位值
//     __int64                            n64_custid;                                //客户代码    custid    Int64        查询返回定位值
//     char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)        查询返回定位值
//     char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)        查询返回定位值
//     char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)        查询返回定位值
//     char                            sz_stkname[STKNAME_LEN];                //证券名称    stkname    char(16)        查询返回定位值
//     int                                n_orderdate;                            //委托日期    orderdate    int        查询返回定位值
//     int                                n_ordersno;                                //委托序号    ordersno    int        查询返回定位值
//     int                                n_matchdate;                            //中签日期    matchdate    Int        查询返回定位值
//     int                                n_hitqty;                                //中签数量    hitqty    Int        查询返回定位值
//     int                                n_giveupqty;                            //放弃数量    giveupqty    Int        查询返回定位值
//     double                            db_matchprice;                            //价格    matchprice    numeric(9,3)        查询返回定位值
//     double                            db_hitamt;                                //中签金额    hitamt    numeric(19,2)        查询返回定位值
//     int                                n_payqty;                                //已缴款数量    payqty    Int        查询返回定位值
//     double                            db_payamt;                                //已缴款金额    payamt    numeric(19,2)        查询返回定位值
//     int                                n_clearsno;                                //交收顺序    clearsno    int        查询返回定位值
//     char                            sz_issuetype[2];                        //发行方式    issuetype    char(1)        查询返回定位值
//     char                            sz_status[2];                            //流程状态    status    char(1)        ‘0’新股中签‘1’中签缴款‘2’中签确认
// };
//
//
// struct    SNStkHistoryHitQueryA
// {
//     DWORD        dwCount;
//     SNStkHistoryHitQueryAItem        pItem[0];
// };




//(新股申购中签顺序设置)市值配售交收顺序修改（411548）功能代码    411548 功能描述    客户从外围发起配售中签缴款优先级设置
#define PID_TRADE_GATE__NSTKDEALNO_SET    (PID_TRADE_GATE_BASE+205)

// struct    SNStkDealNoSet
// {
//     STradeGateUserInfo                userinfo;                                    //用户信息
//     int                                n_matchdate;                                    //中签日期    matchdate    Int    Y
//     char                            sz_market[MARKET_LEN];                        //交易市场    market    char(1)    Y
//     char                            sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)    Y
//     char                            sz_stkcode[STKCODE_LEN];                    //证券代码    stkcode    char(8)    Y
//     int                                n_clearsno;                                        //交收顺序    clearsno    Int    Y    大于等于1，该值越小，缴款优先级越高
// };
//
// struct SNStkDealNoSetA
// {
//     int                            n_serialno;                                    //操作流水号    serialno    Int
// };

//中签预冻结资金解冻(411550)    功能代码    411550    功能描述    客户从外围解冻中签预冻结资金

#define PID_TRADE_GATE__PREFREEZINGFUND_THAWING                (PID_TRADE_GATE_BASE+206)

// struct    SPreFreezingFundThaw
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     __int64                            n64_fundid;                                //资金帐户    fundid    Int64    Y
//     int                                n_matchdate;                            //中签日期    matchdate    Int    Y
// };
//
//
// struct    SPreFreezingFundThawA
// {
//     int                                n_sno;                                    //操作流水号    sno    int
//     double                            db_unfrzamt;                            //解冻预冻结金额    unfrzamt    numeric(19,2)
// };
//
/*********************************************************************************
 ********************全国股转系统**************************************************
 委托申报、撤单请参见410611、410413功能；
 委托查询请参见410510功能；
 可委托数量查询请参见410614功能；
 成交明细查询请参见410512功能；
 可交易证券查询请参见PID_TRADE_GATE__STOCK_QUERY 功能
 **********************************************************************************/
//股转系统证券委托买卖申报(410611) 功能代码    410611功能描述    股转系统证券委托申报
#define PID_TRADE_GATE__STOCKTRANSFERSYS_DECLARATION                (PID_TRADE_GATE_BASE+250)

struct    SStkTransferSYSOrder
{
    STradeGateUserInfo                userinfo;                                //用户信息
    char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)    Y
    __int64                            n64_fundid;                                //资金账户    fundid    int    Y
    char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)    Y
    char                            sz_bsflag[3];                            //买卖类别    bsflag    char(2)    Y
    char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)    Y
    double                            db_price;                                //委托价格    price    numeric(9,3)    Y
    int                                n_qty;                                    //委托数量    qty    int    Y
    int                                n_ordergroup;                            //委托批号    ordergroup    int    Y
    char                            sz_linkman[13];                            //联系人    linkman    char(12)    N
    char                            sz_linkway[31];                            //联系方式    linkway    char(30)    N
    char                            sz_targetseat[7];                        //对方席位    targetseat    char(6)    N
    char                            sz_targetsecuid[11];                    //对方股东账户    targetsecuid    char(10)    N
    double                            db_confernum;                            //成交约定号    confernum    numeric(8,0)    N
    __int64                            n64_risksignsno;                        //风险揭示签署流水号    risksignsno    int64    N
};

struct    SStkTransferSYSOrderA
{
    int                                n_ordersno;                                //委托序号    ordersno    int
    char                            sz_orderid[11];                            //合同序号    orderid    char(10)
    int                                n_ordergroup;                            //委托批号    ordergroup    int
};
//股转系统协议转让行情查询(410612)    功能代码    410612    功能描述    股转系统协议转让行情查询
#define PID_TRADE_GATE__STOCKTRANSFERSYS_QUOTATION_QUERY            (PID_TRADE_GATE_BASE+251)

struct    SStkTransferSYSQuotationQuery
{
    STradeGateUserInfo                userinfo;                                //用户信息
    char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)    Y
    char                            sz_bsflag[3];                            //委托类别    bsflag    char(2)    N    不送查询全部6B 定价申报的买入申报记录6S 定价申报的卖出申报记录
    double                            db_confernum;                            //成交约定号    confernum    numeric(8,0)    N    不送查询全部
    char                            sz_ordertime[7];                        //委托时间    ordertime    char(6)    N    格式为HHMMSS
};

struct    SStkTransferSYSQuotationQueryAItem
{
    char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)
    char                            sz_seat[7];                                //席位代码    seat    char(6)
    char                            sz_bsflag[3];                            //委托类别    bsflag    char(2)
    int                                n_orderqty;                                //委托数量    orderqty    int
    double                            db_orderprice;                            //委托价格    orderprice    numeric(9,3)
    double                            db_Confernum;                            //成交约定号    Confernum    numeric(8,0)
    char                            sz_ordertime[7];                        //委托时间    ordertime    char(6)
    int                                n_status;                                //记录状态    status    int
    char                            sz_remark[REMARK_LEN];                    //备用标志    remark    char(1)
};

struct SStkTransferSYSQuotationQueryA
{
    DWORD        dwCount;
    SStkTransferSYSQuotationQueryAItem        pItem[0];
};


//权证行权，转股回售 请参阅 委托买卖业务(410411)

//修改交易密码(410302)  功能代码    410302 功能描述    修改密码
#define PID_TRADE_GATE__MODIFY_TRADEPWD                                (PID_TRADE_GATE_BASE+270)
//
// struct    SModifyTradePwdReq
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     char                            sz_newpwd[33];                            //新密码    newpwd    char(32)    Y    加密后
// };
//
// struct    SModifyTradePwdA
// {
//     char                            sz_msgok[33];                            //成功信息    msgok    char(32)
//     char                            sz_custcert[129];                        //客户证书    custcert    char(128)        修改密码后返回的会话信息，以后做委托或查询时传入作为入参
// };

//修改资金密码
#define  PID_TRADE_GATE__MODIFY_FUNDSPWD                            (PID_TRADE_GATE_BASE+271)

// struct    SModifyFundsPwdReq
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     __int64                            n64_fundid;                                //资金帐户    fundid    INT    Y
//     char                            sz_oldfundpwd[33];                        //老资金密码    oldfundpwd    char(32)    Y    加密后
//     char                            sz_newfundpwd[33];                        //新资金密码    newfundpwd    char(32)    Y    加密后
// };
//
// struct    SModifyFundsPwdA
// {
//     char                            sz_msgok[33];                                //成功信息    msgok    char(32)        qqq
// };

//股东查询(410501)功能代码    410501功能描述    股东查询，查询客户对应的股东代码
#define PID_TRADE_GATE_SHAREHOLDER_QUERY                            (PID_TRADE_GATE_BASE+272)

// struct    SShareHolderQuery
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     __int64                            n64_fundid;                                //资金帐户    fundid    INT    N
//     char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)    N    不送查询全部
//     char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)    N    不送查询全部
//     char                            sz_qryflag[QRYFLAG_LEN];                //查询方向    qryflag    char(1)     Y    向下/向上查询方向
//     int                                n_count;                                //请求行数    count    int     Y    每次取的行数
//     char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)    Y    第一次填空
// };
//
// struct    SShareHolderQueryAItem
// {
//     char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)        查询返回定位值
//     __int64                            n64_custid;                                //客户代码    custid    int        查询返回定位值
//     char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)        查询返回定位值
//     char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)        查询返回定位值
//     char                            sz_name[17];                            //股东姓名    name    char(16)        查询返回定位值
//     int                                n_secuseq;                                //股东序号    secuseq    int        查询返回定位值
//     char                            sz_regflag[2];                            //指定交易状态    regflag    char(1)        0：未指定1：指定交易2：首日指定3：撤指未回
// };
//
//
// struct    SShareHolderQueryA
// {
//     DWORD        dwCount;
//     SShareHolderQueryAItem        pItem[0];
// };
/*****************************************************************************************
 ***********************债券相关业务*******************************************************
 *******************************************************************************************/

//债券买入，债券卖出，质押券入库，质押券出库，请参考业务（410411）

//债券撤单请参考业务 （410413）

//融资回购，融券回购
//债券质押式回购业务(BOUND PLEDGE-STYLE REPO)    标准券汇总查询(410574)
#define PID_TRADEGATE__STANDARDBOUND_AGGREGATE_QUERY                  (PID_TRADE_GATE_BASE+300)

// struct    SStandArdBoundAggregateQuery
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)    N    不送查询全部
//     __int64                            n64_fundid;                                //资金帐户    fundid    Int64    N    不送查询全部
//     char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)    N    不送查询全部
//     char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)    N    不送查询全部
//     char                            sz_qryflag[QRYFLAG_LEN];                //查询方向    qryflag    char(1)     Y    向下/向上查询方向
//     int                                n_count;                                //请求行数    count    int     Y    每次取的行数
//     char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)    Y    第一次填空
// };
//
// struct    SStandArdBoundAggregateQueryAItem
// {
//     char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)
//     int                                n_serverid;                                //节点编号    serverid    int
//     char                            sz_orgid[ORGID_LEN];                    //机构编码    orgid    char(4)
//     __int64                            n64_custid;                                //客户代码    custid    Int64
//     __int64                            n64_fundid;                                //资金账户    fundid    Int64
//     char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)
//     char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)
//     char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)
//     char                            sz_stktype[2];                            //证券类别    stktype    char(1)        3 国债回购6企债回购G国债质押回购H企债质押回购K报价回购
//     char                            sz_moneytype[MONEYTYPE_LEN];            //货币代码    moneytype    char(1)        3 国债回购6企债回购G国债质押回购H企债质押回购K报价回购
//     double                            db_exchrate;                            //折算率    exchrate    numeric(12,8)        3 国债回购6企债回购G国债质押回购H企债质押回购K报价回购
//     __int64                            n64_bondassetavl;                        //质押券可用    bondassetavl    Int64        3 国债回购6企债回购G国债质押回购H企债质押回购K报价回购
//     __int64                            n64_bondunfrz;                            //标准券解冻    bondunfrz    Int64        3 国债回购6企债回购G国债质押回购H企债质押回购K报价回购
//     __int64                            n64_bondfrz;                            //标准券冻结    bondfrz    Int64        3 国债回购6企债回购G国债质押回购H企债质押回购K报价回购
//     __int64                            n64_remainqty;                            //标准券可用    remainqty    Int64        3 国债回购6企债回购G国债质押回购H企债质押回购K报价回购
// };
//
//
// struct    SStandArdBoundAggregateQueryA
// {
//     DWORD        dwCount;
//     SStandArdBoundAggregateQueryAItem        pItem[0];
// };
//
//未到期回购查询请参考  （未到期回购查询(410545)#define PID_TRADE_GATE__UNEXPIREDREPURCHASE_HISTORY        (PID_TRADE_GATE_BASE+140)）

/**********************************************************************************************
 **************************港股通业务***********************************************************
 ***********************************************************************************************/

//买卖委托（430010）功能代码    430010    功能描述    港股通委托买卖交易
#define PID_TRADEGATE__HKORDER                                        (PID_TRADE_GATE_BASE+400)

struct    SSHKStockOrder
{
    STradeGateUserInfo                userinfo;                                    //用户信息
    char                            sz_market[MARKET_LEN];                        //交易市场    market    char(1)    Y    5 沪港通S 深港通
    char                            sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)    Y    5 沪港通S 深港通
    __int64                            n64_fundid;                                    //资金账户    fundid    int64    Y    5 沪港通S 深港通
    char                            sz_stkcode[STKCODE_LEN];                    //证券代码    stkcode    char(8)    Y    5 沪港通S 深港通
    char                            sz_bsflag[3];                                //买卖类别    bsflag    char(2)    Y    2B竞价限价买入2S竞价限价卖出3B增强限价买入3S增强限价卖出4S零股限价卖出
    double                            db_price;                                    //价格(价格)    price    numeric(9,3)    Y    2B竞价限价买入2S竞价限价卖出3B增强限价买入3S增强限价卖出4S零股限价卖出
    int                            n_qty;                                            //数量    qty    int    Y    2B竞价限价买入2S竞价限价卖出3B增强限价买入3S增强限价卖出4S零股限价卖出
    int                            n_ordergroup;                                    //委托批号    ordergroup    int    Y    0,
    char                            sz_timeinforce[2];                            //订单有效时间类型    timeinforce    char(1)    N    仅港股通竞价买卖有效:'0'-GFD(当日有效,即正常情况允许部分成交), '1'-FOK(即时全部成交否则撤销,即不允许部分成交)
    __int64                            n64_risksignsno;                            //风险揭示签署流水号    risksignsno    int64    N    调用411017获取
};

struct    SSHKStockOrderA
{
    int                            n_ordersno;                                        //委托序号    ordersno    int        返回给股民
    char                        sz_orderid[11];                                    //合同序号    orderid    char(10)        返回给股民
    int                            n_ordergroup;                                    //委托批号    ordergroup    int        返回给股民
};

//撤单委托（430011）    功能代码    430011            功能描述    港股通撤单委托
#define PID_TRADEGATE__HKORDER_CANCEL                                (PID_TRADE_GATE_BASE+401)

struct    SHKOrderCancel
{
    STradeGateUserInfo                userinfo;                                    //用户信息
    int                                n_orderdate;                                //委托日期    orderdate    int    Y
    __int64                            n64_fundid;                                    //资金帐户    fundid    int    Y
    int                                n_ordersno;                                    //委托序号    ordersno    int    Y    委托返回的
    char                            sz_bsflag[3];                                //买卖类别    bsflag    char(2)    N    2B竞价限价买2S竞价限价卖出3B增强限价买入3S增强限价卖出4S零股限价卖出
};

struct    SHKOrderCancelA
{
    int                            n_ordersno;                                        //撤单委托序号    ordersno    int
};


//可撤单委托查询(430012)    功能代码    430012    功能描述    委托撤单查询
#define PID_TRADEGATE__CANCANCELORDER_QUERY                            (PID_TRADE_GATE_BASE+402)

struct    SCanCancelOrderQuery
{
    STradeGateUserInfo                userinfo;                                    //用户信息
    int                                n_orderdate;                                //委托日期    orderdate    int    N
    __int64                            n64_fundid;                                    //资金帐户    fundid    int    N
    char                            sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)    N
    char                            sz_stkcode[STKCODE_LEN];                    //证券代码    stkcode    char(8)    N
    int                                n_ordersno;                                    //委托序号    ordersno    int    N
    char                            sz_qryflag[QRYFLAG_LEN];                    //查询方向    qryflag    char(1)     Y    向下/向上查询方向
    int                                n_count;                                    //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];                    //定位串      poststr    char(32)    Y    第一次填空
};


struct    SCanCancelOrderQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];                    //定位串      poststr    char(32)        查询返回定位值
    int                                n_ordersno;                                    //委托序号    ordersno    int        查询返回定位值
    int                                n_ordergroup;                                //委托批号    ordergroup    int        查询返回定位值
    char                            sz_orderid[11];                                //合同序号    orderid    char(10)        查询返回定位值
    int                                n_orderdate;                                //委托日期    orderdate    int        查询返回定位值
    int                                n_opertime;                                    //委托时间    opertime    int        查询返回定位值
    __int64                            n64_fundid;                                    //资金帐户    fundid    int        查询返回定位值
    char                            sz_market[MARKET_LEN];                        //市场代码    market    char(1)        查询返回定位值
    char                            sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)        查询返回定位值
    char                            sz_stkname[STKNAME_LEN];                    //证券名称    stkname    char(8)        查询返回定位值
    char                            sz_Stkcode[STKCODE_LEN];                    //证券代码    Stkcode    char(8)        查询返回定位值
    char                            sz_bsflag[3];                                //买卖类别    bsflag    char(2)        查询返回定位值
    double                            db_orderprice;                                //委托价格(港币)    orderprice    numeric(9,3)        查询返回定位值
    int                                n_orderqty;                                    //委托数量    orderqty    int        查询返回定位值
    int                                n_matchqty;                                    //成交数量    matchqty    int        查询返回定位值
    char                            sz_orderstatus[2];                            //委托状态    orderstatus    char(1)        查询返回定位值
    char                            sz_cancelstatus[2];                            //撤单状态    cancelstatus    char(1)        查询返回定位值
};


struct    SCanCancelOrderQueryA
{
    DWORD        dwCount;
    SCanCancelOrderQueryAItem        pItem[0];
};

//当日委托查询(430001)    功能代码    430001    功能描述    港股通交易委托明细查询
#define PID_TRADEGATE__HKORDER_QUERY                                (PID_TRADE_GATE_BASE+403)

struct    SHKOrderQuery
{
    STradeGateUserInfo                userinfo;                                    //用户信息
    char                            sz_market[MARKET_LEN];                        //交易市场    market    char(1)    N    不送查询全部
    __int64                            n64_fundid;                                    //资金帐户    fundid    int64    N    不送查询全部
    char                            sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)    N    不送查询全部
    char                            sz_stkcode[STKCODE_LEN];                    //证券代码    stkcode    char(8)    N    不送查询全部
    int                                n_ordersno;                                    //委托序号    ordersno    int    N    不送查询全部
    int                                n_ordergroup;                                //委托批号    ordergroup    int    N    不送查询全部
    char                            sz_qryflag[QRYFLAG_LEN];                    //查询方向    qryflag    char(1)     Y    向下/向上查询方向
    int                                n_count;                                    //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];                    //定位串      poststr    char(32)    Y    第一次填空
};

struct    SHKOrderQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];                    //定位串      poststr    char(32)
    int                                n_orderdate;                                //委托日期    orderdate    int
    int                                n_ordersno;                                    //委托序号    ordersno    int
    int                                n_ordergroup;                                //委托批号    ordergroup    int
    __int64                            n64_custid;                                    //客户代码    custid    int64
    char                            sz_custname[17];                            //客户姓名    custname    char(16)
    __int64                            n64_fundid;                                    //资金账户    fundid    int64
    char                            sz_moneytype[MONEYTYPE_LEN];                //货币    moneytype    char(1)
    char                            sz_orgid[ORGID_LEN];                        //机构编码    orgid    char(4)
    char                            sz_secuid[SECUID_LEN];                        //股东代码    secuid    char(10)
    char                            sz_bsflag[3];                                //买卖类别    bsflag    char(2)        2B：竞价限价买入2S: 竞价限价卖出3B: 增强限价买入3S: 增强限价卖出4S: 零股限价卖出
    char                            sz_orderid[11];                                //申报合同序号    orderid    char(10)        2B：竞价限价买入2S: 竞价限价卖出3B: 增强限价买入3S: 增强限价卖出4S: 零股限价卖出
    int                                n_reporttime;                                //报盘时间    reporttime    int        2B：竞价限价买入2S: 竞价限价卖出3B: 增强限价买入3S: 增强限价卖出4S: 零股限价卖出
    int                                n_opertime;                                    //委托时间    opertime    int        2B：竞价限价买入2S: 竞价限价卖出3B: 增强限价买入3S: 增强限价卖出4S: 零股限价卖出
    char                            sz_market[MARKET_LEN];                        //交易市场    market    char(1)        5港股通S深港
    char                            sz_stkcode[STKCODE_LEN];                    //证券代码    stkcode    char(8)        5港股通S深港
    char                            sz_stkname[STKNAME_LEN];                    //证券名称    stkname    char(8)        5港股通S深港
    double                            db_orderprice;                                //委托价格(港币)    orderprice    numeric(9,3)        5港股通S深港
    int                                n_orderqty;                                    //委托数量    orderqty    int        5港股通S深港
    double                            db_orderfrzamt_rmb;                            //冻结金额(人民币)    orderfrzamt_rmb    numeric(19,2)        5港股通S深港
    int                                n_matchqty;                                    //成交数量    matchqty    int        5港股通S深港
    double                            db_matchamt;                                //成交金额(港币)    matchamt    numeric(19,2)        5港股通S深港
    int                                n_cancelqty;                                //撤单数量    cancelqty    int        5港股通S深港
    char                            sz_orderstatus[2];                            //委托状态    orderstatus    char(1)        5港股通S深港
    char                            sz_cancelstatus[2];                            //撤单状态    cancelstatus    char(1)        5港股通S深港
    char                            sz_seat[7];                                    //交易席位    seat    char(6)        5港股通S深港
    char                            sz_cancelflag[2];                            //撤单标识    cancelflag    char(1)        'F' 正常 'T' 撤单
    int                                n_operdate;                                    //操作日期    operdate    int        'F' 正常 'T' 撤单
    double                            db_referrate;                                //参考汇率    referrate    numeric(12,8)        'F' 正常 'T' 撤单
    char                            sz_remark[REMARK_LEN];                        //备注    remark    char(128)        'F' 正常 'T' 撤单
    double                            db_afundamt;                                //A股资金变动金额    afundamt    numeric(19,2)        'F' 正常 'T' 撤单
    double                            db_hfundamt;                                //港股资金变动金额    hfundamt    numeric(19,2)        'F' 正常 'T' 撤单
};


struct    SHKOrderQueryA
{
    DWORD                    dwCount;
    SHKOrderQueryAItem        pItem[0];
};

//当日成交查询(430003)    功能代码    430003    功能描述    港股通交易成交查询
#define PID_TRADEGATE__HKDEAL_QUERY                                    (PID_TRADE_GATE_BASE+404)
struct    SHKDealQuery
{
    STradeGateUserInfo                userinfo;                                //用户信息
    __int64                            n64_fundid;                                //资金帐户    fundid    int64    N    不送查询全部
    char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)    N    不送查询全部
    char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)    N    不送查询全部
    char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)    N    不送查询全部
    int                                n_ordersno;                                //委托序号    ordersno    int    N    不送查询全部
    int                                n_strdate;                                //起始日期    strdate    int    N    不送查询当日成交
    int                                n_enddate;                                //终止日期    enddate    int    N    不送查询当日成交
    char                            sz_qryflag[QRYFLAG_LEN];                //查询方向    qryflag    char(1)     Y    向下/向上查询方向
    int                                n_count;                                //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)    Y    第一次填空
};


struct    SHKDealQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)        查询返回定位值
    int                                n_trddate;                                //成交日期    trddate    int        查询返回定位值
    char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)        查询返回定位值
    char                            sz_bsflag[3];                            //买卖类别    bsflag    char(2)        查询返回定位值
    int                                n_ordersno;                                //委托序号    ordersno    int        查询返回定位值
    char                            sz_orderid[11];                            //申报合同序号    orderid    char(10)        查询返回定位值
    char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)        查询返回定位值
    char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)        查询返回定位值
    char                            sz_stkname[STKNAME_LEN];                //证券名称    stkname    char(8)        查询返回定位值
    int                                n_matchtime;                            //成交时间    matchtime    int        不足8位时前补0，格式为HHMMSSZZ
    char                            sz_matchcode[21];                        //成交序号    matchcode    char(20)        不足8位时前补0，格式为HHMMSSZZ
    double                            db_matchprice;                            //成交价格(港币)    matchprice    numeric(9,3)        不足8位时前补0，格式为HHMMSSZZ
    int                                n_matchqty;                                //成交数量    matchqty    int        不足8位时前补0，格式为HHMMSSZZ
    double                            db_matchamt;                            //成交金额(港币)    matchamt    numeric(19,2)        不足8位时前补0，格式为HHMMSSZZ
    char                            sz_matchtype[2];                        //成交类型    matchtype    char(1)        '0'普通成交'1'撤单成交
    int                                n_orderqty;                                //委托数量    orderqty    int        '0'普通成交'1'撤单成交
    double                            db_orderprice;                            //委托价格(港币)    orderprice    numeric(9,3)        '0'普通成交'1'撤单成交
};


struct    SHKDealQueryA
{
    DWORD        dwCount;
    SHKDealQueryAItem        pItem[0];
};

//历史委托查询(430002)    功能代码    430002        功能描述    港股通交易历史委托查询
#define PID_TRADEGATE__HKHISTORY_ORDER_QUERY                        (PID_TRADE_GATE_BASE+405)

struct    SHKHistoryOrderQuery
{
    STradeGateUserInfo                userinfo;                                //用户信息
    int                                n_strdate;                                //起始日期    strdate    int    Y
    int                                n_enddate;                                //终止日期    enddate    int    Y
    __int64                            n64_fundid;                                //资金帐号    fundid    int64    Y
    char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)    Y    送空或送空格查询全部
    char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(10)    Y    送空查询全部
    char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)    Y    送空查询全部
    int                                n_ordersno;                                //委托序号    ordersno    int    Y    送小于等于0查询全部
    int                                n_ordergroup;                            //委托批号    ordergroup    int    Y    送小于等于0查询全部
    char                            sz_qryflag[QRYFLAG_LEN];                //查询方向    qryflag    char(1)     Y    向下/向上查询方向
    int                                n_count;                                //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)    Y    第一次填空
};

struct    SHKHistoryOrderQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];                                //定位串      poststr    char(32)        查询返回定位值
    int                            n_orderdate;                                //委托日期    orderdate    int        查询返回定位值
    __int64                            n64_custid;                                //客户代码    custid    int64        查询返回定位值
    char                            sz_custname[17];                                //客户姓名    custname    char(16)        查询返回定位值
    __int64                            n64_fundid;                                //资金账户    fundid    int64        查询返回定位值
    char                            sz_moneytype[MONEYTYPE_LEN];                                //货币    moneytype    char(1)        查询返回定位值
    char                            sz_orgid[ORGID_LEN];                                //机构编码    orgid    char(4)        查询返回定位值
    char                            sz_secuid[SECUID_LEN];                                //股东代码    secuid    char(10)        查询返回定位值
    char                            sz_bsflag[3];                                //买卖类别    bsflag    char(2)        2B：竞价限价买入2S: 竞价限价卖出3B: 增强限价买入3S: 增强限价卖出4S: 零股限价卖出
    char                            sz_orderid[11];                                //申报合同序号    orderid    char(10)        2B：竞价限价买入2S: 竞价限价卖出3B: 增强限价买入3S: 增强限价卖出4S: 零股限价卖出
    int                            n_ordergroup;                                //委托批号    ordergroup    int        2B：竞价限价买入2S: 竞价限价卖出3B: 增强限价买入3S: 增强限价卖出4S: 零股限价卖出
    int                            n_reporttime;                                //报盘时间    reporttime    int        2B：竞价限价买入2S: 竞价限价卖出3B: 增强限价买入3S: 增强限价卖出4S: 零股限价卖出
    int                            n_opertime;                                //委托时间    opertime    int        不足8位时前补0，格式为HHMMSSZZ
    char                            sz_market[MARKET_LEN];                                //交易市场    market    char(1)        不足8位时前补0，格式为HHMMSSZZ
    char                            sz_stkcode[STKCODE_LEN];                                //证券代码    stkcode    char(8)        不足8位时前补0，格式为HHMMSSZZ
    char                            sz_stkname[STKNAME_LEN];                                //证券名称    stkname    char(8)        不足8位时前补0，格式为HHMMSSZZ
    double                            db_orderprice;                                //委托价格(港币)    orderprice    numeric(9,3)        不足8位时前补0，格式为HHMMSSZZ
    int                            n_orderqty;                                //委托数量    orderqty    int        不足8位时前补0，格式为HHMMSSZZ
    double                            db_orderfrzamt_rmb;                                //冻结金额（人民币）    orderfrzamt_rmb    numeric(19,2)        不足8位时前补0，格式为HHMMSSZZ
    int                            n_matchqty;                                //成交数量    matchqty    int        不足8位时前补0，格式为HHMMSSZZ
    double                            db_matchamt;                                //成交金额(港币)    matchamt    numeric(19,2)        不足8位时前补0，格式为HHMMSSZZ
    int                            n_cancelqty;                                //撤单数量    cancelqty    int        不足8位时前补0，格式为HHMMSSZZ
    char                            sz_cancelflag [2];                                //撤单标志    cancelflag     char(1)        'F' 正常 'T' 撤单
    char                            sz_orderstatus[2];                                //委托状态    orderstatus    char(1)        'F' 正常 'T' 撤单
    char                            sz_cancelstatus[2];                                //撤单状态    cancelstatus    char(1)        'F' 正常 'T' 撤单
    char                            sz_seat[7];                                //交易席位    seat    char(6)        'F' 正常 'T' 撤单
    char                            sz_operway[2];                                //操作方式    operway    char(1)        'F' 正常 'T' 撤单
    int                            n_operdate;                                //操作日期    operdate    int        'F' 正常 'T' 撤单
    double                            db_referrate;                                //参考汇率    referrate    numeric(12,8)        'F' 正常 'T' 撤单
    double                            db_afundamt;                                //A股资金变动金额    afundamt    numeric(19,2)        'F' 正常 'T' 撤单
    double                            db_hfundamt;                                //港股资金变动金额    hfundamt    numeric(19,2)        'F' 正常 'T' 撤单
};


struct    SHKHistoryOrderQueryA
{
    DWORD        dwCount;
    SHKHistoryOrderQueryAItem        pItem[0];
};

//历史成交查询(430013)    功能代码    430013    功能描述    港股通交易成交历史查询
#define PID_TRADEGATE__HKHISTORY_DEAL_QUERY                            (PID_TRADE_GATE_BASE+406)

struct    SHKHistoryDealQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    int                                n_strdate;                            //起始日期    strdate    int    Y
    int                                n_enddate;                            //终止日期    enddate    int    Y
    __int64                            n64_fundid;                            //资金帐户    fundid    int64    N    不送查询全部
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)    N    不送查询全部
    char                            sz_secuid[SECUID_LEN];                //股东代码    secuid    char(10)    N    不送查询全部
    char                            sz_stkcode[STKCODE_LEN];            //证券代码    stkcode    char(8)    N    不送查询全部
    char                            sz_qryflag[QRYFLAG_LEN];            //查询方向    qryflag    char(1)     Y    向下/向上查询方向
    int                                n_count;                            //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)    Y    第一次填空
};

struct    SHKHistoryDealQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)        查询返回定位值
    int                                n_bizdate;                            //交收日期    bizdate    int        查询返回定位值
    int                                n_cleardate;                        //成交日期    cleardate    int        查询返回定位值
    char                            sz_secuid[SECUID_LEN];                //股东代码    secuid    char(10)        查询返回定位值
    char                            sz_bsflag[3];                        //买卖类别    bsflag    char(2)        查询返回定位值
    char                            sz_orderid[11];                        //申报合同序号    orderid    char(10)        查询返回定位值
    int                                n_ordersno;                            //委托序号    ordersno    int        查询返回定位值
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)        查询返回定位值
    char                            sz_stkcode[STKCODE_LEN];            //证券名称    stkcode    char(8)        查询返回定位值
    char                            sz_stkname[STKNAME_LEN];            //证券代码    stkname    char(8)        查询返回定位值
    int                                n_matchtime;                        //成交时间    matchtime    int        格式为HHMMSS
    char                            sz_matchcode[21];                    //成交序号    matchcode    char(20)        格式为HHMMSS
    double                            db_matchprice;                        //成交价格    matchprice    numeric(9,3)        格式为HHMMSS
    int                                n_matchqty;                            //成交数量    matchqty    int        格式为HHMMSS
    double                            db_matchamt;                        //成交金额    matchamt    numeric(15,2)        格式为HHMMSS
    int                                n_orderqty;                            //委托数量    orderqty    int        格式为HHMMSS
    double                            db_orderprice;                        //委托价格(港币)    orderprice    numeric(9,3)        格式为HHMMSS
    int                                n_stkbal;                            //股份本次余额    stkbal    int        格式为HHMMSS
    double                            db_fee_jsxf;                        //净手续费    fee_jsxf    numeric(15,2)        格式为HHMMSS
    double                            db_fee_sxf;                            //手续费    fee_sxf    numeric(15,2)        格式为HHMMSS
    double                            db_fee_yhs;                            //印花税    fee_yhs    numeric(15,2)        格式为HHMMSS
    double                            db_fee_ghf;                            //过户费    fee_ghf    numeric(15,2)        格式为HHMMSS
    double                            db_fee_qsf;                            //清算费    fee_qsf    numeric(15,2)        格式为HHMMSS
    double                            db_fee_jygf;                        //交易规费    fee_jygf    numeric(15,2)        格式为HHMMSS
    double                            db_fee_jsf;                            //经手费    fee_jsf    numeric(15,2)        格式为HHMMSS
    double                            db_fee_zgf;                            //证管费    fee_zgf    numeric(15,2)        格式为HHMMSS
    double                            db_fee_qtf;                            //其他费    fee_qtf    numeric(15,2)        格式为HHMMSS
    double                            db_feefront;                        //前台费用    feefront    numeric(15,2)        格式为HHMMSS
    double                            db_fundeffect;                        //资金发生数    fundeffect    numeric(15,2)        格式为HHMMSS
    double                            db_fundbal;                            //使用金额    fundbal    numeric(15,2)        格式为HHMMSS
    double                            db_settrate;                        //结算汇率    settrate    numeric(12,8)        格式为HHMMSS
};


struct    SHKHistoryDealQueryA
{
    DWORD        dwCount;
    SHKHistoryDealQueryAItem        pItem[0];
};

//港股通参考汇率信息查询（430009）功能代码    430009    功能描述    港股通参考汇率查询
#define PID_TRADEGATE__HKEXCHANGERATE_QUERY                            (PID_TRADE_GATE_BASE+407)

struct    SHKExchangeRateQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)    Y
    char                            sz_moneytype[MONEYTYPE_LEN];        //币种    moneytype    char(1)    Y
};

struct    SHKExchangeRateQueryAItem
{
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)
    char                            sz_moneytype[MONEYTYPE_LEN];        //币种    moneytype    char(1)
    double                            db_buyrate;                            //买入参考汇率    buyrate    numeric(12，8)
    double                            db_daybuyriserate;                    //日间买入参考汇率浮动比例    daybuyriserate    numeric(12，8)
    double                            db_nightbuyriserate;                //夜市买入参考汇率浮动比例    nightbuyriserate    numeric(12，8)
    double                            db_salerate;                        //卖出参考汇率    salerate    numeric(12，8)
    double                            db_daysaleriserate;                    //日间卖出参考汇率浮动比例    daysaleriserate    numeric(12，8)
    double                            db_nightsaleriserate;                //夜市卖出参考汇率浮动比例    nightsaleriserate    numeric(12，8)
    double                            db_midrate;                            //中间参考汇率    midrate    numeric(12，8)
    int                                n_sysdate;                            //使用日期    sysdate    int
    double                            db_settrate;                        //结算汇率    settrate    numeric(12，8)
    char                            sz_remark[REMARK_LEN];                //备注    remark    char(128)
};

struct SHKExchangeRateQueryA
{
    DWORD        dwCount;
    SHKExchangeRateQueryAItem        pItem[0];
};

//港股通标的证券信息查询（430015）功能代码    430015    功能描述    港股通标的证券查询
#define PID_TRADEGATE__HKTRADESECURITIES_QUERY                        (PID_TRADE_GATE_BASE+408)

struct    SHKTradeSecuritiesQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)    Y
    char                            sz_stkcode[STKCODE_LEN];            //证券代码    stkcode    char(8)    Y
};


struct    SHKTradeSecuritiesQueryAItem
{
    char                            sz_Market[MARKET_LEN];                //交易市场    Market    char(1)
    char                            sz_Stkcode[STKCODE_LEN];            //证券代码    Stkcode    char(8)
    char                            sz_Stkname[STKNAME_LEN];            //证券名称    Stkname    char(8)
    char                            sz_zstrdstatus[2];                    //整手交易状态    zstrdstatus    char(1)        A' 买卖'B' 只买'S' 只卖'N' 不可买卖
    char                            sz_lgtrdstatus[2];                    //零股交易状态    lgtrdstatus    char(1)        A' 买卖'B' 只买'S' 只卖 'N' 不可买卖
    int                            n_upddate;                                //更新日期    upddate    int        A' 买卖'B' 只买'S' 只卖 'N' 不可买卖
    char                            sz_remark[REMARK_LEN];                //备注    remark    char(128)        A' 买卖'B' 只买'S' 只卖 'N' 不可买卖
};

struct    SHKTradeSecuritiesQueryA
{
    DWORD        dwCount;
    SHKTradeSecuritiesQueryAItem        pItem[0];
};
//港股通资金资产查询(430004)    功能代码    430004
#define PID_TRADEGATE__FUNDSASSETS_QUERY                            (PID_TRADE_GATE_BASE+409)

struct    SFundsAssetsQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    __int64                            n64_fundid;                            //资金帐号    fundid    int64    Y
    char                            sz_orgid[ORGID_LEN];                //机构编码    orgid    char(4)    Y
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币代码    moneytype    char(1)    N    不传查全部
    char                            sz_fundcode[9];                        //资金科目    fundcode    char(8)    N    不传查全部
};

struct    SFundsAssetsQueryAItem
{
    char                            sz_orgid[ORGID_LEN];                //机构编码    orgid    char(4)
    __int64                            n64_fundid;                            //资金帐号    fundid    int64
    char                            sz_fundcode[9];                        //资金科目    fundcode    char(8)
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币代码    moneytype    char(1)
    double                            db_lasttotalamt;                    //上日总数    lasttotalamt    numeric(19,2)
    double                            db_totalamt;                        //当前总数    totalamt    numeric(19,2)
    double                            db_avlamt;                            //可用数    avlamt    numeric(19,2)
    double                            db_funduncomein;                    //在途入账    funduncomein    numeric(19,2)
    double                            db_funduncomeout;                    //在途出账    funduncomeout    numeric(19,2)
    double                            db_fundrealin;                        //实时入账    fundrealin    numeric(19,2)
    double                            db_fundrealout;                        //实时出账    fundrealout    numeric(19,2)
    double                            db_fundnightout;                    //夜市出账资金    fundnightout    numeric(19,2)
    double                            db_fundtranin;                        //划入金额    fundtranin    numeric(19,2)
    double                            db_fundtranout;                        //划出金额    fundtranout    numeric(19,2)
    double                            db_otheramt;                        //其他金额    otheramt    numeric(19,2)
    char                            sz_remark[REMARK_LEN];                //备注信息    remark     char(128)
};

struct    SFundsAssetsQueryA
{
    DWORD        dwCount;
    SFundsAssetsQueryAItem        pItem[0];
};

//证券组合费交收明细（430005）功能代码    430005功能描述    港股通证券组合费交收明细
#define PID_TRADEGATE__SECURITYCOMBINE_FEE_QUERY                    (PID_TRADE_GATE_BASE+410)

struct    SSecurityCombineFeeQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    __int64                            n64_fundid;                            //资金帐号    fundid    int64    Y
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币代码    moneytype    char(1)    Y
    char                            sz_orgid[ORGID_LEN];                //机构编码    orgid    char(4)    Y
    int                                n_strdate;                            //清算开始时间    strdate    int    N
    int                                n_enddate;                            //清算结束时间    enddate    int    N
    int                                n_count;                            //请求行数    count    int    Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];            //定位串    poststr    char(32)    Y    第一次取填空
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)    N    第一次取填空
};

struct    SSecurityCombineFeeQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)        查询返回定位值
    __int64                            n64_fundid;                            //资金帐号    fundid    int64        查询返回定位值
    char                            sz_orgid[ORGID_LEN];                //机构编码    orgid    char(4)        查询返回定位值
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币代码    moneytype    char(1)        查询返回定位值
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)        查询返回定位值
    char                            sz_secuid[SECUID_LEN];                //证券账号    secuid    char(10)        查询返回定位值
    char                            sz_debtsid[2];                        //负债类型    debtsid    char(1)        查询返回定位值
    char                            sz_sno[17];                            //清算流水号    sno    char(16)        查询返回定位值
    int                                n_qsdate;                            //清算日期    qsdate    int        查询返回定位值
    int                                n_jsdate;                            //交收日期    jsdate    int        查询返回定位值
    double                            db_lastmktvalue;                    //T-1日持有市值    lastmktvalue    numeric(19,2)        查询返回定位值
    double                            db_settrate;                        //结算汇率    settrate    numeric(12,8)        查询返回定位值
    double                            db_combinfee_hk;                    //港币应收付金额    combinfee_hk    numeric(19,2)        查询返回定位值
    double                            db_combinfee_rmb;                    //人民币应收付金额    combinfee_rmb    numeric(19,2)        查询返回定位值
    char                            sz_remark[REMARK_LEN];                //备注    remark    char(128)        查询返回定位值
};


struct    SSecurityCombineFeeQueryA
{
    DWORD        dwCount;
    SSecurityCombineFeeQueryAItem        pItem[0];
};


//港股通未交收明细查询(430018)    功能代码    430018    功能描述    港股通未交收明细查询
#define PID_TRADEGATE__UNPAID_DETAIL_QUERY                            (PID_TRADE_GATE_BASE+420)

struct    SUnpaidDetailQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)    N    不送查询全部
    __int64                            n64_fundid;                            //资金帐户    fundid    int    N    不送查询全部
    char                            sz_secuid[SECUID_LEN];                //股东代码    secuid    char(10)    N    不送查询全部
    char                            sz_stkcode[STKCODE_LEN];            //证券代码    stkcode    char(8)    N    不送查询全部
    char                            sz_qryflag[QRYFLAG_LEN];            //查询方向    qryflag    char(1)     Y    向下/向上查询方向
    int                                n_count;                            //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)    Y    第一次填空
};

struct    SUnpaidDetailQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)
    int                                n_orderdate;                        //委托日期    orderdate    int
    int                                n_ordersno;                            //成交日期    ordersno    int
    __int64                            n64_custid;                            //客户代码    custid    int
    char                            sz_custname[17];                    //客户姓名    custname    char(16)
    __int64                            n64_fundid;                            //资金账户    fundid    int
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币    moneytype    char(1)
    char                            sz_orgid[ORGID_LEN];                //机构编码    orgid    char(4)
    char                            sz_secuid[SECUID_LEN];                //股东代码    secuid    char(10)
    char                            sz_busitype[5];                        //业务类别    busitype    char(4)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    char                            sz_orderid[11];                        //合同序号    orderid    char(10)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    char                            sz_stkcode[STKCODE_LEN];            //证券代码    stkcode    char(8)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    char                            sz_stkname[STKNAME_LEN];            //证券名称    stkname    char(8)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_orderprice;                        //委托价格(港币)    orderprice    numeric(9,3)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    int                                n_orderqty;                            //委托数量    orderqty    int        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    int                                n_matchqty;                            //成交数量    matchqty    int        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_matchprice;                        //成交价格(人民币)    matchprice    numeric(9,3)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_matchamt;                        //成交金额    matchamt    numeric(19,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_clearedamt;                        //清算金额    clearedamt    numeric(19,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_yhs;                            //印花税    fee_yhs    numeric(12,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_jsxf;                        //标准手续费    fee_jsxf    numeric(12,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_sxf;                            //手续费    fee_sxf    numeric(12,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_qsf;                            //股份交收费    fee_qsf    numeric(12,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_jygf;                        //交易费    fee_jygf    numeric(12,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_feefront;                        //前台费用    feefront    numeric(12,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_jsf;                            //交易系统使用费费    fee_jsf    numeric(12,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_zgf;                            //交易征费    fee_zgf    numeric(12,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_one_yhs;                        //一级印花税    fee_one_yhs    numeric(15,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_one_ghf;                        //一级过户费    fee_one_ghf    numeric(15,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_one_qsf;                        //一级股份交收费    fee_one_qsf    numeric(15,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_one_jygf;                    //一级交易费    fee_one_jygf    numeric(15,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_one_jsf;                        //一级交易系统使用费    fee_one_jsf    numeric(15,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_one_zgf;                        //一级交易征费    fee_one_zgf    numeric(15,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_one_qtf;                        //一级其他费    fee_one_qtf    numeric(15,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_fee_one_fxj;                        //一级风险金    fee_one_fxj    numeric(15,2)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    double                            db_settrate;                        //结算汇率    settrate    numeric(15,5)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
    char                            sz_remark[REMARK_LEN];                //备注    remark    char(128)        hgmc 港股通股票卖hgmr 港股通股票买入hqmc 港股通权证卖出hqmr 港股通权证买入hrgc 港股通股认购权出hrgr 港股通股认购权入
};


struct    SUnpaidDetailQueryA
{
    DWORD        dwCount;
    SUnpaidDetailQueryAItem        pItem[0];
};

//港股通客户负债查询（430006）功能代码    430006    功能描述    港股通客户负债查询
#define PID_TRADEGATE__HKCUSTOMERDEBT_QUERY                                    (PID_TRADE_GATE_BASE+421)

struct    SHKCustmerDebtQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    __int64                            n64_fundid;                            //资金账号    fundid    int64    N
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币代码    moneytype    char(1)    N
    char                            sz_debtsid[2];                        //负债类型    debtsid    char(1)    N
    char                            sz_orgid[ORGID_LEN];                //机构编码    orgid    char(4)    N
};

struct    SHKCustmerDebtQueryAItem
{
    char                            sz_orgid[ORGID_LEN];                //机构编码    orgid    char(4)
    __int64                            n64_custid;                            //客户代码    custid    int64
    __int64                            n64_fundid;                            //资金帐号    fundid    int64
    char                            sz_debtsid[2];                        //负债类型    debtsid    char(1)
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币代码    moneytype    char(1)
    double                            db_unpayamt;                        //未支付金额    unpayamt    numeric(19,2)
    double                            db_sumpaidamt;                        //已支付总金额    sumpaidamt    numeric(19,2)
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)
    char                            sz_secuid[SECUID_LEN];                //股东代码    secuid    char(10)
    double                            db_sumdebtamt;                        //应付总金额    sumdebtamt    numeric(19,2)
};


struct    SHKCustmerDebtQueryA
{
    DWORD        dwCount;
    SHKCustmerDebtQueryAItem        pItem[0];
};

//港股通交易最小价差查询（430008）功能代码    430008    功能描述    港股通最小价差查询
#define PID_TRADEGATE__MINPRICEDIFF_QUERY                            (PID_TRADE_GATE_BASE+422)

struct    SMinPriceDiffQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)    Y
    char                            sz_stktype[2];                        //证券类别    stktype    char(1)    N
    double                            db_price;                            //委托价格    price    numeric(9，3)    N
};

struct    SMinPriceDiffQueryAItem
{
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)
    char                            sz_stktype[2];                        //证券类别    stktype    char(1)
    double                            db_beginprice;                        //价格下限    beginprice    numeric(9，3)
    double                            db_endprice;                        //价格上限    endprice    numeric(9，3)
    double                            db_priceunit;                        //价位    priceunit    numeric(9，3)
};

struct    SMinPriceDiffQueryA
{
    DWORD        dwCount;
    SMinPriceDiffQueryAItem        pItem[0];
};
//投票委托查询

//公司行为查询

//对帐单查询(430017)    功能代码    430017    功能描述    港股通对帐单查询
#define PID_TRADEGATE__HKCROSSCHECK_BILL_QUERY                        (PID_TRADE_GATE_BASE+425)

struct    SHKCrossCheckBillQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    int                                n_strdate;                            //起始日期    strdate    int    Y
    int                                n_enddate;                            //终止日期    enddate    int    Y
    __int64                            n64_fundid;                            //资金帐户    fundid    int    N
    char                            sz_printflag[2];                    //重打标识    printflag    char(1)    Y    1－不允许重打0－允许重打重打标志=1时，资金帐户为必输
    char                            sz_qryflag[QRYFLAG_LEN];            //查询方向    qryflag    char(1)     Y    向下/向上查询方向
    int                                n_count;                            //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)    y    第一次填空
};

struct    SHKCrossCheckBillQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)        查询返回定位值
    int                                n_cleardate;                        //清算日期    cleardate    int        查询返回定位值
    int                                n_bizdate;                            //交收日期    bizdate    int        查询返回定位值
    int                                n_orderdate;                        //发生日期    orderdate    int        查询返回定位值
    int                                n_ordertime;                        //发生时间    ordertime    int        查询返回定位值
    int                                n_digestid;                            //业务代码    digestid    int        查询返回定位值
    char                            sz_digestname[17];                    //业务说明    digestname    char(16)        查询返回定位值
    __int64                            n64_custid;                            //客户代码    custid    int        查询返回定位值
    char                            sz_custname[17];                    //客户姓名    custname    char(16)        查询返回定位值
    char                            sz_orgid[ORGID_LEN];                //机构代码    orgid    char(4)        查询返回定位值
    __int64                            n64_fundid;                            //资金帐号    fundid    int        查询返回定位值
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币代码    moneytype    char(1)        查询返回定位值
    char                            sz_market[MARKET_LEN];                //市场代码    market    char(1)        查询返回定位值
    char                            sz_secuid[SECUID_LEN];                //股东代码    secuid    char(32)        查询返回定位值
    double                            db_fundeffect;                        //资金发生数    fundeffect    numeric(15,2)        查询返回定位值
    double                            db_fundbal;                            //资金本次余额    fundbal    numeric(15,2)        查询返回定位值
    int                                n_stkbal;                            //股份本次余额    stkbal    int        查询返回定位值
    char                            sz_orderid[11];                        //合同序号    orderid    char(10)        查询返回定位值
    char                            sz_stkcode[STKCODE_LEN];            //证券代码    stkcode    char(16)        查询返回定位值
    char                            sz_stkname[STKNAME_LEN];            //证券名称    stkname    char(16)        查询返回定位值
    char                            sz_bsflag[3];                        //买卖类别    bsflag    char(2)        查询返回定位值
    int                                n_matchqty;                            //成交数量    matchqty    int        查询返回定位值
    double                            db_matchprice;                        //成交价格    matchprice    numeric(9,3)        查询返回定位值
    double                            db_matchamt;                        //成交金额    matchamt    numeric(15,2)        查询返回定位值
    double                            db_settrate;                        //结算汇率    settrate    numeric(15,5)        查询返回定位值
};


struct    SHKCrossCheckBillQueryA
{
    DWORD        dwCount;
    SHKCrossCheckBillQueryAItem        pItem[0];
};

//交割单查询(430016) 功能代码    430016    功能描述    港股通交割单查询
#define PID_TRADEGATE__HKDELIVERY_BILL_QUERY                            (PID_TRADE_GATE_BASE+426)

struct    SHKDeliveryBillQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    int                                n_strdate;                            //起始日期    strdate    int    Y
    int                                n_enddate;                            //终止日期    enddate    int    Y
    __int64                            n64_fundid;                            //资金帐户    fundid    int    N
    char                            sz_printflag[2];                    //重打标识    printflag    char(1)    Y    1－不允许重打0－允许重打
    char                            sz_qryflag[QRYFLAG_LEN];            //查询方向    qryflag    char(1)     Y    向下/向上查询向
    int                                n_count;                            //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)    Y    第一次填空
};

struct    SHKDeliveryBillQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)        查询返回定位值
    int                                n_cleardate;                        //清算日期    cleardate    int        查询返回定位值
    int                                n_bizdate;                            //交收日期    bizdate    int        查询返回定位值
    int                                n_orderdate;                        //委托日期    orderdate    int        查询返回定位值
    int                                n_ordertime;                        //委托时间    ordertime    int        查询返回定位值
    int                                n_digestid;                            //业务代码    digestid    int        见备注信息
    char                            sz_digestname[17];                    //业务代码说明    digestname    char(16)        见备注信息
    __int64                            n64_fundid;                            //资金帐户    fundid    int        见备注信息
    char                            sz_moneytype[MONEYTYPE_LEN];        //货币代码    moneytype    char(1)        见备注信息
    char                            sz_market[MARKET_LEN];                //交易所代码    market    char(1)        见备注信息
    char                            sz_secuid[SECUID_LEN];                //股东代码    secuid    char(32)        见备注信息
    char                            sz_custname[17];                    //客户姓名    custname    char(16)        见备注信息
    char                            sz_orderid[11];                        //合同序号    orderid    char(10)        见备注信息
    char                            sz_stkcode[STKCODE_LEN];            //证券代码    stkcode    char(16)        见备注信息
    char                            sz_stkname[STKNAME_LEN];            //证券名称    stkname    char(16)        见备注信息
    char                            sz_bsflag[3];                        //买卖类别    bsflag    char(2)        见备注信息
    int                                n_matchtime;                        //成交时间    matchtime    int        见备注信息
    char                            sz_matchcode[21];                    //成交号码    matchcode    char(20)        见备注信息
    int                                n_matchtimes;                        //成交笔数    matchtimes    int        见备注信息
    int                                n_matchqty;                            //成交数量    matchqty    int        见备注信息
    double                            db_matchprice;                        //成交价格    matchprice    numeric(9,3)        见备注信息
    double                            db_matchamt;                        //成交金额    matchamt    numeric(15,2)        见备注信息
    double                            db_fundeffect;                        //清算金额    fundeffect    numeric(15,2)        见备注信息
    double                            db_fee_yhs;                            //印花税    fee_yhs    numeric(12,2)        见备注信息
    double                            db_fee_jsxf;                        //标准手续费    fee_jsxf    numeric(12,2)        见备注信息
    double                            db_fee_sxf;                            //手续费    fee_sxf    numeric(12,2)        见备注信息
    double                            db_fee_qsf;                            //股份交收费    fee_qsf    numeric(12,2)        见备注信息
    double                            db_fee_jygf;                        //交易费    fee_jygf    numeric(12,2)        见备注信息
    double                            db_feefront;                        //前台费用    feefront    numeric(12,2)        见备注信息
    double                            db_fee_jsf;                            //交易系统使用费费    fee_jsf    numeric(12,2)        见备注信息
    double                            db_fee_zgf;                            //交易征费    fee_zgf    numeric(12,2)        见备注信息
    double                            db_fee_qtf;                            //其它费    fee_qtf    numeric(15,2)        见备注信息
    double                            db_fundbal;                            //资金本次余额    fundbal    numeric(15,2)        见备注信息
    int                                n_stkbal;                            //股份本次余额    stkbal    int        见备注信息
    int                                n_orderqty;                            //委托数量    orderqty    int        见备注信息
    double                            db_orderprice;                        //委托价格(港币)    orderprice    numeric(9,3)        见备注信息
    double                            db_fee_one_yhs;                        //一级印花税    fee_one_yhs    numeric(15,2)        见备注信息
    double                            db_fee_one_ghf;                        //一级过户费    fee_one_ghf    numeric(15,2)        见备注信息
    double                            db_fee_one_qsf;                        //一级股份交收费    fee_one_qsf    numeric(15,2)        见备注信息
    double                            db_fee_one_jygf;                    //一级交易费    fee_one_jygf    numeric(15,2)        见备注信息
    double                            db_fee_one_jsf;                        //一级交易系统使用费    fee_one_jsf    numeric(15,2)        见备注信息
    double                            db_fee_one_zgf;                        //一级交易征费    fee_one_zgf    numeric(15,2)        见备注信息
    double                            db_fee_one_qtf;                        //一级其他费    fee_one_qtf    numeric(15,2)        见备注信息
    double                            db_fee_one_fxj;                        //一级风险金    fee_one_fxj    numeric(15,2)        见备注信息
    double                            db_settrate;                        //结算汇率    settrate    numeric(15,5)        见备注信息
};


struct    SHKDeliveryBillQueryA
{
    DWORD        dwCount;
    SHKDeliveryBillQueryAItem        pItem[0];
};

//额度查询 港股通市场交易状态信息查询（430014）功能代码    430014    功能描述    港股通市场交易状态查询
#define    PID_TRADEGATE__HKMARKETSTATE_QUERY                            (PID_TRADE_GATE_BASE+427)

struct    SHKMarketStateQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)    Y
};

struct    SHKMarketStateQueryA
{
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)
    __int64                            n64_quotabal;                        //每日初始额度    quotabal    int64
    __int64                            n64_quotaavl;                        //日中剩余额度    quotaavl    int64
    char                            sz_quotastatus[2];                    //额度状态    quotastatus    char(1)        '1'额度用完'2'额度可用
    char                            sz_mkttrdstatus[2];                    //市场交易状态    mkttrdstatus    char(1)        'A' 买卖'B' 只买'S' 只卖'N' 不可买卖
    int                                n_upddate;                            //更新日期    upddate    int        'A' 买卖'B' 只买'S' 只卖'N' 不可买卖
    char                            sz_remark[REMARK_LEN];                //备注    remark    char(128)        'A' 买卖'B' 只买'S' 只卖'N' 不可买卖
};

//通知信息，未找到对应项

//港股通交易日历查询(430019)    功能代码    430019    功能描述    港股通交易日历查询
#define PID_TRADEGATE__HKTRADECALENDAR_QUERY                            (PID_TRADE_GATE_BASE+429)

struct    SHKTradeCalendarQuery
{
    STradeGateUserInfo                userinfo;                            //用户信息
    int                                n_phydate;                            //物理日期    phydate    int    N    不送查询全部
    int                                n_enddate;                            //结束日期    enddate    int    N    结束日期
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)    N    不送查港股通
    int                                n_count;                            //请求行数    count    int     Y    每次取的行数
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)    Y    第一次填空
};

struct    SHKTradeCalendarQueryAItem
{
    char                            sz_poststr[POSTSTR_LEN];            //定位串      poststr    char(32)
    char                            sz_market[MARKET_LEN];                //交易市场    market    char(1)
    char                            sz_moneytype[MONEYTYPE_LEN];        //币种    moneytype    char(1)
    int                                n_phydate;                            //物理日期    phydate    int
    char                            sz_businessflag[2];                    //交易日标识    businessflag    char(1)        '0' 交易日'1' 非交易日
    char                            sz_commitflag[2];                    //交收日标识    commitflag    char(1)        '0' 交收日'1' 非交收日
};


struct    SHKTradeCalendarQueryA
{
    DWORD        dwCount;
    SHKTradeCalendarQueryAItem        pItem[0];
};

/*************************************************************************************************
 ***************************基金业务数据包*********************************************************
 **************************************************************************************************/

//基金分红方式设置，请参考买卖委托(410411)业务


/************************************************************************************************
 *******************************常用查询业务
 **************************************************************************************************/
//当日成交汇总(410516)功能代码    410516功能描述    当日成交汇总查询, 按证券代码和买卖方向
#define PID_TRADE_GATE__DEALAGGREGATE_QUERY (PID_TRADE_GATE_BASE+500)

// struct    SDealAggregateQuery
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)    N    不送查询全部
//     __int64                            n64_fundid;                                //资金帐户    fundid    int    N    不送查询全部
//     char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)    N    不送查询全部
//     char                            sz_bankcode[5];                            //外部银行    bankcode    char(4)    N    不送查询全部
// };
//
// struct    SDealAggregateQueryAItem
// {
//     int                                n_operdate;                                //交易日期    operdate    int
//     __int64                            n64_custid;                                //客户代码    custid    int
//     char                            sz_custname[17];                        //客户姓名    custname    char(16)
//     char                            sz_orgid[ORGID_LEN];                    //机构编码    orgid    char(4)
//     char                            sz_bsflag[3];                            //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                            sz_market[MARKET_LEN];                    //交易市场    market    char(1)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                            sz_stkname[STKNAME_LEN];                //证券名称    stkname    char(8)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     double                            db_matchprice;                            //成交价格    matchprice    numeric(9,3)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     int                                n_matchqty;                                //成交数量    matchqty    bigint        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     double                            db_matchamt;                            //成交金额    matchamt    numeric(15,2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                            sz_matchtype[2];                        //成交类型    matchtype    char(1)        0-
//     char                            sz_bankcode[5];                            //外部银行    bankcode    char(4)        0-
//     char                            sz_bankid[33];                            //外部账户    bankid    char(32)        0-
// };
//
//
// struct    SDealAggregateQueryA
// {
//     DWORD        dwCount;
//     SDealAggregateQueryAItem        pItem[0];
// };

//当日资金流水查询(410523) 功能代码    410523 功能描述    当日资金流水查询
#define PID_TRADE_GATE__FUNDSFLOWS_QUERY       (PID_TRADE_GATE_BASE+501)

// struct    SFundsFlowsQuery
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     __int64                            n64_fundid;                                //资金帐户    fundid    int    N
//     char                            sz_qryflag[QRYFLAG_LEN];                //查询方向    qryflag    char(1)     Y    向下/向上查询方向
//     int                                n_count;                                //请求行数    count    int     Y    每次取的行数
//     char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)    y    第一次填空
// };
//
// struct    SFundsFlowsQueryAItem
// {
//     char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)        查询返回定位值
//     int                                n_cleardate;                            //清算日期    cleardate    int        查询返回定位值
//     int                                n_bizdate;                                //交收日期    bizdate    int        查询返回定位值
//     int                                n_orderdate;                            //发生日期    orderdate    int        查询返回定位值
//     int                                n_ordertime;                            //发生时间    ordertime    int        查询返回定位值
//     int                                n_digestid;                                //业务代码    digestid    int        查询返回定位值
//     char                            sz_digestname[17];                        //业务说明    digestname    char(16)        查询返回定位值
//     __int64                            n64_custid;                                //客户代码    custid    int        查询返回定位值
//     char                            sz_custname[17];                        //客户姓名    custname    char(16)        查询返回定位值
//     char                            sz_orgid[ORGID_LEN];                    //机构代码    orgid    char(4)        查询返回定位值
//     __int64                            n64_fundid;                                //资金帐号    fundid    int        查询返回定位值
//     char                            sz_moneytype[MONEYTYPE_LEN];            //货币代码    moneytype    char(1)        查询返回定位值
//     char                            sz_market[MARKET_LEN];                    //市场代码    market    char(1)        查询返回定位值
//     char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(32)        查询返回定位值
//     double                            db_fundeffect;                            //资金发生数    fundeffect    numeric(15,2)        查询返回定位值
//     double                            db_fundbal;                                //资金本次余额    fundbal    numeric(15,2)        查询返回定位值
//     int                                n_stkbal;                                //股份本次余额    stkbal    int        查询返回定位值
//     char                            sz_orderid[11];                            //合同序号    orderid    char(10)        查询返回定位值
//     char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(16)        查询返回定位值
//     char                            sz_stkname[STKNAME_LEN];                //证券名称    stkname    char(16)        查询返回定位值
//     char                            sz_bsflag[3];                            //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     int                                n_matchqty;                                //成交数量    matchqty    int        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     double                            db_matchprice;                            //成交价格    matchprice    numeric(9,3)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     double                            db_matchamt;                            //成交金额    matchamt    numeric(15,2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
// };
//
//
// struct    SFundsFlowsQueryA
// {
//     DWORD        dwCount;
//     SFundsFlowsQueryAItem        pItem[0];
// };

//查询系统当前状态(410232)    功能代码    410232    功能描述    查询当前系统状态
#define PID_TRADE_GATE__SYSSTATE_QUERY       (PID_TRADE_GATE_BASE+502)

// struct    SSystemStateQuery
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
// };
//
struct    SSystemStateQueryA
{
    int                            n_sysdate;                                    //系统日期    sysdate    int
    int                            n_orderdate;                                //委托日期    orderdate      int
#define SYSSTATUS_RUNNING            0        //正常运行
#define SYSSTATUS_INITIALIZING        1        //正在初始化
#define SYSSTATUS_PREPARELIQUIDATE    2        //准备清算
#define SYSSTATUS_LIQUIDATED        3        //清算完成
#define SYSSTATUS_PREPAREDELIVERY    4        //准备交收
#define SYSSTATUS_DELIVERING        5        //正在交收
#define SYSSTATUS_DELIVEREDNOTINIT  9        //交收完成没有初始化
    char                        sz_status[2];                                //系统运行状态    status    char(1)        ‘0’正常运行‘1’正在初始化‘2’清算准备‘3’清算完成‘4’交收准备‘5’正在交收‘9’交收完成没有初始化
    char                        sz_nightremark[33];                            //夜市状态注释    nightremark    varchar(32)
    int                            n_systime;                                    //物理时间      systime    int
};


//取字典信息(410220)    功能代码    410220    功能描述    取字典信息
#define PID_TRADE_GATE__DICTIONARY_INFO_QUERY         (PID_TRADE_GATE_BASE+503)
/*字典条目:全局字典在返回时机构代码为：空
 Ajys     市场            Ecdbz    撤单标志
 Ammlb    买卖类别        Ecdwt    撤单委托
 Aqsmc    券商名称        Ejysjd   允许交易时间段
 Bkhbz    客户标志        Ejyzyjd  交易作用阶段
 Bkhllpd  客户联络频度    Emmbz    买卖标志
 Bkhlx    客户类型        Emmlb    买卖类别
 Bkhlxfs  客户联系方式    Ezqlb    证券类别
 Bkhxb    客户性别        Ezqzt    证券状态
 Bxl      学历            Fyhdm    银行
 Bzdbz    指定标志        Fyhmmbz  银行密码校验标志
 Bzgdbz   主股东标志      Fyhzhbz  银行帐号校验标志
 Bzhbz    多帐号标志      Fyhzzbs  银行转帐标识
 Bzhzt    帐户状态        Fyztdm   银证通
 Bzjzhgl  资金帐号关联    Fzjjnbz  证件校验标志
 Bzzhbz   主资金帐号标志  Fzjmmbz  资金密码校验标志
 Cgyzy    摘要显示        Fzzfs    转帐方式
 Cjlx     成交类型        Gfqf     发起方
 Dgtyhzt  银行状态        Gfqfs    发起方式
 Djsbz    结算币种
 Dzzzt    转帐状态
 */
//数据字典定义
#define DICTNAME_FUNDSTATUS            "Njjzt"                //基金状态
#define DICTNAME_INVESTPERIOD       "Ntzqx"                //投资期限
#define DICTNAME_INVESTTYPE            "Ntzpz"                //投资品种
#define DICTNAME_EXPECTEDPROFIT        "Bsdxyqsy"            //期望收益
#define DICTNAME_CUSTSEX            "Bkhxb"                //客户性别
#define DICTNAME_IDTYPE                "Bzjlx"                //证件类型
#define DICTNAME_CUSTTYPE            "Nkhlx"                //账户类型
#define DICTNAME_ACCSTATUS            "Nzhzt"                //账户状态
#define DICTNAME_RISKLEVEL            "Bkhfxjb"            //风险级别
#define DICTNAME_ORDERSTATUS        "Ewtzt"                //委托状态
#define DICTNAME_STKTYPE            "Ezqlb"                //证券类别
#define DICTNAME_PRODUCTRISKLEVEL    "Bcpfxdj"            //产品风险等级
// struct    SDictionnaryInfoQuery
// {
//     STradeGateUserInfo                userinfo;                //用户信息
//     char                            sz_dictitem[9];                                //字典条目    dictitem    char(8)    N
//     char                            sz_subitem[5];                                //字典子项    subitem    char(4)    N
// };
//
//
// struct    SDictionnaryInfoQueryAItem
// {
//     char                            sz_orgid[ORGID_LEN];                        //机构代码    orgid    Char(4)        若是全局，返回:空格,其他返回对应的机构代码
//     char                            sz_dictitem[9];                                //字典条目    dictitem    char(8)
//     char                            sz_subitem[5];                                //字典子项    subitem    char(4)
//     char                            sz_subitemname[33];                            //子项名称    subitemname    char(32)
//     char                            sz_dispflag[2];                                //显示标识    dispflag    char(1)        0 显示 1 不显示
//     char                            sz_addenable[2];                            //字典可否增加子项    addenable    char(1)        ‘0’ 可增加 ‘1’ 不可增加
//     char                            sz_itemname[33];                            //字典条目名称    itemname    char(32)
// };
//
// struct    SDictionnaryInfoQueryA
// {
//     DWORD        dwCount;
//     SDictionnaryInfoQueryAItem        pItem[0];
// };


//功能代码    410514 功能描述    当日委托汇总查询, 按证券代码和买卖方向
#define PID_TRADE_GATE__ORDER_AGGREGATE_QUERY         (PID_TRADE_GATE_BASE+504)

// struct    SOrderAggregateQuery
// {
//     STradeGateUserInfo                userinfo;                                        //用户信息
//     char                            sz_market[MARKET_LEN];                            //交易市场    market    char(1)    N
//     __int64                            n64_fundid;                                        //资金帐户    fundid    int    N
//     char                            sz_stkcode[STKCODE_LEN];                        //证券代码    stkcode    char(8)    N
//     char                            sz_bankcode[5];                                    //外部银行    bankcode    char(4)    N
// };
//
// struct    SOrderAggregateQueryAItem
// {
//     int                                n_orderdate;                                        //委托日期    orderdate    int
//     __int64                            n64_custid;                                            //客户代码    custid    int
//     char                            sz_custname[17];                                //客户姓名    custname    char(16)
//     char                            sz_orgid[ORGID_LEN];                            //机构编码    orgid    char(4)
//     char                            sz_bsflag[3];                                    //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                            sz_market[MARKET_LEN];                            //交易市场    market    char(1)
//     char                            sz_stkname[STKNAME_LEN];                        //证券名称    stkname    char(8)
//     char                            sz_stkcode[STKCODE_LEN];                        //证券代码    stkcode    char(8)
//     double                            db_orderprice;                                    //委托价格    orderprice    numeric(9,3)
//     int                            n_orderqty;                                            //委托数量    orderqty    bigint
//     double                            db_orderfrzamt;                                    //委托金额    orderfrzamt    numeric(15,2)
//     int                            n_matchqty;                                            //成交数量    matchqty    bigint
//     int                            n_cancelqty;                                        //撤单数量    cancelqty    bigint
//     double                            db_matchamt;                                    //成交金额    matchamt    numeric(15,2)
// };
//
//
// struct    SOrderAggregateQueryA
// {
//     DWORD        dwCount;
//     SOrderAggregateQueryAItem        pItem[0];
// };

//历史委托汇总(411515)功能代码    411515功能描述    历史委托汇总查询, 按证券代码和买卖方向

#define PID_TRADE_GATE__HISTORYORDER_AGGREGATE_QUERY         (PID_TRADE_GATE_BASE+505)

// struct    SHistoryOrderAggregateQuery
// {
//     STradeGateUserInfo                userinfo;                                        //用户信息
//     int                                n_strdate;                                        //起始日期    strdate    int    Y
//     int                                n_enddate;                                        //终止日期    enddate    int    Y
//     __int64                            n64_fundid;                                        //资金帐户    fundid    int    N
//     char                            sz_market[MARKET_LEN];                            //交易市场    market    char(1)    N    不送查询全部
//     char                            sz_stkcode[STKCODE_LEN];                        //证券代码    stkcode    char(8)    N    不送查询全部
//     char                            sz_bankcode[5];                                    //外部银行    bankcode    char(4)    N    不送查询全部
// };
//
// struct    SHistoryOrderAggregateQueryAItem
// {
//     int                                n_orderdate;                                    //委托日期    orderdate    int
//     __int64                            n64_custid;                                        //客户代码    custid    int
//     char                            sz_custname[17];                                //客户姓名    custname    char(16)
//     char                            sz_orgid[ORGID_LEN];                            //机构编码    orgid    char(4)
//     char                            sz_bsflag[3];                                    //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                            sz_market[MARKET_LEN];                            //交易市场    market    char(1)
//     char                            sz_stkname[STKNAME_LEN];                        //证券名称    stkname    char(8)
//     char                            sz_stkcode[STKCODE_LEN];                        //证券代码    stkcode    char(8)
//     double                            db_orderprice;                                    //委托价格    orderprice    numeric(9,3)
//     int                                n_orderqty;                                        //委托数量    orderqty    bigint
//     double                            db_orderfrzamt;                                    //委托金额    orderfrzamt    numeric(15,2)
//     int                                n_matchqty;                                        //成交数量    matchqty    bigint
//     int                                n_cancelqty;                                    //撤单数量    cancelqty    bigint
//     double                            db_matchamt;                                    //成交金额    matchamt    numeric(15,2)
// };
//
//
// struct    SHistoryOrderAggregateQueryA
// {
//     DWORD        dwCount;
//     SHistoryOrderAggregateQueryAItem        pItem[0];
// };


//历史成交汇总(411517)功能代码    411517功能描述    成交汇总查询, 按证券代码和买卖方向
#define PID_TRADE_GATE__HISTORYDEAL_AGGREGATE_QUERY         (PID_TRADE_GATE_BASE+506)

// struct    SHistoryDealAggregateQuery
// {
//     STradeGateUserInfo                userinfo;                                        //用户信息
//     int                                n_strdate;                                        //起始日期    strdate    int    Y
//     int                                n_enddate;                                        //终止日期    enddate    int    Y
//     __int64                            n64_fundid;                                        //资金帐户    fundid    int    N
//     char                            sz_market[MARKET_LEN];                            //交易市场    market    char(1)    N    不送查询全部
//     char                            sz_stkcode[STKCODE_LEN];                        //证券代码    stkcode    char(8)    N    不送查询全部
//     char                            sz_bankcode[5];                                    //外部银行    bankcode    char(4)    N    不送查询全部
//     char                            sz_qryoperway[2];                                //交易渠道    qryoperway    char(1)    N    不送查询全部
// };
//
// struct    SHistoryDealAggregateQueryAItem
// {
//     int                                n_bizdate;                                        //发生日期    bizdate    int
//     __int64                            n64_custid;                                        //客户代码    custid    int
//     char                            sz_custname[17];                                //客户姓名    custname    char(16)
//     char                            sz_orgid[ORGID_LEN];                            //机构编码    orgid    char(4)
//     char                            sz_bsflag[3];                                    //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                            sz_market[MARKET_LEN];                            //交易市场    market    char(1)
//     char                            sz_stkname[STKNAME_LEN];                        //证券名称    stkname    char(8)
//     char                            sz_stkcode[STKCODE_LEN];                        //证券代码    stkcode    char(8)
//     double                            db_matchprice;                                    //成交价格    matchprice    numeric(9,3)
//     int                                n_matchqty;                                        //成交数量    matchqty    bigint
//     double                            db_matchamt;                                    //成交金额    matchamt    numeric(15,2)
//     char                            sz_bankcode[5];                                    //外部银行    bankcode    char(4)
//     char                            sz_bankid[33];                                    //外部账户    bankid    char(32)
//     double                            db_fee_jsxf;                                    //净佣金    fee_jsxf    numeric(15,2)
//     double                            db_fee_sxf;                                        //佣金    fee_sxf    numeric(15,2)
//     double                            db_fee_yhs;                                        //印花税    fee_yhs    numeric(15,2)
//     double                            db_fee_ghf;                                        //过户费    fee_ghf    numeric(15,2)
//     double                            db_fee_qsf;                                        //清算费    fee_qsf    numeric(15,2)
//     double                            db_fee_jygf;                                    //交易规费    fee_jygf    numeric(15,2)
//     double                            db_fee_jsf;                                        //经手费    fee_jsf    numeric(15,2)
//     double                            db_fee_zgf;                                        //证管费    fee_zgf    numeric(15,2)
//     double                            db_fee_qtf;                                        //其他费    fee_qtf    numeric(15,2)
//     double                            db_feefront;                                    //前台费用    feefront    numeric(15,2)
//     double                            db_fundeffect;                                    //资金发生数    fundeffect    numeric(15,2)
// };
//
//
// struct    SHistoryDealAggregateQueryA
// {
//     DWORD        dwCount;
//     SHistoryDealAggregateQueryAItem        pItem[0];
// };

//交割查询(411520) 功能代码    411520功能描述    交割单查询，根据柜台普通对帐的摘要配置打印信息打印单据
#define PID_TRADE_GATE__DELIVERY_QUERY         (PID_TRADE_GATE_BASE+507)

// struct    SDeliveryQuery
// {
//     STradeGateUserInfo                userinfo;                                        //用户信息
//     int                                n_strdate;                                        //起始日期    strdate    int    Y
//     int                                n_enddate;                                        //终止日期    enddate    int    Y
//     __int64                            n64_fundid;                                        //资金帐户    fundid    INT    N
//     char                            sz_printflag[2];                                //重打标识    printflag    char(1)    Y    1－不允许重打0－允许重打
//     char                            sz_qryflag[QRYFLAG_LEN];                        //查询方向    qryflag    char(1)     Y    向下/向上查询方向
//     int                                n_count;                                        //请求行数    count    int     Y    每次取的行数
//     char                            sz_poststr[POSTSTR_LEN];                        //定位串      poststr    char(32)    Y    第一次填空
// };
//
// struct    SDeliveryQueryAItem
// {
//     char                            sz_poststr[POSTSTR_LEN];                        //定位串      poststr    char(32)        查询返回定位值
//     int                                n_cleardate;                                    //清算日期    cleardate    int
//     int                                n_bizdate;                                        //交收日期    bizdate    int
//     int                                n_orderdate;                                    //委托日期    orderdate    int
//     int                                n_ordertime;                                    //委托时间    ordertime    int
//     int                                n_digestid;                                        //业务代码    digestid    int
//     char                            sz_digestname[17];                                //业务代码说明    digestname    char(16)
//     __int64                            n64_fundid;                                        //资金帐户    fundid    int
//     char                            sz_moneytype[MONEYTYPE_LEN];                    //货币代码    moneytype    char(1)
//     char                            sz_market[MARKET_LEN];                            //交易所代码    market    char(1)
//     char                            sz_secuid[SECUID_LEN];                            //股东代码    secuid    char(32)
//     char                            sz_custname[17];                                //客户姓名    custname    char(16)
//     char                            sz_orderid[11];                                    //合同序号    orderid    char(10)
//     char                            sz_stkcode[STKCODE_LEN];                        //证券代码    stkcode    char(16)
//     char                            sz_stkname[STKNAME_LEN];                        //证券名称    stkname    char(16)
//     char                            sz_bsflag[3];                                    //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     int                                n_matchtime;                                    //成交时间    matchtime    int
//     char                            sz_matchcode[21];                                //成交号码    matchcode    char(20)
//     int                                n_matchtimes;                                    //成交笔数    matchtimes    int
//     int                                n_matchqty;                                        //成交数量    matchqty    int
//     double                            db_matchprice;                                    //成交价格    matchprice    numeric(9,3)
//     double                            db_matchamt;                                    //成交金额    matchamt    numeric(15,2)
//     double                            db_fundeffect;                                    //清算金额    fundeffect    numeric(15,2)
//     double                            db_fee_yhs;                                        //印花税    fee_yhs    numeric(12,2)
//     double                            db_fee_jsxf;                                    //标准手续费    fee_jsxf    numeric(12,2)
//     double                            db_fee_sxf;                                        //手续费    fee_sxf    numeric(12,2)
//     double                            db_fee_ghf;                                        //过户费    fee_ghf    numeric(12,2)
//     double                            db_fee_qsf;                                        //清算费    fee_qsf    numeric(12,2)
//     double                            db_fee_jygf;                                    //交易规费    fee_jygf    numeric(12,2)
//     double                            db_feefront;                                    //前台费用    feefront    numeric(12,2)
//     double                            db_fee_jsf;                                        //经手费    fee_jsf    numeric(12,2)
//     double                            db_fee_zgf;                                        //证管费    fee_zgf    numeric(12,2)
//     double                            db_fundbal;                                        //资金本次余额    fundbal    numeric(15,2)
//     int                                n_stkbal;                                        //股份本次余额    stkbal    int
//     int                                n_orderqty;                                        //委托数量    orderqty    int
//     double                            db_orderprice;                                    //委托价格    orderprice    numeric(9,3)
//     char                            sz_sourcetype[2];                                //发起方    sourcetype    char(1)
//     char                            sz_bankcode[5];                                    //外部银行    bankcode    char(4)
//     char                            sz_bankid[33];                                    //外部账户    bankid    char(32)
//     double                            db_fee_one_yhs;                                    //一级印花税    fee_one_yhs    numeric(15,2)
//     double                            db_fee_one_ghf;                                    //一级过户费    fee_one_ghf    numeric(15,2)
//     double                            db_fee_one_qsf;                                    //一级清算费    fee_one_qsf    numeric(15,2)
//     double                            db_fee_one_jygf;                                //一级交易规费    fee_one_jygf    numeric(15,2)
//     double                            db_fee_one_jsf;                                    //一级经手费    fee_one_jsf    numeric(15,2)
//     double                            db_fee_one_zgf;                                    //一级证管费    fee_one_zgf    numeric(15,2)
//     double                            db_fee_one_qtf;                                    //一级其他费    fee_one_qtf    numeric(15,2)
//     double                            db_fee_one_fxj;                                    //一级风险金    fee_one_fxj    numeric(15,2)
// };
//
//
// struct    SDeliveryQueryA
// {
//     DWORD        dwCount;
//     SDeliveryQueryAItem        pItem[0];
// };

//对帐查询(411521) 功能代码    411521 功能描述    对帐单查询
#define PID_TRADE_GATE__STATEMENTOFACCOUNT_QUERY         (PID_TRADE_GATE_BASE+508)

// struct    SStatementOfAccountQuery
// {
//     STradeGateUserInfo                userinfo;                                        //用户信息
//     int                                n_strdate;                                        //起始日期    strdate    int    Y
//     int                                n_enddate;                                        //终止日期    enddate    int    Y
//     __int64                            n64_fundid;                                        //资金帐户    fundid    int    N
//     char                            sz_printflag[2];                                //重打标识    printflag    char(1)    Y    1－不允许重打0－允许重打
//     char                            sz_qryflag[QRYFLAG_LEN];                        //查询方向    qryflag    char(1)     Y    向下/向上查询方向
//     int                                n_count;                                        //请求行数    count    int     Y    每次取的行数
//     char                            sz_poststr[POSTSTR_LEN];                        //定位串      poststr    char(32)    y    第一次填空
// };
//
// struct    SStatementOfAccountQueryAItem
// {
//     char                            sz_poststr[POSTSTR_LEN];                        //定位串      poststr    char(32)        查询返回定位值
//     int                                n_cleardate;                                    //清算日期    cleardate    int
//     int                                n_bizdate;                                        //交收日期    bizdate    int
//     int                                n_orderdate;                                    //发生日期    orderdate    int
//     int                                n_ordertime;                                    //发生时间    ordertime    int
//     int                                n_digestid;                                        //业务代码    digestid    int
//     char                            sz_digestname[17];                                //业务说明    digestname    char(16)
//     __int64                            n64_custid;                                        //客户代码    custid    int
//     char                            sz_custname[17];                                //客户姓名    custname    char(16)
//     char                            sz_orgid[ORGID_LEN];                            //机构代码    orgid    char(4)
//     __int64                            n64_fundid;                                        //资金帐号    fundid    int
//     char                            sz_moneytype[MONEYTYPE_LEN];                    //货币代码    moneytype    char(1)
//     char                            sz_market[MARKET_LEN];                            //市场代码    market    char(1)
//     char                            sz_secuid[SECUID_LEN];                            //股东代码    secuid    char(32)
//     double                            db_fundeffect;                                    //资金发生数    fundeffect    numeric(15,2)
//     double                            db_fundbal;                                        //资金本次余额    fundbal    numeric(15,2)
//     int                                n_stkbal;                                        //股份本次余额    stkbal    int
//     char                            sz_orderid[11];                                    //合同序号    orderid    char(10)
//     char                            sz_stkcode[STKCODE_LEN];                        //证券代码    stkcode    char(16)
//     char                            sz_stkname[STKNAME_LEN];                        //证券名称    stkname    char(16)
//     char                            sz_bsflag[3];                                    //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     int                                n_matchqty;                                        //成交数量    matchqty    int
//     double                            db_matchprice;                                    //成交价格    matchprice    numeric(9,3)
//     double                            db_matchamt;                                    //成交金额    matchamt    numeric(15,2)
//     double                            db_fee_jsxf;                                    //净佣金    fee_jsxf    numeric(15,2)
//     double                            db_fee_sxf;                                        //佣金    fee_sxf    numeric(15,2)
//     double                            db_fee_yhs;                                        //印花税    fee_yhs    numeric(15,2)
//     double                            db_fee_ghf;                                        //过户费    fee_ghf    numeric(15,2)
//     double                            db_fee_qsf;                                        //清算费    fee_qsf    numeric(15,2)
//     double                            db_fee_jygf;                                    //交易规费    fee_jygf    numeric(15,2)
//     double                            db_fee_jsf;                                        //经手费    fee_jsf    numeric(15,2)
//     double                            db_fee_zgf;                                        //证    管费    fee_zgf    numeric(15,2)
//     double                            db_fee_qtf;                                        //其他费    fee_qtf    numeric(15,2)
//     double                            db_feefront;                                    //前台费用    feefront    numeric(15,2)
//     char                            sz_bankcode[17];                                //银行代码    bankcode    char(16)
//     __int64                            n64_stkeffect;                                    //股份发生数    stkeffect    int64
//     char                            sz_matchcode[21];                                //成交编号    matchcode    char(20)
// };
//
//
// struct    SStatementOfAccountQueryA
// {
//     DWORD        dwCount;
//     SStatementOfAccountQueryAItem        pItem[0];
// };

//当日委托汇总(410522) 功能代码    410522  功能描述    当日委托汇总查询,ordergroup+stkcode+bsflag  带委托批号版
#define PID_TRADE_GATE__ORDERGROUP_AGGREGATE_QUERY         (PID_TRADE_GATE_BASE+509)

// struct    SOrderGroupAggregateQuery
// {
//     STradeGateUserInfo                    userinfo;                                //用户信息
//     char                                sz_market[MARKET_LEN];                    //交易市场    market    char(1)    N
//     __int64                                n64_fundid;                                //资金帐户    fundid    int    N
//     char                                sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)    N
//     char                                sz_bankcode[5];                            //外部银行    bankcode    char(4)    N
// };
//
// struct    SOrderGroupAggregateQueryAItem
// {
//     int                                    n_orderdate;                            //委托日期    orderdate    int
//     int                                    n_ordergroup;                            //委托批号    ordergroup    int
//     __int64                                n64_custid;                                //客户代码    custid    int
//     char                                sz_custname[17];                        //客户姓名    custname    char(16)
//     char                                sz_orgid[ORGID_LEN];                    //机构编码    orgid    char(4)
//     char                                sz_bsflag[3];                            //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     char                                sz_market[MARKET_LEN];                    //交易市场    market    char(1)
//     char                                sz_stkname[STKNAME_LEN];                //证券名称    stkname    char(8)
//     char                                sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(8)
//     double                                db_orderprice;                            //委托价格    orderprice    numeric(9,3)
//     int                                    n_orderqty;                                //委托数量    orderqty    bigint
//     double                                db_orderfrzamt;                            //委托金额    orderfrzamt    numeric(15,2)
//     int                                    n_matchqty;                                //成交数量    matchqty    bigint
//     int                                    n_cancelqty;                            //撤单数量    cancelqty    bigint
//     double                                db_matchamt;                            //成交金额    matchamt    numeric(15,2)
//     int                                    n_qty;                                    //可撤数量    qty    bigint
// };
//
//
// struct    SOrderGroupAggregateQueryA
// {
//     DWORD        dwCount;
//     SOrderGroupAggregateQueryAItem        pItem[0];
// };

//对帐单查询(411525) 功能代码    411525功能描述    对帐单查询
#define PID_TRADE_GATE__STATEMENT_QUERY         (PID_TRADE_GATE_BASE+510)

// struct    SStatementQuery
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     int                                n_strdate;                                //起始日期    strdate    int    Y
//     int                                n_enddate;                                //终止日期    enddate    int    Y
//     __int64                            n64_fundid;                                //资金帐户    fundid    int    N
//     char                            sz_moneytype[MONEYTYPE_LEN];            //货币代码    moneytype    char(1)    n
//     char                            sz_printflag[2];                        //重打标识    printflag    char(1)    Y    0－允许重打1－不允许重打 重打标志=1时，资金帐户为必填
//     char                            sz_qryflag[QRYFLAG_LEN];                //查询方向    qryflag    char(1)     Y    向下/向上查询方向
//     int                                n_count;                                //请求行数    count    int     Y    每次取的行数
//     char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)    y    第一次填空
// };
//
// struct    SStatementQueryAItem
// {
//     char                            sz_poststr[POSTSTR_LEN];                //定位串      poststr    char(32)        查询返回定位值
//     int                                n_cleardate;                            //清算日期    cleardate    int
//     int                                n_bizdate;                                //交收日期    bizdate    int
//     int                                n_orderdate;                            //发生日期    orderdate    int
//     int                                n_ordertime;                            //发生时间    ordertime    int
//     int                                n_digestid;                                //业务代码    digestid    int
//     char                            sz_digestname[17];                        //业务说明    digestname    char(16)
//     __int64                            n64_custid;                                //客户代码    custid    int
//     char                            sz_custname[17];                        //客户姓名    custname    char(16)
//     char                            sz_orgid[ORGID_LEN];                    //机构代码    orgid    char(4)
//     __int64                            n64_fundid;                                //资金帐号    fundid    int
//     char                            sz_moneytype[MONEYTYPE_LEN];            //货币代码    moneytype    char(1)
//     char                            sz_market[MARKET_LEN];                    //市场代码    market    char(1)
//     char                            sz_secuid[SECUID_LEN];                    //股东代码    secuid    char(32)
//     double                            db_fundeffect;                            //资金发生数    fundeffect    numeric(15,2)
//     double                            db_fundbal;                                //资金本次余额    fundbal    numeric(15,2)
//     int                                n_stkbal;                                //股份本次余额    stkbal    int
//     char                            sz_orderid[11];                            //合同序号    orderid    char(10)
//     char                            sz_stkcode[STKCODE_LEN];                //证券代码    stkcode    char(16)
//     char                            sz_stkname[STKNAME_LEN];                //证券名称    stkname    char(16)
//     char                            sz_bsflag[3];                            //买卖类别    bsflag    char(2)        为了与以前保持兼容,对于非报价转让业务返回的依然是一位
//     int                                n_matchqty;                                //成交数量    matchqty    int
//     double                            db_matchprice;                            //成交价格    matchprice    numeric(9,3)
//     double                            db_matchamt;                            //成交金额    matchamt    numeric(15,2)
// };
//
//
// struct    SStatementQueryA
// {
//     DWORD        dwCount;
//     SStatementQueryAItem        pItem[0];
// };

//外围客户资产明细查询（410201）功能代码    410201  功能描述    客户总资产查询(新)
#define PID_TRADE_GATE__ASSETS_DETAIL_QUERY         (PID_TRADE_GATE_BASE+511)

// struct    SAssertDetailQuery
// {
//     STradeGateUserInfo                userinfo;                                //用户信息
//     char                            sz_orgid[ORGID_LEN];                    //机构编码    orgid    char(4）    Y
//     __int64                            n64_custid;                                //客户帐号    custid    int64    Y
//     char                            sz_moneytype[MONEYTYPE_LEN];            //货币代码    moneytype    char(1)    N    送空查全部
//     char                            sz_assetcategory[2];                    //资产类别    assetcategory    char(1)    N    送空查全部0表示查资产1 表示查负债
// };
//
//
// struct    SAssertDetailQueryAItem
// {
//     __int64                            n64_fundid;                                //资金帐号    fundid    int64
//     char                            sz_moneytype[MONEYTYPE_LEN];            //货币代码    moneytype    char(1)
//     char                            sz_species[5];                            //资产科目代码    species    char(4)
//     char                            sz_speciesname[33];                        //资产科目名称    speciesname    char(32)
//     char                            sz_assetcategory[2];                    //资产类别    assetcategory    char(1)
//     char                            sz_categoryname[33];                    //资产类别名称    categoryname    char(32)
//     double                            db_asset;                                //资 产    asset    numeric (19,2)
// };
//
//
// struct    SAssertDetailQueryA
// {
//     DWORD        dwCount;
//     SAssertDetailQueryAItem        pItem[0];
// };

//取权证信息（410231）功能代码    410231功能描述    取权证信息
#define PID_TRADE_GATE__EXERCISEINFO_QUERY     (PID_TRADE_GATE_BASE+512)

/*struct    SExerciseInfoQuery
 {
 STradeGateUserInfo                userinfo;                                  //用户信息
 char                            sz_market;                                  //交易市场    market    char    Y
 char                            sz_stkcode[STKCODE_LEN];                  //证券代码    stkcode    char(6)    Y
 };

 struct    SExerciseInfoQueryAItem
 {
 char                            sz_market[MARKET_LEN];                        //交易市场    market    char(1)
 char                            sz_stkcode[STKCODE_LEN];                    //行权代码    stkcode    char(8)
 char                            sz_warrantcode[9];                            //权证代码    warrantcode    char(8)
 char                            sz_targetstk[9];                            //标的证券代码    targetstk    char(8)
 char                            sz_exertype[2];                                //行权方式    exertype    char(1)        A美式 E欧式 B百慕大式 Z自主行权
 char                            sz_warrantkind[2];                            //期权类型    warrantkind    char(1)
 char                            sz_cleartype[2];                            //结算方式    cleartype    char(1)
 double                            db_exerprice;                                //行权价格    exerprice    numeric(12,8)
 double                            db_exerratio;                                //行权比例    exerratio    numeric(12,8)
 char                            sz_totquantity[33];                            //权证流通总余额    totquantity    varchar(32)
 double                            db_clearprice;                                //结算价格    clearprice    numeric(17,9)
 int                                n_expdate;                                    //到期日期    expdate    int
 int                                n_exerbegindate;                            //行权开始日期    exerbegindate    int
 int                                n_exerenddate;                                //行权结束日期    exerenddate    int
 };


 struct    SExerciseInfoQueryA
 {
 DWORD        dwCount;
 SExerciseInfoQueryAItem        pItem[0];
 };
 */

#pragma warning( default: 4200 )    //#pragma warning( disable : 4200 )
#pragma pack(pop)






/*BizFun字符串例子
 入参:
 FUN=410501&TBL_IN=fundid,market,secuid,qryflag,count,poststr;111,0,34524,1,100,;

 出参,单集合
 FUN=410501
 &TBL_OUT=poststr,custid,market,secuid,name,secuseq,regflag;
 xx,222,1,333,xxx,12,1;
 xx,222,1,333,xxx,12,1;
 xx,222,1,333,xxx,12,1;
 
 出参,多集合
 FUN=410501
 &TBL_OUT=poststr,custid,market,secuid,name,secuseq,regflag;
 xx,222,1,333,xxx,12,1;
 xx,222,1,333,xxx,12,1;
 xx,222,1,333,xxx,12,1;
 &TBL_OUT2=poststr,custid,market,secuid,name,secuseq,regflag;
 xx,222,1,333,xxx,12,1;
 xx,222,1,333,xxx,12,1;
 xx,222,1,333,xxx,12,1;
 
 出参,错误信息？
 FUN=410501
 &ERR=xxxxxxxx;
 
 
 关键字符：    '#'        '='        ','        ';'        '&'
 编码成：    "##"    "#E"    "#C"    "#S"    "#A"
 */




#endif /* Packet_TradeGate_h */
