package com.god.kotlin.data.entity;

public class TradeHandEntity {
    public String market;
    public String stkname;
    public String stkcode;
    public int stkbal; //持仓股票
    public int stkavl; //可用股票
//    public String buycost; //当前成本
    public double costprice; //成本价格
    public double mktval; //市值
    public double income; //盈亏
    public double lastprice; //最新价
    public String moneyType;
}
