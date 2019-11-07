package com.ez08.trade.user;

public class TradeUser {

    public TradeUser(String name, String market, String fundid, String custcert, String secuid, String custid) {
        this.name = name;
        this.market = market;
        this.fundid = fundid;
        this.custcert = custcert;
        this.secuid = secuid;
        this.custid = custid;
    }

    public TradeUser() {
    }

    public String name;
    public String market;
    public String fundid;
    public String custcert;
    public String secuid;
    public String custid;
}
