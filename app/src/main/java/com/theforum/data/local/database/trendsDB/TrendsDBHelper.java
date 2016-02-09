package com.theforum.data.local.database.trendsDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theforum.TheForumApplication;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.utils.enums.VoteStatus;

import java.util.ArrayList;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TrendsDBHelper {

    private TrendsDB trendsDB;
    private static TrendsDBHelper trendsDBHelper;
    private SQLiteDatabase trendsDatabase;


    public static TrendsDBHelper getHelper(){
        if(trendsDBHelper == null) trendsDBHelper = new TrendsDBHelper();
        return trendsDBHelper;
    }
    private TrendsDBHelper(){
        trendsDB = new TrendsDB(TheForumApplication.getAppContext());
        trendsDatabase = trendsDB.getWritableDatabase();
    }


    public void addTrend(TrendsDataModel opinion){
        ContentValues values = new ContentValues();
        values.put(TrendsDBConstants.KEY_SERVER_ID,opinion.getuId());
        values.put(TrendsDBConstants.KEY_TOPIC_ID,opinion.getTopicId());
        values.put(TrendsDBConstants.KEY_TOPIC,opinion.getTopicName());
        values.put(TrendsDBConstants.KEY_TREND_ID,opinion.getTrendId());
        values.put(TrendsDBConstants.KEY_OPINION,opinion.getOpinionText());
        values.put(TrendsDBConstants.KEY_UPVOTES, opinion.getUpVoteCount());
        values.put(TrendsDBConstants.KEY_DOWNVOTES, opinion.getDownVoteCount());
        values.put(TrendsDBConstants.KEY_HOURS_LEFT, opinion.getHoursLeft());
        if(opinion.getVoteStatus()== VoteStatus.DOWNVOTED)
            values.put(TrendsDBConstants.KEY_VOTE_STATUS,0);
        else if(opinion.getVoteStatus()==VoteStatus.NONE)
            values.put(TrendsDBConstants.KEY_VOTE_STATUS,1);
        else if(opinion.getVoteStatus() == VoteStatus.UPVOTED)
            values.put(TrendsDBConstants.KEY_VOTE_STATUS,2);

        trendsDatabase.insert(TrendsDBConstants.TABLE_NAME, null, values);
    }

    public void updateUPDVStatus(TrendsDataModel trend){
        ContentValues values = new ContentValues();
        if(trend.getVoteStatus()==VoteStatus.UPVOTED)
            values.put(TrendsDBConstants.KEY_VOTE_STATUS,2);
        else if (trend.getVoteStatus()==VoteStatus.NONE)
            values.put(TrendsDBConstants.KEY_VOTE_STATUS,1);
        else if (trend.getVoteStatus()==VoteStatus.DOWNVOTED)
            values.put(TrendsDBConstants.KEY_VOTE_STATUS,0);

        trendsDatabase.update(TrendsDBConstants.TABLE_NAME, values, TrendsDBConstants.KEY_OPINION
                       +" = ?", new String[]{trend.getOpinionText()});
    }

     public void addTrends(ArrayList<TrendsDataModel> trendsList){
        for(int j = 0;j<trendsList.size();j++){
            addTrend(trendsList.get(j));
        }
    }

    public ArrayList<TrendsDataModel> getAllTrends(){
        ArrayList<TrendsDataModel> trends = new ArrayList<>();

        Cursor cursor = trendsDatabase.rawQuery("SELECT  * FROM " + TrendsDBConstants.TABLE_NAME, null);

        if(cursor!=null){
            if (cursor.moveToFirst()) {
                do {
                    TrendsDataModel obj = new TrendsDataModel();

                    obj.setuId(cursor.getString(1));
                    obj.setTopicId(cursor.getString(2));
                    obj.setTopicName(cursor.getString(3));
                    obj.setTrendId(cursor.getString(4));
                    obj.setOpinionText(cursor.getString(5));
                    obj.setUpVoteCount(cursor.getInt(6));
                    obj.setDownVoteCount(cursor.getInt(7));
                    obj.setHoursLeft(cursor.getInt(8));
                    if(cursor.getInt(9)==0)
                        obj.setVoteStatus(VoteStatus.DOWNVOTED);
                    else if (cursor.getInt(9)==1)
                        obj.setVoteStatus(VoteStatus.NONE);
                    else if (cursor.getInt(9)==2)
                        obj.setVoteStatus(VoteStatus.UPVOTED);
                    trends.add(obj);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return trends;
    }

    public void deleteAllTrends(){
        if(!trendsDatabase.isOpen()) trendsDatabase = trendsDB.getWritableDatabase();
        trendsDatabase.execSQL("DELETE from " + TrendsDBConstants.TABLE_NAME);
    }

    public void closeDatabase(){
        trendsDatabase.close();
        trendsDB.close();
    }
}
