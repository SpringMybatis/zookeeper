package com.cetc32.zookeeper.subscribe;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.List;

/**
 * User: zhongjun
 * Date: 2017/5/14
 * Time: 11:23
 */
public class ManageServer {

    /*  服务器列表的根节点  */
    private String serversPath;

    /* command节点 */
    private String commandPath;

    /* config节点 */
    private String configPath;

    /* ZkClient */
    private ZkClient zkClient;

    /* 配置节点信息 */
    private ServerConfig config;

    /*  服务器列表子节点监听  */
    private IZkChildListener childListener;

    /* config节点的数据监听 */
    private IZkDataListener dataListener;

    /*  服务器列表  */
    private List<String> workServerList;

    public ManageServer(String serversPath, String commandPath,
                        String configPath, ZkClient zkClient, ServerConfig config){
        this.serversPath = serversPath;
        this.commandPath = commandPath;
        this.zkClient = zkClient;
        this.config = config;
        this.configPath = configPath;

        /**
         * 监听server子节点列表的变化(服务发现,订阅)
         */
        this.childListener = new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                workServerList = currentChilds;
                System.out.println("manage server : work server list changed, new list is ");

                //  测试：输出服务器列表
                execList();
            }
        };


        /**
         * 监听command节点数据的变化(配置管理,发布)
         */
        this.dataListener = new IZkDataListener() {
            public void handleDataDeleted(String dataPath) throws Exception {

            }
            /**
             * 数据改变触发(配置管理,发布)
             */
            public void handleDataChange(String dataPath, Object data) throws Exception {
                String cmd = new String((byte[]) data);
                System.out.println("manage server : cmd = " + cmd);
                exeCmd(cmd);
            }
        };

    }


    /*
	 * 执行命令
	 * 1: list 2: create 3: modify
	 */
    private void exeCmd(String cmdType) {
        if ("list".equals(cmdType)) {
            execList();
        } else if ("create".equals(cmdType)) {
            execCreate();
        } else if ("modify".equals(cmdType)) {
            execModify();
        } else {
            System.out.println("mange server ：error command! cmdType = " + cmdType);
        }
    }

    /**
     * 输出服务器列表
     */
    private void execList() {
        System.out.println(workServerList.toString() + "\n");
    }


    /**
     *
     * 创建config节点
     *
     */
    private void execCreate() {
        if (!zkClient.exists(configPath)) {
            try {
                zkClient.createPersistent(configPath, JSON.toJSONString(config).getBytes());
            } catch (ZkNodeExistsException e) {
                zkClient.writeData(configPath, JSON.toJSONString(config).getBytes());
            } catch (ZkNoNodeException e) {
                String parentDir = configPath.substring(0, configPath.lastIndexOf('/'));
                zkClient.createPersistent(parentDir, true);
                execCreate();
            }
        }else{
            System.out.println("manage server : " + configPath + " is exist.");
        }
    }

    /**
     * 修改config节点的数据
     *
     */
    private void execModify() {
        config.setDbUser(config.getDbUser() + "_modify");
        try {
            zkClient.writeData(configPath, JSON.toJSONString(config).getBytes());
        } catch (ZkNoNodeException e) {
            execCreate();
        }
    }


    /**
     * 启动服务
     */
    public void startServer() {
        System.out.println("start manage server.");
        zkClient.subscribeChildChanges(serversPath, childListener);
        zkClient.subscribeDataChanges(commandPath, dataListener);
    }

    /**
     * 停止服务
     */
    public void stopServer() {
        System.out.println("stop manage server.");
        zkClient.unsubscribeChildChanges(serversPath, childListener);
        zkClient.unsubscribeDataChanges(commandPath, dataListener);
    }

}
