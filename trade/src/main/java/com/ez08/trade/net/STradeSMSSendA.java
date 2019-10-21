package com.ez08.trade.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * struct STradeSMSSendA
 * {
 * 	WORD	wSeqNo;				//短信序号，会在短信中，服务器只保留最近的验证码
 * 	char szError[31];
 * 	DWORD	dwReserved[10];
 * };
 */
public class STradeSMSSendA extends AbsResponse{

    private int wSeqNo;
    private byte[] szError = new byte[31];
    private int[] dwReserved = new int[10];
    public STradeSMSSendA(byte[] head, byte[] originBody, byte[] aesKey) {
        super(head, originBody, aesKey);
        ByteBuffer buffer = ByteBuffer.wrap(body);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.get(szError);
        for (int i = 0; i < dwReserved.length; i++) {
            dwReserved[i] = buffer.getInt(i);
        }
    }

    public String getSzError() {
        return NetUtil.byteToStr(szError);
    }
}
