package com.example.campus.netty;

import android.util.Log;


import com.example.campus.protoModel.BaseMessageOuterClass;

import java.util.ArrayDeque;
import java.util.Queue;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyClient {
    private static final String TAG = "NettyClient";
    private static final String host = "5y15h36412.oicp.vip";
    private final int port;

    // 客户端Bootstrap参数
    private EventLoopGroup group;

    // 通过对象发送数据到服务端
    private SocketChannel channel;
    //private final Bootstrap bootstrap;
    //-1: 未连接 ；0： 正在连接； 1：连接成功
    private int connecting;
    //是否需要进行重连
    private static boolean isNeedReconnect;
    private final Queue<BaseMessageOuterClass.BaseMessage> messageQueue = new ArrayDeque<>();

    private int reconnectTime = 10;

    public NettyClient(int port) {
        this.port = port;
    }

    public boolean connectState() {
        return connecting == -1;
    }

    public void connect() {
        Log.e(TAG, " connecting: " + connecting);
        if (connecting != -1) {
            return;
        }
        connectServer();
    }

    public void connectServer() {
        synchronized (NettyClient.this) {
            Log.e(TAG, "开始连接");
            ChannelFuture channelFuture = null;// 连接管理对象
            if (connecting == -1) {
                connecting = 0;
                group = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)// 设置的一系列连接参数操作等
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000)
                        .handler(new ClientChannelInitializer());
                try {
                    //连接监听
                    channelFuture = bootstrap.connect(host, port);
                    channelFuture.addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            channel = (SocketChannel) future.channel();
                            connecting = 1;
                            sendPreMsg();
                            Log.e(TAG, "连接成功s");
                        } else {
                            connecting = -1;
                            Log.e(TAG, "连接失败, port: " + port + "fail reason: " + future.cause());
                            reconnect();
                        }
                    }).sync();
                    // 等待连接关闭
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    Log.e(TAG, " 断开连接");
                    Log.e(TAG, "e:" + e);
                    e.printStackTrace();
                } finally {
                    connecting = connecting == 0 ? -1 : connecting;
                    if (null != channelFuture) {
                        if (channelFuture.channel() != null && channelFuture.channel().isOpen()) {
                            channelFuture.channel().close();
                        }
                    }
                    group.shutdownGracefully();
                }
            }
        }
    }

    public void sendMsg(BaseMessageOuterClass.BaseMessage msg) {
        if (msg == null) {
            Log.e(TAG, "msg is null or channel is null");
            return;
        }
        if (connecting == 1 && channel != null && channel.isActive()) {
            channel.writeAndFlush(msg.toByteArray());
            return;
        }
        addMessage(msg);
        if (connecting == -1 || channel == null || !channel.isActive()) {
            synchronized (NettyClient.this) {
                connectChannel((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        connecting = 1;
                        isNeedReconnect = false;
                        Log.e(TAG, "连接成功,将要发送消息！");
                        sendPreMsg();
                    } else {
                        connecting = -1;
                        isNeedReconnect = true;
                        reconnect();
                    }
                });
            }
        }
    }

    //重新连接
    public void reconnect() {
        Log.e(TAG, "reconnect");
        if (isNeedReconnect && reconnectTime > 0 && connecting != -1) {
            reconnectTime--;
            //SystemClock.sleep(10000);
            if (reconnectTime > 0) {
                Log.e(TAG, "重新连接, reconnectTime: " + reconnectTime);
                connectServer();
            } else {
                Log.e(TAG, "断开连接 ");
                disconnect();
            }
        }
    }

    //断开连接
    public void disconnect() {
        Log.e(TAG, "disconnect");
        isNeedReconnect = false;
        group.shutdownGracefully();
    }

    //连接时的回调
    private void connectChannel(GenericFutureListener<? extends Future<? super Void>> listener) {
        synchronized (NettyClient.this) {
            connecting = 0;
            ChannelFuture channelFuture = null;// 连接管理对象
            try {
                //连接监听
                group = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                channelFuture = bootstrap.connect("192.168.1.198", 9090);
                if (listener != null) {
                    channelFuture.addListener(listener);
                } else {
                    channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                        if (channelFuture1.isSuccess()) {
                            isNeedReconnect = false;
                            Log.e(TAG, "连接成功c");
                            connecting = 1;
                        } else {
                            Log.e(TAG, "连接失败, port: " + port);
                            isNeedReconnect = true;
                            connecting = -1;
                            reconnect();
                        }
                    });
                }
                channel = (SocketChannel) channelFuture.channel();
                // 等待连接关闭
                //channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                Log.e(TAG, " 断开连接");
                Log.e(TAG, "e:" + e);
                e.printStackTrace();
            } finally {
                connecting = connecting == 0 ? -1 : connecting;
                //listener.onServiceStatusConnectChanged(ChatListener.STATUS_CONNECT_CLOSED);
                if (null != channelFuture) {
                    if (channelFuture.channel() != null && channelFuture.channel().isOpen()) {
                        channelFuture.channel().close();
                    }
                }
                group.shutdownGracefully();
                reconnect();//重新连接
            }
        }
    }

    private synchronized void addMessage(BaseMessageOuterClass.BaseMessage msg) {
        if (msg == null) {
            return;
        }
        messageQueue.add(msg);
    }

    private void sendPreMsg() {
        while (!messageQueue.isEmpty()) {
            channel.writeAndFlush(messageQueue.poll());
        }
    }
}
