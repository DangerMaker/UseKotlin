package com.ez08.trade.tools;

import java.util.concurrent.atomic.AtomicInteger;

public class SnFactory {

    private static AtomicInteger snClient = new AtomicInteger(100);

    public static int getSnClient() {
        return snClient.getAndIncrement();
    }
}