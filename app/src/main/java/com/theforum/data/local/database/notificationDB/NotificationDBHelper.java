package com.theforum.data.local.database.notificationDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    private NotificationDB notificationDB;
    private static NotificationDBHelper notificationDBHelper;
    private SQLiteDatabase notificationDatabase;

    public static NotificationDBHelper getHelper(){
        if(notificationDBHelper == null) notificationDBHelper = new NotificationDBHelper();
        return notificationDBHelper;
    }

    private NotificationDBHelper(){
        notificationDB = new NotificationDB(TheForumApplication.getAppContext());
        notificationDatabase = notificationDB.getWritableDatabase();
    }

    public void openDatabase(){
        notificationDatabase = notificationDB.getWritableDatabase();
    }

    public void addNotification(NotificationDataModel NotificationDataModel){
        ContentValues values = new ContentValues();
        values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,NotificationDataModel.notificationType);

        switch (NotificationDataModel.notificationType){

            case NotificationType.NOTIFICATION_TYPE_RENEWED:
                values.put(NotificationDBConstants.KEY_HEADER,"Your Topic " + NotificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT, NotificationDataModel.renewedCount + " Renewal");
                values.put(NotificationDBConstants.KEY_TOPIC_ID, NotificationDataModel.topicId);
                break;

            case NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST:
                values.put(NotificationDBConstants.KEY_HEADER, "Your Topic " + NotificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT, NotificationDataModel.renewalRequest+ " Renewal Requests");
                values.put(NotificationDBConstants.KEY_TOPIC_ID, NotificationDataModel.topicId);
                break;

            case NotificationType.NOTIFICATION_TYPE_OPINIONS:
                values.put(NotificationDBConstants.KEY_HEADER, "Your Topic " + NotificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT, NotificationDataModel.opinions + " opinions");
                values.put(NotificationDBConstants.KEY_TOPIC_ID, NotificationDataModel.topicId);
                break;

            case NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES:
                values.put(NotificationDBConstants.KEY_HEADER,"Your Opinion on " + NotificationDataModel.topicText + " received");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT, NotificationDataModel.newCount + " more Upvotes");
                values.put(NotificationDBConstants.KEY_DESCRIPTION, NotificationDataModel.description);
                values.put(NotificationDBConstants.KEY_TOPIC_ID, NotificationDataModel.topicId);
                break;

        }
        values.put(NotificationDBConstants.KEY_TIME_HOLDER, NotificationDataModel.hoursLeft + "hrs left to decay | 01:30 PM Today");
        notificationDatabase.insert(NotificationDBConstants.TABLE_NAME, null, values);

    }

    public boolean checkIfNotificationExist(){
        Cursor cursor = notificationDatabase.rawQuery("SELECT  * FROM " + NotificationDBConstants.TABLE_NAME, null);

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        } else return false;

    }

    public void addNotifications(List<NotificationDataModel> NotificationDataModels){

        for(int j=0; j< NotificationDataModels.size();j++){
            addNotification(NotificationDataModels.get(j));
        }
    }

    public void deleteNotification(){
        //delete notification from table when the entries exceed a certain fixed number
    }

    public void deleteAllNotif(){
        notificationDatabase.execSQL("delete from " + NotificationDBConstants.TABLE_NAME);
    }

    public ArrayList<NotificationDataModel> getAllNotifications(){
        ArrayList<NotificationDataModel> notifications = new ArrayList<>();
        Cursor cursor = notificationDatabase.rawQuery("SELECT  * FROM " + NotificationDBConstants.TABLE_NAME, null);

        if(cursor!=null){

            if (cursor.moveToLast()) {
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
                } while (cursor.moveToPrevious());
            }
            cursor.close();
        }

        return notifications;
    }

    public void closeDataBase(){
        notificationDatabase.close();
    }
}
