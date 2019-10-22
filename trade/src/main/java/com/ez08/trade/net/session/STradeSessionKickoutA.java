package com.ez08.trade.net.session;

import com.ez08.trade.net.AbsResponse;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * //重复登录，踢出
 * #define PID_TRADE_SESSION_KICKOUT	(PID_TRADE_SESSION_BASE+11)
 * struct STradeSessionKickoutA
 * {
 * STradeSessionId	sSessionId;
 * DWORD	dwReserved[10];
 * };
 */
public class STradeSessionKickoutA extends AbsResponse {

    private byte[] sSessionId = new byte[30];
    private int[] dwReserved = new int[10];
    public STradeSessionKickoutA(byte[] head, byte[] originBody, byte[] aesKey) {
        super(head, originBody, aesKey);
        ByteBuffer buffer = ByteBuffer.wrap(body);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.get(sSessionId);

        for (int i = 0; i < dwReserved.length; i++) {
            dwReserved[i] = buffer.getInt(i);
        }
    }

    public byte[] getsSessionId(){
        return sSessionId;
    }
}
