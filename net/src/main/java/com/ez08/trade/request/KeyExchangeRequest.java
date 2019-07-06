package com.ez08.trade.request;


import com.ez08.trade.SnFactory;
import com.ez08.trade.YCRequest;
import com.ez08.trade.net.NativeTools;

public class KeyExchangeRequest extends YCRequest {

    public KeyExchangeRequest() {
        super(SnFactory.getSnClient());
        this.mData = NativeTools.genTradePacketKeyExchangePackFromJNI(sn);
    }

    @Override
    public String parse(byte[] head, byte[] body) {
        return NativeTools.parseTradePacketKeyExchangeFromJNI(body);
    }

}
