package com.cetc32.zookeeper.subscribe;

/**
 * User: zhongjun
 * Date: 2017/5/14
 * Time: 11:22
 */
public class ServerConfig {

    private String dbUrl;

    private String dbPwd;

    private String dbUser;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbPwd() {
        return dbPwd;
    }

    public void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }
}
