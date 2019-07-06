package com.ez08.trade;

import android.util.Log;

public class YCRequest {

    Callback callback;
    public int sn;
    public long mSendTime;    //发数据的时间
    public int mTimeout;    //超时时间
    public byte[] mData;

    public YCRequest(int sn) {
        this.sn = sn;
    }

    public YCRequest(Callback callback) {
        this.callback = callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public void failed(Client client) {
        if (callback == null)
            return;

        Response response = new Response();
        response.setSucceed(false);
        callback.callback(client, response);
    }

    public void received(Response response, Client client) {
        if (callback == null)
            return;

        if (response.getPid() == 2009) {
            response.setSucceed(false);
        } else {
            response.setSucceed(true);
        }

        Log.e("Request receiver", response.getData());
        callback.callback(client, response);
    }

    public void cancel(Client client) {
//        mResult = RESULT_CANCELED;
//        failed(client);
        System.out.println("Sn=" + sn + ", status = cancel");
    }


    public String parse(byte[] head, byte[] body) {
        return "";
    }


    public static final int REQUEST_STATE_READY = 0;
    public static final int REQUEST_STATE_SEND = 1;
    public static final int REQUEST_STATE_SEVER_RECEIVED = 2;
    public static final int REQUEST_STATE_RESPONSE_RECEIVED = 3;
    public int mState;    //
}
