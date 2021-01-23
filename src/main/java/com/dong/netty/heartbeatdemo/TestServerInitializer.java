package com.dong.netty.heartbeatdemo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //配置Handler
         //websocket基于http协议，需要http编码和解码工具
        pipeline.addLast(new HttpServerCodec());
         //对于大数据流得支持
        pipeline.addLast(new ChunkedWriteHandler());
         //对于http得消息进行聚合，聚合成FullHttpRequest或者FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024*64));
         //针对客户端，如果一分钟之内没有向服务器发送读写心跳，则主动断开
         //如果客户端40s没有发来消息，进入读空闲；
         //如果服务端50s没有给该客户端发送消息进入写空闲
         //如果45s没有读也没有写，就关闭该通道
        pipeline.addLast(new IdleStateHandler(40,50,45));
         //自定义得读写空闲状态检测
        pipeline.addLast(new HeartBeatHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
         //定义自己得handler，主要是对请求进行处理和发送
        pipeline.addLast(new ChatHandler());


    }






























}
