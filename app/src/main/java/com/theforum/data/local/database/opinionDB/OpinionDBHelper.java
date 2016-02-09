package com.theforum.data.local.database.opinionDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theforum.TheForumApplication;
import com.theforum.data.local.models.OpinionDataModel;
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
    private SQLiteDatabase opinionDatabase;

    public static OpinionDBHelper getHelper(){
        if(opinionDBHelper == null) opinionDBHelper = new OpinionDBHelper();
        return opinionDBHelper;
    }

    private OpinionDBHelper(){
        opinionDB = new OpinionDB(TheForumApplication.getAppContext());
        opinionDatabase = opinionDB.getWritableDatabase();
    }

    public void addOpinion(opinion opinion){

        ContentValues values = new ContentValues();

        Cursor c = opinionDatabase.rawQuery("SELECT * FROM user WHERE"+ OpinionDBConstants.KEY_OPINION_ID
                +"="+opinion.getOpinionId(), null);

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

        if(c.moveToFirst()) {
            opinionDatabase.update(OpinionDBConstants.TABLE_OPINION_NAME, values, OpinionDBConstants.KEY_ID+" = ?" + c.getInt(0), null);
        }
        else {
            opinionDatabase.insert(OpinionDBConstants.TABLE_OPINION_NAME, null, values);            // Inserting record
        }
        c.close();
        opinionDatabase.close();
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

    public OpinionDataModel getOpinion(String opinionId){
        OpinionDataModel opinionToReturn = new OpinionDataModel();
        Cursor cursor = opinionDatabase.rawQuery("SELECT  * FROM " + OpinionDBConstants.TABLE_OPINION_NAME + " WHERE "+
                OpinionDBConstants.KEY_OPINION_ID + " = "+opinionId, null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                opinionToReturn.setDownVoteCount(cursor.getInt(3));
                opinionToReturn.setuId(cursor.getString(1));
                //opinionToReturn.setNotificationCount(cursor.getInt(8));
                opinionToReturn.setOpinionId(cursor.getString(5));
                opinionToReturn. setTopicId(cursor.getString(7));
                opinionToReturn.setTopicName(cursor.getString(11));
                //opinionToReturn.setUserId(cursor.getString(6));
               // opinionToReturn.setmNotifNewDownvotes(cursor.getInt(10));
               // opinionToReturn.setmNotifNewUpvotes(cursor.getInt(9));
                opinionToReturn.setOpinionText(cursor.getString(4));
                opinionToReturn.setUpVoteCount(cursor.getInt(2));
            }
            return opinionToReturn;
        }
        return null;
    }
    public OpinionDataModel getOpinion(String opinionText, String opinionId){
        OpinionDataModel opinionToReturn = new OpinionDataModel();
        Cursor cursor = opinionDatabase.rawQuery("SELECT  * FROM " + OpinionDBConstants.TABLE_OPINION_NAME + " WHERE "+
                OpinionDBConstants.KEY_OPINION + " = "+opinionText, null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                opinionToReturn.setDownVoteCount(cursor.getInt(3));
                opinionToReturn.setuId(cursor.getString(1));
                //opinionToReturn.setNotificationCount(cursor.getInt(8));
                opinionToReturn.setOpinionId(cursor.getString(5));
                opinionToReturn.setTopicId(cursor.getString(7));
                opinionToReturn.setTopicName(cursor.getString(11));
                //opinionToReturn.setUserId(cursor.getString(6));
                // opinionToReturn.setmNotifNewDownvotes(cursor.getInt(10));
                // opinionToReturn.setmNotifNewUpvotes(cursor.getInt(9));
                opinionToReturn.setOpinionText(cursor.getString(4));
                opinionToReturn.setUpVoteCount(cursor.getInt(2));
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
