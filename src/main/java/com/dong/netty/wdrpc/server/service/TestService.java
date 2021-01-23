package com.dong.netty.wdrpc.server.service;

import java.util.List;

public interface TestService {
    List<String> listAll();

    String listByid(Integer id);

}
