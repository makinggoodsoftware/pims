package com.mgs.spring.bean;

public class MongoDbDef {
    private final String host;
    private final int port;
    private final String dbName;

    public MongoDbDef(String host, int port, String dbName) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }
}
