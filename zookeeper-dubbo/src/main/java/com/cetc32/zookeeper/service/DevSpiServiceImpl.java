package com.cetc32.zookeeper.service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zhongjun
 * Date: 2017/5/11
 * Time: 10:18
 */
public class DevSpiServiceImpl implements SpiService {

    public void sayHello(String greetings) {
        System.out.println("DevSpiServiceImpl hello,"+greetings);
    }

    public List<String> getUsers() {
        List<String> users = new ArrayList<String>();
        users.add("dev-tom");
        users.add("dev-lily");
        users.add("dev-jhon");
        users.add("dev-alex");
        return users;
    }
}
