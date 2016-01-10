package com.theforum.data.local.topicDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theforum.Constants;
import com.theforum.data.dataModels.topic;

import java.util.ArrayList;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TopicDBHelper {

    private TopicDB topicDB;
    private static TopicDBHelper topicDBHelper;
    private static Context context;

    public static TopicDBHelper getTopicDBHelper(Context context){
        if(topicDBHelper == null) topicDBHelper = new TopicDBHelper(context);
        return topicDBHelper;
    }

    private TopicDBHelper(Context context){
        topicDB = new TopicDB(context);
    }

    public void addTopic(topic topic , int j){
        SQLiteDatabase db = topicDB.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TopicDBConstants.KEY_SERVER_ID,topic.getServerId());
        values.put(TopicDBConstants.KEY_TOPIC_ID,topic.getTopicId());
        values.put(TopicDBConstants.KEY_TOPIC,topic.getmTopic());
        values.put(TopicDBConstants.KEY_DESCRIPTION,topic.getTopicDescription());
        values.put(TopicDBConstants.KEY_RENEWAL_REQUEST, topic.getRenewalRequests());
        values.put(TopicDBConstants.KEY_TOTAL_OPINIONS, topic.getTotalOpinions());
        values.put(TopicDBConstants.KEY_TIME, "datetime(now)");

        // Inserting Row
        switch (j) {
            case Constants.ADD_TOPICS_ALL:
                db.insert(TopicDBConstants.TABLE_NAME, null, values);
                break;
            case Constants.ADD_MY_TOPIC:
                db.insert(TopicDBConstants.MY_DATA_TABLE, null, values);
                break;
        }    db.close(); // Closing database connection

    }

    public void addTopicFromServer(topic topic){
        SQLiteDatabase db = topicDB.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor c=db.rawQuery("SELECT * FROM user WHERE" + TopicDBConstants.KEY_TOPIC_ID + "=" + topic.getTopicId(), null);
        if(c.moveToFirst()) {
            Log.e("Error", "Record exist");
        }
        else {
            addTopic(topic,Constants.ADD_MY_TOPIC);
        }
        c.close();
        db.close();
    }


    public void addTopicsFromServer(ArrayList<topic> topics){
        for (int k = 0; k<topics.size();k++){
            addTopicFromServer(topics.get(k));
        }
    }

    public void deleteTopic(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM "+TopicDBConstants.TABLE_NAME +" WHERE "+ TopicDBConstants.KEY_TIME +" <= date('now','-2 day')";
        String sql2 = "DELETE FROM "+TopicDBConstants.MY_DATA_TABLE +" WHERE "+ TopicDBConstants.KEY_TIME +" <= date('now','-2 day')";
        //  SELECT * FROM test WHERE age <= datetime('now', '-5 minutes')
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    public ArrayList<String> getMyTopicId(){
        ArrayList<String> s = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String readTopicId = "SELECT DISTINCT" + TopicDBConstants.KEY_TOPIC_ID +"FROM" + TopicDBConstants.MY_DATA_TABLE;
        Cursor c = db.rawQuery(readTopicId,null);
        if(c!=null){
            c.moveToFirst();
        }
        for(int i =0; i<c.getCount();i++){
            s.add(c.getString(0));
        }

        return s;
    }
}
