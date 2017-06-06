package com.cetc32.zookeeper.client;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * User: zhongjun
 * Date: 2017/5/13
 * Time: 22:06
 */
public class SubscribeDataChanges {

    private static class ZKDataListener implements IZkDataListener {


        public void handleDataChange(String dataPath, Object data) throws Exception {
            System.out.println(dataPath + ":" + data.toString());
        }

        public void handleDataDeleted(String dataPath) throws Exception {
            System.out.println(dataPath);
        }
    }


    public static void main(String[] args) throws InterruptedException {

        String zkServers = "192.168.1.214:2181,192.168.1.215:2181,192.168.1.216:2181";

        ZkClient zkClient = new ZkClient(zkServers, 10000, 10000, new SerializableSerializer());
        System.out.println("服务器和客户端连接Session创建成功！");

        /**
         * "/testUserNode" 监听的节点，可以是现在存在的也可以是不存在的
         */
        zkClient.subscribeDataChanges("/testUserNode", new ZKDataListener());
        Thread.sleep(Integer.MAX_VALUE);

    }


}
