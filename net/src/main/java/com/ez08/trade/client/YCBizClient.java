package com.ez08.trade.client;

import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.ez08.trade.*;
import com.ez08.trade.net.NativeTools;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author Administrator
 * 此类为socket长连接的管理及数据交互助手，规格说明：
 * 1、指定服务地址及端口，建立连接并通过心跳侦测保持连接
 * 2、管理数据请求，建立请求队列
 * 3、通过delegate向请求者回馈数据请求结果及进度情况
 * 4、基于ACTION管理服务器推送信息的监听器
 * 5、通过EzNetHelperDelegate反馈网络状态变化信息，从而由应用扩展网络管理功能或改变网络管理的逻辑,地址分发也在这里处理
 * 6、支持多实例
 * 7、在网络连接开始建立之后即可随时接受网络请求调用（如果是连接断开后自动重连，那么即使当时处于未连接状态，也可以随时接受网络请求调用
 * 8、这里保存握手状态以及加密相关的所有信息，网络请求可以指示是否在握手成功后方可发送，如果是，则握手成功后自动将队列中未发送的请求进行发送
 * 9、支持确保送到发送（可以考虑用加大超时时间来实现，系统对超时时间大于一定时间的请求做持续化处理）
 */
public class YCBizClient implements Client {

    private static final String tag = "YCSocketClient";
    private final static Logger logger = Logger.getLogger(tag);
    public static final int RETRY = 2;//指定连接丢失后重试连接的次数
    public static final int MAX_MIS_HEARTBEAT_COUNT = 3;

    public static final int STATE_NONE = 0;//没有网络连接
    public static final int STATE_CONNECTING = 1;//正在建立网络连接
    public static final int STATE_CONNECTED = 2;//网络已连接，尚未发送握手包
    public static final int STATE_HANDSHAKEING = 3;//握手包已发送，等待服务器回应
    public static final int STATE_HANDSHAKEED = 4;//握手成功
    private boolean mDisconnectByClient;

    private int TIME_OUT = 20;
    private int TIME_WRITE_OUT = 20;
    private int mState = 0; // 状态
    private String mHost;
    private int mPort;

    private int mMisHeartBeatCount;
    private ConnectListener connectListener;
    private ChannelHandlerContext ctx;
    // netty相关对象
    private EventLoopGroup mWorkerGroup;
    private Bootstrap mBootstrap;

    //请求存储表
    private Hashtable<Integer, YCRequest> mRequestTable;
    private Hashtable<Integer, Integer> mTimeOutTable;
    private Hashtable<Callback, IntentFilter> mListenerTable;

    YCBizClient client = this;

    public YCBizClient(String host, int port) {
        mHost = host;
        mPort = port;
        mTimeOutTable = new Hashtable<>();
        mRequestTable = new Hashtable<>();
        mListenerTable = new Hashtable<>();

        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel arg0) throws Exception {
                ChannelPipeline pipeline = arg0.pipeline();
                pipeline.addLast("encoder", new NetPackageEncoder());
                pipeline.addLast("idleStateHandler", new IdleStateHandler(TIME_OUT, TIME_WRITE_OUT, 0, TimeUnit.SECONDS));
                pipeline.addLast(new EzMessageDecoder(),
                        new PackageClientHandler());    //	PackageClientHandler处理激活、数据接收、断开连接等事件
            }
        };
        try {
            if (null == mWorkerGroup) {
                mWorkerGroup = new NioEventLoopGroup();
            }
            if (null == mBootstrap) {
                mBootstrap = new Bootstrap();
                mBootstrap.group(mWorkerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
//                        .option(ChannelOption.ALLOW_HALF_CLOSURE, true)
                        .handler(initializer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络请求超时设置
     *
     * @param timeout
     */
    public void setTimeOut(int timeout) {
        TIME_OUT = timeout;
    }

    /**
     * 设置网络扩展接口，非必须
     *
     * @param
     */
    public void setOnConnectListener(ConnectListener connectListener) {
        this.connectListener = connectListener;
    }

    /**
     * 建立连接，支持带参数的连接函数，这样可在没有初始化的情况下直接使用参数中的服务地址和端口建立连接
     */
    public boolean connect() {
        logger.info("开始建立连接....");
        mDisconnectByClient = false;
        if (mHost == null || mHost.equals("") || mPort == 0)
            return false;
        try {
            //断开之前的连接

            ChannelFuture cf = mBootstrap.connect(new InetSocketAddress(mHost, mPort));
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        logger.info("开始建立连接....已完成，连接成功");
                    } else {
                        logger.info("开始建立连接....，连接失败");
                        Thread.sleep(10 * 1000);
                        connectFailed();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("开始建立连接....失败1");
            connectFailed();
        }
        return true;
    }

    /**
     * 关闭连接，并通知所有请求者连接中断
     */
    public void disconnect() {
        mDisconnectByClient = true;
        //通知所有尚未完成的请求连接已经中断
        clearRequestTable();
        mState = STATE_NONE;
    }

    private synchronized void clearRequestTable() {
        if (mRequestTable == null || mRequestTable.size() == 0) {
            return;
        }
        Set<Integer> set = mRequestTable.keySet();
        for (Integer sn : set) {
            YCRequest request = mRequestTable.get(sn);
            request.failed(this);
        }
    }

    public int getState() {
        return mState;
    }

    /**
     * 发送请求，可以使用EzRequest作为参数，也可以使用EzRequest中的属性直接作为参数，由send创建EzRequest对象
     * 返回值为网络请求SN序号
     *
     * @return
     */
    @Override
    public int send(YCRequest request) {
        if (request == null)
            return -1;

        mRequestTable.put(request.sn, request);
        send2Net(request);
        mTimeOutTable.put(request.sn, request.mTimeout);
        return request.sn;
    }


    /**
     * 取消请求需要以SN作为参数
     */
    public void cancel(int sn) {
        YCRequest request = mRequestTable.remove(sn);
        //向delegate发送EzFailed消息，告知请求被取消
        if (request != null)
            request.cancel(this);
    }


    /**
     * 网络丢失,如果不是客户端主动关闭连接，最多重试两次连接
     */
    private void connectLost() {
        mState = STATE_NONE;
        if (connectListener != null)
            connectListener.connectLost(this);
    }

    private void connectFailed() {
        mState = STATE_NONE;
        if (connectListener != null)
            connectListener.connectFail(this);
    }

    /**
     * 网络socket相关处理
     *
     * @param request
     */
    private void send2Net(YCRequest request) {
        request.mState = YCRequest.REQUEST_STATE_SEND;
        request.mSendTime = System.currentTimeMillis();
        //向网络连接发送，异步发送
//        NetPackage netPackage = request.getNetPackage();
        if (ctx != null)
            ctx.writeAndFlush(request.mData);
    }

    /**
     * 登记所有需要监听网络消息的handler
     *
     * @param handler
     * @param filter
     */
    public void registerListener(Callback handler, IntentFilter filter) {
        if (handler == null)
            return;
        IntentFilter f = mListenerTable.get(handler);
        if (f != null) {
            mListenerTable.remove(handler);
        }
        if (filter == null) {
            mListenerTable.remove(handler);
        } else {
            mListenerTable.put(handler, filter);
        }

    }

    public void unregisterListener(Callback handler) {
        mListenerTable.remove(handler);
    }


    private void checkListener(Response msg) {
//        if (msg == null) return;
//        String action = "";
//        if (msg.mDataType == EzRequest.DATA_TYPE_EZMESSAGE) {
//            EzMessage ezmsg = msg.getEzMessage();
//            action = ezmsg.getKVData("action").getString();
//        } else if (msg.mDataType == EzRequest.DATA_TYPE_TEXT) {
//            action = msg.mAction;
//        }
//        if (action == null || action.equalsIgnoreCase(""))
//            return;
//        EzLog.e(true, "action", action);
//        Set<EzNetDataListener> set = mListenerTable.keySet();
//        if (set == null || set.size() == 0) return;
//        for (EzNetDataListener handler : set) {
//            IntentFilter f = mListenerTable.get(handler);
//            if (f != null) {
//                if (f.hasAction(action)) {
//                    handler.ezReceived(this, msg);
//                }
//            }
//        }
    }

    private String mErrorMsg;

    public String getErrorMsg() {
        return mErrorMsg;
    }

    private long mLastHeartBeatTime = 0;

    public long getLastHeartBeatTime() {
        return mLastHeartBeatTime;
    }

    private void checkTimeOut() {
        Iterator<Integer> it1 = mTimeOutTable.keySet().iterator();
        while (it1.hasNext()) {
            int sn = it1.next();
            int timeRemain = mTimeOutTable.get(sn);
            if (timeRemain > 0) {
                timeRemain--;
                mTimeOutTable.put(sn, timeRemain);
                logger.info("write=" + timeRemain + "");
            } else {
                YCRequest request = mRequestTable.get(sn);
                if (request != null) {
                    request.failed(this);
                    mRequestTable.remove(sn);
                    mTimeOutTable.remove(sn);
                }

                logger.info("write=" + "超时：" + sn);
            }
        }
    }

    /**
     * 网络协议解析
     *
     * @author lilongtan
     */
    public class EzMessageDecoder extends ByteToMessageDecoder {
        public final int HEADSIZE = 28;

        @Override
        protected void decode(ChannelHandlerContext arg0, ByteBuf buffer,
                              List<Object> out) throws Exception {

            if (buffer.readableBytes() < HEADSIZE) {
                return;
            }
            //
            buffer.markReaderIndex();

            //获取包头对象
            byte[] headdecoded = new byte[HEADSIZE];
            buffer.readBytes(headdecoded);
            String jsonhead = NativeTools.parseTradeHeadFromJNI(headdecoded);
            JSONObject jsonObject = new JSONObject(jsonhead);
            int pid = jsonObject.getInt("wPid");
            int bodyLen = jsonObject.getInt("dwBodyLen");
            int sn = jsonObject.getInt("dwReqId");

            //获取包体对象
            int bodylength = bodyLen;//需要换成json对应的值
            if (buffer.readableBytes() < bodylength) {
                buffer.resetReaderIndex();
                return; // (2)
            }

            if (pid == 110) {
                buffer.skipBytes(bodyLen);
                logger.info("heart receiver 110.......");
                return;
            }

            byte[] bodydecoded = new byte[bodylength];
            buffer.readBytes(bodydecoded);

            String jsonbody;
            if (pid == 2009) {
                jsonbody = NativeTools.parseTradeGateErrorFromJNI(headdecoded, bodydecoded);
            } else {
                jsonbody = mRequestTable.get(sn).parse(headdecoded, bodydecoded);
            }
            logger.info("EzMessageDecoder");

            //获取图片内容,在bodydecoded内容里面,bodydecoded+VerifyCodeBodySizeB偏移
            NetPackage netPackage = new NetPackage();
            netPackage.pid = pid;
            netPackage.sn = sn;
            netPackage.data = jsonbody;
            out.add(netPackage);
            return;


        }
    }

    /**
     * 网络协议编码
     */
    public class NetPackageEncoder extends MessageToByteEncoder {

        @Override
        protected void encode(ChannelHandlerContext arg0, Object arg1, ByteBuf arg2)
                throws Exception {
//            if (arg1 instanceof NetPackage) {
//                NetPackage message = (NetPackage) arg1;
////                message.socketWrite(arg2);
//                return;
//            }
            arg2.writeBytes((byte[]) arg1);
            logger.info("NetPackageEncoder");
        }
    }


    /**
     * @author lilongtan
     * 处理socket连接的激活、断开、数据接收、心跳、数据包确认、异常处理等等事件
     */
    public class PackageClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            logger.info("连接激活 channelId = " + ctx.toString() + "...channelActive");
            mState = STATE_CONNECTED;

            YCBizClient.this.ctx = ctx;

            if (connectListener != null)
                connectListener.connectSuccess(YCBizClient.this);
            mMisHeartBeatCount = 0;
        }


        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            logger.info("channelInactive连接关闭成功 channelId = " + ctx.toString());
            if (YCBizClient.this.ctx != null) {
                if (YCBizClient.this.ctx == ctx) {//mCtx不为空并且和当前值相同，说明曾经连接成功过，这时才算是连接丢失，
                    YCBizClient.this.ctx = null;
                    mState = STATE_NONE;
                    connectLost();
                }
            } else {//连接尚未建立，直接失败了
                //理论上讲，当mCtx为空时，表示连接没有建立过或主动断开后将mCtx清空，这时该连接的中断是无需理会的
//				connectFailed();
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            super.channelRead(ctx, msg);
            logger.info("channelRead");
            if (msg != null && msg instanceof NetPackage) {
                Message message = getMainHandler().obtainMessage(MESSAGE_POST_RESULT, new AsyncTaskResult(YCBizClient.this, (NetPackage) msg));
                message.sendToTarget();
//                NetPackage netPackage = (NetPackage) msg;
//                YCRequest request = mRequestTable.get(netPackage.pid);
//                Response response = new Response();
//                response.setData(netPackage.data);
//                request.received(response,client);
//                mRequestTable.remove(netPackage.pid);
            }

//            send(new YCRequest());
//            if (msg != null && msg instanceof NetPakage) {
//                mMisHeartBeatCount = 0;
//                NetPakage pakage = (NetPakage) msg;
//                EzRequest request;
//                NetPakage resPakage;
//                switch (pakage.flag) {
//                    case NetPakage.NETPAKAGE_TYPE_HEARTBEAT:
//                        resPakage = new NetPakage(NetPakage.NETPAKAGE_TYPE_HEARTBEAT_RESPONSE);
//                        ctx.writeAndFlush(resPakage);
//                        EzLog.i(D, tag, "收到一个心跳...");
//                        break;
//                    case NetPakage.NETPAKAGE_TYPE_HEARTBEAT_RESPONSE:
//                        EzLog.i(D, tag, "收到一个心跳回应...");
//                        break;
//                    case NetPakage.NETPAKAGE_TYPE_CONFIRM:
//                        request = mRequestTable.get(pakage.sn);
//                        EzLog.i(D, tag, "收到一个确认包...sn=" + pakage.sn);
//                        if (request != null) {
//                            request.serverConfirmed(EzNetHelper.this);
//                        }
//                        break;
//                    case NetPakage.NETPAKAGE_TYPE_DATA:
//                        request = mRequestTable.get(pakage.sn);
//                        EzLog.i(D, tag, "收到一个数据包...sn=" + pakage.sn + Thread.currentThread());
//                        resPakage = new NetPakage(NetPakage.NETPAKAGE_TYPE_CONFIRM);
//
//                        resPakage.sn = pakage.sn;
//                        resPakage.childSn = pakage.childSn;
//                        resPakage.action = pakage.action;
//                        ctx.writeAndFlush(resPakage);
//                        EzResponseData data = new EzResponseData(pakage.contentType, pakage.content, resPakage.action);
//                        if (request != null) {
//                            boolean finished = (pakage.childFlag | 0x4000) > 0;
//                            request.response(EzNetHelper.this, data, finished);
//                            if (finished)
//                                mRequestTable.remove(pakage.sn);
//                        } else {//没有等待的监听句柄，可能是服务器推送报
//                            checkListener(data);
//                        }
//                }
//            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE) {

//                    logger.info("定时心跳间隔触发...userEventTriggered" + ctx.channel());
//                    mLastHeartBeatTime = System.currentTimeMillis();
//                    //心跳及超时处理,
//                    if (mState == STATE_HANDSHAKEED) {//如果握手已经成功，则发送心跳包并检查队列包是否有超时的
////                        NetPakage heartBeatPakage = new NetPakage(NetPakage.NETPAKAGE_TYPE_HEARTBEAT);
////                        ctx.writeAndFlush(heartBeatPakage);
//                        //检查队列中的请求
//
//                    }
//                    mMisHeartBeatCount++;
//                    if(mMisHeartBeatCount % 5 == 0){
                    logger.info("userEventTriggered");
//                    ctx.writeAndFlush(NativeTools.genTradeHeartPackFromJNI());
//                    }

//                    if (mMisHeartBeatCount > MAX_MIS_HEARTBEAT_COUNT) {
//                        //网络心跳丢失过多，断网,以触发重连机制
//                        logger.info("userEventTriggered定时心跳接收失败超过三次，断开网络..." + ctx.channel());
//                        mMisHeartBeatCount = 0;
//                        ctx.close();
//                    }
//
//                    if (getState() != STATE_HANDSHAKEED) {
//                        logger.info(" 握手超时，关闭连接");
//                        ctx.close();
//                    }
                } else if (event.state() == IdleState.WRITER_IDLE) {
////                    checkTimeOut();
                    logger.info("WRITER_IDLE");
                    ctx.writeAndFlush(NativeTools.genTradeHeartBeatFromJNI());
                }
//            } else {
//                logger.info("EzNetHelper = " + evt.getClass().getCanonicalName() + ",异常，断开连接");
//                ctx.close();
//                mErrorMsg = evt.getClass().getCanonicalName();
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            super.exceptionCaught(ctx, cause);
            //网络异常，断开网络
            logger.info("连接异常,关闭连接，不做其它操作...exceptionCaught");
            cause.printStackTrace();
            ctx.close();
            mErrorMsg = cause.getLocalizedMessage();
        }
    }

    private static InternalHandler sHandler;
    private static final int MESSAGE_POST_RESULT = 0x1;

    private static Handler getMainHandler() {
        synchronized (AsyncTask.class) {
            if (sHandler == null) {
                sHandler = new InternalHandler(Looper.getMainLooper());
            }
            return sHandler;
        }
    }

    private static class InternalHandler extends Handler {
        public InternalHandler(Looper looper) {
            super(looper);
        }

        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
        @Override
        public void handleMessage(Message msg) {
            AsyncTaskResult result = (AsyncTaskResult) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    // There is only one result
                    NetPackage netPackage = result.netPackage;
                    YCRequest request = result.client.mRequestTable.get(netPackage.sn);
                    Response response = new Response();
                    response.setData(netPackage.data);
                    response.setPid(netPackage.pid);
                    request.received(response, result.client);
                    result.client.mRequestTable.remove(netPackage.sn);
                    break;
            }
        }
    }

    private static class AsyncTaskResult {
        YCBizClient client;
        NetPackage netPackage;

        AsyncTaskResult(YCBizClient client, NetPackage netPackage) {
            this.client = client;
            this.netPackage = netPackage;
        }
    }

}
