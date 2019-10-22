package com.ez08.trade.net.sms;

import com.ez08.trade.net.AbsSendable;
import com.ez08.trade.net.head.STradeBaseHead;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import static com.ez08.trade.net.Constant.UTF8;
import static com.ez08.trade.net.NetUtil.byteCopy;

/**
 * //发送手机短信
 * #define PID_TRADE_SMS_SEND				(PID_TRADE_COMM_BASE+13)
 * struct STradeSMSSend
 * {
 * 	char	szMobile[21];
 * 	DWORD	dwReserved[10];
 * };
 */
public class STradeSMSSend extends AbsSendable {

    private byte[] szMobile = new byte[21];
    private int[]  dwReserved = new int[10];
    private String phoneNum;

    public STradeSMSSend(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    protected void getHead(STradeBaseHead header) {
        header.wPid = PID_TRADE_SMS_SEND;
    }

    @Override
    protected void getBody(ByteBuffer bodyBuffer) {
        try {
            byteCopy(phoneNum.getBytes(UTF8),szMobile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        bodyBuffer.put(szMobile);
        for (int value : dwReserved) {
            bodyBuffer.putInt(value);
        }
    }

    @Override
    protected int getBodyLength() {
        return sizeof(szMobile)+sizeof(dwReserved);
    }
}
