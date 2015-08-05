package com.mgs.mongo;

import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class MongoDaoFactory {
    public MongoDao mongoDao(String url, int port, String databaseName){
        try {
            MongoClient mongo = new MongoClient( url , port );
            return new MongoDao(mongo.getDB(databaseName));
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }
}
