package com.theforum.data.local.database.notificationDB;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.theforum.Constants;
import com.theforum.TheForumApplication;
import com.theforum.data.server.NotificationDataModel;

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
                break;
            case Constants.NOTIFICATION_TYPE_RENEWAL_REQUEST:
                values.put(NotificationDBConstants.KEY_VIEW_TYPE,1);
                values.put(NotificationDBConstants.KEY_HEADER, "Your Topic " + notificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.renewalRequest+ " Renewal Requests");
                break;
            case Constants.NOTIFICATION_TYPE_OPINIONS:
                values.put(NotificationDBConstants.KEY_VIEW_TYPE,1);
                values.put(NotificationDBConstants.KEY_HEADER, "Your Topic " + notificationDataModel.topicText + " recieved");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.renewedCount + " Renewal");
                break;
            case Constants.NOTIFICATION_TYPE_OPINION_UP_VOTES:
                values.put(NotificationDBConstants.KEY_VIEW_TYPE, 0);
                values.put(NotificationDBConstants.KEY_HEADER,"Your Opinion on " + notificationDataModel.topicText + " received");
                values.put(NotificationDBConstants.KEY_MAIN_TEXT,notificationDataModel.newCount + " more Upvotes");
                values.put(NotificationDBConstants.KEY_DESCRIPTION, notificationDataModel.opinionText);
                break;
        }
        values.put(NotificationDBConstants.KEY_TIME_HOLDER,notificationDataModel.hoursLeft + "hrs left to decay | 01:30 PM Today");
        sqLiteDatabase.insert(NotificationDBConstants.TABLE_NAME,null,values);
    }

    public void addNotifications(List<NotificationDataModel> notificationDataModels){
        for(int j=0; j<notificationDataModels.size();j++){
            addNotification(notificationDataModels.get(j));
        }
    }

    public void deleteNotification(){
        //delete notification from table when the entries exceed a certain fixed number

    }
}
