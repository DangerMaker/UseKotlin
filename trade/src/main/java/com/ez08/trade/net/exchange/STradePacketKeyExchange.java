package com.ez08.trade.net.exchange;

import com.ez08.trade.net.AbsSendable;
import com.ez08.trade.net.OpensslHelper;
import com.ez08.trade.net.head.STradeBaseHead;
import com.xuhao.didi.core.iocore.interfaces.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * struct STradePacketKeyExchange
 * {
 * DWORD	dwIP;					//	服务器发送给客户端:	表示服务端看到的客户端IP
 * BYTE	btAES128;				//	客户端发送给服务器:	1==说明使用AES-128 CBC
 * BYTE	btClientType;			//	客户端发送给服务器:	0==客户端   1==手机
 * BYTE	btSupportVerification;	//	服务器发送给客户端:	表示前置机直接支持验证码服务
 * BYTE	btReserved[1];
 * DWORD	dwReserved[9];
 * char	szGX[0];			// 有零结尾的字符串
 * };
 */
public class STradePacketKeyExchange extends AbsSendable {
    public int dwIP = 0;
    public byte btAES128 = 1;
    public byte btClientType = 1;
    public byte btSupportVerification = 0;
    public byte btReserved = 0;
    public int[] dwReserved = new int[9];
    public byte[] gx;
    public byte empty = 0;

    public STradePacketKeyExchange() {
        gx = OpensslHelper.genPublicKey();
    }

    @Override
    protected void getHead(STradeBaseHead header) {
        header.wPid = PID_TRADE_KEY_EXCHANGE;
    }

    @Override
    protected void getBody(ByteBuffer bodyBuffer) {
        bodyBuffer.putInt(dwIP);
        bodyBuffer.put(btAES128);
        bodyBuffer.put(btClientType);
        bodyBuffer.put(btSupportVerification);
        bodyBuffer.put(btReserved);

        for (int i = 0; i < 9; i++) {
            bodyBuffer.putInt(dwReserved[i]);
        }
        bodyBuffer.put(gx);
        bodyBuffer.put(empty);
    }

    @Override
    protected int getBodyLength() {
        return sizeof(dwIP) +
                sizeof(btAES128) +
                sizeof(btClientType) +
                sizeof(btSupportVerification) +
                sizeof(btReserved) +
                sizeof(dwReserved) +
                sizeof(gx) +
                sizeof(empty);
    }

    @Override
    public String toString() {
        return "STradePacketKeyExchangeResp{" +
                "dwIP=" + dwIP +
                ", btAES128=" + btAES128 +
                ", btClientType=" + btClientType +
                ", btSupportVerification=" + btSupportVerification +
                ", btReserved=" + btReserved +
                ", dwReserved=" + Arrays.toString(dwReserved) +
                ", gx=" + Arrays.toString(gx) +
                ", empty=" + empty +
                '}';
    }
}
