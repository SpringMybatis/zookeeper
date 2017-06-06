package com.cetc32.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;

import java.util.List;

/**
 * 获取节点的子节点
 *
 * @author jerome_s@qq.com
 */
public class GetChildren {

    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.10.5:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        client.start();

        List<String> cList = client.getChildren().forPath("/node1");

        System.out.println(cList.toString());
    }

    // console:
    // [node11, node12]

}
