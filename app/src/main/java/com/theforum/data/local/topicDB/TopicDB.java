package com.theforum.data.local.topicDB;

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
 * @author  Ashish on 1/2/2016.
 */
public class TopicDB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version



    public TopicDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public TopicDB(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " +TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SERVER_ID + " TEXT,"+KEY_TOPIC_ID+" TEXT,"
                +KEY_TOPIC+" TEXT,"  + KEY_DESCRIPTION+ " TEXT,"  + KEY_RENEWAL_REQUEST + " INTEGER,"
                + KEY_TOTAL_OPINIONS + " INTEGER," + KEY_TIME + " INTEGER)";
        db.execSQL(CREATE_TOPIC_TABLE);

        String CREATE_MY_TOPIC_TABLE = "CREATE TABLE " +MY_DATA_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SERVER_ID + " TEXT,"+KEY_TOPIC_ID+" TEXT,"
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


}
