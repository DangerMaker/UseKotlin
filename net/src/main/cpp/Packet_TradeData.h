//
//  Packet_TradeData.h
//  XFrame
//
//  Created by guangming xu on 2019/3/26.
//  Copyright © 2019年 guangguang. All rights reserved.
//

#ifndef Packet_TradeData_h
#define Packet_TradeData_h

#define __int64         long long
#define BYTE            unsigned char
#define WORD            u_int16_t
#define DWORD           u_int32_t
#define LPBYTE          unsigned char *
#define INT             int32_t
#define UINT            uint32_t
#define BOOLEAN            int32_t

struct STradeUserInfo
{
    char                m_szUserType[51];
    char                m_szUserId[51];
    char                m_szUserPwd[256];
    char                m_szMarket[51];    //股东帐号登录时送
    DWORD               m_dwCheckNo;
    char                m_szCheckCode[51];
    BYTE                m_btMD5ofClient[16];
    char                m_szVerifiCodeId[21]; //验证码ID
};
#endif /* Packet_TradeData_h */
