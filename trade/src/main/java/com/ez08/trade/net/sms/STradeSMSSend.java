package com.ez08.trade.net.sms;

import com.ez08.trade.net.AbsSendable;
import com.ez08.trade.net.STradeBaseHead;

import java.nio.ByteBuffer;

public class STradeSMSSend extends AbsSendable {

    byte[] szMobile = new byte[21];
    int[] dwReserved = new int[10];

    @Override
    protected void getHead(STradeBaseHead header) {

    }

    @Override
    protected void getBody(ByteBuffer bodyBuffer) {

    }

    @Override
    protected int getBodyLength() {
        return 0;
    }
}
