package com.dong.netty.wdrpc.client.netty;

import com.dong.netty.tackdemo.TestChlientInitializer;
import com.dong.netty.wdrpc.entity.ClassInfo;
import com.dong.netty.wdrpc.server.netty.ServerSocketNettyHendler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientRpcProxy {
    public static Object create(Class clazz)
    {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassName(clazz.getName());
                classInfo.setMethodName(method.getName());
                classInfo.setArgs(args);
                classInfo.setClazzType(method.getParameterTypes());
                //创建一个线程组
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                ClientSocketNerrtHendler clientSocketNerrtHendler = new ClientSocketNerrtHendler();
                try{
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(bossGroup)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>(){

                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    //添加编码器 序列化
                                    pipeline.addLast("encoder",new ObjectEncoder());
                                    //添加解码器 反序列化
                                    pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                    //将自己编写得服务器端得业务逻辑处理类加入pipeline
                                    pipeline.addLast(clientSocketNerrtHendler);
                                }
                            });
                    //连接
                    System.out.println("-------------client init-----------");
                    ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",9090).sync();

                    Channel channel=channelFuture.channel();

                    channel.writeAndFlush(classInfo).sync();


                    //关闭连接
                    channelFuture.channel().closeFuture().sync();




                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                return clientSocketNerrtHendler.getResponse();
            }
        });

    }
























}
