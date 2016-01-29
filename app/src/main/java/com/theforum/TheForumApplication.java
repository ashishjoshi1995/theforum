package com.theforum;

import android.app.Application;
import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.theforum.constants.SortType;
import com.theforum.utils.ProfileUtils;
import com.theforum.utils.SettingsUtils;

import java.net.MalformedURLException;


/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class TheForumApplication extends Application {

    private static MobileServiceClient mServerClient;
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        initializeServerClient();

        ProfileUtils.initialize(context);
        SettingsUtils.initialize(context);

        if(!SettingsUtils.getInstance().contains(SettingsUtils.TOPIC_FEED_SORT_STATUS)){
            SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.TOPIC_FEED_SORT_STATUS,
                    SortType.SORT_BASIS_MOST_RENEWAL);
        }


    }

    public static MobileServiceClient getClient(){
        if(mServerClient==null) initializeServerClient();

        return mServerClient;
    }

    public static Context getAppContext(){return context;}

    private static void initializeServerClient(){
        try {
            mServerClient = new MobileServiceClient(
                    "https://theforum.azure-mobile.net/",
                    "XxfUFnBzgZmLkJrRXmjnMvwMzYnznB23",
                    context);
        } catch (MalformedURLException e) {
            mServerClient = null;
        }
    }

}
