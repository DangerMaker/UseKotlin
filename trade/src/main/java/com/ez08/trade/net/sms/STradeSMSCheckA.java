package com.ez08.trade.net.sms;

import com.ez08.trade.net.AbsResponse;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * struct STradeSMSCheckA
 * {
 * 	BOOL	bSuccess;
 * 	DWORD	dwReserved[10];
 * };
 */
public class STradeSMSCheckA extends AbsResponse {

    private byte bSuccess;
    private int[] dwReserved = new int[10];
    public STradeSMSCheckA(byte[] head, byte[] originBody, byte[] aesKey) {
        super(head, originBody, aesKey);
        ByteBuffer buffer = ByteBuffer.wrap(body);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        bSuccess = buffer.get();
        for (int i = 0; i < dwReserved.length; i++) {
            dwReserved[i] = buffer.getInt(i);
        }
    }

    public boolean getbSuccess() {
        return bSuccess==1;
    }
}
