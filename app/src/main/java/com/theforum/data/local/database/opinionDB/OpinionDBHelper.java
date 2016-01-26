package com.theforum.data.local.database.opinionDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theforum.data.local.database.topicDB.TopicDBConstants;
import com.theforum.data.server.opinion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class OpinionDBHelper {

    private OpinionDB opinionDB;
    private static OpinionDBHelper opinionDBHelper;
    private static Context context;
    private SQLiteDatabase opinionDatabase;

    public static OpinionDBHelper getOpinionDBHelper(Context context){
        if(opinionDBHelper == null) opinionDBHelper = new OpinionDBHelper(context);
        return opinionDBHelper;
    }

    private OpinionDBHelper(Context context){
        opinionDB = new OpinionDB(context);
        context = context;
        opinionDatabase = opinionDB.getWritableDatabase();
    }

    public void addOpinion(opinion opinion){
        SQLiteDatabase db = opinionDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor c=db.rawQuery("SELECT * FROM user WHERE"+ OpinionDBConstants.KEY_OPINION_ID +"="+opinion.getOpinionId(), null);
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
        if(c.moveToFirst())
        {
            Log.e("Error", "Record exist");
            db.update(OpinionDBConstants.TABLE_OPINION_NAME, values, OpinionDBConstants.KEY_ID+" = ?" + c.getInt(0), null);
        }
        else
        {
            db.insert(OpinionDBConstants.TABLE_OPINION_NAME, null, values);            // Inserting record
        }
        db.close();
    }

     public void addOpinions(ArrayList<opinion> opinions){
        for(int j = 0;j<opinions.size();j++){
            addOpinion(opinions.get(j));
        }
    }

    public void addOpinions(List<opinion> opinions){
        for(int j = 0;j<opinions.size();j++){
            addOpinion(opinions.get(j));
        }
    }

    public opinion getOpinion(String opinionId){
        opinion opinionToReturn = new opinion();
        Cursor cursor = opinionDatabase.rawQuery("SELECT  * FROM " + OpinionDBConstants.TABLE_OPINION_NAME + " WHERE "+
                OpinionDBConstants.KEY_OPINION_ID + " = "+opinionId, null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                opinionToReturn.setDownVotes(cursor.getInt(3));
                opinionToReturn.setServerId(cursor.getString(1));
                opinionToReturn.setmNotifCount(cursor.getInt(8));
                opinionToReturn.setOpinionId(cursor.getString(5));
                opinionToReturn.setTopicId(cursor.getString(7));
                opinionToReturn.setTopicName(cursor.getString(11));
                opinionToReturn.setUserId(cursor.getString(6));
                opinionToReturn.setmNotifNewDownvotes(cursor.getInt(10));
                opinionToReturn.setmNotifNewUpvotes(cursor.getInt(9));
                opinionToReturn.setOpinionName(cursor.getString(4));
                opinionToReturn.setUpVotes(cursor.getInt(2));
            }
            return opinionToReturn;
        }
        return null;
    }



    public void deleteOpinions(){
        SQLiteDatabase db = opinionDB.getWritableDatabase();
        String sql = "DELETE FROM "+OpinionDBConstants.TABLE_OPINION_NAME +" WHERE "+ OpinionDBConstants.KEY_TIME +" <= date('now','-2 day')";
        //  SELECT * FROM test WHERE age <= datetime('now', '-5 minutes')
        db.execSQL(sql);
    }


}
