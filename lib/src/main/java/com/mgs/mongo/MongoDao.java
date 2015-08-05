package com.mgs.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import java.util.Map;

public class MongoDao {
    private final DB db;

    public MongoDao(DB db) {
        this.db = db;
    }

    public void persist(String collectionName, Map<String, Object> toSave){
        collection(collectionName).insert(parse(toSave));
    }

    public DBCursor findLiteral (String collectionName, Map<String, Object> query){
        return collection(collectionName).find(parse(query));
    }

    private DBCollection collection(String collectionName) {
        return db.getCollection(collectionName);
    }

    private BasicDBObject parse(Map<String, Object> toSave) {
        return new BasicDBObject(toSave);
    }
}
