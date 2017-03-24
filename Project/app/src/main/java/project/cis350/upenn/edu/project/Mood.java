package com.example.jamietomlinson.iteration2;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AK47 on 2/23/17.
 */

public class Mood extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            //send to API
            try {
                URL url = new URL("https://api.theysay.io/v1/emotion");
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.addRequestProperty("text", params[0]);
                urlConn.addRequestProperty("level", "document");
                urlConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConn.setDoInput(true);
                urlConn.setRequestMethod("POST");
                urlConn.connect();
                System.out.println(urlConn.getResponseMessage());
                try {
                    InputStream in = urlConn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                } finally {
                    urlConn.disconnect();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("here");
            return "success";
        }


    }

