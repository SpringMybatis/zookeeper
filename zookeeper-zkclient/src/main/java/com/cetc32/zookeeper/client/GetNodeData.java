package com.cetc32.zookeeper.client;

import com.cetc32.zookeeper.model.User;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;

/**
 * User: zhongjun
 * Date: 2017/5/13
 * Time: 21:26
 */
public class GetNodeData {

    public static void main(String[] args) {

        String zkServers = "192.168.1.214:2181,192.168.1.215:2181,192.168.1.216:2181";

        ZkClient zkClient = new ZkClient(zkServers,10000,10000,new SerializableSerializer());
        System.out.println("服务器和客户端连接Session创建成功！");

        Stat stat = new Stat();
        User user = zkClient.readData("/update",stat);
        System.out.println(user.getName());
        System.out.println(stat.toString());

    }

}
