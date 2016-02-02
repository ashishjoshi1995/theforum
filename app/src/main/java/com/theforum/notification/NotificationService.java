package com.theforum.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

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
        Log.e("hello","mmmmmmmmmmmmmmmmmmmmmmmmmm");
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TheForumWakeLock");
        wakeLock.acquire();

        if (!CommonUtils.isInternetAvailable()) {
            stopSelf();
            Log.e("no net","no net");
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
                Log.e("topic size", "" + topics.size());
                for (int j = 0; j < topics.size(); j++) {

                    if (topics.get(j).getNotifRenewalRequests() > 0) {

                        NotificationDataModel notificationDataModel = new NotificationDataModel();
                        notificationDataModel.notificationType = NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST;
                        notificationDataModel.topicId = topics.get(j).getTopicId();
                        notificationDataModel.topicText = topics.get(j).getTopicName();
                        notificationDataModel.notificationCount = topics.get(j).getNotifRenewalRequests();
                        Log.e("ooooo",""+notificationDataModel.notificationCount);
                        notificationDataModel.isRead = false;
                        calendar = Calendar.getInstance();
                        notificationDataModel.timeHolder = topics.get(j).getHoursLeft() + " hrs left to decay | "
                                + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " "
                                + calendar.get(Calendar.AM_PM) + " Today";

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
                        Log.e("ggghhhhh",""+dataModel.notificationCount);
                        dataModel.isRead = false;

                        calendar = Calendar.getInstance();
                        dataModel.timeHolder = topics.get(j).getHoursLeft() + " hrs left to decay | "
                                + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " "
                                + calendar.get(Calendar.AM_PM) + " Today";

                        notificationsList.add(dataModel);

                        if (SettingsUtils.getInstance().getBoolPreference(SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION))
                            notificationCount++;
                    }
                }
                Log.e("notification count", "" + notificationCount);

                if (topics.size() > 0) {

                    if (NotificationHelper.one && NotificationHelper.two && count == 0) {
                        helper.cleanItUp();
                        count++;
                    }

                    if (notificationCount > 0) {
                        Log.i("notify is calling", "");
                        Notify(notificationCount, TheForumApplication.getAppContext());
                        stream = 0;

                    }

                    else if (stream == 0) stream++;
                    NotificationDBHelper.getHelper().openDatabase();
                    NotificationDBHelper.getHelper().addNotifications(notificationsList);
                    for(int i = 0;i<notificationsList.size();i++){
                        Log.e("checcnt inside db",""+notificationsList.get(i).notificationCount);
                    }
                    notificationsList.clear();
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
                    NotificationDataModel.notificationCount = opinions.get(j).getmNotifNewUpvotes();
                    NotificationDataModel.description = opinions.get(j).getOpinionName();

                    notificationsList.add(NotificationDataModel);
                    notificationCount++;
                    Log.e("ffffffffff",""+notificationCount);
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
                    Log.e("task started", "" + notificationsList.size());
                    NotificationDBHelper.getHelper().openDatabase();
                    NotificationDBHelper.getHelper().addNotifications(notificationsList);
                    for(int i = 0;i<notificationsList.size();i++){
                        Log.e("checking cnt inside db",""+notificationsList.get(i).notificationCount);
                    }
                    notificationsList.clear();
                    Log.e("temporary", notificationsList.toString());
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
Log.e("bbbbbbbb",""+notificationCount);
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
