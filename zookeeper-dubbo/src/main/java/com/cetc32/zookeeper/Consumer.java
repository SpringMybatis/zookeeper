package com.cetc32.zookeeper;

import com.cetc32.zookeeper.service.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: zhongjun
 * Date: 2017/5/11
 * Time: 11:00
 */
public class Consumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        context.start();

        DemoService demoService = (DemoService)context.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.sayHello("world"); // 执行远程方法

        System.out.println( hello ); // 显示调用结果
    }

}
