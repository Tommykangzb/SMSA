package com.example.campus.netty;

import static com.example.campus.message.MessageManager.WHAT_WS_MESSAGE;
import static com.example.campus.message.MessageManager.WHAT_WS_MESSAGE_FAIL;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.campus.message.MessageManager;
import java.net.InetSocketAddress;
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
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        Log.e(TAG, "local: " + channel.localAddress() + "  客户端 " + channel.remoteAddress() + "下线, port：" + port);
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg != null) {
            Message handlerMsg = new Message();
            handlerMsg.what = WHAT_WS_MESSAGE;
            handlerMsg.obj = msg;
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
        Message handlerMsg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("IMESSAGE_STRING", cause.toString());
        handlerMsg.setData(bundle);
        handlerMsg.what = WHAT_WS_MESSAGE_FAIL;
        if (messageHandler == null) {
            messageHandler = MessageManager.INSTANCE;
        }
        messageHandler.sendMessage(handlerMsg);
    }

    public void setMessageHandler(Handler handler){
        messageHandler = handler;
    }

    public void setCurrentId(String id){
        currentId = id;
    }
}
