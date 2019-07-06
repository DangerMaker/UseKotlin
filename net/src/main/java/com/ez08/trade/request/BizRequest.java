package com.ez08.trade.request;

import android.util.Log;
import com.ez08.trade.Constant;
import com.ez08.trade.SnFactory;
import com.ez08.trade.YCRequest;
import com.ez08.trade.net.NativeTools;

import java.io.UnsupportedEncodingException;

public class BizRequest extends YCRequest {
    public BizRequest() {
        super(SnFactory.getSnClient());
    }

    @Override
    public String parse(byte[] head, byte[] body) {
        return NativeTools.parseTradeGateBizFunFromJNI(head, body);
    }

    public void setBody(String body) {
        Log.e("BizRequest",body);
        try {
            byte[] a = body.getBytes(Constant.SERVER_CHARSET);
            this.mData = NativeTools.genTradeGateBizFunFromJNI(a, sn);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
