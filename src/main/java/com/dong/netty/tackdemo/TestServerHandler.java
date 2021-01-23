package com.dong.netty.tackdemo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

//自己写得处理器
public class TestServerHandler extends SimpleChannelInboundHandler<String> {
    //专门存放已经连接上来得客户端集合
    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //Channel处理数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //拿到发送消息得客户端
        Channel channel=ctx.channel();
        group.forEach(ch->{
            if(channel!=ch)
            {
                ch.writeAndFlush(channel.remoteAddress()+":"+msg+"\r\n");
            }
        });
        //向下转发【把消息转发给下一个Handler，因为这个Handler是最后一个，所以改例子中这个方法没用】
        ctx.fireChannelRead(msg);
    }

    //当有客户端连接上来的时候就会回调这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx)throws Exception
    {
        Channel channel = ctx.channel();
        //通知其他连接上来的客户端，某某加入了【消息】
        group.writeAndFlush(channel.remoteAddress()+"加入\n");
        group.add(channel);
    }

    //通道就绪，说明客户端已经做好和服务器断做好通信得准备
    @Override
    public  void channelActive(ChannelHandlerContext ctx)throws Exception
    {
        Channel channel  = ctx.channel();
        System.out.println(channel.remoteAddress()+"上线\n");
        System.out.println(group.size());
    }

    //当客户端断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx)throws Exception
    {
        Channel channel = ctx.channel();
        group.writeAndFlush(channel.remoteAddress()+"离开\n");
    }

    //通道关闭
    @Override
    public void channelInactive(ChannelHandlerContext ctx)throws Exception
    {
        Channel channel =ctx.channel();
        group.writeAndFlush(channel.remoteAddress()+"下线了\n");
    }


    //发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }









}
