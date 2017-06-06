package com.cetc32.zookeeper.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;

/**
 * 创建节点(同步)
 *
 * @author jerome_s@qq.com
 */
public class CreateNodeSync implements Watcher {

    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.10.5:2181", 5000, new CreateNodeSync());
        System.out.println(zookeeper.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething() {
        try {
            // Ids.OPEN_ACL_UNSAFE 任何人可以对这个节点进行任何操作
            String path = zookeeper.create("/node2", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("return path:" + path);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("do something");
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("收到事件：" + event);
        if (event.getState() == KeeperState.SyncConnected) {
            doSomething();
        }
    }

	/*
    输出
	CONNECTING
	收到事件：WatchedEvent state:SyncConnected type:None path:null
	return path:/node2
	do something
	*/

}
