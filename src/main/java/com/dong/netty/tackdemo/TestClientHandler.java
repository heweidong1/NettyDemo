package com.dong.netty.tackdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestClientHandler extends SimpleChannelInboundHandler<String> {
    //读取客户端发送来的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg.trim()+"\n");
    }

    //发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }

}
