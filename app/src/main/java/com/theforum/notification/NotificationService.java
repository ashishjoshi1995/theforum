package com.theforum.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.theforum.Constants;
import com.theforum.data.local.database.opinionDB.OpinionDBHelper;
import com.theforum.ui.home.HomeActivity;
import com.theforum.R;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.data.helpers.NotificationHelper;
import com.theforum.data.interfaces.NotificationIfAny;
import com.theforum.data.server.NotificationDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Ashish on 12/9/2015.
 */
public class NotificationService extends Service {
    private PowerManager.WakeLock mWakeLock;
    private Boolean temp = false;
    private int count = 0;
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
        //TODO remove the below comment
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
            final NotificationHelper helper = new NotificationHelper();
            helper.readNotification(new NotificationIfAny() {
            int jaiHo = 0;
                @Override
                public void topicNotif(List<topic> topics) {
                    ArrayList<NotificationDataModel> inflatorItemDatas = new ArrayList<NotificationDataModel>();
                    Log.e("topic count",""+topics.size());
                    for(int j =0; j<topics.size();j++){
                        if(topics.get(j).getmNotifRenewalRequests()>0) {
                            NotificationDataModel inflatorItemDataRenewal = new NotificationDataModel();
                            inflatorItemDataRenewal.hoursLeft = topics.get(j).getHoursLeft();
                            inflatorItemDataRenewal.topicId = topics.get(j).getTopicId();
                            inflatorItemDataRenewal.topicText = topics.get(j).getTopicName();
                            inflatorItemDataRenewal.renewalRequest = topics.get(j).getmNotifRenewalRequests();
                            inflatorItemDataRenewal.notificationType = Constants.NOTIFICATION_TYPE_RENEWAL_REQUEST;
                            inflatorItemDatas.add(inflatorItemDataRenewal);
                            jaiHo++;
                            Log.e("salma", "" + jaiHo);
                        }
                        if(topics.get(j).getmNotifOpinions()>0) {
                            NotificationDataModel inflatorItemDataOpinions = new NotificationDataModel();
                            inflatorItemDataOpinions.notificationType = Constants.NOTIFICATION_TYPE_OPINIONS;
                            inflatorItemDataOpinions.hoursLeft = topics.get(j).getHoursLeft();
                            inflatorItemDataOpinions.topicId = topics.get(j).getTopicId();
                            inflatorItemDataOpinions.topicText = topics.get(j).getTopicName();
                            inflatorItemDataOpinions.opinions = topics.get(j).getmNotifOpinions();
                            inflatorItemDatas.add(inflatorItemDataOpinions);
                            jaiHo++;
                        }
                    }
                    //if(stop){
                    if(inflatorItemDatas.size()>0) {
                        if(NotificationHelper.one && NotificationHelper.two && count ==0){
                            helper.cleanItUP();
                            count++;
                        }
                        NotificationDBHelper.getNotificationDBHelper().addNotifications(inflatorItemDatas);
                        Notify(jaiHo);
                    }
                    else if(count>0){
                        count = 0;
                    }
                }
                @Override
                public void opinionNotif(List<opinion> opinions) {
                    ArrayList<NotificationDataModel> inflatorItemDatas = new ArrayList<NotificationDataModel>();
                    Log.e("opinion size",""+opinions.size());
                        for(int j=0;j<opinions.size();j++){
                            NotificationDataModel inflatorItemData = new NotificationDataModel();
                            inflatorItemData.notificationType = Constants.NOTIFICATION_TYPE_OPINION_UP_VOTES;
                            inflatorItemData.topicText = opinions.get(j).getTopicName();
                            inflatorItemData.topicId =opinions.get(j).getTopicId();
                            inflatorItemData.newCount = opinions.get(j).getmNotifCount();
                            inflatorItemData.totalUpvotes = opinions.get(j).getUpVotes();
                            inflatorItemData.totalDownvotes = opinions.get(j).getDownVotes();
                            inflatorItemData.opinionText = opinions.get(j).getOpinionName();
                            inflatorItemDatas.add(inflatorItemData);
                            jaiHo++;
                            Log.e("salma2",""+jaiHo);
                        }
                    //if(stop){

                    if(inflatorItemDatas.size()>0){
                        if(NotificationHelper.one && NotificationHelper.two && count == 0){
                            helper.cleanItUP();
                            count ++;
                        }
                        else if(count>0){
                            count = 0;
                        }
                        Notify(jaiHo);
                        NotificationDBHelper.getNotificationDBHelper().addNotifications(inflatorItemDatas);
                        OpinionDBHelper.getOpinionDBHelper(getApplicationContext()).addOpinions(opinions);
                    }
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

        private void Notify(int j){
            Log.e("notify method",""+j);
            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.mipmap.ic_launcher, "theforum", when);

            NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            //TODO modify here for response
            contentView.setTextViewText(R.id.title, "theforum");
            contentView.setTextViewText(R.id.text, "you have "+j+" new notifications");


            notification.contentView = contentView;

            Intent notificationIntent = new Intent(getApplicationContext(), NotificationFragment.class);
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
