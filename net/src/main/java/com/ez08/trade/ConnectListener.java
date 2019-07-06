package com.ez08.trade;

public interface ConnectListener {

    void connectSuccess(Client client);

    void connectFail(Client client);

    void connectLost(Client client);
}
