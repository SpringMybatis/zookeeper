package com.cetc32.zookeeper;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * User: zhongjun
 * Date: 2017/5/11
 * Time: 10:55
 */
public class Provider {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider.xml");

        context.start();

        System.in.read();

    }

}
