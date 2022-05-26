package com.example.campus.netty;

import com.example.campus.protoModel.MessageBase;

/**
 * 观察者模式，对于所有需要接收IM消息的场景
 * 只需要注册即可。
 */
public interface IMessage {
    void onMessage(MessageBase.Message msg);
}
