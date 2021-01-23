package com.dong.netty.tackdemo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class TestServerInitializer  extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline=ch.pipeline();
        //基于分隔符解决 粘包拆包问题  入栈
        pipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
        //入栈
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        //出栈
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //入栈
        pipeline.addLast(new TestServerHandler());
    }


}
