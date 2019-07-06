package com.ez08.trade;

public class Error {
    public String dwReqId;
    public String dwErrorCode;
    public String szError;

    public Error() {
    }

    public Error(String dwReqId, String dwErrorCode, String szError) {
        this.dwReqId = dwReqId;
        this.dwErrorCode = dwErrorCode;
        this.szError = szError;
    }
}
