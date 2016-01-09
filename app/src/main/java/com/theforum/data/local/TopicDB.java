package com.theforum.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.theforum.Constants;
import com.theforum.data.dataModels.topic;

import java.util.ArrayList;

/**
 * Created by Ashish on 1/2/2016.
 */
public class TopicDB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TOPIC_DB";

    private static final String TABLE_NAME = "TOPICS_TABLE_FEED";
    private static final String MY_DATA_TABLE = "MY_ADDED_TOPICS";


    //statistics table coloumn
    private static final String KEY_ID = "id";
    private static final String KEY_SERVER_ID="server_id";
    private static final String KEY_OPINION_IDS="opinion_ids";
    private static final String KEY_TOPIC_ID="topic_id";
    private static final String KEY_RENEWAL_REQUEST="renewal_request";
    private static final String KEY_TOTAL_OPINIONS="total_opinions";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_TOPIC="topic";
    private static final String KEY_TIME = "time";


    public TopicDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " +TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SERVER_ID + " TEXT,"+KEY_OPINION_IDS+" TEXT,"+KEY_TOPIC_ID+" TEXT,"
                +KEY_TOPIC+" TEXT,"  + KEY_DESCRIPTION+ " TEXT,"  + KEY_RENEWAL_REQUEST + " INTEGER,"
                + KEY_TOTAL_OPINIONS + " INTEGER," + KEY_TIME + " INTEGER)";
        db.execSQL(CREATE_TOPIC_TABLE);

        String CREATE_MY_TOPIC_TABLE = "CREATE TABLE " +MY_DATA_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SERVER_ID + " TEXT,"+KEY_OPINION_IDS+" TEXT,"+KEY_TOPIC_ID+" TEXT,"
                +KEY_TOPIC+" TEXT,"  + KEY_DESCRIPTION+ " TEXT,"  + KEY_RENEWAL_REQUEST + " INTEGER,"
                + KEY_TOTAL_OPINIONS + " INTEGER," + KEY_TIME + " INTEGER)";
        db.execSQL(CREATE_MY_TOPIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + MY_DATA_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void addTopic(topic topic , int j){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_ID,topic.getmId());
        values.put(KEY_OPINION_IDS,topic.getmOpinionIds());
        values.put(KEY_TOPIC_ID,topic.getmTopicId());
        values.put(KEY_TOPIC,topic.getmTopic());
        values.put(KEY_DESCRIPTION,topic.getmDescription());
        values.put(KEY_RENEWAL_REQUEST, topic.getmRenewalRequests());
        values.put(KEY_TOTAL_OPINIONS, topic.getmTotalOpinions());
        values.put(KEY_TIME, "datetime(now)");

        // Inserting Row
        switch (j) {
            case Constants.ADD_TOPICS_ALL:
                db.insert(TABLE_NAME, null, values);
                break;
            case Constants.ADD_MY_TOPIC:
                db.insert(MY_DATA_TABLE, null, values);
                break;
        }    db.close(); // Closing database connection

    }

    public void addTopicFromServer(topic topic){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor c=db.rawQuery("SELECT * FROM user WHERE" + KEY_TOPIC_ID + "=" + topic.getmTopicId(), null);
        if(c.moveToFirst())
        {
            Log.e("Error", "Record exist");
        }
        else
        {
            addTopic(topic,Constants.ADD_MY_TOPIC);
        }
        db.close();
    }


    public void addTopicsFromServer(ArrayList<topic>topics){
        for (int k = 0; k<topics.size();k++){
            addTopicFromServer(topics.get(k));
        }
    }

    public void deleteTopic(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM "+TABLE_NAME +" WHERE "+ KEY_TIME +" <= date('now','-2 day')";
        String sql2 = "DELETE FROM "+MY_DATA_TABLE +" WHERE "+ KEY_TIME +" <= date('now','-2 day')";
        //  SELECT * FROM test WHERE age <= datetime('now', '-5 minutes')
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    public ArrayList<String> getMyTopicId(){
       ArrayList<String> s = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String readTopicId = "SELECT DISTINCT" + KEY_TOPIC_ID +"FROM" + MY_DATA_TABLE;
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
