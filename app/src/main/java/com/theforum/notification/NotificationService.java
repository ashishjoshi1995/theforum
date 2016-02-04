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

import com.theforum.R;
import com.theforum.TheForumApplication;
import com.theforum.constants.NotificationType;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.data.local.models.NotificationDataModel;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.SettingsUtils;
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

        getNotifications();

        wakeLock.release();
    }


    private void getNotifications(){
        notificationsList = new ArrayList<>();

        final NotificationHelper helper = new NotificationHelper();
        helper.readNotification(new NotificationListener() {

            @Override
            public void topicNotification(List<topic> topics) {
                Calendar calendar;
                String median ;
                for (int j = 0; j < topics.size(); j++) {

                    if (topics.get(j).getNotifRenewalRequests() > 0) {

                        NotificationDataModel notificationDataModel = new NotificationDataModel();
                        notificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST;
                        notificationDataModel.topicId = topics.get(j).getTopicId();
                        notificationDataModel.topicText = topics.get(j).getTopicName();
                        notificationDataModel.notificationCount = topics.get(j).getNotifRenewalRequests();
                        notificationDataModel.isRead = false;

                        calendar = Calendar.getInstance();
                        if(calendar.get(Calendar.AM_PM)==1){
                            median = "PM";
                        }else median = "AM";

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
                        if(calendar.get(Calendar.AM_PM)==1){
                            median = "PM";
                        }else median = "AM";
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
                    }

                    else if (stream == 0) stream++;
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

                    if(calendar.get(Calendar.AM_PM)==1){
                        median = "PM";
                    }else median = "AM";
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
                .setContentTitle("the forum")
                .setContentText("You have " + notificationCount + " new Notifications");

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


}
