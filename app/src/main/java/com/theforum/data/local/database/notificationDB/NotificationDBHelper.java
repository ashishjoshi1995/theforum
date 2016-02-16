package com.theforum.data.local.database.notificationDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public void addNotification(NotificationDataModel notificationDataModel){
        ContentValues values = new ContentValues();

        values.put(NotificationDBConstants.KEY_NOTIFICATION_TYPE,notificationDataModel.notificationType);
        values.put(NotificationDBConstants.KEY_IS_READ,0);
        values.put(NotificationDBConstants.KEY_MAIN_TEXT, notificationDataModel.topicText);
        values.put(NotificationDBConstants.KEY_HEADER, String.valueOf(notificationDataModel.notificationCount));

        if(notificationDataModel.description!=null) {
            values.put(NotificationDBConstants.KEY_DESCRIPTION, notificationDataModel.description);
        }

        values.put(NotificationDBConstants.KEY_TIME_HOLDER, notificationDataModel.timeHolder);
        values.put(NotificationDBConstants.KEY_TOPIC_ID, notificationDataModel.topicId);

        notificationDatabase.insert(NotificationDBConstants.TABLE_NAME, null, values);
    }

    public boolean checkIfNotificationExist(){
        Cursor cursor = notificationDatabase.rawQuery("SELECT " + NotificationDBConstants.KEY_ID + " FROM "
                + NotificationDBConstants.TABLE_NAME, null);

        if(cursor.getCount()>0){
            cursor.close();
            return true;

        } else return false;
    }

    public void addNotifications(List<NotificationDataModel> NotificationDataModels){
        //here we will add code to keep the total count to less than eq to 30
        int c =NotificationDataModels.size();
        Log.e("1", "" + c);
        Cursor cc = notificationDatabase.rawQuery("select * from "+NotificationDBConstants.TABLE_NAME,null);
        int d=cc.getCount();
        Log.e("Number of Records", " :: " + d);


        if(c<30) {
            if(c+d<=30){
                //addNotifications(NotificationDataModels);
            }
            else{
                int temp = c+d - 30;
                Log.e("chali k nahi","?");

                String ALTER_TBL ="delete from " + NotificationDBConstants.TABLE_NAME +
                        " LIMIT 15;";
               String a = "delete from " + NotificationDBConstants.TABLE_NAME + " where "+ NotificationDBConstants.KEY_ID+" in " +
                        "(select "+ NotificationDBConstants.KEY_ID+" from " + NotificationDBConstants.TABLE_NAME + " order by "+
                       NotificationDBConstants.KEY_ID+" limit 10)";
                notificationDatabase.execSQL(a);
                Log.e("chal gai re","just chill");
            }
            }


        else{
            //empty the entire db
            deleteAllNotifications();
        }

        for(int j=0; j< NotificationDataModels.size();j++){
            addNotification(NotificationDataModels.get(j));
        }
    }

    public int getNewNotificationCount(){

        int count = 0;
        Cursor cursor = notificationDatabase.rawQuery("SELECT " + NotificationDBConstants.KEY_IS_READ +" FROM "
                + NotificationDBConstants.TABLE_NAME,null);

        if(cursor!=null){
            if (cursor.moveToLast()) {
                do {
                    if(cursor.getInt(0)==0) {
                        count++;
                    }else break;
                } while (cursor.moveToPrevious());
            }
            cursor.close();
        }

        return count;
    }

    public void updateNotificationReadStatus(ArrayList<NotificationDataModel> list){

        for(int i=0;i<list.size();i++){
            ContentValues values = new ContentValues();
            values.put(NotificationDBConstants.KEY_IS_READ, 1);
            notificationDatabase.update(NotificationDBConstants.TABLE_NAME, values,
                    NotificationDBConstants.KEY_ID+"="+list.get(i).localId, null);
        }

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
                    dataModel.localId = cursor.getInt(0);
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
