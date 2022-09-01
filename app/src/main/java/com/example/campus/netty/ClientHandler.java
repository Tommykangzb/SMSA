package com.example.campus.netty;

import static com.example.campus.message.MessageManager.WHAT_WS_MESSAGE;
import static com.example.campus.message.MessageManager.WHAT_WS_MESSAGE_FAIL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.campus.message.MessageManager;
import com.example.campus.protoModel.MessageBase;
import com.google.protobuf.ByteString;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final String TAG = "ClientHandler ";
    private List<IMessage> observers;
    private Handler messageHandler;
    private String currentId;

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
        Channel channel = ctx.channel();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        Log.e(TAG, "local: " + channel.localAddress() + "  客户端 " + channel.remoteAddress() + "上线, port：" + port);
        MessageBase.Message.Builder builder = MessageBase.Message.newBuilder();
        builder.setSenderId(currentId)
                .setReceiverId("-1")
                .setData(ByteString.copyFrom("data".getBytes()))
                .setTimeStamp(System.currentTimeMillis())
                .setAckMsgId("123")
                .setMsgId("123")
                .setType(2)
                .setSource(1);
        ctx.writeAndFlush(builder.build());
        super.channelActive(ctx);
        //listener.onServiceStatusConnectChanged(ChatListener.STATUS_CONNECT_SUCCESS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        Log.e(TAG, "local: " + channel.localAddress() + "  客户端 " + channel.remoteAddress() + "上线, port：" + port);
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageBase.Message) {
            String str = ((MessageBase.Message) msg).getData().toStringUtf8();
            if (str == null) {
                return;
            }
            Log.e(TAG, "receive msg! " + str + " size: " + str.length());
            //onMessage((MessageBase.Message) msg);
            Message handlerMsg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("IMESSAGE_STRING", str);
            handlerMsg.setData(bundle);
            handlerMsg.what = WHAT_WS_MESSAGE;
            if (messageHandler == null) {
                messageHandler = MessageManager.INSTANCE;
                handlerMsg.what = WHAT_WS_MESSAGE_FAIL;
            }
            messageHandler.sendMessage(handlerMsg);
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

    public void setMessageHandler(Handler handler){
        messageHandler = handler;
    }

    public void onMessage(MessageBase.Message message) {
        if (observers == null || message == null) {
            return;
        }
        for (IMessage observer : observers) {
            observer.onMessage(message);
        }
    }

    public void setCurrentId(String id){
        currentId = id;
    }
}
