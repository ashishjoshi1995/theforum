package com.theforum.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ashish on 12/17/2015.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isAvailable()||mobile.isAvailable()){
            Log.e("NetworkCHangeReceiver", "net chalu hai");
            Toast.makeText(context,"net chalu",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context,NotificationService.class);
            context.startService(i);

        }

    }

}
