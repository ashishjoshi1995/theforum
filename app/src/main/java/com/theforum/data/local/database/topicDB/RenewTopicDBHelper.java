package com.theforum.data.local.database.topicDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Ashish
 * @since 1/15/2016
 */
public class RenewTopicDBHelper {

    private TopicDB topicDB;
    private static RenewTopicDBHelper renewTopicDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    public RenewTopicDBHelper getHelper(){
        if(renewTopicDBHelper == null) renewTopicDBHelper = new RenewTopicDBHelper();
        return renewTopicDBHelper;
    }

    private RenewTopicDBHelper(){
        this.topicDB = TopicDBHelper.getHelper().getTopicDB();
        this.sqLiteDatabase = topicDB.getWritableDatabase();
    }


    public void saveRenewalRequest(String topicId){
        ContentValues values = new ContentValues();
        values.put(TopicDBConstants.KEY_TOPIC_ID,topicId);
        sqLiteDatabase.insert(TopicDBConstants.TABLE_TWO_NAME,null,values);
    }

    public boolean isTopicRenewed(String topicId){
        Cursor c= sqLiteDatabase.rawQuery("SELECT * FROM "+ TopicDBConstants.TABLE_TWO_NAME+ " WHERE " +
                TopicDBConstants.KEY_TOPIC_ID + " =?", new String[] {topicId});

        boolean status = c.moveToFirst();
        c.close();

        return status;
    }

    public void deleteRenewRequestTopic(String topic_id){
        SQLiteDatabase db = topicDB.getWritableDatabase();
        String sql = "DELETE FROM "+TopicDBConstants.TABLE_TWO_NAME +" WHERE "+
                TopicDBConstants.KEY_TOPIC_ID +" = " +topic_id ;
        db.execSQL(sql);
    }

}
