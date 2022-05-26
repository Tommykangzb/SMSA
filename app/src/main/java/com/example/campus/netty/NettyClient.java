package com.example.campus.netty;

import android.os.SystemClock;
import android.util.Log;

import com.example.campus.protoModel.MessageBase;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyClient {
    private static final String TAG = "NettyClient";
    private static final String host = "t51z536412.qicp.vip";
    private final int port;
    private boolean connectedState;

    // 客户端Bootstrap参数
    private EventLoopGroup group;

    // 通过对象发送数据到服务端
    private Channel channel;
    //private final Bootstrap bootstrap;
    //防止冗余连接请求
    private boolean connecting;
    //是否需要进行重连
    private static boolean isNeedReconnect;

    private int reconnectTime = 5;

    public NettyClient(int port) {
        //group = new NioEventLoopGroup();
        //bootstrap = new Bootstrap();
        this.port = port;
    }

    public boolean getConnectState() {
        return connectedState;
    }

    public void connect() {
        Log.e(TAG," connecting: " + connecting);
        if (connecting) {
            return;
        }
        new Thread(() -> {
            isNeedReconnect = true;
            connectServer(null);
        }).start();
    }

    //连接时的回调
    private void connectChannel(GenericFutureListener<? extends Future<? super Void>> listener) {
        synchronized (NettyClient.this) {
            connecting = true;
            ChannelFuture channelFuture = null;// 连接管理对象
            try {
                //连接监听
                group = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                channelFuture = bootstrap.connect("192.168.234.198", 9090);
                if (listener != null) {
                    channelFuture.addListener(listener);
                } else {
                    channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                        if (channelFuture1.isSuccess()) {
                            connectedState = true;
                            isNeedReconnect = false;
                            Log.e(TAG, "连接成功");
                        } else {
                            Log.e(TAG, "连接失败, port: " + port);
                            connectedState = false;
                            isNeedReconnect = true;
                            reconnect();
                        }
                        connecting = false;
                    });
                }
                channel = channelFuture.channel();
                // 等待连接关闭
                //channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                Log.e(TAG, " 断开连接");
                Log.e(TAG, "e:" + e);
                e.printStackTrace();
            } finally {
                connecting = false;
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


    public void connectServer(GenericFutureListener<? extends Future<? super Void>> listener) {
        synchronized (NettyClient.this) {
            Log.e(TAG, "开始连接");
            ChannelFuture channelFuture = null;// 连接管理对象
            if (!connectedState) {
                connecting = true;
                group = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)// 设置的一系列连接参数操作等
                        .channel(NioSocketChannel.class)
                        .handler(new ClientChannelInitializer());
                try {
                    //连接监听
                    channelFuture = bootstrap.connect("192.168.234.198", 9090);
//                    if (listener != null) {
//                        channelFuture.addListener(listener);
//                    } else {
                    channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                        if (channelFuture1.isSuccess()) {
                            connectedState = true;
                            isNeedReconnect = false;
                            channel = channelFuture1.channel();
                            Log.e(TAG, "连接成功");
                        } else {
                            Log.e(TAG, "连接失败, port: " + port);
                            connectedState = false;
                            isNeedReconnect = true;
                            reconnect();
                        }
                        connecting = false;
                    });
                    // 等待连接关闭
                    //channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    Log.e(TAG, " 断开连接");
                    Log.e(TAG, "e:" + e);
                    e.printStackTrace();
                } finally {
                    connecting = false;
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
    }

    public void sendMsg(MessageBase.Message msg) {
        if (msg == null || channel == null) {
            return;
        }

        if (!connectedState || !channel.isActive()) {
            synchronized (NettyClient.this) {
                connectedState = false;
                connectChannel((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        connectedState = true;
                        isNeedReconnect = false;
                        Log.e(TAG, "连接成功,将要发送消息！");
                        channel.writeAndFlush(msg.toByteArray());
                    } else {
                        isNeedReconnect = true;
                        reconnect();
                    }
                    connecting = false;
                });
            }
        }

    }

    //重新连接
    public void reconnect() {
        Log.e(TAG, "reconnect");
        if (isNeedReconnect && reconnectTime > 0 && !connecting) {
            reconnectTime--;
            SystemClock.sleep(10000);
            if (isNeedReconnect && reconnectTime > 0 && !connecting) {
                Log.e(TAG, "重新连接");
                connectServer(null);
            } else {
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
}
