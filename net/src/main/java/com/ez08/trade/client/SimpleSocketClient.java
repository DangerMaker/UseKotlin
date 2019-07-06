package com.ez08.trade.client;

import android.text.TextUtils;
import com.ez08.trade.*;
import com.ez08.trade.net.NativeTools;
import com.ez08.trade.request.VerityPicRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Administrator
 * simple client
 * only for no state connect
 */
public class SimpleSocketClient implements Client {

    private static final String tag = "SimpleSocketClient";
    private final static Logger logger = Logger.getLogger(tag);
    private String mHost;
    private int mPort;
    private int pid = 100;
    private VerityPicRequest request = new VerityPicRequest();

    // netty相关对象
    private EventLoopGroup mWorkerGroup;
    private Bootstrap mBootstrap;

    SimpleSocketClient client = this;

    public SimpleSocketClient(String host, int port) {
        mHost = host;
        mPort = port;

        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel arg0) throws Exception {
                ChannelPipeline pipeline = arg0.pipeline();
                pipeline.addLast("encoder", new NetPackageEncoder());
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
                        .handler(initializer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int send(YCRequest request) {
        logger.info("开始建立连接....");
        if (TextUtils.isEmpty(mHost) || mPort == 0) {
            Response response = new Response();
            response.setSucceed(false);
            response.setPid(pid);
            response.setData("请求地址失效");
            callback.callback(client, response);
            return 0;
        }
        try {
            ChannelFuture cf = mBootstrap.connect(new InetSocketAddress(mHost, mPort));
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        logger.info("开始建立连接....已完成，连接成功");
                    } else {
                        throw new Exception();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("开始建立连接....失败");
            connectFailed();
        }
        return 0;
    }

    private void connectFailed() {
        Response response = new Response();
        response.setSucceed(false);
        response.setPid(pid);
        response.setData("连接服务器失败");
        callback.callback(client, response);
    }


    /**
     * 网络协议解析
     *
     * @author lilongtan
     */
    public class EzMessageDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext arg0, ByteBuf buffer,
                              List<Object> out) throws Exception {

            buffer.markReaderIndex();
            if (buffer.readableBytes() < 6) {
                return;
            }
            //获取包头对象
            byte[] headdecoded = new byte[6];
            buffer.readBytes(headdecoded);
            String jsonhead = NativeTools.parseVerifyCodeHeadFromJNI(headdecoded);
            JSONObject jsonObject = new JSONObject(jsonhead);
            int pid = jsonObject.getInt("wPid");
            int bodyLen = jsonObject.getInt("dwBodyLen");

            //获取包体对象
            int bodylength = bodyLen;//需要换成json对应的值
            if (buffer.readableBytes() < bodylength) {
                return; // (2)
            }
            byte[] bodydecoded = new byte[bodylength];
            buffer.readBytes(bodydecoded);
            String jsonbody = request.parse(headdecoded, bodydecoded);
            //获取图片内容,在bodydecoded内容里面,bodydecoded+VerifyCodeBodySizeB偏移
            NetPackage netPackage = new NetPackage();
            netPackage.pid = pid;
            netPackage.data = jsonbody;
            out.add(netPackage);
            logger.info("EzMessageDecoder");
            return;
        }
    }

    /**
     * 网络协议编码
     */
    public class NetPackageEncoder extends MessageToByteEncoder {

        @Override
        protected void encode(ChannelHandlerContext arg0, Object arg1, ByteBuf arg2) {
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
            ctx.writeAndFlush(request.mData);
        }


        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            logger.info("channelInactive连接关闭成功 channelId = " + ctx.toString());
            ctx.close();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            super.channelRead(ctx, msg);
            logger.info("channelRead");
            if (msg != null && msg instanceof NetPackage) {
                NetPackage netPackage = (NetPackage) msg;
                Response response = new Response();
                response.setData(netPackage.data);
                response.setPid(pid);
                response.setSucceed(true);
                request.received(response, client);
                callback.callback(client,response);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            super.exceptionCaught(ctx, cause);
            //网络异常，断开网络
            logger.info("连接异常,关闭连接，不做其它操作...exceptionCaught");
        }
    }

}
