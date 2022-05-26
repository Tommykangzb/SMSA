package com.example.campus.netty;

import android.util.Log;

import com.example.campus.protoModel.MessageBase;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final String TAG = "ClientHandler ";
    private List<IMessage> observers;

    private static volatile ClientHandler clientHandler;
    public static ClientHandler getInstance(){
        if (clientHandler == null){
            synchronized (ClientHandler.class){
                if (clientHandler == null){
                    clientHandler = new ClientHandler();
                }
            }
        }
        return clientHandler;
    }
    /**
     * 连接成功
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //listener.onServiceStatusConnectChanged(ChatListener.STATUS_CONNECT_SUCCESS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Log.e(TAG, "channelInactive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageBase.Message) {
            Log.e(TAG, "receive msg!");
            onMessage((MessageBase.Message) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当引发异常时关闭连接。
        Log.e(TAG, "引发异常,关闭连接:" + cause.toString());
        //listener.onServiceStatusConnectChanged(ChatListener.STATUS_CONNECT_ERROR);
        cause.printStackTrace();
        ctx.close();
    }

    public void addListener(IMessage message) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        if (message == null) {
            return;
        }
        observers.add(message);
    }

    public void deleteListener(IMessage message) {
        if (observers == null || message == null) {
            return;
        }
        observers.remove(message);
    }

    public void onMessage(MessageBase.Message message) {
        if (observers == null || message == null) {
            return;
        }
        for (IMessage observer : observers) {
            observer.onMessage(message);
        }
    }
}
