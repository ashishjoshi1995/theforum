package com.theforum.data.local.database.topicDB;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.theforum.TheForumApplication;

/**
 * @author Ashish
 * @since 1/15/2016
 */
public class RenewTopicDBHelper {
    private TopicDB topicDB;
    private static RenewTopicDBHelper renewTopicDBHelper;
    private SQLiteDatabase sqLiteDatabase;
    private TopicDBHelper topicDBHelper;

    private RenewTopicDBHelper(){
        this.topicDBHelper = TopicDBHelper.getHelper();
        this.topicDB = TopicDBHelper.getHelper().getTopicDB();
        this.sqLiteDatabase = topicDB.getWritableDatabase();
    }

    public RenewTopicDBHelper getRenewTopicDBHelper(){
        if(renewTopicDBHelper == null) renewTopicDBHelper = new RenewTopicDBHelper();
        return renewTopicDBHelper;
    }

    public void addRenewRequestTopic(String topic_id){
        ContentValues values = new ContentValues();
        values.put(TopicDBConstants.KEY_TOPIC_ID,topic_id);
        sqLiteDatabase.insert(TopicDBConstants.TABLE_TWO_NAME,null,values);
    }

    public void deleteRenewRequestTopic(String topic_id){
        SQLiteDatabase db = topicDB.getWritableDatabase();
        String sql = "DELETE FROM "+TopicDBConstants.TABLE_TWO_NAME +" WHERE "+ TopicDBConstants.KEY_TOPIC_ID +" = " +topic_id ;
        db.execSQL(sql);
    }
}
