package com.mycompany.server.database.nosql;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDbDatabaseHandler
{
    
    public <T> void write(T obj, Class<T> objClass)
    {
        openConnection();
        if(!isConnected) {
            return;
        }
        JacksonDBCollection<T, Integer> collection = JacksonDBCollection.wrap(database.getCollection("games"), objClass, Integer.class);
        collection.insert(obj);
        closeConnection();
    }
    
    public <T> T read(Class<T> objClass, int id)
    {
        T result;
        
        openConnection();
        if(!isConnected) {
            return null;
        }
        JacksonDBCollection<T, Integer> collection = JacksonDBCollection.wrap(database.getCollection("games"), objClass, Integer.class);
        result = collection.findOneById(id);
        closeConnection();
        return result;
    }
    
    public void delete(int id)
    {
        openConnection();
        if(!isConnected) {
            return;
        }
        database.getCollection("games").remove(new BasicDBObject("_id", id));
        closeConnection();
    }
    
    public <T> void update(T obj, Class<T> objClass, int id)
    {
        openConnection();
        if(!isConnected) {
            return;
        }
        JacksonDBCollection<T, Integer> collection = JacksonDBCollection.wrap(database.getCollection("games"), objClass, Integer.class);
        collection.updateById(id, obj);
        closeConnection();
    }
    
    public void deleteAll()
    {
        openConnection();
        if(!isConnected) {
            return;
        }
        database.getCollection("games").drop();
        closeConnection();
    }
    
    @SuppressWarnings("unchecked")
    public <T> T[] readAll(Class<T> objClass)
    {
        List<T> result = new ArrayList<>();
        DBCursor<T> cursor;
        
        openConnection();
        if(!isConnected) {
            return null;
        }
        JacksonDBCollection<T, Integer> collection = JacksonDBCollection.wrap(database.getCollection("games"), objClass, Integer.class);
        cursor = collection.find();
        result.add(cursor.curr());
        while(cursor.hasNext()) {
            result.add(cursor.next());
        }
        closeConnection();
        return (T[]) result.toArray();
    }
    
    public int getMaxId()
    {
        int result = -1;
        
        openConnection();
        if(!isConnected) {
            return result;
        }
        result = (int) database.getCollection("games").findOne(new BasicDBObject(), new BasicDBObject("_id", 1), new BasicDBObject("_id", -1)).get("_id");
        closeConnection();
        return result;
    }
    
    private void openConnection()
    {
        MongoCredential credential = MongoCredential.createMongoCRCredential(userName, "admin", password.toCharArray());
        try {
            mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(credential));
        } catch (UnknownHostException e) {
            return;
        }
        database = mongoClient.getDB("games");
        isConnected = true;
    }
    
    private void closeConnection()
    {
        mongoClient.close();
        isConnected = false;
    }
    
    private boolean isConnected = false;
    private DB database;
    private MongoClient mongoClient;
    private final String userName = "user";
    private final String password = "qwerty";
}
