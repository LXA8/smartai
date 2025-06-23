package com.tcy.smartai.api;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class MongodbTest {
    public static void main(String[] args) {
        String url = "mongodb://user:password@localhost:27017/test?authSource=admin";
        try (MongoClient mongoClient = MongoClients.create(url)) {
            MongoDatabase database = mongoClient.getDatabase("mydatabase");
            System.out.println("Connected to MongoDB");
        }
    }
}
