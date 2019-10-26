package com.ez08.trade;

import com.xuhao.didi.core.utils.SLog;

public class TradeInitalizer {
    public static void init(){
        System.loadLibrary("opensslLib");
        SLog.setIsDebug(true);
    }
}
