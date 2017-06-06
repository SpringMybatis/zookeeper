package com.cetc32.zookeeper.model;

import java.io.Serializable;

/**
 * User: zhongjun
 * Date: 2017/5/13
 * Time: 21:37
 */
// 注意：一定要实现序列化接口  implements Serializable
public class User implements Serializable{

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
