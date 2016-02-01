package com.theforum.data.local.database.notificationDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theforum.TheForumApplication;
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
        values.put(NotificationDBConstants.KEY_IS_READ,0);
        values.put(NotificationDBConstants.KEY_MAIN_TEXT, NotificationDataModel.topicText);
        values.put(NotificationDBConstants.KEY_HEADER, String.valueOf(NotificationDataModel.notificationCount));

        if(NotificationDataModel.description!=null) {
            values.put(NotificationDBConstants.KEY_DESCRIPTION, NotificationDataModel.description);
        }

        values.put(NotificationDBConstants.KEY_TIME_HOLDER, NotificationDataModel.timeHolder);
        values.put(NotificationDBConstants.KEY_TOPIC_ID, NotificationDataModel.topicId);

        notificationDatabase.insert(NotificationDBConstants.TABLE_NAME, null, values);
    }

    public boolean checkIfNotificationExist(){
        Cursor cursor = notificationDatabase.rawQuery("SELECT DISTINCT" + NotificationDBConstants.KEY_ID +"FROM"
                + NotificationDBConstants.TABLE_NAME,null);

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

    public int getNewNotificationCount(){

        int count = 0;
        Cursor cursor = notificationDatabase.rawQuery("SELECT DISTINCT" + NotificationDBConstants.KEY_IS_READ +"FROM"
                + NotificationDBConstants.TABLE_NAME,null);

        if(cursor!=null){
            if (cursor.moveToLast()) {
                do {
                    if(cursor.getInt(2)==0) {
                        count++;
                    }else break;
                } while (cursor.moveToPrevious());
            }
            cursor.close();
        }

        return count;
    }

    public void deleteAllNotifications(){
        notificationDatabase.execSQL("delete from " + NotificationDBConstants.TABLE_NAME);
    }

    public ArrayList<NotificationDataModel> getAllNotifications(){

        ArrayList<NotificationDataModel> notifications = new ArrayList<>();
        Cursor cursor = notificationDatabase.rawQuery("SELECT  * FROM " + NotificationDBConstants.TABLE_NAME, null);

        if(cursor!=null){
            NotificationDataModel dataModel;

            if (cursor.moveToLast()) {
                do {
                    dataModel = new NotificationDataModel();
                    dataModel.notificationType = cursor.getInt(1);
                    if(cursor.getInt(2)==1) dataModel.isRead = true;
                    dataModel.topicText = cursor.getString(3);
                    dataModel.notificationCount = Integer.parseInt(cursor.getString(4));
                    dataModel.description = cursor.getString(5);
                    dataModel.timeHolder = cursor.getString(6);
                    dataModel.topicId = cursor.getString(7);

                    notifications.add(dataModel);
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
