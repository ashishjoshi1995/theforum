package com.theforum.data.local;

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

    public String getFromPreferences(String key) {
        String value = getPreferences().getString(key, "empty");

        if (value.equals("empty")) {
            value = null;
        }

        return value;
    }

    public void deletePreferences(String key) {
        getPreferences().edit().remove(key);
    }

    public boolean contains(String key) {
        return getPreferences().contains(key);
    }
}
