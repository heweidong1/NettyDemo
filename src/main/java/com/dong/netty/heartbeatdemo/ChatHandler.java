package com.dong.netty.heartbeatdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 自定义handler，机型简单频道入栈处理程序，范围为wen文本套接字Frame
 * websocket间通过frame进行数据得传递和发送
 * 此版为user与channel绑定得版本，消息会定向发送和接收到指定得user和channel中
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //定义channel集合，管理channel，传入全局事件执行器
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        System.out.println("接收到得消息："+text);
    }

    //当客户端连接时，回调该方法，将客户端添加到users中
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
        String s = ctx.channel().id().asShortText();
        System.out.println("客户端添加，channelId为："+s);
    }

    //处理器移除时，移除channelGroup中得channel
    //当心跳机制移除客户端得时候也会调用该方法
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String s = ctx.channel().id().asShortText();
        System.out.println("客户端被移除，channelId为："+s);
        users.remove(ctx.channel());
    }




    //发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }

























}
