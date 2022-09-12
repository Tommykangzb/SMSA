package com.example.campus.netty;

import android.util.Log;

import com.example.campus.protoModel.BaseMessageOuterClass;

public class NettyConnectManager {
    private static final int port = 51811;

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
            if (client.getConnectState()){
                Log.e("NettyConnectManager"," connect");
                client.connect();
            }
        }).start();
    }

    public void reConnect(){
        new Thread(()->{
            if (client.getConnectState()){
                client.reconnect();
            }
        }).start();
    }

    public void sendTextMsg(String senderId, String remoteId, String data) {
        BaseMessageOuterClass.BaseMessage.Builder builder = BaseMessageOuterClass.BaseMessage.newBuilder();
        builder.setSenderId(senderId)
                .setReceiverId(remoteId)
                .setTimeStamp(System.currentTimeMillis())
                .setAckMsgId("123")
                .setMsgId("123")
                .setType(1)
                .setSource(1);
        client.sendMsg(builder.build());
    }
}
