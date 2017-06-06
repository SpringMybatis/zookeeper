package com.cetc32.zookeeper.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import java.io.IOException;

/**
 * 删除节点(异步)
 *
 * @author jerome_s@qq.com
 */
public class DeleteNodeASync implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("192.168.10.5:2181", 5000, new DeleteNodeASync());
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(WatchedEvent event) {
        zooKeeper.delete("/node2/node22", -1, new IVoidCallback(), null);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(event);
            }
        }
    }

    static class IVoidCallback implements AsyncCallback.VoidCallback {

        @Override
        public void processResult(int rc, String path, Object ctx) {
            StringBuilder sb = new StringBuilder();
            sb.append("rc=" + rc).append("\n");
            sb.append("path" + path).append("\n");
            sb.append("ctx=" + ctx).append("\n");
            System.out.println(sb.toString());
        }
    }

	/*
    输出：
	rc=0
	path/node2/node22
	ctx=null
	*/
}
