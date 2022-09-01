package com.example.campus.netty;

import android.util.Log;

import com.example.campus.protoModel.MessageBase;

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
    private boolean connectedState;

    // 客户端Bootstrap参数
    private EventLoopGroup group;

    // 通过对象发送数据到服务端
    private SocketChannel channel;
    //private final Bootstrap bootstrap;
    //防止冗余连接请求
    private boolean connecting;
    //是否需要进行重连
    private static boolean isNeedReconnect;

    private int reconnectTime = 10;

    public NettyClient(int port) {
        this.port = port;
    }

    public boolean getConnectState() {
        return !connectedState;
    }

    public void connect() {
        Log.e(TAG," connecting: " + connecting);
        if (connecting) {
            return;
        }
        connectServer(null);
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
                channelFuture = bootstrap.connect("192.168.1.198", 9090);
                if (listener != null) {
                    channelFuture.addListener(listener);
                } else {
                    channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                        if (channelFuture1.isSuccess()) {
                            connectedState = true;
                            isNeedReconnect = false;
                            Log.e(TAG, "连接成功c");
                        } else {
                            Log.e(TAG, "连接失败, port: " + port);
                            connectedState = false;
                            isNeedReconnect = true;
                            reconnect();
                        }
                        connecting = false;
                    });
                }
                channel = (SocketChannel)channelFuture.channel();
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
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10*1000)
                        .handler(new ClientChannelInitializer());
                try {
                    //连接监听
                    channelFuture = bootstrap.connect(host, port);
                    channelFuture.addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            connectedState = true;
                            channel = (SocketChannel)future.channel();
                            Log.e(TAG, "连接成功s");
                        } else {
                            Log.e(TAG, "连接失败, port: " + port + "fail reason: " + future.cause());
                            connectedState = false;
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
                    if (!connecting) {
                        reconnect();
                    }
                    connecting = false;
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

    public void sendMsg(MessageBase.Message msg) {
        if (msg == null || channel == null) {
            Log.e(TAG,"msg is null or channel is null");
            return;
        }
        if (!connectedState || !channel.isActive()) {
            Log.e(TAG, "重连！  ");
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
        } else {
            Log.e(TAG, "将要发送消息！  " + msg.getData());
            channel.writeAndFlush(msg).addListener(future -> {
                if (future.isSuccess()){
                    Log.e(TAG, "发送消息成功！  " + msg.getMsgId());
                } else {
                    Log.e(TAG, "发送消息失败！  " + future.cause());
                }
            });
        }

    }

    //重新连接
    public void reconnect() {
        Log.e(TAG, "reconnect");
        if (isNeedReconnect && reconnectTime > 0 && !connecting) {
            reconnectTime--;
            //SystemClock.sleep(10000);
            if (reconnectTime > 0) {
                Log.e(TAG, "重新连接, reconnectTime: " + reconnectTime);
                connectServer(null);
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
}
