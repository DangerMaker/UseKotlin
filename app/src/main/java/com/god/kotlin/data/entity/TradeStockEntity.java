package com.god.kotlin.data.entity;

import java.util.List;

public class TradeStockEntity {
    public String market;
    public String stkname;
    public String stkcode;
    public String stopflag;
    public String maxqty;
    public String minqty;
    public double fixprice;
    public double maxrisevalue;
    public double maxdownvalue;

    public double fLastClose;
    public double fHigh;
    public double fLow;
    public double fNewest;
    public double fOpen;
    public List<Dang> ask;
    public List<Dang> bid;


    public static class Dang{
        public double fPrice;
        public int fOrder;
    }
}
