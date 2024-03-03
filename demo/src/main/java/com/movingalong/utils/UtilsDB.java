package com.movingalong.utils;

import com.mongodb.client.*;

import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class UtilsDB {
    private Datastore datastore;
    private MongoClient mongoClient;

    public void closeClient() {
        try {
            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Datastore getDatastore() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            datastore = Morphia.createDatastore(mongoClient, "moving_along");

            // Configure the data store
            datastore.getMapper().mapPackage("com.mongodb.morphia.entities");
            datastore.ensureIndexes();


            return datastore;
        } catch (Exception e) {
            e.printStackTrace();
            
            return null; 
        }
    }


}
