package com.dong.netty.wdrpc.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientSocketNerrtHendler extends ChannelInboundHandlerAdapter {

    private Object response;


    public Object getResponse() {
        return response;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response=msg;
        ctx.close();
    }
}
