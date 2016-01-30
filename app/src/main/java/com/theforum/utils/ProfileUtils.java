package com.theforum.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author  Ashish on 1/2/2016.
 */
public class ProfileUtils {
    private Context mContext;
    private static ProfileUtils profileUtils;
    private SharedPreferences sharedPreferences;

    /**
     * Key Values to store in SharedPreferences
     */

    public static final String USER_ID = "forum_id";
    public static final String STATUS = "status";
    public static final String POINTS_COLLECTED = "points_collected";
    public static final String TOPICS_CREATED = "topics_created";
    public static final String CURRENT_TOPICS = "current_topics";
    public static final String SERVER_ID = "server_id";
    public static final String AGE = "age";
    public static final String COUNTRY = "country";

    //statistics
    public static final String mRecUpvotes = "rupvotes";
    public static final String mRecDownvoted = "rdownvotes";
    public static final String mRecOpinions = "ropinions";
    public static final String mRecRenewals = "rrenewals";
    public static final String mRecTopicsRenewed = "rtopicsrenewed";

    public static final String mCrcUpvotes = "cupvotes";
    public static final String mCrcDownvotes = "cdownvotes";
    public static final String mCrcOpinions = "copinions";
    public static final String mCrcRenewals = "crenewals";
    public static final String mCrcTopicsRenewed = "ctopicsrenewed";

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
                    "theforum_profile", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void saveIntegralPreference(String key, int value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getIntPreference(String key){
        int value = getPreferences().getInt(key, -1);

        if (value == -1) {
            value = 0;
        }

        return value;
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