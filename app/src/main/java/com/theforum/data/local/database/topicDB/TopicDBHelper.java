package com.theforum.data.local.database.topicDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theforum.TheForumApplication;
import com.theforum.data.local.models.TopicDataModel;

import java.util.ArrayList;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TopicDBHelper {

    private TopicDB topicDB;
    private static TopicDBHelper topicDBHelper;
    private SQLiteDatabase topicDatabase;


    public static TopicDBHelper getHelper(){
        if(topicDBHelper == null) topicDBHelper = new TopicDBHelper();
        return topicDBHelper;
    }

    private TopicDBHelper(){
        topicDB = new TopicDB(TheForumApplication.getAppContext());
        topicDatabase = topicDB.getWritableDatabase();
    }

    /**
     *
     * @param topic data model to save into local database
     */
    public void addTopic(TopicDataModel topic){

        ContentValues values = new ContentValues();

        values.put(TopicDBConstants.KEY_SERVER_ID,topic.getServerId());
        values.put(TopicDBConstants.KEY_TOPIC_ID,topic.getTopicId());
        values.put(TopicDBConstants.KEY_TOPIC,topic.getTopicName());
        values.put(TopicDBConstants.KEY_DESCRIPTION,topic.getTopicDescription());
        values.put(TopicDBConstants.KEY_RENEWAL_REQUEST, topic.getRenewalRequests());
        values.put(TopicDBConstants.KEY_RENEWED_COUNT, topic.getRenewedCount());
        values.put(TopicDBConstants.KEY_HOURS_LEFT, topic.getHoursLeft());
        values.put(TopicDBConstants.KEY_MY_TOPIC, (topic.isMyTopic())? "yes" :"no");
        values.put(TopicDBConstants.KEY_IS_RENEWED, (topic.isRenewed())? "yes" :"no");

        // Inserting Row
        topicDatabase.insert(TopicDBConstants.TABLE_NAME, null, values);

    }

    public void addTopicFromServer(TopicDataModel topic){

        Cursor c= topicDatabase.rawQuery("SELECT * FROM "+ TopicDBConstants.TABLE_NAME+ " WHERE " +
                TopicDBConstants.KEY_TOPIC_ID + " =?", new String[] {topic.getTopicId()});

        if(c.moveToFirst()) {
            updateTopic(topic);
        }
        else {
            addTopic(topic);
        }
        c.close();
    }

    /**
     * This methods add the topics into the db.
     *
     * @param topics list of topics that you want to save into db.
     *
     */
    public void addTopicsFromServer(ArrayList<TopicDataModel> topics){
        for (int k = 0; k<topics.size();k++){
            addTopic(topics.get(k));
        }
    }

    public void updateTopic(TopicDataModel topic){
        ContentValues values = new ContentValues();

        values.put(TopicDBConstants.KEY_RENEWAL_REQUEST, topic.getRenewalRequests());
        values.put(TopicDBConstants.KEY_RENEWED_COUNT, topic.getRenewedCount());
        values.put(TopicDBConstants.KEY_HOURS_LEFT, topic.getHoursLeft());

        topicDatabase.update(TopicDBConstants.TABLE_NAME, values, TopicDBConstants.KEY_TOPIC_ID
                +" = ?", new String[]{topic.getTopicId()});
    }

    public void updateTopicRenewalStatus(TopicDataModel topic){
        ContentValues values = new ContentValues();

        values.put(TopicDBConstants.KEY_IS_RENEWED, (topic.isRenewed())? "yes" :"no");

        topicDatabase.update(TopicDBConstants.TABLE_NAME, values, TopicDBConstants.KEY_TOPIC_ID
                +" = ?", new String[]{topic.getTopicId()});
    }

    /**
     *
     * @return array list of topics stored in local database
     */

    public ArrayList<TopicDataModel> getAllTopics(){
        ArrayList<TopicDataModel> topics = new ArrayList<>();
        ArrayList<TopicDataModel> myTopics = new ArrayList<>();
        Cursor cursor = topicDatabase.rawQuery("SELECT  * FROM " + TopicDBConstants.TABLE_NAME, null);

        if(cursor!=null){
            if (cursor.moveToFirst()) {
                do {
                    TopicDataModel obj = new TopicDataModel();
                    boolean renewed=false;
                    obj.setServerId(cursor.getString(1));
                    obj.setTopicId(cursor.getString(2));
                    obj.setTopicName(cursor.getString(3));
                    obj.setTopicDescription(cursor.getString(4));
                    obj.setRenewalRequests(cursor.getInt(5));
                    obj.setRenewedCount(cursor.getInt(6));
                    obj.setHoursLeft(cursor.getInt(7));
                    if(cursor.getString(9).equals("yes")){
                        renewed=true;
                    }
                    obj.setIsRenewed(renewed);

                    if(cursor.getString(8).equals("yes")) {
                        obj.setIsMyTopic(true);
                        myTopics.add(obj);
                    }
                    else {
                        obj.setIsMyTopic(false);
                        topics.add(obj);
                    }

                } while (cursor.moveToNext());
            }
            topics.addAll(0,myTopics);
            cursor.close();
        }

        return topics;
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
            for(int i =0; i<c.getCount();i++){
                s.add(c.getString(0));
            }
            c.close();
        }

        return s;
    }
    public TopicDataModel getTopicById(String id){
        Cursor cursor= topicDatabase.rawQuery("SELECT * FROM "+ TopicDBConstants.TABLE_NAME+ " WHERE " +
                TopicDBConstants.KEY_TOPIC_ID + " =?", new String[] {id});
        if(cursor!=null) {
            cursor.moveToFirst();
            TopicDataModel obj = new TopicDataModel();
            boolean renewed = false;
            obj.setServerId(cursor.getString(1));
            obj.setTopicId(cursor.getString(2));
            obj.setTopicName(cursor.getString(3));
            obj.setTopicDescription(cursor.getString(4));
            obj.setRenewalRequests(cursor.getInt(5));
            obj.setRenewedCount(cursor.getInt(6));
            obj.setHoursLeft(cursor.getInt(7));
            if (cursor.getString(9).equals("yes")) {
                renewed = true;
            }
            obj.setIsRenewed(renewed);

            if (cursor.getString(8).equals("yes")) {
                obj.setIsMyTopic(true);
                // myTopics.add(obj);
            } else {
                obj.setIsMyTopic(false);
                //topics.add(obj);
            }
            return obj;
        }
        else return null;
    }
    public ArrayList<String> getMyTopicText(){
        topicDatabase = topicDB.getWritableDatabase();
        ArrayList<String> s = new ArrayList<>();
        String readTopicId = "SELECT DISTINCT " + TopicDBConstants.KEY_TOPIC +" FROM " + TopicDBConstants.TABLE_NAME;
        Cursor c = topicDatabase.rawQuery(readTopicId,null);

        if(c!=null){
            c.moveToFirst();

            for(int i =0; i<c.getCount();i++){
                s.add(c.getString(0));
                c.moveToNext();
            }

            c.close();
        }

        return s;
    }

    public void closeDataBase(){
        topicDatabase.close(); // Closing database connection
    }
}
