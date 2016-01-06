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
import com.theforum.R;
import com.theforum.TheForumApplication;

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
            MobileServiceClient client = TheForumApplication.getClient();


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            // handle your data
            Notify("You've received new message","messAGE");
            Toast.makeText(getBaseContext(),"asynctask",Toast.LENGTH_SHORT).show();
            stopSelf();
        }
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void Notify(String notificationTitle, String notificationMessage){
            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.mipmap.ic_launcher, "Spur", when);

            NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            //TODO modify here for ewsponse
            contentView.setTextViewText(R.id.title, "KEEP WORKING");
            contentView.setTextViewText(R.id.text, "Kindly rate your efficiency in the last hour");


            notification.contentView = contentView;

           // Intent notificationIntent = new Intent(this, MainActivity.class);
           // PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
           // notification.contentIntent = contentIntent;

            //notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
            notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
            notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
            notification.defaults |= Notification.DEFAULT_SOUND; // Sound

            mNotificationManager.notify(1, notification);
            stopSelf();

        }


    }

}
