package com.cetc32.zookeeper.client;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.List;

/**
 * User: zhongjun
 * Date: 2017/5/13
 * Time: 22:02
 */
// 订阅节点的信息改变（创建节点，删除节点，添加子节点）
public class SubscribeChildChanges {


    private static class ZKChildListener implements IZkChildListener {
        /**
         * handleChildChange： 用来处理服务器端发送过来的通知
         * parentPath：对应的父节点的路径
         * currentChilds：子节点的相对路径
         */
        public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
            System.out.println(parentPath);
            System.out.println(currentChilds.toString());
        }
    }


    public static void main(String[] args) throws InterruptedException {

        String zkServers = "192.168.1.214:2181,192.168.1.215:2181,192.168.1.216:2181";

        ZkClient zkClient = new ZkClient(zkServers,10000,10000,new SerializableSerializer());
        System.out.println("服务器和客户端连接Session创建成功！");


        //zk集群的地址
        /**
         * "/testUserNode" 监听的节点，可以是现在存在的也可以是不存在的
         */
        zkClient.subscribeChildChanges("/update", new ZKChildListener());
        Thread.sleep(Integer.MAX_VALUE);

    }

}
