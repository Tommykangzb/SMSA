package com.example.campus.netty;

import android.util.Log;

import com.example.campus.protoModel.MessageBase;
import com.google.protobuf.ByteString;

public class NettyConnectManager {
    private static final int port = 36874;

    private static volatile NettyConnectManager instance;
    private final NettyClient client;
    //private Handler handler;

    public static NettyConnectManager getInstance() {
        if (instance == null) {
            synchronized (NettyConnectManager.class) {
                if (instance == null) {
                    instance = new NettyConnectManager();
                }
            }
        }
        return instance;
    }

    public NettyConnectManager(){
        client = new NettyClient(port);
    }

    public void connect(){
        new Thread(()->{
            if (!client.getConnectState()){
                Log.e("NettyConnectManager"," connect");
                client.connect();
            }
        }).start();
    }

    public void reConnect(){
        new Thread(()->{
            if (!client.getConnectState()){
                client.reconnect();
            }
        }).start();
    }

    public void sendTextMsg(String senderId, String remoteId, String data) {
        MessageBase.Message.Builder builder = MessageBase.Message.newBuilder();
        builder.setSenderId(senderId)
                .setReceiverId(remoteId)
                .setData(ByteString.copyFrom(data.getBytes()))
                .setTimeStamp(System.currentTimeMillis())
                .setAckMsgId("123")
                .setMsgId("123")
                .setSource(1)
                .setSource(1);
        client.sendMsg(builder.build());
    }
}
