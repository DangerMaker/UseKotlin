package com.ez08.trade;


import com.ez08.trade.client.YCBizClient;

public class ClientHelper {

    private static YCBizClient bizClient = null;

    public static YCBizClient get(){
        if (bizClient == null) {
            bizClient = new YCBizClient(Constant.SERVER_IP, Constant.BIZ_SERVER_PORT);
        }
        return bizClient;
    }

}
