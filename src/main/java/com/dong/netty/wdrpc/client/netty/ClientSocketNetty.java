package com.dong.netty.wdrpc.client.netty;

import com.dong.netty.wdrpc.server.service.TestService;

public class ClientSocketNetty {
    public static void main(String[] args) {
        TestService o =(TestService) ClientRpcProxy.create(TestService.class);
        System.out.println(o.listAll());
    }
}
