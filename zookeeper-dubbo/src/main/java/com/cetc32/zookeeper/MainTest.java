package com.cetc32.zookeeper;

import com.cetc32.zookeeper.service.SpiService;

import java.util.List;
import java.util.ServiceLoader;

/**
 * User: zhongjun
 * Date: 2017/5/11
 * Time: 10:15
 */
public class MainTest {

    public static void main(String[] args) {
        ServiceLoader<SpiService> loader = ServiceLoader.load(SpiService.class);
        for (SpiService service : loader) {
            service.sayHello("good morning");
            List<String> users = service.getUsers();
            for (String user : users) {
                System.out.println(user);
            }
        }
    }

}
