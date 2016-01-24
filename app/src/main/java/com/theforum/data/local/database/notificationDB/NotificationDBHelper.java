package com.theforum.data.local.database.notificationDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theforum.Constants;
import com.theforum.TheForumApplication;
import com.theforum.data.local.models.NotificationInflatorModel;
import com.theforum.data.server.NotificationDataModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashish
 * @since 1/15/2016
 */
public class NotificationDBHelper {
    private NotificationDB notificationDB;
    private static NotificationDBHelper notificationDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static NotificationDBHelper getNotificationDBHelper(){
        if(notificationDBHelper == null)notificationDBHelper = new NotificationDBHelper();
        return notificationDBHelper;
    }

    private NotificationDBHelper(){
        notificationDB = new NotificationDB(TheForumApplication.getAppContext());
        sqLiteDatabase = notificationDB.getWritableDatabase();
    }

    public void addNotification(NotificationDataModel notificationDataModel){
        ContentValues values = new ContentValues();
        int k = notificationDataModel.notificationType;
        switch (k){
            case Constants.NOTIFICATION_TYPE_RENEWED:
                values.put(NotificationDBConstants.KEY_VIEW_TYPE, 1);
                values.put(NotificationDBConstants.KEY_HEADER,"Your Topic " + notificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.renewedCount + " Renewal");
                values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,k);
                values.put(NotificationDBConstants.KEY_TOPIC_ID,notificationDataModel.topicId);
                break;
            case Constants.NOTIFICATION_TYPE_RENEWAL_REQUEST:
                values.put(NotificationDBConstants.KEY_VIEW_TYPE,1);
                values.put(NotificationDBConstants.KEY_HEADER, "Your Topic " + notificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.renewalRequest+ " Renewal Requests");
                values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,k);
                values.put(NotificationDBConstants.KEY_TOPIC_ID,notificationDataModel.topicId);
                break;
            case Constants.NOTIFICATION_TYPE_OPINIONS:
                values.put(NotificationDBConstants.KEY_VIEW_TYPE,1);
                values.put(NotificationDBConstants.KEY_HEADER, "Your Topic " + notificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.opinions + " opinions");
                values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,k);
                values.put(NotificationDBConstants.KEY_TOPIC_ID,notificationDataModel.topicId);
                break;
            case Constants.NOTIFICATION_TYPE_OPINION_UP_VOTES:
                values.put(NotificationDBConstants.KEY_VIEW_TYPE, 0);
                values.put(NotificationDBConstants.KEY_HEADER,"Your Opinion on " + notificationDataModel.topicText + " received");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.newCount + " more Upvotes");
                values.put(NotificationDBConstants.KEY_DESCRIPTION, notificationDataModel.opinionText);
                values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,k);
                values.put(NotificationDBConstants.KEY_TOPIC_ID,notificationDataModel.topicId);
                break;

        }
        values.put(NotificationDBConstants.KEY_TIME_HOLDER,notificationDataModel.hoursLeft + "hrs left to decay | 01:30 PM Today");
        Log.e("notificationDBhelper",""+values.size());
        sqLiteDatabase.insert(NotificationDBConstants.TABLE_NAME, null, values);

    }

    public void addNotifications(List<NotificationDataModel> notificationDataModels){
        Log.e("addnotificationssss",""+notificationDataModels.size());
        for(int j=0; j<notificationDataModels.size();j++){
            addNotification(notificationDataModels.get(j));
        }
    }

    public void deleteNotification(){
        //delete notification from table when the entries exceed a certain fixed number
    }

    public void deleteAllNotif(){
        sqLiteDatabase.execSQL("delete from " + NotificationDBConstants.TABLE_NAME);
    }

    public ArrayList<NotificationInflatorModel> getAllNotifications(){
        ArrayList<NotificationInflatorModel> notifications = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT  * FROM " + NotificationDBConstants.TABLE_NAME, null);

        if(cursor!=null){
            Log.e("getAllNotiications",""+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    NotificationInflatorModel obj = new NotificationInflatorModel();
                    obj.setNotificationType(cursor.getInt(1));
                    obj.setViewType(cursor.getInt(2));
                    obj.setTimeHolder(cursor.getString(3));
                    obj.setMainText(cursor.getString(4));
                    obj.setHeader(cursor.getString(5));
                    obj.setDescription(cursor.getString(6));
                    obj.setTopicId(cursor.getString(7));
                    notifications.add(obj);
                } while (cursor.moveToNext());
            }
        }
        return notifications;
    }
}
