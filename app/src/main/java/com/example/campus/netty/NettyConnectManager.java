package com.example.campus.netty;

import android.util.Log;

import com.example.campus.helper.SnowFlake;
import com.example.campus.message.MessageType;
import com.example.campus.protoModel.BaseMessageOuterClass;
import com.example.campus.protoModel.ChatMessageOuterClass;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;

public class NettyConnectManager {
    private static final int port = 51811;

    private static final int CHAT_MSG_TEXT = 1;
    private static final int CHAT_MSG_IMAGE = 2;
    private static final int CHAT_MSG_LINK = 3;
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
            if (client.connectState()) {
                Log.e("NettyConnectManager", " connect");
                client.connect();
            }
        }).start();
    }

    public void reConnect(){
        new Thread(()->{
            if (client.connectState()) {
                client.reconnect();
            }
        }).start();
    }

    public void sendTextMsg(String senderId, String remoteId, String data) {
        synchronized (this) {
            BaseMessageOuterClass.BaseMessage.Builder builder = BaseMessageOuterClass.BaseMessage.newBuilder();
            builder.setSenderId(senderId)
                    .setReceiverId(remoteId)
                    .setTimeStamp(System.currentTimeMillis())
                    .setAckMsgId("-1L")
                    .setMsgId(SnowFlake.getInstance().nextMsgId())
                    .setType(MessageType.CHAT_MESSAGE)
                    .setSource(1)
                    .setData(
                            Any.pack(
                                    ChatMessageOuterClass.ChatMessage.newBuilder()
                                            .setData(ByteString.copyFromUtf8(data))
                                            .setDataType(CHAT_MSG_TEXT)
                                            .build()));
            client.sendMsg(builder.build());
        }
    }
}
