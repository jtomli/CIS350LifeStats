package com.example.jamietomlinson.iteration2;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentEmotion;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
import android.content.*;

/**
 * Created by AK47 on 2/23/17.
 */

public class Mood extends AsyncTask<String, Void, String> {

        private Context context;

        public Mood(Context c) {
            context = c;
        }

        @Override
        protected String doInBackground(String[] params) {

            //send to API

            AlchemyLanguage service = new AlchemyLanguage();
            service.setApiKey("7661d71d86a4308318a43b1dbe7a22a0407c6ad9");
            Map<String,Object> param = new HashMap<String, Object>();
            param.put(AlchemyLanguage.TEXT, params[0]);
            DocumentEmotion sentiment = service.getEmotion(param).execute();


            Double anger = sentiment.getEmotion().getAnger();
            Double disgust = sentiment.getEmotion().getDisgust();
            Double fear = sentiment.getEmotion().getFear();
            Double joy = sentiment.getEmotion().getJoy();
            Double sadness = sentiment.getEmotion().getSadness();

            SentimentDatabaseOpenHelper dbh = new SentimentDatabaseOpenHelper(context);
            SQLiteDatabase db = dbh.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SentimentDatabaseContract.SentimentDB.COL_DATE, "Feb 20");
            values.put(SentimentDatabaseContract.SentimentDB.COL_ANGER, anger);
            values.put(SentimentDatabaseContract.SentimentDB.COL_DISGUST, disgust);
            values.put(SentimentDatabaseContract.SentimentDB.COL_FEAR, fear);
            values.put(SentimentDatabaseContract.SentimentDB.COL_JOY, joy);
            values.put(SentimentDatabaseContract.SentimentDB.COL_SADNESS, sadness);
            values.put(SentimentDatabaseContract.SentimentDB.COL_USERNAME, params[1]);
            db.insert(SentimentDatabaseContract.SentimentDB.TABLE_NAME, null, values);
            db.close();

            return "success";
        }



    }

