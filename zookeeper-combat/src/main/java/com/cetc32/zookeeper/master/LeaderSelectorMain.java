package com.cetc32.zookeeper.master;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zhongjun
 * Date: 2017/5/14
 * Time: 11:04
 */
public class LeaderSelectorMain {

    /** 启动的服务个数 */
    private static final int CLIENT_QTY = 10;

    /** zookeeper服务器的地址 */
    private static final String ZOOKEEPER_SERVER = "192.168.1.215:2181";


    public static void main(String[] args) throws Exception {
        // 保存所有zkClient的列表
        List<ZkClient> clients = new ArrayList<ZkClient>();
        // 保存所有服务器的列表
        List<WorkServer> workServers = new ArrayList<WorkServer>();


        try {
            for (int i = 0; i < CLIENT_QTY; i++) {
                // 创建zkClient
                ZkClient client = new ZkClient(ZOOKEEPER_SERVER, 5000, 5000, new SerializableSerializer());
                // 保存zkClient的列表
                clients.add(client);

                // 创建serverData
                WorkServerInfo runningInfo = new WorkServerInfo();
                runningInfo.setCid(Long.valueOf(i));
                runningInfo.setName("Client #" + i);
                runningInfo.setAddress("192.168.2.1"+i);

                // 创建server
                WorkServer workServer = new WorkServer(runningInfo);
                workServer.setZkClient(client);
                // 保存服务器的列表
                workServers.add(workServer);

                //启动服务
                workServer.startServer();

            }
            System.out.println("敲回车键退出！\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("Shutting down...");
            for (WorkServer workServer : workServers) {
                try {
                    workServer.stopServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (ZkClient client : clients) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
