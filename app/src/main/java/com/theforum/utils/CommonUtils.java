package com.theforum.utils;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.theforum.ui.ContainerActivity;
import com.theforum.TheForumApplication;

import java.io.Serializable;
import java.util.ArrayList;


public class CommonUtils {

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public static Drawable tintDrawable(Drawable drawable,String color){
        drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY));
        return drawable;
    }

    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void goToUrl(Context context,String url){
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        context.startActivity(launchBrowser);
    }

    public static void emailIntent(Context context){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "contact@theforumapp.co", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public static void shareViaWatsapp(Context context, String message){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void openContainerActivity(Context context,int idValue){
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra("id", idValue);
        context.startActivity(intent);
    }

    public static void openContainerActivity(Context context,int idValue,Pair<String,Serializable> extras){
        if(extras==null){
            openContainerActivity(context, idValue);
        }else{

            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("id", idValue);
            intent.putExtra(extras.first,extras.second);
            context.startActivity(intent);
        }
    }

    public static void openContainerActivity(Context context,int idValue,ArrayList<Pair<String,Serializable>> extras){
        if(extras==null){
            openContainerActivity(context, idValue);
        }else{
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("id", idValue);
            for(int i = 0;i<extras.size();i++){
                intent.putExtra(extras.get(i).first,extras.get(i).second);
            }
            context.startActivity(intent);
        }
    }

    /**
     * Checking for all possible internet providers
     *
     * @return status of the connection, true if net is available
     */

    public static boolean isInternetAvailable(){

        ConnectivityManager connectivity = (ConnectivityManager) TheForumApplication.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void setStatusBarColor(Activity activity, int color){
        if(Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(color);
        }
    }
}
