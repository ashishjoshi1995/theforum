package com.theforum.data.local.topicDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        super(context,TopicDBConstants.DATABASE_NAME,null,TopicDBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " +TopicDBConstants.TABLE_NAME + "("
                + TopicDBConstants.KEY_ID + " INTEGER PRIMARY KEY," + TopicDBConstants.KEY_SERVER_ID + " TEXT,"+TopicDBConstants.KEY_TOPIC_ID+" TEXT,"
                +TopicDBConstants.KEY_TOPIC+" TEXT,"  + TopicDBConstants.KEY_DESCRIPTION+ " TEXT,"  + TopicDBConstants.KEY_RENEWAL_REQUEST + " INTEGER,"
                + TopicDBConstants.KEY_TOTAL_OPINIONS + " INTEGER," + TopicDBConstants.KEY_TIME + " INTEGER,"
                +TopicDBConstants.KEY_IF_RENEWED+" INTEGER)";
        db.execSQL(CREATE_TOPIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TopicDBConstants.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

}
