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
import java.util.List;

/**
 * @author DEEPANKAR
 * @since 01-02-2016.
 */
public class NotificationPollTask extends AsyncTask<Void, Void, Void> {

    private Service mService;
    int stream=0;
    int notificationCount = 0;
    private int count = 0;

    public NotificationPollTask(Service service){
        this.mService = service;
    }

    @Override
    protected Void doInBackground(Void... params) {

        final NotificationHelper helper = new NotificationHelper();

        helper.readNotification(new NotificationListener() {

            @Override
            public void topicNotification(List<topic> topics) {

                ArrayList<NotificationDataModel> notificationsList = new ArrayList<>();

                for (int j = 0; j < topics.size(); j++) {
                    if (topics.get(j).getNotifRenewalRequests() > 0) {

                        NotificationDataModel notificationDataModelModel = new NotificationDataModel();
                        notificationDataModelModel.hoursLeft = topics.get(j).getHoursLeft();
                        notificationDataModelModel.topicId = topics.get(j).getTopicId();
                        notificationDataModelModel.topicText = topics.get(j).getTopicName();
                        notificationDataModelModel.renewalRequest = topics.get(j).getNotifRenewalRequests();
                        notificationDataModelModel.notificationType = NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST;
                        notificationsList.add(notificationDataModelModel);

                        if (SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_RENEWAL_REQUESTS_NOTIFICATION))
                            notificationCount++;
                    }

                    if (topics.get(j).getNotifOpinions() > 0) {
                        NotificationDataModel inflatorItemDataOpinions = new NotificationDataModel();
                        inflatorItemDataOpinions.notificationType = NotificationType.NOTIFICATION_TYPE_OPINIONS;
                        inflatorItemDataOpinions.hoursLeft = topics.get(j).getHoursLeft();
                        inflatorItemDataOpinions.topicId = topics.get(j).getTopicId();
                        inflatorItemDataOpinions.topicText = topics.get(j).getTopicName();
                        inflatorItemDataOpinions.opinions = topics.get(j).getNotifOpinions();
                        notificationsList.add(inflatorItemDataOpinions);
                        if (SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION))
                            notificationCount++;
                    }
                }

                if (notificationsList.size() > 0) {
                    if (NotificationHelper.one && NotificationHelper.two && count == 0) {
                        helper.cleanItUp();
                        count++;
                    }
                    NotificationDBHelper.getHelper().addNotifications(notificationsList);


                    if (notificationCount > 0 && stream == 1) {

                        Notify(notificationCount, TheForumApplication.getAppContext());
                        stream = 0;

                    } else if (stream == 0) {
                        stream++;
                    }

                } else if (count > 0) {
                    count = 0;
                }

            }

            @Override
            public void opinionNotification(List<opinion> opinions) {
                ArrayList<NotificationDataModel> inflatorItemDatas = new ArrayList<>();

                for (int j = 0; j < opinions.size(); j++) {

                    NotificationDataModel NotificationDataModel = new NotificationDataModel();
                    NotificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES;
                    NotificationDataModel.topicText = opinions.get(j).getTopicName();
                    NotificationDataModel.topicId = opinions.get(j).getTopicId();
                    NotificationDataModel.newCount = opinions.get(j).getmNotifCount();
                    NotificationDataModel.upVoteCount = opinions.get(j).getUpVotes();
                    NotificationDataModel.downVoteCount = opinions.get(j).getDownVotes();
                    NotificationDataModel.description = opinions.get(j).getOpinionName();
                    inflatorItemDatas.add(NotificationDataModel);
                    notificationCount++;

                }


                if (inflatorItemDatas.size() > 0) {
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

                    NotificationDBHelper.getHelper().addNotifications(inflatorItemDatas);
                    //OpinionDBHelper.getHelper().addOpinions(opinions);
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
