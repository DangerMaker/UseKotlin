package com.ez08.trade;


import android.content.Context;

public class SocketInitializer {
    private static SocketInitializer sInstance;
    private static Context appContext;

    static {
        System.loadLibrary("native-lib");
    }

    public static SocketInitializer getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SocketInitializer(context);
        }

        return sInstance;
    }

    public static SocketInitializer getInstance() {
        if (sInstance == null) {
            sInstance = new SocketInitializer();
        }

        return sInstance;
    }

    public SocketInitializer(){

    }

    public SocketInitializer(Context context) {
        appContext = context;
    }
}
