package com.theforum.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.theforum.R;
import com.theforum.TheForumApplication;
import com.theforum.constants.NotificationType;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.data.local.models.NotificationDataModel;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.listeners.NotificationListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author DEEPANKAR
 * @since 01-02-2016.
 */
public class NotificationPollTask extends AsyncTask<Void, Void, Void> {

    private Service mService;
    private  ArrayList<NotificationDataModel> notificationsList;

    int stream=0;
    int notificationCount = 0;
    private int count = 0;


    public NotificationPollTask(Service service){
        this.mService = service;
        notificationsList = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {

        final NotificationHelper helper = new NotificationHelper();

        helper.readNotification(new NotificationListener() {

            @Override
            public void topicNotification(List<topic> topics) {
                Calendar calendar;
                for (int j = 0; j < topics.size(); j++) {

                    if (topics.get(j).getNotifRenewalRequests() > 0) {

                        NotificationDataModel notificationDataModel = new NotificationDataModel();
                        notificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST;
                        notificationDataModel.topicId = topics.get(j).getTopicId();
                        notificationDataModel.topicText = topics.get(j).getTopicName();
                        notificationDataModel.notificationCount = topics.get(j).getNotifRenewalRequests();
                        notificationDataModel.isRead = false;
                        calendar = Calendar.getInstance();
                        notificationDataModel.timeHolder = topics.get(j).getHoursLeft() + " hrs left to decay | "
                                +calendar.get(Calendar.HOUR) + ":" +calendar.get(Calendar.MINUTE) +" "
                                +calendar.get(Calendar.AM_PM)+" Today" ;

                        notificationsList.add(notificationDataModel);

                        if (SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_RENEWAL_REQUESTS_NOTIFICATION))
                            notificationCount++;
                    }

                    if (topics.get(j).getNotifOpinions() > 0) {
                        NotificationDataModel dataModel = new NotificationDataModel();
                        dataModel.notificationType = NotificationType.NOTIFICATION_TYPE_OPINIONS;
                        dataModel.topicId = topics.get(j).getTopicId();
                        dataModel.topicText = topics.get(j).getTopicName();
                        dataModel.notificationCount = topics.get(j).getNotifOpinions();
                        dataModel.isRead = false;

                        calendar = Calendar.getInstance();
                        dataModel.timeHolder = topics.get(j).getHoursLeft() + " hrs left to decay | "
                                +calendar.get(Calendar.HOUR) + ":" +calendar.get(Calendar.MINUTE) +" "
                                +calendar.get(Calendar.AM_PM)+" Today" ;

                        notificationsList.add(dataModel);

                        if (SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION))
                            notificationCount++;
                    }
                }

                if (topics.size() > 0) {

                    if (NotificationHelper.one && NotificationHelper.two && count == 0) {
                        helper.cleanItUp();
                        count++;
                    }

                    if (notificationCount > 0 && stream == 1) {
                        Notify(notificationCount, TheForumApplication.getAppContext());
                        stream = 0;

                    } else if (stream == 0) stream++;

                } else if (count > 0) {
                    count = 0;
                }

            }

            @Override
            public void opinionNotification(List<opinion> opinions) {

                for (int j = 0; j < opinions.size(); j++) {

                    NotificationDataModel NotificationDataModel = new NotificationDataModel();
                    NotificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES;
                    NotificationDataModel.topicText = opinions.get(j).getTopicName();
                    NotificationDataModel.topicId = opinions.get(j).getTopicId();
                    NotificationDataModel.notificationCount = opinions.get(j).getmNotifCount();
                    NotificationDataModel.description = opinions.get(j).getOpinionName();

                    notificationsList.add(NotificationDataModel);
                    notificationCount++;

                }

                if (opinions.size() > 0) {
                    if (NotificationHelper.one && NotificationHelper.two && count == 0) {
                        helper.cleanItUp();
                        count++;
                    } else if (count > 0) {
                        count = 0;
                    }

                    if (notificationCount > 0 && stream == 1) {
                        Notify(notificationCount, TheForumApplication.getAppContext());
                        stream = 0;
                    }

                    NotificationDBHelper.getHelper().addNotifications(notificationsList);
                }

            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        mService.stopSelf();
    }

    /**
     *
     * @param notificationCount gives number of new notifications
     * @param context context of the app to notify
     */
    private void Notify(int notificationCount, Context context){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.notification_icon))
                .setSmallIcon(R.drawable.system_bar_icon)
                .setAutoCancel(true)
                .setContentTitle("the forum")
                .setContentText("You have "+notificationCount+" new Notifications");

        Intent resultIntent = new Intent(context, NotificationActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
