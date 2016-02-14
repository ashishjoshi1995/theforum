package com.theforum.data.local.database.topicDB;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.theforum.TheForumApplication;
import com.theforum.constants.SortType;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.SettingsUtils;

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
     *
     */
    public void addTopic(TopicDataModel topic){
        Log.e("addTopic","addTopics");
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
        values.put(TopicDBConstants.KEY_LOCAL_TOPIC,(topic.isLocalTopic())?  "yes" :"no");

        // Inserting Row
        topicDatabase.insert(TopicDBConstants.TABLE_NAME, null, values);

    }


    /**
     * This methods add the topics into the db.
     *
     * @param topics list of topics that you want to save into db.
     *
     */
    public void addTopicsFromServer(ArrayList<TopicDataModel> topics, boolean ifLocal){
        for (int k = 0; k<topics.size();k++){
           // Log.e("addtopics","addtopics"+topics.get());
            topics.get(k).setIsLocalTopic(ifLocal);
            addTopic(topics.get(k));
        }
    }

    public int updateTopic(TopicDataModel topic){

        ContentValues values = new ContentValues();
        values.put(TopicDBConstants.KEY_TOPIC,topic.getTopicName());
        values.put(TopicDBConstants.KEY_DESCRIPTION,topic.getTopicDescription());
//        values.put(TopicDBConstants.KEY_RENEWAL_REQUEST, topic.getRenewalRequests());
//        values.put(TopicDBConstants.KEY_RENEWED_COUNT, topic.getRenewedCount());
//        values.put(TopicDBConstants.KEY_HOURS_LEFT, topic.getHoursLeft());
       return topicDatabase.update(TopicDBConstants.TABLE_NAME, values, TopicDBConstants.KEY_TOPIC_ID
                +" = ?", new String[]{topic.getTopicId()});
    }

    public void updateTopicRenewalStatus(TopicDataModel topic){
        ContentValues values = new ContentValues();

        values.put(TopicDBConstants.KEY_IS_RENEWED, (topic.isRenewed())? "yes" :"no");
        values.put(TopicDBConstants.KEY_RENEWAL_REQUEST, topic.getRenewalRequests());
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
                    obj.setServerId(cursor.getString(1));
                    obj.setTopicId(cursor.getString(2));
                    obj.setTopicName(cursor.getString(3));
                    obj.setTopicDescription(cursor.getString(4));
                    obj.setRenewalRequests(cursor.getInt(5));
                    obj.setRenewedCount(cursor.getInt(6));
                    obj.setHoursLeft(cursor.getInt(7));
                    if(cursor.getString(9).equals("yes")){
                        obj.setIsRenewed(true);
                    }

                    if(cursor.getString(8).equals("yes")) {
                        obj.setIsMyTopic(true);
                        myTopics.add(obj);
                    }
                    else {
                        obj.setIsMyTopic(false);
                        topics.add(obj);
                    }
                    if(cursor.getInt(10)==1){
                        obj.setIsLocalTopic(true);
                    }
                    else {
                        obj.setIsLocalTopic(false);
                    }

                } while (cursor.moveToNext());
            }
            topics.addAll(0,myTopics);
            cursor.close();
        }

        return topics;
    }

    public ArrayList<TopicDataModel> getAllLocalTopics(){
        //String v = SettingsUtils.TOPIC_FEED_SORT_STATUS;
        ArrayList<TopicDataModel> topics = new ArrayList<>();
        ArrayList<TopicDataModel> myTopics = new ArrayList<>();
        Cursor cursor = null;
  
        cursor = topicDatabase.rawQuery("SELECT  * FROM " + TopicDBConstants.TABLE_NAME + " WHERE "+
                TopicDBConstants.KEY_LOCAL_TOPIC + " =?" , new String[]{"yes"});

        if(cursor!=null){
            if (cursor.moveToFirst()) {
                do {
                    TopicDataModel obj = new TopicDataModel();
                    obj.setServerId(cursor.getString(1));
                    obj.setTopicId(cursor.getString(2));
                    obj.setTopicName(cursor.getString(3));
                    obj.setTopicDescription(cursor.getString(4));
                    obj.setRenewalRequests(cursor.getInt(5));
                    obj.setRenewedCount(cursor.getInt(6));
                    obj.setHoursLeft(cursor.getInt(7));

                    if(cursor.getString(9).equals("yes")){
                        obj.setIsRenewed(true);
                    }

                    if(cursor.getString(8).equals("yes")) {
                        obj.setIsMyTopic(true);
                        myTopics.add(obj);
                    }
                    else {
                        obj.setIsMyTopic(false);
                        topics.add(obj);
                    }
                    if(cursor.getString(10).equals("yes")){
                        obj.setIsLocalTopic(true);
                        Log.e("qwertyyes",""+obj.isLocalTopic());
                    }
                    else {
                        obj.setIsLocalTopic(false);
                        Log.e("qwertyno",""+obj.isLocalTopic());
                    }

                } while (cursor.moveToNext());
            }
            topics.addAll(0,myTopics);
            cursor.close();
        }

        return topics;
    }

    public ArrayList<TopicDataModel> getAllGlobalTopics(){
        ArrayList<TopicDataModel> topics = new ArrayList<>();
        ArrayList<TopicDataModel> myTopics = new ArrayList<>();
        Cursor cursor = topicDatabase.rawQuery("SELECT  * FROM " + TopicDBConstants.TABLE_NAME + " WHERE "+
                TopicDBConstants.KEY_LOCAL_TOPIC + " =?" , new String[]{"no"});

        if(cursor!=null){
            if (cursor.moveToFirst()) {
                do {
                    TopicDataModel obj = new TopicDataModel();
                    obj.setServerId(cursor.getString(1));
                    obj.setTopicId(cursor.getString(2));
                    obj.setTopicName(cursor.getString(3));
                    obj.setTopicDescription(cursor.getString(4));
                    obj.setRenewalRequests(cursor.getInt(5));
                    obj.setRenewedCount(cursor.getInt(6));
                    obj.setHoursLeft(cursor.getInt(7));

                    if(cursor.getString(9).equals("yes")){
                        obj.setIsRenewed(true);
                    }

                    if(cursor.getString(8).equals("yes")) {
                        obj.setIsMyTopic(true);
                        myTopics.add(obj);
                    }
                    else {
                        obj.setIsMyTopic(false);
                        topics.add(obj);
                    }
                    if(cursor.getString(10)=="yes"){
                        obj.setIsLocalTopic(true);
                    }
                    else {
                        obj.setIsLocalTopic(false);
                    }

                } while (cursor.moveToNext());
            }
            topics.addAll(0,myTopics);
            cursor.close();
        }

        return topics;
    }


    public void deleteAll() {
        if(topicDatabase.isOpen()){}else topicDatabase = topicDB.getWritableDatabase();
        topicDatabase.execSQL("DELETE from " + TopicDBConstants.TABLE_NAME);
    }

    public void deleteAllLocalTopics(){
        Log.e("deleteAllLocalTopics","deleteAllLocalTopics");
        if(topicDatabase.isOpen()){}else topicDatabase = topicDB.getWritableDatabase();
        topicDatabase.execSQL("DELETE from " + TopicDBConstants.TABLE_NAME+" WHERE "+ TopicDBConstants.KEY_LOCAL_TOPIC
        + " =?", new String[]{"yes"});


    }

    public void deleteAllGlobalTopics(){
        Log.e("deleteAllglobalTopics","deleteAllglovblaTopics");
        if(topicDatabase.isOpen()){}else topicDatabase = topicDB.getWritableDatabase();
        topicDatabase.execSQL("DELETE from " + TopicDBConstants.TABLE_NAME+" WHERE "+ TopicDBConstants.KEY_LOCAL_TOPIC
                + " =?", new String[]{"no"});
    }

    public TopicDataModel getTopicById(String id){
        Cursor cursor= topicDatabase.rawQuery("SELECT * FROM "+ TopicDBConstants.TABLE_NAME+ " WHERE " +
                TopicDBConstants.KEY_TOPIC_ID + " =?", new String[] {id});
        if(cursor!=null) {
            cursor.moveToFirst();
            TopicDataModel obj = new TopicDataModel();
            obj.setServerId(cursor.getString(1));
            obj.setTopicId(cursor.getString(2));
            obj.setTopicName(cursor.getString(3));
            obj.setTopicDescription(cursor.getString(4));
            obj.setRenewalRequests(cursor.getInt(5));
            obj.setRenewedCount(cursor.getInt(6));
            obj.setHoursLeft(cursor.getInt(7));
            if (cursor.getString(9).equals("yes")) {
                obj.setIsRenewed(true);
            }

            if (cursor.getString(8).equals("yes")) {
                obj.setIsMyTopic(true);
            }

            cursor.close();
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
        topicDatabase.close();
        topicDB.close();
    }

}
