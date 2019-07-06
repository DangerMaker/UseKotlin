package com.ez08.trade.net;


public class NativeTools {
    public static native byte[] getVerifyCodePackFromJNI(int width,int height);
    public static native String parseVerifyCodeHeadFromJNI(byte[] buffer);
    public static native String parseVerifyCodeBodyAFromJNI(byte[] buffer);

    public static native String parseTradeHeadFromJNI(byte[] buffer);

    public static native String parseTradeGateErrorFromJNI(byte[] head,byte[] body);

    public static native byte[] genTradeHeartBeatFromJNI();

    public static native byte[] genTradePacketKeyExchangePackFromJNI(int reqId);
    public static native String parseTradePacketKeyExchangeFromJNI(byte[] buffer);

    public static native byte[] genTradeGateLoginPackFromJNI(String userType,String userId,String password,String checkCode,String verifiCodeId,int reqId);
    public static native String parseTradeGateLoginFromJNI(byte[] head,byte[] body);

    public static native byte[] genTradeGateBizFunFromJNI(byte[] content,int reqId);
    public static native String parseTradeGateBizFunFromJNI(byte[] head,byte[] body);

    public static native byte[] genTradeHQQueryFromJNI(String market,String secucode,int reqId);
    public static native String parseTradeHQQueryFromJNI(byte[] head,byte[] body);
}
