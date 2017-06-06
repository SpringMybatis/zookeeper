package com.cetc32.zookeeper.service;

import java.util.List;

/**
 * User: zhongjun
 * Date: 2017/5/11
 * Time: 10:15
 */
public interface SpiService {

    public void sayHello(String greetings);

    public List<String> getUsers();

}
