package com.cetc32.zookeeper.service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zhongjun
 * Date: 2017/5/11
 * Time: 10:24
 */
public class TestSpiServiceImpl implements SpiService {

    public void sayHello(String greetings) {
        System.out.println("TestSpiServiceImpl hello,"+greetings);
    }

    public List<String> getUsers() {

        List<String> users = new ArrayList<String>();
        users.add("test-tom");
        users.add("test-lily");
        users.add("test-jhon");
        users.add("test-alex");

        return users;
    }
}
