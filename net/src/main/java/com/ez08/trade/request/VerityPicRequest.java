package com.ez08.trade.request;


import com.ez08.trade.YCRequest;
import com.ez08.trade.net.NativeTools;

public class VerityPicRequest extends YCRequest {

    //验证码只传pid
    public VerityPicRequest() {
        //验证码需要传
        super(100);
        setBody(30,15);
    }

    public void setBody(int width,int height){
        this.mData = NativeTools.getVerifyCodePackFromJNI(width, height);
    }

    @Override
    public String parse(byte[] head,byte[] body) {
       return NativeTools.parseVerifyCodeBodyAFromJNI(body);
    }
}
