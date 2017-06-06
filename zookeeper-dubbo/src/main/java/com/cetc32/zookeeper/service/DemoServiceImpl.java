package com.cetc32.zookeeper.service;

/**
 * User: zhongjun
 * Date: 2017/5/11
 * Time: 10:48
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
