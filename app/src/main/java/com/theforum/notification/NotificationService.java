package com.theforum.notification;

import android.annotation.TargetApi;
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
import android.widget.Toast;

import com.theforum.R;

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


        }


    }

}
