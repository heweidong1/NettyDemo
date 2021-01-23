package com.dong.netty.wdrpc.server.netty;

import com.dong.netty.heartbeatdemo.TestServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerSocketNetty {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //辅助类
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列中等待连接得个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //添加编码器 序列化
                            pipeline.addLast("encoder",new ObjectEncoder());
                            //添加解码器 反序列化
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            //将自己编写得服务器端得业务逻辑处理类加入pipeline
                            pipeline.addLast(ServerSocketNettyHendler.serverSocketNeetHendler);

                        }
                    });
            System.out.println("------------server  init-------------");
            ChannelFuture channelFuture = serverBootstrap.bind(9090).sync();
            System.out.println("------------server  start------------");
            channelFuture.channel().closeFuture().sync();

        }catch (Exception e)
        {
            e.fillInStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }



































}
