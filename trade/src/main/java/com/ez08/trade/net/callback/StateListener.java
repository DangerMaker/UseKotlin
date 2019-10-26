package com.ez08.trade.net.callback;

public interface StateListener {

    void connect();

    void exchange();

    void login();

    void kickOut();

    void disconnect(Exception e);

}
