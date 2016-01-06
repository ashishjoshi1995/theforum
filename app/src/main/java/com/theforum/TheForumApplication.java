package com.theforum;

import android.app.Application;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;


/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class TheForumApplication extends Application {

    private static MobileServiceClient mClient;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mClient = new MobileServiceClient(
                    "https://theforum.azure-mobile.net/",
                    "XxfUFnBzgZmLkJrRXmjnMvwMzYnznB23",
                    this);
        } catch (MalformedURLException e) {
            mClient = null;
            e.printStackTrace();
        }


    }

    public static MobileServiceClient getClient(){
        return mClient;
    }
}
