package com.theforum.data.local.database.trendsDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theforum.data.local.models.TrendsDataModel;

import java.util.ArrayList;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TrendsDBHelper {

    private TrendsDB trendsDB;
    private static TrendsDBHelper trendsDBHelper;


    public static TrendsDBHelper getHelper(Context context){
        if(trendsDBHelper == null) trendsDBHelper = new TrendsDBHelper(context);
        return trendsDBHelper;
    }

    private TrendsDBHelper(Context context){
        trendsDB = new TrendsDB(context);
    }


    public void addTrend(TrendsDataModel opinion){
        SQLiteDatabase db = trendsDB.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TrendsDBConstants.KEY_SERVER_ID,opinion.getServerId());
        values.put(TrendsDBConstants.KEY_TOPIC_ID,opinion.getTopicId());
        values.put(TrendsDBConstants.KEY_TOPIC,opinion.getTopicName());
        values.put(TrendsDBConstants.KEY_TREND_ID,opinion.getTrendId());
        values.put(TrendsDBConstants.KEY_OPINION,opinion.getOpinionText());
        values.put(TrendsDBConstants.KEY_UPVOTES, opinion.getUpVoteCount());
        values.put(TrendsDBConstants.KEY_DOWNVOTES,opinion.getDownVoteCount());
        values.put(TrendsDBConstants.KEY_HOURS_LEFT,"datetime(now)");

        db.insert(TrendsDBConstants.TABLE_NAME, null, values);            // Inserting record

        db.close();

    }

     public void addTrends(ArrayList<TrendsDataModel> trendsList){
        for(int j = 0;j<trendsList.size();j++){
            addTrend(trendsList.get(j));
        }
    }

    public ArrayList<TrendsDataModel> getAllTrends(){
        ArrayList<TrendsDataModel> trends = new ArrayList<>();

        Cursor cursor = trendsDB.getWritableDatabase().rawQuery("SELECT  * FROM " + TrendsDBConstants.TABLE_NAME, null);

        if(cursor!=null){
            if (cursor.moveToFirst()) {
                do {
                    TrendsDataModel obj = new TrendsDataModel();

                    obj.setServerId(cursor.getString(1));
                    obj.setTopicId(cursor.getString(2));
                    obj.setTopicName(cursor.getString(3));
                    obj.setTrendId(cursor.getString(4));
                    obj.setOpinionText(cursor.getString(5));
                    obj.setUpVoteCount(cursor.getInt(6));
                    obj.setDownVoteCount(cursor.getInt(7));
                    obj.setHoursLeft(cursor.getInt(8));

                    trends.add(obj);

                } while (cursor.moveToNext());
            }

        }
        trendsDB.close();

        return trends;
    }

    public void deleteAllTrends(){
        trendsDB.getWritableDatabase().execSQL("DELETE from " + TrendsDBConstants.TABLE_NAME);
        trendsDB.close();
    }

}
