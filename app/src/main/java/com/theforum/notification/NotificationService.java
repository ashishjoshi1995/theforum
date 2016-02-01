package com.theforum.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import com.theforum.utils.CommonUtils;


/**
 * checks whether a notification has arrived or not
 * uses polling
 */
public class NotificationService extends Service {

    private PowerManager.WakeLock mWakeLock;

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

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TheForumWakeLock");
        mWakeLock.acquire();

        if (!CommonUtils.isInternetAvailable()) {
            stopSelf();
            return;
        }

        // do the actual work, in a separate thread
        new NotificationPollTask(this).execute();
    }

}
