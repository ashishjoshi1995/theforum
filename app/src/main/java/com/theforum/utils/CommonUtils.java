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
import android.os.Build;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.theforum.ContainerActivity;


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

    public static void openContainerActivity(Context context,int idValue){
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra("id", idValue);
        context.startActivity(intent);
    }

    public static void openContainerActivity(Context context,int idValue,Pair<String,String> extras){
        if(extras==null){
            openContainerActivity(context, idValue);
        }else{
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("id", idValue);
            intent.putExtra(extras.first,extras.second);
            context.startActivity(intent);
        }
    }


    public static void setStatusBarColor(Activity activity, int color){
        if(Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(color);
        }
    }


}
