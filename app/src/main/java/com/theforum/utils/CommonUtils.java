package com.theforum.utils;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.theforum.TheForumApplication;
import com.theforum.ui.activity.ContainerActivity;


public class CommonUtils {

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


    public static void openContainerActivity(Context context,int idValue){
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra("id", idValue);
        context.startActivity(intent);
    }

    public static void openContainerActivity(Context context,int idValue,Pair<String,Parcelable> extras){
        if(extras==null){
            openContainerActivity(context, idValue);
        }
        else{

            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("id", idValue);
            intent.putExtra(extras.first,extras.second);
            context.startActivity(intent);
        }
    }

//    public static void openContainerActivity2(Context context,int idValue, Pair<String,Parcelable[]> extras){
//        if(extras==null){
//            openContainerActivity(context, idValue);
//        }else{
//            Intent intent = new Intent(context, ContainerActivity.class);
//            intent.putExtra("id", idValue);
//            intent.putExtra(extras.first,extras.second);
//            context.startActivity(intent);
//        }
//    }

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

}
