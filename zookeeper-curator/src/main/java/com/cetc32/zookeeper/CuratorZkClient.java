package com.cetc32.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: zhongjun
 * Date: 2017/5/13
 * Time: 22:46
 */
public class CuratorZkClient {

    private CuratorFramework curatorFramework = null;

    ExecutorService executorService = Executors.newFixedThreadPool(5);//线程池

    public void createSession() {
        String zkServers = "192.168.1.214:2181,192.168.1.215:2181,192.168.1.216:2181";
        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);//基本重试间隔时间，重试次数(每次重试时间加长)
        //RetryPolicy retryPolicy = new RetryNTimes(5,1000);//重试次数，重试间隔时间
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);//重试时间，重试间隔时间
        //curatorFramework = CuratorFrameworkFactory.newClient("192.168.117.128:2181",5000,5000,retryPolicy);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkServers)
                //.authorization() 设置访问权限 设置方法同原生API
                .sessionTimeoutMs(5000).connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy).build();
        curatorFramework.start();
    }

    public void createNode() throws Exception {
        createSession();
        String path = curatorFramework.create()
                .creatingParentsIfNeeded()//如果父节点没有自动创建
                //.withACL()设置权限  权限创建同原生API
                .withMode(CreateMode.PERSISTENT)//节点类型
                .forPath("/note_curator/02", "02".getBytes());
        System.out.println("path:" + path);
    }

    public void del() throws Exception {
        createSession();

        // curatorFramework.delete().


        curatorFramework.delete()
                .guaranteed()//保证机制，出错后后台删除 直到删除成功
                .forPath("/note_curator");
    }


    public void getChildren() throws Exception {
        createSession();
        List<String> children = curatorFramework.getChildren().forPath("/note_curator");
        System.out.println(children);

    }

    public void getData() throws Exception {
        createSession();
        Stat stat = new Stat();
        byte[] u = curatorFramework.getData().storingStatIn(stat).forPath("/note_curator");
        System.out.println(new String(u));
        System.out.println(stat);
    }

    public void setData() throws Exception {
        createSession();
        curatorFramework.setData()
                //.withVersion(1) 设置版本号 乐观锁概念
                .forPath("/note_curator/01", "shengke0815".getBytes());
    }

    public void exists() throws Exception {
        createSession();
        Stat s = curatorFramework.checkExists().forPath("/note_curator");
        System.out.println(s);
    }


    public void setDataAsync() throws Exception {
        createSession();
        curatorFramework.setData().inBackground(new BackgroundCallback() {//设置节点信息时回调方法
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {

                System.out.println(curatorFramework.getZookeeperClient());
                System.out.println(curatorEvent.getResultCode());
                System.out.println(curatorEvent.getPath());
                System.out.println(curatorEvent.getContext());
            }
        }, "shangxiawen", executorService).forPath("/note_curator", "sksujer0815".getBytes());
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void nodeListen() throws Exception {
        createSession();
        final NodeCache cache = new NodeCache(curatorFramework, "/note_curator");
        cache.start();
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println(new String(cache.getCurrentData().getData()));
                System.out.println(cache.getCurrentData().getPath());
            }
        });

        Thread.sleep(Integer.MAX_VALUE);

    }


    public void nodeClildrenListen() throws Exception {
        createSession();
        final PathChildrenCache cache = new PathChildrenCache(curatorFramework, "/note_curator", true);
        cache.start();
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType()) {
                    case CHILD_ADDED:
                        System.out.println("add children");
                        System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
                        System.out.println(new String(pathChildrenCacheEvent.getData().getPath()));
                        break;
                    case CHILD_REMOVED:
                        System.out.println("remove children");
                        System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
                        System.out.println(new String(pathChildrenCacheEvent.getData().getPath()));
                        break;
                    case CHILD_UPDATED:
                        System.out.println("update children");
                        System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
                        System.out.println(new String(pathChildrenCacheEvent.getData().getPath()));
                        break;
                }
            }
        });

        Thread.sleep(Integer.MAX_VALUE);

    }

    public static void main(String[] args) throws Exception {

        CuratorZkClient client = new CuratorZkClient();
        // client.createNode();
        client.del();

    }


}
