package com.theforum.data.local.database.topicDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author  Ashish on 1/2/2016.
 */
public class TopicDB extends SQLiteOpenHelper {

    public TopicDB(Context context){
        super(context,TopicDBConstants.DATABASE_NAME,null,TopicDBConstants.DATABASE_VERSION);
    }

    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
            + TopicDBConstants.TABLE_NAME + " ADD COLUMN " + TopicDBConstants.KEY_LOCAL_TOPIC + " INTEGER;";
    private static final String DATABASE_ALTER_TEAM_2 = "ALTER TABLE "
            + TopicDBConstants.TABLE_NAME + " ADD COLUMN "+TopicDBConstants.KEY_UID + " TEXT;";
    private static final String DATABASE_ALTER_TEAM_3 = "ALTER TABLE "
            + TopicDBConstants.TABLE_NAME + " ADD COLUMN "+TopicDBConstants.KEY_LATITUDE + " REAL;";
    private static final String DATABASE_ALTER_TEAM_4 = "ALTER TABLE "
            + TopicDBConstants.TABLE_NAME + " ADD COLUMN " + TopicDBConstants.KEY_LONGITUDE + " REAL;";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " + TopicDBConstants.TABLE_NAME + "("
                + TopicDBConstants.KEY_ID + " INTEGER PRIMARY KEY,"
                + TopicDBConstants.KEY_SERVER_ID + " TEXT,"
                + TopicDBConstants.KEY_TOPIC_ID+" TEXT,"
                + TopicDBConstants.KEY_TOPIC+" TEXT,"
                + TopicDBConstants.KEY_DESCRIPTION+ " TEXT,"
                + TopicDBConstants.KEY_RENEWAL_REQUEST + " INTEGER,"
                + TopicDBConstants.KEY_RENEWED_COUNT + " INTEGER,"
                + TopicDBConstants.KEY_HOURS_LEFT + " INTEGER,"
                + TopicDBConstants.KEY_MY_TOPIC+" INTEGER,"
                + TopicDBConstants.KEY_IS_RENEWED+" INTEGER,"
                + TopicDBConstants.KEY_LATITUDE+" REAL,"
                + TopicDBConstants.KEY_LONGITUDE+" REAL,"
                + TopicDBConstants.KEY_LOCAL_TOPIC+" INTEGER,"
                + TopicDBConstants.KEY_UID+" TEXT)";
        db.execSQL(CREATE_TOPIC_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<2){
            db.execSQL(DATABASE_ALTER_TEAM_3);
            db.execSQL(DATABASE_ALTER_TEAM_4);
            db.execSQL(DATABASE_ALTER_TEAM_1);
            db.execSQL(DATABASE_ALTER_TEAM_2);
        }
    }
}

