package com.cetc32.zookeeper.constant;

/**
 * Created by Administrator on 2017/5/9.
 */
public enum ZkCommon {

    HOSTS("hosts","192.168.1.209:2181"),

    SESSION_TIMEOUT("timeout","2000"),

    ;

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ZkCommon(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
