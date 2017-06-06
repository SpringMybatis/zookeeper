package com.cetc32.zookeeper.master;

import java.io.Serializable;

/**
 * WorkServer的配置信息
 *
 * User: zhongjun
 * Date: 2017/5/14
 * Time: 10:26
 */
public class WorkServerInfo implements Serializable {

    private Long cid;

    private String name;

    private String address;


    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
