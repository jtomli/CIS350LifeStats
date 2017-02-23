package edu.upenn.cis350.lifestats;

import android.os.AsyncTask;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

/**
 * Created by emmharv on 2/22/17.
 */

public class OpenDB extends AsyncTask {

    public OpenDB() {
        MongoClientURI connectionString = new MongoClientURI(
                "mongodb://kitchena:CIS350@ds131139.mlab.com:31139/cis350");
        MongoClient mongoClient = new MongoClient(connectionString);

        MongoDatabase database = mongoClient.getDatabase("cis350");
        MongoCollection<Document> collection = database.getCollection("lifeStats");

        Document doc = new Document("username", "username")
                .append("password", "password");

        collection.insertOne(doc);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}
