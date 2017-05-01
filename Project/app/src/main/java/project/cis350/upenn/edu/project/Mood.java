package project.cis350.upenn.edu.project;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.*;

import android.content.*;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EmotionOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EmotionScores;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
/**
 * Created by AK47 on 2/23/17.
 * This class handles the API call to Watson to retrieve the emotion statistics for a diary
 * entry.
 */

public class Mood extends AsyncTask<String, Void, String> {

    private Context context;

    public Mood(Context c) {
        context = c;
    }

    @Override
    protected String doInBackground(String[] params) {

        //send to API
        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
        "aaf1b1cc-405e-4729-a8db-bdebeec00278", "oLFD0KD6dSic");

        EmotionOptions emotion = new EmotionOptions.Builder().build();
        Features features = new Features.Builder().emotion(emotion).build();
        AnalyzeOptions parameters =
                new AnalyzeOptions.Builder().text(params[0]).features(features).returnAnalyzedText(true).build();

        AnalysisResults results = service.analyze(parameters).execute();

        EmotionScores sentiment = results.getEmotion().getDocument().getEmotion();
        Double anger = sentiment.getAnger();
        Double disgust = sentiment.getDisgust();
        Double fear = sentiment.getFear();
        Double joy = sentiment.getJoy();
        Double sadness = sentiment.getSadness();

        Calendar date = Calendar.getInstance();
        String d = date.get(Calendar.MONTH) + ", " + date.get(Calendar.DAY_OF_MONTH) + ", " + date.get(Calendar.YEAR);

        SentimentDatabaseOpenHelper dbh = new SentimentDatabaseOpenHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SentimentDatabaseContract.SentimentDB.COL_DATE, d + "");
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