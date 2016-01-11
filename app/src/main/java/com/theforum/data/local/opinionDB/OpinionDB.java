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
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "OPINION_DB";
    private static final String TABLE_OPINION_NAME = "TABLE_OPINION";

    private static final String KEY_LOCAL_ID = "local_id";
    private static final String KEY_ID="id";
    private static final String KEY_UPVOTES="upvotes";
    private static final String KEY_DOWNVOTES="downvotes";
    private static final String KEY_OPINION="opinion";
    private static final String KEY_OPINION_ID="opinion_id";
    private static final String KEY_FORUM_ID="uid";
    private static final String KEY_TOPIC_ID="topic_id";
    private static final String KEY_NOTIF_COUNT="notif_count";
    private static final String KEY_NOTIF_NEW_UPVOTES="notif_newupvotes";
    private static final String KEY_NOTIF_NEW_DOWNVOTES="notif_newdownvotes";
    private static final String KEY_TOPIC="topic";
    private static final String KEY_TIME = "time";

    public OpinionDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " +TABLE_OPINION_NAME + "("
                + KEY_LOCAL_ID + " INTEGER PRIMARY KEY," + KEY_ID + " TEXT,"+KEY_UPVOTES+" INTEGER,"+KEY_DOWNVOTES+" INTEGER,"
                +KEY_OPINION+" TEXT,"  + KEY_OPINION_ID+ " TEXT,"  + KEY_FORUM_ID + " TEXT,"
                + KEY_TOPIC_ID + " TEXT," +KEY_NOTIF_COUNT + " INTEGER," + KEY_NOTIF_NEW_UPVOTES + " INTEGER,"
                + KEY_NOTIF_NEW_DOWNVOTES+" INTEGER,"+ KEY_TOPIC + " TEXT,"+KEY_TIME+" INTEGER)";
        db.execSQL(CREATE_TOPIC_TABLE);
       // this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_OPINION_NAME);
        onCreate(db);
    //this.db = db;
    }

    public void addOpinion(opinion opinion){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor c=db.rawQuery("SELECT * FROM user WHERE"+ KEY_OPINION_ID +"="+opinion.getOpinionId(), null);
        if(c.moveToFirst())
        {
            Log.e("Error", "Record exist");
        }
        else
        {
            values.put(KEY_ID,opinion.getServerId());
            values.put(KEY_DOWNVOTES,opinion.getDownVotes());
            values.put(KEY_FORUM_ID,opinion.getUserId());
            values.put(KEY_NOTIF_COUNT,opinion.getmNotifCount());
            values.put(KEY_NOTIF_NEW_DOWNVOTES,opinion.getmNotifNewDownvotes());
            values.put(KEY_NOTIF_NEW_UPVOTES,opinion.getmNotifNewUpvotes());
            values.put(KEY_OPINION,opinion.getOpinionName());
            values.put(KEY_OPINION_ID,opinion.getOpinionId());
            values.put(KEY_UPVOTES, opinion.getUpVotes());
            values.put(KEY_TOPIC,opinion.getTopicName());
            values.put(KEY_TOPIC_ID,opinion.getTopicId());
            values.put(KEY_TIME,"datetime(now)");


            db.insert(TABLE_OPINION_NAME, null, values);            // Inserting record
        }
        db.close();

    }

    public void addOpinions(ArrayList<opinion> opinions){
            for(int j = 0;j<opinions.size();j++){
                addOpinion(opinions.get(j));
            }
    }

    public void deleteOpinions(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM "+TABLE_OPINION_NAME +" WHERE "+ KEY_TIME +" <= date('now','-2 day')";
      //  SELECT * FROM test WHERE age <= datetime('now', '-5 minutes')
        db.execSQL(sql);
    }
}
