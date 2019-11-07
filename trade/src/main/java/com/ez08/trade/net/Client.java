package com.ez08.trade.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ez08.trade.exception.LoginErrorException;
import com.ez08.trade.exception.LogoutException;
import com.ez08.trade.exception.TradeException;
import com.ez08.trade.net.biz.STradeGateBizFun;
import com.ez08.trade.net.biz.STradeGateBizFunA;
import com.ez08.trade.net.biz.STradeGateError;
import com.ez08.trade.net.callback.StateListener;
import com.ez08.trade.net.callback.StringCallback;
import com.ez08.trade.net.exchange.STradePacketKeyExchange;
import com.ez08.trade.net.exchange.STradePacketKeyExchangeResp;
import com.ez08.trade.net.head.STradeBaseHead;
import com.ez08.trade.net.head.STradeCommOK;
import com.ez08.trade.net.login.STradeGateLogin;
import com.ez08.trade.net.login.STradeGateLoginA;
import com.ez08.trade.net.login.STradeGateUserInfo;
import com.ez08.trade.net.session.STradeSessionUpdateA;
import com.ez08.trade.tools.SnFactory;
import com.ez08.trade.user.TradeUser;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.impl.client.action.ActionDispatcher;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.DefaultReconnectManager;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import static com.ez08.trade.net.Constant.BIZ_PORT;
import static com.ez08.trade.net.Constant.IP;

public class Client {

    public enum STATE {
        CONNECTED,
        EXCHANGE,
        LOGIN,
        KICK,
        DISCONNECT
    }

    public byte[] aesKey = null;
    public static byte[] sessionId = null;
    public static String strUserType;
    public static String userId;
    public static String password;
    public static String strNet2;

    public STATE state = STATE.DISCONNECT;   // 0 disconnect 1 connecting 2 connected 3 logined

    private volatile Hashtable<Integer, Callback> mRequestTable;
    private volatile List<StateListener> mStateListeners = new ArrayList<>();

    private Client() {
        mRequestTable = new Hashtable<>();
        mySocketActionAdapter = new MySocketActionAdapter();
    }

    private MySocketActionAdapter mySocketActionAdapter;

    private IConnectionManager manager;

    private static Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }

    public void connect(OnConnectListener onConnectListener) {
        disconnect();

        mRequestTable.clear();
        ConnectionInfo info = new ConnectionInfo(IP, BIZ_PORT);
        manager = OkSocket.open(info);
        OkSocketOptions options = manager.getOption();
        //基于当前参配对象构建一个参配建造者类
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder(options);
        builder.setReadByteOrder(ByteOrder.LITTLE_ENDIAN);
        builder.setWriteByteOrder(ByteOrder.LITTLE_ENDIAN);
        builder.setPulseFrequency(10 * 1000);
        builder.setReaderProtocol(new IReaderProtocol() {
            @Override
            public int getHeaderLength() {
                return Constant.BIZ_HEAD_SIZE;
            }

            @Override
            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
                ByteBuffer buffer = ByteBuffer.wrap(header);
                buffer.order(byteOrder);
                STradeBaseHead head = new STradeBaseHead(buffer);
//                Log.e("STradeBaseHead Response", head.toString());
                return head.dwBodySize;
            }
        });

        builder.setReconnectionManager(new DefaultReconnectManager());
        final Handler handler = new Handler(Looper.getMainLooper());
        builder.setCallbackThreadModeToken(new OkSocketOptions.ThreadModeToken() {
            @Override
            public void handleCallbackEvent(ActionDispatcher.ActionRunnable runnable) {
                handler.post(runnable);
            }
        });
        manager.option(builder.build());
        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        mySocketActionAdapter = new MySocketActionAdapter();
        manager.registerReceiver(mySocketActionAdapter);
        //调用通道进行连接
        manager.connect();
    }

    public void connect() {
//        disconnect();
        ConnectionInfo info = new ConnectionInfo(IP, BIZ_PORT);
        manager = OkSocket.open(info);
        Log.e("Manager", manager.toString());
        OkSocketOptions options = manager.getOption();
        //基于当前参配对象构建一个参配建造者类
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder(options);
        builder.setReadByteOrder(ByteOrder.LITTLE_ENDIAN);
        builder.setWriteByteOrder(ByteOrder.LITTLE_ENDIAN);
        builder.setPulseFrequency(10 * 1000);
        builder.setReaderProtocol(new IReaderProtocol() {
            @Override
            public int getHeaderLength() {
                return Constant.BIZ_HEAD_SIZE;
            }

            @Override
            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
                ByteBuffer buffer = ByteBuffer.wrap(header);
                buffer.order(byteOrder);
                STradeBaseHead head = new STradeBaseHead(buffer);
                Log.e("STradeBaseHead Response", head.toString());
                return head.dwBodySize;
            }
        });

        builder.setReconnectionManager(new DefaultReconnectManager());
        final Handler handler = new Handler(Looper.getMainLooper());
        builder.setCallbackThreadModeToken(new OkSocketOptions.ThreadModeToken() {
            @Override
            public void handleCallbackEvent(ActionDispatcher.ActionRunnable runnable) {
                handler.post(runnable);
            }
        });
        manager.option(builder.build());
        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        manager.registerReceiver(mySocketActionAdapter);
        //调用通道进行连接
        manager.connect();
    }

    private class MySocketActionAdapter extends SocketActionAdapter {
        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            sendState(STATE.CONNECTED);
            manager.send(new STradePacketKeyExchange());
            manager.getPulseManager().setPulseSendable(new STradeCommOK());
        }

        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            ByteBuffer buffer = ByteBuffer.wrap(data.getHeadBytes());
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            STradeBaseHead head = new STradeBaseHead(buffer);

            //心跳
            if (head.wPid == AbsSendable.PID_TRADE_COMM_OK) {
                manager.getPulseManager().feed();
                return;
            }

            if (head.wPid == AbsSendable.PID_TRADE_GATE_LOGIN) {
                STradeGateLoginA gateLoginA = new STradeGateLoginA(data.getHeadBytes(), data.getBodyBytes()
                        , Client.getInstance().aesKey);
                if (gateLoginA.getbLoginSucc()) {
                    List<TradeUser> list = new ArrayList<TradeUser>();
                    for (int i = 0; i < gateLoginA.list.size(); i++) {
                        TradeUser user = new TradeUser(
                                NetUtil.byteToStr(gateLoginA.list.get(i).sz_name),
                                NetUtil.byteToStr(gateLoginA.list.get(i).sz_market),
                                gateLoginA.list.get(i).n64_fundid + "",
                                NetUtil.byteToStr(gateLoginA.list.get(i).sz_custcert),
                                NetUtil.byteToStr(gateLoginA.list.get(i).sz_secuid),
                                gateLoginA.list.get(i).n64_custid + ""
                        );
                        list.add(user);
                    }

                    if (userListener != null) {
                        userListener.callback(list);
                    }

                    if (loginCallback != null) {
                        loginCallback.onResult(true, "");
                    }
                    sendState(STATE.LOGIN);
                } else {
                    //login failure callback
                    if (loginCallback != null) {
                        loginCallback.onResult(false, gateLoginA.getSzErrMsg());
                    }
                    throwException(new LoginErrorException());
                }
                return;
            }

            //握手
            if (head.wPid == AbsSendable.PID_TRADE_KEY_EXCHANGE) {
                sendState(STATE.EXCHANGE);
                STradePacketKeyExchangeResp exchange = new STradePacketKeyExchangeResp(data.getBodyBytes());
//              Log.e("STradePacketKeyExchange", exchange.toString());
                aesKey = OpensslHelper.genMD5(exchange.gy);
//              Log.e("genMD5", BytesUtils.toHexStringForLog(aesKey));
                if (sessionId != null) {
                    setLoginSessionPackage();
                }

                manager.getPulseManager().pulse();
                return;
            }

            //重复登录，踢出
            if (head.wPid == AbsSendable.PID_TRADE_SESSION_KICKOUT) {
                sendState(STATE.KICK);
                return;
            }

            //返回sessionId
            if (head.wPid == AbsSendable.PID_TRADE_SESSION_UPDATE) {
                STradeSessionUpdateA session = new STradeSessionUpdateA(data.getHeadBytes(),
                        data.getBodyBytes(), aesKey);
                sessionId = session.sSessionId;
                return;
            }

            //业务
            Callback callback = mRequestTable.get(head.dwReqId);
            if (callback != null) {
                if (head.wPid == AbsSendable.PID_TRADE_GATE_ERROR) {
                    callback.onResult(false, data);
                } else {
                    callback.onResult(true, data);
                }
                manager.getPulseManager().feed();
                mRequestTable.remove(head.dwReqId);
            }

        }

        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
            super.onSocketDisconnection(info, action, e);
            sendState(STATE.DISCONNECT, e);
        }
    }

    public void send(AbsSendable sendable, Callback callback) {
        int sn = SnFactory.getSnClient();
        sendable.setDwReqId(sn);
        mRequestTable.put(sn, callback);
        manager.send(sendable);
    }


    public void sendBiz(String request, final StringCallback callback) {
//        Log.e("Biz",request);
        if (Client.getInstance().state != STATE.LOGIN) {
            return;
        }

        send(new STradeGateBizFun(request), new Callback() {
            @Override
            public void onResult(boolean success, OriginalData data) {
                String result;
                if (success) {
                    STradeGateBizFunA gateBizFunA = new STradeGateBizFunA(data.getHeadBytes(), data.getBodyBytes(),
                            aesKey);
                    result = gateBizFunA.getResult();
                } else {
                    STradeGateError gateError = new STradeGateError(data.getHeadBytes(), data.getBodyBytes(),
                            aesKey);
                    result = gateError.getResult();
                }
                callback.onResult(success, result);
            }
        });
    }


    public void registerListener(StateListener listener) {
        if (mStateListeners != null) {
            synchronized (mStateListeners) {
                if (!mStateListeners.contains(listener)) {
                    mStateListeners.add(listener);
                }
            }
        }
    }

    public void unRegisterListener(StateListener listener) {
        if (mStateListeners != null) {
            synchronized (mStateListeners) {
                mStateListeners.remove(listener);
            }
        }
    }

    public boolean isConnect() {
        if (manager != null) {
            return manager.isConnect();
        }
        return false;
    }

    public void disconnect() {
        if (manager != null && manager.isConnect()) {
            manager.disconnect();
            manager.unRegisterReceiver(mySocketActionAdapter);
        }
    }

    public void logout() {
        if (manager != null) {
            sessionId = null;
            STradeGateUserInfo.getInstance().clearUserInfo();
            manager.disconnect();
        }
    }

    public void resetVerifyCode(){
        throwException(new LogoutException());
    }

    private void throwException(TradeException exception) {
        if (manager != null) {
            sessionId = null;
            STradeGateUserInfo.getInstance().clearUserInfo();
            manager.disconnect(exception);
        }
    }

    StringCallback loginCallback;

    public void setLoginPasswordPackage(String userType, String userId, String password, String checkCode, String strNet2
            , StringCallback loginCallback) {
        this.loginCallback = loginCallback;
        STradeGateLogin tradeGateLogin = new STradeGateLogin();
        tradeGateLogin.setBody(userType, userId, password, checkCode, strNet2);
        Client.getInstance().manager.send(tradeGateLogin);
    }

    private void setLoginSessionPackage() {
        STradeGateLogin tradeGateLogin = new STradeGateLogin();
        tradeGateLogin.setBody(Client.strUserType, Client.userId, Client.password, Client.sessionId, Client.strNet2);
        Client.getInstance().send(tradeGateLogin, new Callback() {
            @Override
            public void onResult(boolean success, OriginalData data) {
                STradeGateLoginA gateLoginA = new STradeGateLoginA(data.getHeadBytes(), data.getBodyBytes(), Client.getInstance().aesKey);
                if (!gateLoginA.getbLoginSucc()) {
                    logout();
                }
            }
        });
    }

    private void sendState(STATE state, Exception e) {
        if(e == null){
            e = new Exception();
        }

        this.state = state;
        List<StateListener> copyData = new ArrayList<>(mStateListeners);
        Iterator<StateListener> it = copyData.iterator();
        while (it.hasNext()) {
            StateListener listener = it.next();
            switch (state) {
                case CONNECTED:
                    if (listener != null)
                        listener.connect();
                    break;
                case EXCHANGE:
                    if (listener != null)
                        listener.exchange();
                    break;
                case LOGIN:
                    if (listener != null)
                        listener.login();
                    break;
                case KICK:
                    if (listener != null)
                        listener.kickOut();
                    break;
                case DISCONNECT:
                    if (listener != null)
                        listener.disconnect(e);
                    break;
            }
        }
    }

    private void sendState(STATE state) {
        sendState(state, null);
    }

    private UserListener userListener;

    public interface UserListener {
        void callback(List<TradeUser> list);
    }

    public void setOnUserListener(UserListener userListener) {
        this.userListener = userListener;
    }
}
