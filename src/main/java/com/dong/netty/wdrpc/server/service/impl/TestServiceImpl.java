package com.dong.netty.wdrpc.server.service.impl;

import com.dong.netty.wdrpc.server.service.TestService;

import java.util.ArrayList;
import java.util.List;

public class TestServiceImpl implements TestService {

    private static List<String> names = new ArrayList<>();
    static {
        names.add("张三");
        names.add("李四");
    }

    @Override
    public List<String> listAll() {
        return names;
    }

    @Override
    public String listByid(Integer id) {
        return names.get(id);
    }
}
