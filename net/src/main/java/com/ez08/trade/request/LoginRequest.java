package com.ez08.trade.request;


import com.ez08.trade.SnFactory;
import com.ez08.trade.YCRequest;
import com.ez08.trade.net.NativeTools;

public class LoginRequest extends YCRequest {

    public LoginRequest() {
        super(SnFactory.getSnClient());

    }
    public void setBody(String userType, String userId, String password, String checkCode, String verifiCodeId) {
        this.mData = NativeTools.genTradeGateLoginPackFromJNI(userType, userId, password, checkCode, verifiCodeId, this.sn);
    }

    @Override
    public String parse(byte[] head, byte[] body) {
        return NativeTools.parseTradeGateLoginFromJNI(head, body);
    }
}
