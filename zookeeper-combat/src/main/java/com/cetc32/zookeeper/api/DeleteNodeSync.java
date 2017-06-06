package com.cetc32.zookeeper.api;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 删除节点(同步)
 *
 * @author jerome_s@qq.com
 */
public class DeleteNodeSync implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("192.168.10.5:2181", 5000, new DeleteNodeSync());
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(ZooKeeper zooKeeper) {
        try {
            // -1表示不校验版本信息
            zooKeeper.delete("/node2/node21", -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(zooKeeper);
            }
        }
    }

	/*
    输出：
	无返回值
	*/

}
