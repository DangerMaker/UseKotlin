package com.ez08.trade.net.sms;

import com.ez08.trade.net.AbsSendable;
import com.ez08.trade.net.head.STradeBaseHead;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import static com.ez08.trade.net.Constant.UTF8;
import static com.ez08.trade.net.NetUtil.byteCopy;

/**
 * //验证短信验证码
 * #define PID_TRADE_SMS_CHECK				(PID_TRADE_COMM_BASE+14)
 * struct STradeSMSCheck
 * {
 * 	char	szMobile[21];
 * 	char	szCode[21];					//最近的一次验证码
 * 	DWORD	dwReserved[10];
 * };
 */
public class STradeSMSCheck extends AbsSendable {

    private byte[] szMobile = new byte[21];
    private byte[] szCode = new byte[21];
    private int[]  dwReserved = new int[10];

    private String phoneNum;
    private String code;

    public STradeSMSCheck(String phoneNum,String code) {
        this.phoneNum = phoneNum;
        this.code = code;
    }

    @Override
    protected void getHead(STradeBaseHead header) {
        header.wPid = PID_TRADE_SMS_CHECK;
    }

    @Override
    protected void getBody(ByteBuffer bodyBuffer) {
        try {
            byteCopy(phoneNum.getBytes(UTF8),szMobile);
            byteCopy(code.getBytes(UTF8),szCode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
         bodyBuffer.put(szMobile);
         bodyBuffer.put(szCode);
        for (int value:dwReserved) {
            bodyBuffer.putInt(value);
        }
    }

    @Override
    protected int getBodyLength() {
        return sizeof(szMobile)+sizeof(szCode)+sizeof(dwReserved);
    }
}
