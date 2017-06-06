package com.cetc32.zookeeper.master;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * master 服务器工作类
 * <p>
 * User: zhongjun
 * Date: 2017/5/14
 * Time: 10:28
 */
public class WorkServer {

    /**
     * 服务器是否在运行
     */
    private volatile boolean isRunning = false;

    private static final String MASTER_PATH = "/master";

    /**
     * 客户端链接
     */
    private ZkClient zkClient;

    /**
     * 主节点路径
     */
    private WorkServerInfo workServerInfo;

    /**
     * 订阅节点的子节点内容的变化
     */
    private IZkDataListener iZkDataListener;

    /**
     * 从节点
     */
    private WorkServerInfo slaveInfo;

    /**
     * 主节点
     */
    private WorkServerInfo masterInfo;

    /**
     * 延迟执行
     */
    private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(2);

    private int delayTime = 5;

    /**
     * 构造方法
     *
     * @param runInfo
     */
    public WorkServer(WorkServerInfo runInfo) {
        // 开始相当于从节点
        this.slaveInfo = runInfo;

        this.iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                // ignore;
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                // TODO 竞争master节点
                // 直接挣抢
                takeMaster();

                // 对应网络抖动的方法
                // 由于网络抖动，可能误删了master节点导致重新选举，如果master还未宕机，而被其他节点抢到了，
                // 会造成可能有写数据重新生成等资源的浪费。我们这里，增加一个判断，如果上次自己不是master就等待5s在开始争抢master，
                // 这样就能保障没有宕机的master能再次选中为master。
				/*if (masterInfo != null && masterInfo.getName().equals(slaveInfo.getName())) {
					takeMaster();
				} else {
					// 延迟5s再争抢
					delayExector.schedule(new Runnable() {
						public void run() {
							takeMaster();
						}
					}, delayTime, TimeUnit.SECONDS);
				}*/


            }
        };

    }


    /**
     * 启动服务
     *
     * @throws Exception
     */
    public void startServer() throws Exception {
        System.out.println(this.slaveInfo.getName() + "is start!");
        if (isRunning) {
            throw new Exception("server has startup...");
        }
        isRunning = true;
        // 订阅删除事件
        zkClient.subscribeDataChanges(MASTER_PATH, iZkDataListener);
        // 竞争master节点
        takeMaster();
    }


    /**
     * 停止服务
     */
    public void stopServer() throws Exception {
        if (!isRunning) {
            throw new Exception("server has stoped");
        }
        isRunning = false;
        // 关闭线程池
        delayExector.shutdown();
        // 取消订阅删除事件
        zkClient.unsubscribeDataChanges(MASTER_PATH, iZkDataListener);
        // 释放master
        releaseMaster();
    }


    /**
     * 争抢master
     *
     *
     */
    public void takeMaster() {
        // 停止的没有意义
        if (!isRunning) {
            return;
        }

        try {
            // 创建临时节点
            zkClient.create(MASTER_PATH, slaveInfo, CreateMode.EPHEMERAL);
            masterInfo = slaveInfo;
            System.out.println(slaveInfo.getName() + " is master");

            // 测试: 5s后判断是否是master节点,是的话 释放master节点
            // 释放后,其他节点都是有监听删除事件的,会争抢master
            delayExector.schedule(new Runnable() {
                public void run() {
                    if (checkIsMaster()) {
                        releaseMaster();
                    }
                }
            }, 5, TimeUnit.SECONDS);

        } catch (ZkNodeExistsException e) {
            WorkServerInfo runningInfo = zkClient.readData(MASTER_PATH, true);
            if (runningInfo == null) {
                takeMaster();
            } else {
                masterInfo = runningInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     *
     * 释放master
     *
     */
    public void releaseMaster() {
        if (checkIsMaster()) {
            zkClient.delete(MASTER_PATH);
        }
    }

    /**
     * 检测是否master节点
     *
     * @return
     */
    public boolean checkIsMaster() {
        try {
            // 获取master节点数据
            WorkServerInfo serverMasterData = zkClient.readData(MASTER_PATH);
            masterInfo = serverMasterData;
            if (masterInfo.getName().equals(slaveInfo.getName())) {
                return true;
            }
            return false;
        } catch (ZkNoNodeException e) {
            return false;
        } catch (ZkInterruptedException e) {
            return checkIsMaster();
        } catch (ZkException e) {
            return false;
        }
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }
}
