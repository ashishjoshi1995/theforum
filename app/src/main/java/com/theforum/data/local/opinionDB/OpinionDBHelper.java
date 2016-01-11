package com.theforum.data.local.opinionDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theforum.data.dataModels.opinion;
import com.theforum.data.local.topicDB.TopicDB;

import java.util.ArrayList;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class OpinionDBHelper {

    private OpinionDB opinionDB;
    private static OpinionDBHelper opinionDBHelper;
    private static Context context;

    public static OpinionDBHelper getTopicDBHelper(Context context){
        if(opinionDBHelper == null) opinionDBHelper = new OpinionDBHelper(context);
        return opinionDBHelper;
    }

    private OpinionDBHelper(Context context){
        opinionDB = new OpinionDB(context);
    }

    public void addOpinion(opinion opinion){
        SQLiteDatabase db = opinionDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor c=db.rawQuery("SELECT * FROM user WHERE"+ OpinionDBConstants.KEY_OPINION_ID +"="+opinion.getOpinionId(), null);
        if(c.moveToFirst())
        {
            Log.e("Error", "Record exist");
        }
        else
        {
            values.put(OpinionDBConstants.KEY_ID,opinion.getServerId());
            values.put(OpinionDBConstants.KEY_DOWNVOTES,opinion.getDownVotes());
            values.put(OpinionDBConstants.KEY_FORUM_ID,opinion.getUserId());
            values.put(OpinionDBConstants.KEY_NOTIF_COUNT,opinion.getmNotifCount());
            values.put(OpinionDBConstants.KEY_NOTIF_NEW_DOWNVOTES,opinion.getmNotifNewDownvotes());
            values.put(OpinionDBConstants.KEY_NOTIF_NEW_UPVOTES,opinion.getmNotifNewUpvotes());
            values.put(OpinionDBConstants.KEY_OPINION,opinion.getOpinionName());
            values.put(OpinionDBConstants.KEY_OPINION_ID,opinion.getOpinionId());
            values.put(OpinionDBConstants.KEY_UPVOTES, opinion.getUpVotes());
            values.put(OpinionDBConstants.KEY_TOPIC,opinion.getTopicName());
            values.put(OpinionDBConstants.KEY_TOPIC_ID,opinion.getTopicId());
            values.put(OpinionDBConstants.KEY_TIME,"datetime(now)");


            db.insert(OpinionDBConstants.TABLE_OPINION_NAME, null, values);            // Inserting record
        }
        db.close();

    }

    public void addOpinions(ArrayList<opinion> opinions){
        for(int j = 0;j<opinions.size();j++){
            addOpinion(opinions.get(j));
        }
    }

    public void deleteOpinions(){
        SQLiteDatabase db = opinionDB.getWritableDatabase();
        String sql = "DELETE FROM "+OpinionDBConstants.TABLE_OPINION_NAME +" WHERE "+ OpinionDBConstants.KEY_TIME +" <= date('now','-2 day')";
        //  SELECT * FROM test WHERE age <= datetime('now', '-5 minutes')
        db.execSQL(sql);
    }
}
