//
//  Packet_VerificationCode.h
//  XFrame
//
//  Created by guangming xu on 2019/3/26.
//  Copyright © 2019年 guangguang. All rights reserved.
//

#ifndef Packet_VerificationCode_h
#define Packet_VerificationCode_h

#include "Packet_TradeBase.h"
#include "cJSON.h"
#pragma pack(push,1)
#pragma warning( disable : 4200 )


//包头
struct SVerificationCodeHead
{
    WORD        wPid;
    DWORD       dwBodyLen;
    std::string toJSON()
    {
        cJSON *json = cJSON_CreateObject();

        cJSON_AddNumberToObject(json,"wPid",wPid);
        cJSON_AddNumberToObject(json,"dwBodyLen",dwBodyLen);
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};

#define        VERIFICATION_CODE_PID__KEEP_ALIVE                    (WORD(10))
//心跳(没有包体)
//act=alive



#define        VERIFICATION_CODE_PID__REQ_PIC                        (WORD(100))
//请求
//act=req&id=123456
//act=req&id=123456&type=png&width=200&high=20
struct SVerificationCodeBody_ReqPic
{
#define        VERIFICATION_CODE_SIZE__ID                (size_t(20))
    char        szId[VERIFICATION_CODE_SIZE__ID+1];        //含'\0'
    DWORD        dwWidth;     //像素宽(是否起作用,根据服务器设置)
    DWORD        dwHeight;    //像素高(是否起作用,根据服务器设置)
    std::string toJSON()
    {
        cJSON *json = cJSON_CreateObject();

        cJSON_AddStringToObject(json,"szId",szId);
        cJSON_AddNumberToObject(json,"dwWidth",dwWidth);
        cJSON_AddNumberToObject(json,"dwHeight",dwHeight);
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};

//应答
//act=ans&type=png&pic=(base64)
struct SVerificationCodeBody_ReqPicA
{
    char        szId[VERIFICATION_CODE_SIZE__ID+1];        //含'\0'
    DWORD        reserve;
    DWORD        dwLife;            //寿命毫秒
    BYTE        yType;            //0:默认(BMP)
    DWORD        dwPicLen;        //
    BYTE        bufPic[0];
    std::string toJSON(char* bufPic)
    {
        cJSON *json = cJSON_CreateObject();

        cJSON_AddStringToObject(json,"szId",szId);
        cJSON_AddNumberToObject(json,"reserve",reserve);
        cJSON_AddNumberToObject(json,"dwLife",dwLife);
        cJSON_AddNumberToObject(json,"yType",yType);
        cJSON_AddNumberToObject(json,"dwPicLen",dwPicLen);
        cJSON_AddStringToObject(json,"bufPic",bufPic);
        std::string jsonstr = cJSON_Print(json);
        cJSON_Delete(json);
        return jsonstr;
    }
};


#define        VERIFICATION_CODE_PID__CHECK_CODE                    (WORD(200))
//验证
//act=check&id=123456&code=7865
struct SVerificationCodeBody_Check
{
    char        szId[VERIFICATION_CODE_SIZE__ID+1];        //含'\0'
#define        VERIFICATION_CODE_SIZE__CODE            (size_t(8))
    char        szCode[VERIFICATION_CODE_SIZE__CODE+1];    //含'\0'
    DWORD        dwCheckId;
    DWORD        reserve;
};

//验证结果
//act=result&id=123456&code=7865&match=true
struct SVerificationCodeBody_CheckA
{
    char        szId[VERIFICATION_CODE_SIZE__ID+1];        //含'\0'
    char        szCode[VERIFICATION_CODE_SIZE__CODE+1];    //含'\0'
    DWORD        dwCheckId;
    
#define     VERIFICATION_CODE_CHECK_RESULT__UNKNOW_ERROR    (BYTE(0))
#define     VERIFICATION_CODE_CHECK_RESULT__SUCCESS            (BYTE(1))
#define     VERIFICATION_CODE_CHECK_RESULT__FAIL            (BYTE(2))
#define     VERIFICATION_CODE_CHECK_RESULT__TIMEOUT            (BYTE(3))
#define     VERIFICATION_CODE_CHECK_RESULT__NOT_EXISTS        (BYTE(4))
#define     VERIFICATION_CODE_CHECK_RESULT__SERVICE_DISABLE    (BYTE(5))
#define     VERIFICATION_CODE_CHECK_RESULT__SYSTEM_ERROR    (BYTE(6))
#define     VERIFICATION_CODE_CHECK_RESULT__INPUT_ERROR        (BYTE(7))
    BYTE        yCheckResult;
    
    DWORD        reserve;
};


#pragma warning( default: 4200 )    //#pragma warning( disable : 4200 )
#pragma pack(pop)


#endif /* Packet_VerificationCode_h */
