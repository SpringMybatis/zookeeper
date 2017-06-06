package com.cetc32.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 创建连接
 *
 * @author jerome_s@qq.com
 */
public class CreateSession {

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        ZkClient zc = new ZkClient("192.168.10.5:2181", 10000, 10000, new SerializableSerializer());
        System.out.println("conneted ok!");
    }

	/*
    console:
	conneted ok!
	*/
}
