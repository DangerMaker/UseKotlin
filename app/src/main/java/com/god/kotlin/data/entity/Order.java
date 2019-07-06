package com.god.kotlin.data.entity;

public class Order {
    public String stkcode;
    public String stkname;
    public String fundid;
    public String opertime; //委托时间
    public String orderdate; //委托日期
    public double orderprice; //委托价格
    public int orderqty; //委托数量
    public int matchqty; //成交数量
    public double matchprice; //成交价
    public String orderstatus; //委托状态
    public String bsflag; //买卖类别
    public String ordersno; //委托序号
}
