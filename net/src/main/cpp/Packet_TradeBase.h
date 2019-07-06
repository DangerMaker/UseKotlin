//
//  Packet_TradeBase.h
//  XFrame
//
//  Created by guangming xu on 2019/3/25.
//  Copyright © 2019年 guangguang. All rights reserved.
//

#ifndef Packet_TradeBase_h
#define Packet_TradeBase_h

#include "Packet_TradeData.h"
#include "cJSON.h"
#pragma pack(push,1)
#pragma warning( disable : 4200 )

//
#define SOCK_STEP_RAW                        (0)                //原始状态
#define SOCK_STEP_KEY_EXCHANGING             (10)               //本地产生随机数，并发送给对方
#define SOCK_STEP_KEY_CREATED                (20)               //收到对方的一半密码，完成会话密钥生成过程，发送LOGIN请求给交易系统
#define SOCK_STEP_LOGINING                   (30)               //客户端发出登录包，服务器端收到登录包（并转发给网关）
#define SOCK_STEP_LOGINED                    (40)               //成功登录

struct STradeBaseHead
{
    WORD        wPid;
    union{
        DWORD    dwBodyLen;
        DWORD    dwBodySize;
    };
    DWORD        dwReqId;        //dwReqId>0
    BYTE        btCompressFlag;
    DWORD        dwRawSize;
    bool        bEncrypt;
    DWORD        dwCRC_BeforeEnc;
    DWORD		dwCRC_Raw_With_Id;		//原始数据CRC，加上wPid
    DWORD		dwReserved;

    std::string toJSON()
    {
        cJSON *json = cJSON_CreateObject();

        cJSON_AddNumberToObject(json,"wPid",wPid);
        cJSON_AddNumberToObject(json,"dwBodyLen",dwBodyLen);
        cJSON_AddNumberToObject(json,"dwReqId",dwReqId);
        cJSON_AddNumberToObject(json,"btCompressFlag",btCompressFlag);
        cJSON_AddNumberToObject(json,"dwRawSize",dwRawSize);
        cJSON_AddNumberToObject(json,"bEncrypt",(int)bEncrypt);
        cJSON_AddNumberToObject(json,"dwCRC_BeforeEnc",dwCRC_BeforeEnc);
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};

//    DWORD dwCheckSum=0;
//    for(DWORD    ii=0; ii<xHead.dwBodySize;    dwCheckSum += (DWORD)((BYTE*) pBody)[ii++]);



//通信过程，定义加密包
#define PID_TRADE_COMM_BASE         (100)
#define PID_TRADE_COMM_MAX          (PID_TRADE_COMM_BASE+100)

#define PID_TRADE_HQ_BASE           (500)
#define PID_TRADE_HQ_MAX            (PID_TRADE_HQ_BASE+100)

#define PID_TRADE_GATE_BASE         (2000)
#define PID_TRADE_GATE_MAX          (PID_TRADE_GATE_BASE+2000)


#define PID_TRADE_KEY_EXCHANGE      (PID_TRADE_COMM_BASE+1)
struct STradePacketKeyExchange
{
    DWORD    dwIP;
    //    DWORD    dwReserved[10];
    BYTE    btUseRC4;            //    1==说明使用rc4加密
    BYTE    btReserved[3];
    DWORD    dwReserved[9];
    char    szGX[0];            // 有零结尾的字符串

    std::string toJSON(const char * inGX)
    {
        cJSON *json = cJSON_CreateObject();

        cJSON_AddNumberToObject(json,"dwIP",dwIP);
        cJSON_AddNumberToObject(json,"btUseRC4",btUseRC4);
        cJSON_AddStringToObject(json,"szGX",inGX);
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};
//  KEY = 通过异地的一半密钥和本地的一半密钥计算会话密钥

#define PID_TRADE_COMM_OK        (PID_TRADE_COMM_BASE+10)
struct STradeCommOK
{
    DWORD dwReserved[2];
};


//查询代码表
#define PID_TRADE_CN_LIST    (PID_TRADE_HQ_BASE+1)


//查询5档Level1行情，最多同时查询2个股票
#define PID_TRADE_HQ_QUERY    (PID_TRADE_HQ_BASE+2)
struct STradeHQQuery
{
    DWORD    idMarket;//'shhq'
    char    szCode[13];//
};
struct STradeHQOrderItem
{
    float                    fPrice;                    //委托价格，为0表示无效
    float                    fOrder;                    //委托数量，为0表示无效
    cJSON* toJSONObject()
    {
        cJSON *json = cJSON_CreateObject();
        //
        cJSON_AddItemToObject(json,"fPrice",cJSON_CreateDouble(fPrice,1));
        cJSON_AddItemToObject(json,"fOrder",cJSON_CreateDouble(fOrder,1));
        return json;
    }
};
struct STradeHQItem
{
    DWORD                    idMarket;
    char                    szCode[13];                //证券代码
    DWORD                    dwHQDate;
    DWORD                    dwHHMMSSNNN;            //时间，hhmmssNNN
    float                    fLastClose;
    float                    fOpen;                    //开盘价
    float                    fHigh;                    //最高价
    float                    fLow;                    //最低价
    float                    fNewest;                //最新价
    float                    fVolume;                //总成交量，股
    float                    fAmount;                //总成交额，元
    float                    fPreCloseIOPV;            //基金行情
    float                    fIOPV;                    //基金行情
    STradeHQOrderItem        ask[5];
    STradeHQOrderItem        bid[5];
    std::string toJSON()
    {
        cJSON *json = cJSON_CreateObject();
        //
        char market[5];
        memset(market,0,sizeof(market));
        memcpy(market,&idMarket,sizeof(idMarket));
        cJSON_AddStringToObject(json,"idMarket",market);
        cJSON_AddStringToObject(json,"szCode",szCode);
        cJSON_AddNumberToObject(json,"dwHQDate",dwHQDate);
        cJSON_AddNumberToObject(json,"dwHHMMSSNNN",dwHHMMSSNNN);
        cJSON_AddItemToObject(json,"fLastClose",cJSON_CreateDouble(fLastClose,1));
        cJSON_AddItemToObject(json,"fOpen",cJSON_CreateDouble(fOpen,1));
        cJSON_AddItemToObject(json,"fHigh",cJSON_CreateDouble(fHigh,1));
        cJSON_AddItemToObject(json,"fLow",cJSON_CreateDouble(fLow,1));
        cJSON_AddItemToObject(json,"fNewest",cJSON_CreateDouble(fNewest,1));
        cJSON_AddItemToObject(json,"fVolume",cJSON_CreateDouble(fVolume,1));
        cJSON_AddItemToObject(json,"fAmount",cJSON_CreateDouble(fAmount,1));
        cJSON_AddItemToObject(json,"fPreCloseIOPV",cJSON_CreateDouble(fPreCloseIOPV,1));
        cJSON_AddItemToObject(json,"fIOPV",cJSON_CreateDouble(fIOPV,1));
        //
        cJSON *askarray = cJSON_CreateArray();
        for(int i=0;i<5;i++)
        {
            cJSON_AddItemToArray(askarray,ask[i].toJSONObject());
        }
        cJSON *bidarray = cJSON_CreateArray();
        for(int i=0;i<5;i++)
        {
            cJSON_AddItemToArray(bidarray,bid[i].toJSONObject());
        }
        cJSON_AddItemToObject(json,"ask",askarray);
        cJSON_AddItemToObject(json,"bid",bidarray);
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};
struct STradeHQQueryA
{
    STradeHQItem            sHQ;
    std::string toJSON()
    {
        return sHQ.toJSON();
    }
};


struct STradeServiceHead
{
    WORD            wPid;
    DWORD            dwBodySize;
    DWORD            dwIP;
    DWORD            dwThreadIndex;
    DWORD            dwSocketId;
    DWORD            dwReqId;        //dwReqId>0
    __int64            n64_custid;        //        客户代码    custid    Int64    登录后送，客户端要保存，以后的业务要带这个东西
    DWORD            dwReserved[8];
};


#define TRADE_SERVICE__MANAGE        (10)
#define TRADE_SERVICE__ORDER        (20)

#define PID_TRADE_SERVICE__OK        (4999)            //OK包
struct STradeServiceOK
{
    DWORD    dwReserved[4];
};

#define PID_TRADE_SERVICE_MANAGE_BASE        (5000)
#define PID_TRADE_SERVICE_MANAGE_MAX        (PID_TRADE_SERVICE_MANAGE_BASE+999)

#define PID_TRADE_SERVICE_ORDER_BASE        (6000)
#define PID_TRADE_SERVICE_ORDER_MAX            (PID_TRADE_SERVICE_ORDER_BASE+999)




#pragma warning( default: 4200 )    //#pragma warning( disable : 4200 )
#pragma pack(pop)

#endif /* Packet_TradeBase_h */
