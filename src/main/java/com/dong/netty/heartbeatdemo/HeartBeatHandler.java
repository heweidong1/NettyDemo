package com.dong.netty.heartbeatdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/*
* 用于处理客户端与服务器端得心跳，在客户端空闲【飞行模式】时channel，节省服务器资源
* */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    /*
    * 用户事件触发得处理器
    * */

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断evt是否属于IdleStateEvent，用于触发用户事件，包含读空闲，写空闲，读写空闲
        if(evt instanceof IdleStateEvent)
        {
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state()== IdleState.READER_IDLE)
            {
                //读空闲，不做处理，因为可能服务器在进行写操作【往客户端发送消息中】
                System.out.println("进入读空闲");
            }else if(event.state()==  IdleState.WRITER_IDLE)
            {
                //不做处理，因为可能服务器在读取客户端发来的消息
                System.out.println("进入写空闲");
            }else if(event.state()==IdleState.ALL_IDLE)
            {
                System.out.println("channel关闭前，users得数量："+ChatHandler.users.size());
                ctx.channel().close();
                System.out.println("channel关闭后，users得数量："+ChatHandler.users.size());
            }
        }
    }




































}
