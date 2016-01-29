package com.theforum.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.theforum.R;
import com.theforum.TheForumApplication;
import com.theforum.constants.NotificationType;
import com.theforum.data.helpers.NotificationHelper;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.data.server.NotificationDataModel;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.listeners.NotificationListener;

import java.util.ArrayList;
import java.util.List;


/**
 * checks whether a notification has arrived or not
 * uses polling
 */
public class NotificationService extends Service {

    private PowerManager.WakeLock mWakeLock;
    private int count = 0;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mWakeLock.release();
    }

    private void handleIntent() {
        // obtain the wake lock

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TheForumWakeLog");
        mWakeLock.acquire();

        // check the global background data setting

        if (!CommonUtils.isInternetAvailable()) {
            stopSelf();
            return;
        }

        // do the actual work, in a separate thread
        new PollTask().execute();
    }


    private class PollTask extends AsyncTask<Void, Void, Void> {
        int stream=0;
        @Override
        protected Void doInBackground(Void... params) {


            final NotificationHelper helper = new NotificationHelper();
            helper.readNotification(new NotificationListener() {
            int notificationCount = 0;

                @Override
                public void topicNotification(List<topic> topics) {

                    ArrayList<NotificationDataModel> notificationsList = new ArrayList<>();

                    for(int j =0; j<topics.size();j++){
                        if(topics.get(j).getmNotifRenewalRequests()>0) {
                            NotificationDataModel notificationModel = new NotificationDataModel();
                            notificationModel.hoursLeft = topics.get(j).getHoursLeft();
                            notificationModel.topicId = topics.get(j).getTopicId();
                            notificationModel.topicText = topics.get(j).getTopicName();
                            notificationModel.renewalRequest = topics.get(j).getmNotifRenewalRequests();
                            notificationModel.notificationType = NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST;
                            notificationsList.add(notificationModel);
                            if(SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_RENEWAL_REQUESTS_NOTIFICATION))
                                notificationCount++;
                        }

                        if(topics.get(j).getmNotifOpinions()>0) {
                            NotificationDataModel inflatorItemDataOpinions = new NotificationDataModel();
                            inflatorItemDataOpinions.notificationType = NotificationType.NOTIFICATION_TYPE_OPINIONS;
                            inflatorItemDataOpinions.hoursLeft = topics.get(j).getHoursLeft();
                            inflatorItemDataOpinions.topicId = topics.get(j).getTopicId();
                            inflatorItemDataOpinions.topicText = topics.get(j).getTopicName();
                            inflatorItemDataOpinions.opinions = topics.get(j).getmNotifOpinions();
                            notificationsList.add(inflatorItemDataOpinions);
                            if(SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION))
                                notificationCount++;
                        }
                    }

                    if(notificationsList.size()>0) {
                        if(NotificationHelper.one && NotificationHelper.two && count ==0){
                            helper.cleanItUP();
                            count++;
                        }
                        NotificationDBHelper.getHelper().addNotifications(notificationsList);


                        if(notificationCount>0 && stream == 1){

                            Notify(notificationCount, TheForumApplication.getAppContext());
                            stream = 0;

                        } else if(stream == 0){
                            stream++;
                        }

                    } else if(count>0){
                        count = 0;
                    }

                }
                @Override
                public void opinionNotification(List<opinion> opinions) {
                    ArrayList<NotificationDataModel> inflatorItemDatas = new ArrayList<>();

                        for(int j=0;j<opinions.size();j++){

                            NotificationDataModel notificationDataModel = new NotificationDataModel();
                            notificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES;
                            notificationDataModel.topicText = opinions.get(j).getTopicName();
                            notificationDataModel.topicId =opinions.get(j).getTopicId();
                            notificationDataModel.newCount = opinions.get(j).getmNotifCount();
                            notificationDataModel.totalUpvotes = opinions.get(j).getUpVotes();
                            notificationDataModel.totalDownvotes = opinions.get(j).getDownVotes();
                            notificationDataModel.opinionText = opinions.get(j).getOpinionName();
                            inflatorItemDatas.add(notificationDataModel);
                            notificationCount++;

                        }


                    if(inflatorItemDatas.size()>0){
                        if(NotificationHelper.one && NotificationHelper.two && count == 0){
                            helper.cleanItUP();
                            count ++;
                        }
                        else if(count>0){
                            count = 0;
                        }

                        if(notificationCount>0 && stream == 1){
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
            stopSelf();
        }

        private void Notify(int notificationCount, Context context){

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                                R.drawable.notification_icon))
                                        .setSmallIcon(R.drawable.system_bar_icon)
                                        .setContentTitle("the forum")
                                        .setContentText("You have "+notificationCount+" new Notifications");

            Intent resultIntent = new Intent(context, NotificationActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(NotificationActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
