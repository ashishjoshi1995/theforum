package com.theforum.data.localDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.theforum.data.dataModels.topic;

/**
 * Created by Ashish on 1/2/2016.
 */
public class TopicDB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TOPIC_DB";

    private static final String TABLE_NAME = "TOPICS_TABLE";

    //statistics table coloumn
    private static final String KEY_ID = "id";
    private static final String KEY_SERVER_ID="server_id";
    private static final String KEY_OPINION_IDS="opinion_ids";
    private static final String KEY_TOPIC_ID="topic_id";
    private static final String KEY_RENEWAL_REQUEST="renewal_request";
    private static final String KEY_TOTAL_OPINIONS="total_opinions";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_TOPIC="topic";


    public TopicDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " +TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SERVER_ID + " TEXT,"+KEY_OPINION_IDS+" TEXT,"+KEY_TOPIC_ID+" TEXT,"
                +KEY_TOPIC+" TEXT,"  + KEY_DESCRIPTION+ " TEXT,"  + KEY_RENEWAL_REQUEST + " INTEGER,"
                + KEY_TOTAL_OPINIONS + " INTEGER)";
        db.execSQL(CREATE_TOPIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public void addTopic(topic topic){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_ID,topic.getmId());
        values.put(KEY_OPINION_IDS,topic.getmOpinionIds());
        values.put(KEY_TOPIC_ID,topic.getmTopicId());
        values.put(KEY_TOPIC,topic.getmTopic());
        values.put(KEY_DESCRIPTION,topic.getmDescription());
        values.put(KEY_RENEWAL_REQUEST,topic.getmRenewalRequests());
        values.put(KEY_TOTAL_OPINIONS,topic.getmTotalOpinions());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void deleteTopic(){

    }
}
