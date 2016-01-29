package com.theforum.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.theforum.ui.ContainerActivity;
import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.constants.NotificationType;
import com.theforum.data.helpers.NotificationHelper;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.data.server.NotificationDataModel;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.utils.CommonUtils;
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

        @Override
        protected Void doInBackground(Void... params) {

            // here we need to query the two tables and transfer the data to on post execute

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
                            notificationCount++;
                        }
                    }

                    if(notificationsList.size()>0) {
                        if(NotificationHelper.one && NotificationHelper.two && count ==0){
                            helper.cleanItUP();
                            count++;
                        }
                        NotificationDBHelper.getHelper().addNotifications(notificationsList);
                        Notify(notificationCount);
                    }
                    else if(count>0){
                        count = 0;
                    }

                }
                @Override
                public void opinionNotification(List<opinion> opinions) {
                    ArrayList<NotificationDataModel> inflatorItemDatas = new ArrayList<>();
                    Log.e("opinion size",""+opinions.size());
                        for(int j=0;j<opinions.size();j++){
                            NotificationDataModel inflatorItemData = new NotificationDataModel();
                            inflatorItemData.notificationType = NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES;
                            inflatorItemData.topicText = opinions.get(j).getTopicName();
                            inflatorItemData.topicId =opinions.get(j).getTopicId();
                            inflatorItemData.newCount = opinions.get(j).getmNotifCount();
                            inflatorItemData.totalUpvotes = opinions.get(j).getUpVotes();
                            inflatorItemData.totalDownvotes = opinions.get(j).getDownVotes();
                            inflatorItemData.opinionText = opinions.get(j).getOpinionName();
                            inflatorItemDatas.add(inflatorItemData);
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

                        Notify(notificationCount);
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

        private void Notify(int j){

            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.mipmap.ic_launcher, "theforum", when);

            NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);

            contentView.setTextViewText(R.id.title, "theforum");
            contentView.setTextViewText(R.id.text, "you have "+j+" new notifications");

            notification.contentView = contentView;

            Intent notificationIntent = new Intent(getApplicationContext(), NotificationActivity.class);
            notification.contentIntent = PendingIntent.getActivity(getApplication(), 0, notificationIntent, 0);

            notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
            notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
            notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
            notification.defaults |= Notification.DEFAULT_SOUND; // Sound

            mNotificationManager.notify(1, notification);
            stopSelf();
        }
    }
}
