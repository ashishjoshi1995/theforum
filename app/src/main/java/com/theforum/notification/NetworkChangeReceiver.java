package com.theforum.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.theforum.utils.CommonUtils;

/**
 * @author  Ashish on 12/17/2015.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(CommonUtils.isInternetAvailable()){
            Log.e("NetworkCHangeReceiver", "net chalu hai");
            Intent i = new Intent(context,NotificationService.class);
            context.startService(i);

        }

    }

}
