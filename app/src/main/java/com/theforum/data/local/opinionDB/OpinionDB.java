package com.theforum.data.local.opinionDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.theforum.data.dataModels.opinion;

import java.util.ArrayList;

/**
 * @author Ashish
 * @since 1/9/2016
 */
public class OpinionDB extends SQLiteOpenHelper {


    public OpinionDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public OpinionDB(Context context){
        super(context,OpinionDBConstants.DATABASE_NAME,null,OpinionDBConstants.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " +OpinionDBConstants.TABLE_OPINION_NAME + "("
                + OpinionDBConstants.KEY_LOCAL_ID + " INTEGER PRIMARY KEY," + OpinionDBConstants.KEY_ID + " TEXT,"
                +OpinionDBConstants.KEY_UPVOTES+" INTEGER,"+OpinionDBConstants.KEY_DOWNVOTES+" INTEGER,"
                +OpinionDBConstants.KEY_OPINION+" TEXT,"  + OpinionDBConstants.KEY_OPINION_ID+ " TEXT,"
                + OpinionDBConstants.KEY_FORUM_ID + " TEXT,"
                + OpinionDBConstants.KEY_TOPIC_ID + " TEXT," +OpinionDBConstants.KEY_NOTIF_COUNT + " INTEGER,"
                + OpinionDBConstants.KEY_NOTIF_NEW_UPVOTES + " INTEGER,"
                + OpinionDBConstants.KEY_NOTIF_NEW_DOWNVOTES+" INTEGER,"+ OpinionDBConstants.KEY_TOPIC + " TEXT,"
                + OpinionDBConstants.KEY_TIME+" INTEGER)";
        db.execSQL(CREATE_TOPIC_TABLE);
       // this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + OpinionDBConstants.TABLE_OPINION_NAME);
        onCreate(db);
    //this.db = db;
    }


}
