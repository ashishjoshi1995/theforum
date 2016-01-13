package com.theforum.data.local.topicDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theforum.data.dataModels.topic;

import java.util.ArrayList;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TopicDBHelper {

    private TopicDB topicDB;
    private static TopicDBHelper topicDBHelper;
    private SQLiteDatabase topicDatabase;


    public static TopicDBHelper getTopicDBHelper(Context context){
        if(topicDBHelper == null) topicDBHelper = new TopicDBHelper(context);
        return topicDBHelper;
    }

    private TopicDBHelper(Context context){
        topicDB = new TopicDB(context);
        topicDatabase = topicDB.getWritableDatabase();
    }

    public void addTopic(topic topic){

        ContentValues values = new ContentValues();

        values.put(TopicDBConstants.KEY_SERVER_ID,topic.getServerId());
        values.put(TopicDBConstants.KEY_TOPIC_ID,topic.getTopicId());
        values.put(TopicDBConstants.KEY_TOPIC,topic.getmTopic());
        values.put(TopicDBConstants.KEY_DESCRIPTION,topic.getTopicDescription());
        values.put(TopicDBConstants.KEY_RENEWAL_REQUEST, topic.getRenewalRequests());
        values.put(TopicDBConstants.KEY_TOTAL_OPINIONS, topic.getTotalOpinions());
        values.put(TopicDBConstants.KEY_TIME, "datetime(now)");

        // Inserting Row
        topicDatabase.insert(TopicDBConstants.TABLE_NAME, null, values);

    }

    public void addTopicFromServer(topic topic){

        ContentValues values = new ContentValues();

        Cursor c= topicDatabase.rawQuery("SELECT FROM "+TopicDBConstants.TABLE_NAME+" WHERE " +
                TopicDBConstants.KEY_TOPIC_ID + " = " + topic.getTopicId(), null);
        if(c.moveToFirst()) {
            Log.e("Error", "Record exist");
        }
        else {
            addTopic(topic);
        }
        c.close();
    }


    public void addTopicsFromServer(ArrayList<topic> topics){
        for (int k = 0; k<topics.size();k++){
            addTopic(topics.get(k));
        }
        //closeDataBase();
    }

    public void deleteTopic(){
        SQLiteDatabase db = topicDB.getWritableDatabase();
        String sql = "DELETE FROM "+TopicDBConstants.TABLE_NAME +" WHERE "+ TopicDBConstants.KEY_TIME +" <= date('now','-2 day')";
        //  SELECT * FROM test WHERE age <= datetime('now', '-5 minutes')
        db.execSQL(sql);
    }

    public void deleteAll() {
        topicDatabase.execSQL("DELETE from " + TopicDBConstants.TABLE_NAME);
    }

    public ArrayList<String> getMyTopicId(){
        topicDatabase = topicDB.getWritableDatabase();
        ArrayList<String> s = new ArrayList<>();
        String readTopicId = "SELECT DISTINCT" + TopicDBConstants.KEY_TOPIC_ID +"FROM" + TopicDBConstants.TABLE_NAME;
        Cursor c = topicDatabase.rawQuery(readTopicId,null);
        if(c!=null){
            c.moveToFirst();
        }
        for(int i =0; i<c.getCount();i++){
            s.add(c.getString(0));
        }

        return s;
    }

    public void closeDataBase(){
        topicDatabase.close(); // Closing database connection
    }
}
