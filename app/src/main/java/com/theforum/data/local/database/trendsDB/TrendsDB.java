package com.theforum.data.local.database.trendsDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Ashish
 * @since 1/9/2016
 */
public class TrendsDB extends SQLiteOpenHelper {


    public TrendsDB(Context context){
        super(context, TrendsDBConstants.DATABASE_NAME,null, TrendsDBConstants.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " + TrendsDBConstants.TABLE_NAME + "("
                + TrendsDBConstants.KEY_LOCAL_ID + " INTEGER PRIMARY KEY,"
                + TrendsDBConstants.KEY_SERVER_ID + " TEXT,"
                + TrendsDBConstants.KEY_TOPIC_ID + " TEXT,"
                + TrendsDBConstants.KEY_TOPIC + " TEXT,"
                + TrendsDBConstants.KEY_TREND_ID + " TEXT,"
                + TrendsDBConstants.KEY_OPINION+" TEXT,"
                + TrendsDBConstants.KEY_UPVOTES+" INTEGER,"
                + TrendsDBConstants.KEY_DOWNVOTES+" INTEGER,"
                + TrendsDBConstants.KEY_HOURS_LEFT+" INTEGER)";

        db.execSQL(CREATE_TOPIC_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TrendsDBConstants.TABLE_NAME);
        onCreate(db);

    }


}