package com.theforum.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.theforum.Constants;
import com.theforum.HomeActivity;
import com.theforum.R;
import com.theforum.TheForumApplication;
import com.theforum.data.dataModels.OpinionNotification;
import com.theforum.data.dataModels.TopicNotification;
import com.theforum.data.dataModels.opinion;
import com.theforum.data.dataModels.topic;
import com.theforum.data.helpers.NotificationHelper;
import com.theforum.data.interfaces.NotificationIfAny;
import com.theforum.data.local.NotificationInflatorItemData;
import com.theforum.data.local.NotificationStack;

import java.util.List;

/**
 * Created by Ashish on 12/9/2015.
 */
public class NotificationService extends Service {
    private PowerManager.WakeLock mWakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleIntent(Intent intent) {
        // obtain the wake lock
        Log.e("NotificationService","handleIntent");
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLockTag");
        mWakeLock.acquire();

        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }

        // do the actual work, in a separate thread
        new PollTask().execute();
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("NotificationService","onStartCommand");
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
        Log.e("NotificationService", "onDestroy");
    }

    private class PollTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // do stuff!
            // here we need to query the two tables and transfer the data to on post execute
            Log.e("PollTask doinbackground","notficationhelper called");
            NotificationHelper helper = new NotificationHelper();
            helper.readNotification(new NotificationIfAny() {
            int jaiHo = 0;
            boolean stop = false;
                @Override
                public void topicNotif(List<topic> topics) {
                    List<NotificationInflatorItemData> inflatorItemDatas;
                    for(int j =0; j<topics.size();j++){
                        NotificationInflatorItemData inflatorItemDataRenewal = new NotificationInflatorItemData();
                        inflatorItemDataRenewal.hoursLeft = topics.get(j).getmHoursLeft();
                        inflatorItemDataRenewal.topicId = topics.get(j).getmTopicId();
                        inflatorItemDataRenewal.topicText = topics.get(j).getmTopic();
                        inflatorItemDataRenewal.renewalRequest = topics.get(j).getmRenewalRequests();
                        inflatorItemDataRenewal.notificationType = Constants.NOTIFICATION_TYPE_RENEWALREQUEST;
                        NotificationStack.pushNotificationInflatorItemData(inflatorItemDataRenewal);
                        jaiHo++;

                        NotificationInflatorItemData inflatorItemDataOpinions = new NotificationInflatorItemData();
                        inflatorItemDataOpinions.notificationType = Constants.NOTIFICATION_TYPE_OPINIONS;
                        inflatorItemDataOpinions.hoursLeft = topics.get(j).getmHoursLeft();
                        inflatorItemDataOpinions.topicText = topics.get(j).getmTopic();
                        inflatorItemDataOpinions.opinions = topics.get(j).getmTotalOpinions();
                        NotificationStack.pushNotificationInflatorItemData(inflatorItemDataOpinions);
                        jaiHo++;

                    }
                    if(stop){
                        Notify(jaiHo);
                        stop = false;
                    }
                    stop= true;
                }

                @Override
                public void opinionNotif(List<opinion> opinions) {
                        for(int j=0;j<opinions.size();j++){
                            NotificationInflatorItemData inflatorItemData = new NotificationInflatorItemData();
                            inflatorItemData.notificationType = Constants.NOTIFICATION_TYPE_OPINIONUPVOTES;
                            inflatorItemData.topicText = opinions.get(j).getmTopic();
                            inflatorItemData.newCount = opinions.get(j).getmNotifCount();
                            inflatorItemData.totalUpvotes = opinions.get(j).getmUpVotes();
                            inflatorItemData.totalDownvotes = opinions.get(j).getmDownVotes();
                            inflatorItemData.opinionText = opinions.get(j).getmOpinion();
                            NotificationStack.pushNotificationInflatorItemData(inflatorItemData);
                            jaiHo++;
                        }
                    if(stop){
                        Notify(jaiHo);
                        stop = false;
                    }
                    stop= true;
                }
            });


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            // handle your data
           // Notify("You've received new message","messAGE");
           // Toast.makeText(getBaseContext(),"asynctask",Toast.LENGTH_SHORT).show();
            stopSelf();
        }



        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void Notify(int j){
            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.mipmap.ic_launcher, "theforum", when);

            NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            //TODO modify here for ewsponse
            contentView.setTextViewText(R.id.title, "theforum");
            contentView.setTextViewText(R.id.text, "you have "+j+" new notifications");


            notification.contentView = contentView;

            Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplication(), 0, notificationIntent, 0);
            notification.contentIntent = contentIntent;

            //notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
            notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
            notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
            notification.defaults |= Notification.DEFAULT_SOUND; // Sound

            mNotificationManager.notify(1, notification);
            stopSelf();

        }


    }

}