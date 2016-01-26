package com.theforum.data.local.database.notificationDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theforum.TheForumApplication;
import com.theforum.constants.NotificationType;
import com.theforum.data.local.models.NotificationDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashish
 * @since 1/15/2016
 */
public class NotificationDBHelper {


    private static NotificationDBHelper notificationDBHelper;
    private SQLiteDatabase notificationDatabase;

    public static NotificationDBHelper getHelper(){
        if(notificationDBHelper == null) notificationDBHelper = new NotificationDBHelper();
        return notificationDBHelper;
    }

    private NotificationDBHelper(){
        NotificationDB notificationDB = new NotificationDB(TheForumApplication.getAppContext());
        notificationDatabase = notificationDB.getWritableDatabase();
    }

    public void addNotification(com.theforum.data.server.NotificationDataModel notificationDataModel){
        ContentValues values = new ContentValues();
        int k = notificationDataModel.notificationType;
        switch (k){
            case NotificationType.NOTIFICATION_TYPE_RENEWED:
                values.put(NotificationDBConstants.KEY_HEADER,"Your Topic " + notificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.renewedCount + " Renewal");
                values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,k);
                values.put(NotificationDBConstants.KEY_TOPIC_ID,notificationDataModel.topicId);
                break;
            case NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST:
                values.put(NotificationDBConstants.KEY_HEADER, "Your Topic " + notificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.renewalRequest+ " Renewal Requests");
                values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,k);
                values.put(NotificationDBConstants.KEY_TOPIC_ID,notificationDataModel.topicId);
                break;
            case NotificationType.NOTIFICATION_TYPE_OPINIONS:
                values.put(NotificationDBConstants.KEY_HEADER, "Your Topic " + notificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.opinions + " opinions");
                values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,k);
                values.put(NotificationDBConstants.KEY_TOPIC_ID,notificationDataModel.topicId);
                break;
            case NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES:
                values.put(NotificationDBConstants.KEY_HEADER,"Your Opinion on " + notificationDataModel.topicText + " received");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.newCount + " more Upvotes");
                values.put(NotificationDBConstants.KEY_DESCRIPTION, notificationDataModel.opinionText);
                values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,k);
                values.put(NotificationDBConstants.KEY_TOPIC_ID,notificationDataModel.topicId);
                break;

        }
        values.put(NotificationDBConstants.KEY_TIME_HOLDER, notificationDataModel.hoursLeft + "hrs left to decay | 01:30 PM Today");
        notificationDatabase.insert(NotificationDBConstants.TABLE_NAME, null, values);

    }

    public boolean checkIfNotificationExist(){
        Cursor cursor = notificationDatabase.rawQuery("SELECT  * FROM " + NotificationDBConstants.TABLE_NAME, null);

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        } else return false;

    }

    public void addNotifications(List<com.theforum.data.server.NotificationDataModel> notificationDataModels){

        for(int j=0; j<notificationDataModels.size();j++){
            addNotification(notificationDataModels.get(j));
        }
    }

    public void deleteNotification(){
        //delete notification from table when the entries exceed a certain fixed number
    }

    public void deleteAllNotif(){
        Cursor cursor = notificationDatabase.rawQuery("SELECT  * FROM " + NotificationDBConstants.TABLE_NAME, null);
        if(cursor.getCount()>35){

        }
        cursor.close();
        notificationDatabase.execSQL("delete from " + NotificationDBConstants.TABLE_NAME);
    }

    public ArrayList<NotificationDataModel> getAllNotifications(){
        ArrayList<NotificationDataModel> notifications = new ArrayList<>();
        Cursor cursor = notificationDatabase.rawQuery("SELECT  * FROM " + NotificationDBConstants.TABLE_NAME, null);

        if(cursor!=null){
            Log.e("getAllNotiications",""+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    NotificationDataModel obj = new NotificationDataModel();
                    obj.setNotificationType(cursor.getInt(1));
                    if(cursor.getInt(2)==1) obj.setIsRead(true);
                    obj.setMainText(cursor.getString(3));
                    obj.setHeader(cursor.getString(4));
                    obj.setDescription(cursor.getString(5));
                    obj.setTimeHolder(cursor.getString(6));
                    obj.setTopicId(cursor.getString(7));
                    notifications.add(obj);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return notifications;
    }
}
