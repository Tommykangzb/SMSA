package com.example.campus.netty;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8))
                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                .addLast(new IdleStateHandler(30, 30, 30, TimeUnit.SECONDS))
                .addLast(new HeartBeatHandler())
                .addLast(ClientHandler.getInstance());
    }
}
