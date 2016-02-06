package com.theforum.data.local.database.notificationDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author Ashish
 * @since 1/15/2016
 */
public class NotificationDB extends SQLiteOpenHelper {

    public NotificationDB(Context context){
        super(context,NotificationDBConstants.DATABASE_NAME,null,NotificationDBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " + NotificationDBConstants.TABLE_NAME + "("
                + NotificationDBConstants.KEY_ID + " INTEGER PRIMARY KEY,"
                + NotificationDBConstants.KEY_NOTIFICATION_TYPE + " INTEGER,"
                + NotificationDBConstants.KEY_IS_READ + " INTEGER,"
                + NotificationDBConstants.KEY_MAIN_TEXT+ " TEXT,"
                + NotificationDBConstants.KEY_HEADER + " TEXT,"
                + NotificationDBConstants.KEY_DESCRIPTION+" TEXT,"
                + NotificationDBConstants.KEY_TIME_HOLDER+" TEXT,"
                + NotificationDBConstants.KEY_TOPIC_ID + " TEXT)";

        db.execSQL(CREATE_TOPIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<2){
            db.execSQL("DROP TABLE IF EXISTS" + NotificationDBConstants.TABLE_NAME);
            onCreate(db);
            Log.e("onUpgrade","onUpgrade");
        }
    }
}
