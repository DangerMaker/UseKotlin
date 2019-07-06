package com.ez08.trade.request;

import android.util.Log;
import com.ez08.trade.SnFactory;
import com.ez08.trade.YCRequest;
import com.ez08.trade.net.NativeTools;

public class QueryRequest extends YCRequest {
    public QueryRequest() {
        super(SnFactory.getSnClient());
    }

    @Override
    public String parse(byte[] head, byte[] body) {
        return NativeTools.parseTradeHQQueryFromJNI(head, body);
    }

    public void setBody(String market, String secucode) {
        Log.e("QueryRequest", getMarketByTag(market) + "," + secucode);
        this.mData = NativeTools.genTradeHQQueryFromJNI(getMarketByTag(market), secucode, sn);
    }

    public static String getMarketByTag(String tag) {
        if (tag.equals("0") || tag.equals("2") || tag.equals("")) {
            return "SZHQ";
        } else {
            return "SHHQ";
        }
    }

}
