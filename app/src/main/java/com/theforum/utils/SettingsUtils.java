package com.theforum.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Ashish
 * @since 1/8/2016
 */
public class SettingsUtils {
    private Context mContext;
    private static SettingsUtils settingsUtils;
    private SharedPreferences sharedPreferences;

    /**
     * Key Values to store in SharedPreferences
     */

    public static final String TOPIC_FEED_SORT_STATUS = "topic_feed_sort_status";

    public static final String ENABLE_UPVOTES_RECIEVED_NOTIFICATION = "enable_upvotes_received_notif";
    public static final String ENABLE_RENEWAL_REQUESTS_NOTIFICATION = "enable_renewal_request_notif";
    public static final String ENABLE_TOPIC_RENEWED_NOTIFICATION = "enable_topic_renewed_notification";
    public static final String ENABLE_OPINIONS_RECEIVED_NOTIFICATION = "enable_opinions_received_notification";


    public static SettingsUtils getInstance() {
        return settingsUtils;
    }

    public static void initialize(Context context) {

        if (settingsUtils == null) {
            settingsUtils = new SettingsUtils(context);
        }
    }

    private SettingsUtils(Context context) {
        mContext = context;
    }

    private SharedPreferences getPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = mContext.getApplicationContext().getSharedPreferences(
                    "theforum_settings", Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }

    public void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void saveIntegerarPreference(String key,int value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveBooleanPreference(String key, Boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getFromPreferences(String key) {
        String value = getPreferences().getString(key, "empty");

        if (value.equals("empty")) {
            value = null;
        }

        return value;
    }
    public int getIntFromPreferences(String key) {
        int value = getPreferences().getInt(key, -1);

        if (value == -1) {
            value = -1;
        }

        return value;
    }

    public Boolean getBoolPreference(String key){
        Boolean value = getPreferences().getBoolean(key, true);
        return value;
    }

    public void deletePreferences(String key) {
        getPreferences().edit().remove(key);
    }

    public boolean contains(String key) {
        return getPreferences().contains(key);
    }
}
