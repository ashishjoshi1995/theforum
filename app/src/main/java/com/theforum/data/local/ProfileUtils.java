package com.theforum.data.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ashish on 1/2/2016.
 */
public class ProfileUtils {
    private Context mContext;
    private static ProfileUtils profileUtils;
    private SharedPreferences sharedPreferences;

    /**
     * Key Values to store in SharedPreferences
     */

    public static final String FORUM_ID = "forum_id";
    public static final String STATUS = "status";
    public static final String POINTS_COLLECTED = "points_collected";
    public static final String TOPICS_CREATED = "topics_created";
    public static final String CURRENT_TOPICS = "current_topics";
    public static final String SERVER_ID = "server_id";
    public static final String AGE = "age";

    public static ProfileUtils getInstance() {
        return profileUtils;
    }

    public static void initialize(Context context) {

        if (profileUtils == null) {
            profileUtils = new ProfileUtils(context);
        }
    }

    private ProfileUtils(Context context) {
        mContext = context;
    }

    private SharedPreferences getPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = mContext.getApplicationContext().getSharedPreferences(
                    "Flur_Profile", Context.MODE_PRIVATE);
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