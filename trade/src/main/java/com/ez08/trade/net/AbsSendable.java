package com.ez08.trade.net;

import android.util.Log;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.utils.BytesUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class AbsSendable implements ISendable {

    protected int dwReqId = 0;
    private static final int PID_TRADE_COMM_BASE = 100;
    public static final int PID_TRADE_COMM_OK = 110;
    public static final int PID_TRADE_KEY_EXCHANGE = 101;
    public static final int PID_TRADE_VERIFICATION_CODE = 112;
    public static final int PID_TRADE_GATE_LOGIN = 2010;
    public static final int PID_TRADE_GATE_BIZFUN = 2015;
    public static final int PID_TRADE_GATE_ERROR = 2009;
    public static final int PID_TRADE_SESSION_UPDATE = 310;
    public static final int PID_TRADE_HQ_QUERY = 502;
    //发送手机短信
    public static final int PID_TRADE_SMS_SEND = PID_TRADE_COMM_BASE+13;
    //验证短信验证码
    public static final int PID_TRADE_SMS_CHECK = PID_TRADE_COMM_BASE+14;
    //重复登录，踢出
    public static final int PID_TRADE_SESSION_KICKOUT = 311;
    public static final String NOTICE_RECEIVER_TICKOUT = "com.ez08.trade.tickout";


    protected int sizeof(Object type){
        return NetUtil.sizeOf(type);
    }

    @Override
    public byte[] parse() {
        ByteBuffer buffer = ByteBuffer.allocate(Constant.BIZ_HEAD_SIZE + getBodyLength());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        //fill header
        STradeBaseHead header = new STradeBaseHead();
        header.dwBodySize = header.dwRawSize = getBodyLength();
        header.dwReqId = dwReqId;
        getHead(header);
        buffer.put(header.parse());
        //fill body
        getBody(buffer);
        Log.e("STradeBaseHead Request", header.toString());
        return buffer.array();
    }

    protected abstract void getHead(STradeBaseHead header);

    protected abstract void getBody(ByteBuffer bodyBuffer);

    protected abstract int getBodyLength();

    public void setDwReqId(int dwReqId){
        this.dwReqId = dwReqId;
    }
}
