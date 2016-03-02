package com.theforum.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.theforum.R;
import com.theforum.TheForumApplication;
import com.theforum.constants.NotificationType;
import com.theforum.data.local.database.notificationDB.NotificationDB;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.data.local.models.NotificationDataModel;
import com.theforum.data.server.areaopinions;
import com.theforum.data.server.areatopics;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.ui.home.HomeActivity;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.listeners.KillerNotificationListener;
import com.theforum.utils.listeners.NotificationListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * checks whether a notification has arrived or not
 * uses polling
 */
public class NotificationService extends IntentService {

    private ArrayList<NotificationDataModel> notificationsList;

    int stream=0;
    int notificationCount = 0;
    private int count = 0;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public NotificationService() {
        super(NotificationService.class.getName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TheForumWakeLock");
        wakeLock.acquire();

        if (!CommonUtils.isInternetAvailable()) {
            stopSelf();
            return;
        }
        killInactivity();
        getNotifications();
Log.e("Service started","service");
        wakeLock.release();
    }

    private void killInactivity(){
        Log.e("kill inactivity","function");
        //get the count, increment it, resave to prefernce
        //if count > 12 call helper to read about the latest topic in global
        //TODO remove the testing modification from here
       int j = SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.INACTIVITY_KILLER_NOTIFICATION);
        CommonUtils.showToast(getApplication(),"dafuk");
        j++;
        if(j>10){
            j=0;
            Log.e("inside j","inside j");
            final NotificationHelper helper = new NotificationHelper();
            helper.readKillerNotification(new KillerNotificationListener() {
                @Override
                public void topicsGot(List<topic> topicNotifications, int count) {
                    int c = 0;
                    Log.e("inside","topicgots");
                    int index = 0;
                    for(int i=0;i<topicNotifications.size();i++){
                        if(c>= topicNotifications.get(i).getHoursLeft()){

                        }else{
                            c = topicNotifications.get(i).getHoursLeft();
                            index = i;
                        }
                    }
                    killerNotify(TheForumApplication.getAppContext(),topicNotifications.get(index),count-1);
                }
            });
        }
        SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.INACTIVITY_KILLER_NOTIFICATION, j);

    }


    private void getNotifications(){
        notificationsList = new ArrayList<>();

        final NotificationHelper helper = new NotificationHelper();
        helper.readNotification(new NotificationListener() {

            @Override
            public void topicNotification(List<topic> topics) {
                Calendar calendar;
                String median;
                for (int j = 0; j < topics.size(); j++) {

                    if (topics.get(j).getNotifRenewalRequests() > 0) {

                        NotificationDataModel notificationDataModel = new NotificationDataModel();
                        notificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST;
                        notificationDataModel.topicId = topics.get(j).getTopicId();
                        notificationDataModel.topicText = topics.get(j).getTopicName();
                        notificationDataModel.notificationCount = topics.get(j).getNotifRenewalRequests();
                        notificationDataModel.isRead = false;

                        calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.AM_PM) == 1) {
                            median = "PM";
                        } else median = "AM";

                        notificationDataModel.timeHolder = topics.get(j).getHoursLeft() + " hrs left to decay | "
                                + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " "
                                + median;

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
                        if (calendar.get(Calendar.AM_PM) == 1) {
                            median = "PM";
                        } else median = "AM";
                        dataModel.timeHolder = topics.get(j).getHoursLeft() + " hrs left to decay | "
                                + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " "
                                + median;

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

                    if (notificationCount > 0) {
                        Notify(notificationCount, TheForumApplication.getAppContext());
                        stream = 0;
                    } else if (stream == 0) stream++;
                    NotificationDBHelper.getHelper().openDatabase();
                    NotificationDBHelper.getHelper().addNotifications(notificationsList);
                    notificationsList.clear();
                } else if (count > 0) {
                    count = 0;
                }

            }

            @Override
            public void opinionNotification(List<opinion> opinions) {
                Calendar calendar = Calendar.getInstance();
                String median;

                for (int j = 0; j < opinions.size(); j++) {

                    NotificationDataModel notificationDataModel = new NotificationDataModel();
                    notificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES;
                    notificationDataModel.topicText = opinions.get(j).getTopicName();
                    notificationDataModel.topicId = opinions.get(j).getTopicId();
                    notificationDataModel.notificationCount = opinions.get(j).getmNotifNewUpvotes();
                    notificationDataModel.description = opinions.get(j).getOpinionName();

                    if (calendar.get(Calendar.AM_PM) == 1) {
                        median = "PM";
                    } else median = "AM";
                    notificationDataModel.timeHolder = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " "
                            + median;

                    notificationsList.add(notificationDataModel);
                    notificationCount++;
                }

                if (opinions.size() > 0) {
                    if (NotificationHelper.one && NotificationHelper.two && count == 0) {
                        helper.cleanItUp();
                        count++;
                    } else if (count > 0) {
                        count = 0;
                    }

                    if (notificationCount > 0) {
                        Notify(notificationCount, TheForumApplication.getAppContext());
                        stream = 0;
                    }

                    NotificationDBHelper.getHelper().openDatabase();
                    NotificationDBHelper.getHelper().addNotifications(notificationsList);
                    notificationsList.clear();

                }
            }

            @Override
            public void areaTopicNotification(List<areatopics> areatopics) {
                Calendar calendar;
                String median;
                for (int j = 0; j < areatopics.size(); j++) {

                    if (areatopics.get(j).getNotifRenewalRequests() > 0) {

                        NotificationDataModel notificationDataModel = new NotificationDataModel();
                        notificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST;
                        notificationDataModel.topicId = areatopics.get(j).getTopicId();
                        notificationDataModel.topicText = areatopics.get(j).getTopicName();
                        notificationDataModel.notificationCount = areatopics.get(j).getNotifRenewalRequests();
                        notificationDataModel.isRead = false;

                        calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.AM_PM) == 1) {
                            median = "PM";
                        } else median = "AM";

                        notificationDataModel.timeHolder = areatopics.get(j).getHoursLeft() + " hrs left to decay | "
                                + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " "
                                + median;

                        notificationsList.add(notificationDataModel);

                        if (SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_RENEWAL_REQUESTS_NOTIFICATION))
                            notificationCount++;
                    }

                    if (areatopics.get(j).getNotifOpinions() > 0) {
                        NotificationDataModel dataModel = new NotificationDataModel();
                        dataModel.notificationType = NotificationType.NOTIFICATION_TYPE_OPINIONS;
                        dataModel.topicId = areatopics.get(j).getTopicId();
                        dataModel.topicText = areatopics.get(j).getTopicName();
                        dataModel.notificationCount = areatopics.get(j).getNotifOpinions();
                        dataModel.isRead = false;

                        calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.AM_PM) == 1) {
                            median = "PM";
                        } else median = "AM";
                        dataModel.timeHolder = areatopics.get(j).getHoursLeft() + " hrs left to decay | "
                                + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " "
                                + median;

                        notificationsList.add(dataModel);

                        if (SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION))
                            notificationCount++;
                    }
                }
                if (areatopics.size() > 0) {

                    if (NotificationHelper.one && NotificationHelper.two && count == 0) {
                        helper.cleanItUp();
                        count++;
                    }

                    if (notificationCount > 0) {
                        Notify(notificationCount, TheForumApplication.getAppContext());
                        stream = 0;
                    } else if (stream == 0) stream++;
                    NotificationDBHelper.getHelper().openDatabase();
                    NotificationDBHelper.getHelper().addNotifications(notificationsList);
                    notificationsList.clear();
                } else if (count > 0) {
                    count = 0;
                }
            }

            @Override
            public void areaOpinionNotification(List<areaopinions> areaopinionss) {
                Calendar calendar = Calendar.getInstance();
                String median;

                for (int j = 0; j < areaopinionss.size(); j++) {

                    NotificationDataModel notificationDataModel = new NotificationDataModel();
                    notificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES;
                    notificationDataModel.topicText = areaopinionss.get(j).getTopicName();
                    notificationDataModel.topicId = areaopinionss.get(j).getTopicId();
                    notificationDataModel.notificationCount = areaopinionss.get(j).getmNotifNewUpvotes();
                    notificationDataModel.description = areaopinionss.get(j).getOpinionName();

                    if (calendar.get(Calendar.AM_PM) == 1) {
                        median = "PM";
                    } else median = "AM";
                    notificationDataModel.timeHolder = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " "
                            + median;

                    notificationsList.add(notificationDataModel);
                    notificationCount++;
                }

                if (areaopinionss.size() > 0) {
                    if (NotificationHelper.one && NotificationHelper.two && count == 0) {
                        helper.cleanItUp();
                        count++;
                    } else if (count > 0) {
                        count = 0;
                    }

                    if (notificationCount > 0) {
                        Notify(notificationCount, TheForumApplication.getAppContext());
                        stream = 0;
                    }

                    NotificationDBHelper.getHelper().openDatabase();
                    NotificationDBHelper.getHelper().addNotifications(notificationsList);
                    notificationsList.clear();

                }
            }
        });
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
                .setContentTitle("theforum")
                .setContentText(context.getResources().getQuantityString(R.plurals.notification_count_message,
                        notificationCount,notificationCount));

        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

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

    private void killerNotify(Context context,topic topic, int count){
        String message = "Croak about "+topic.getTopicName()+" and "+count+" other topics on theforum";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.notification_icon))
                .setSmallIcon(R.drawable.system_bar_icon)
                .setAutoCancel(true)
                .setContentTitle("theforum")
                .setContentText(message);

        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent resultIntent = new Intent(context, HomeActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }

}
