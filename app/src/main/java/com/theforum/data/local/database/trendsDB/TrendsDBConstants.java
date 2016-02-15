package com.theforum.data.local.database.trendsDB;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TrendsDBConstants {
    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 3;

    // Database Name
    public static final String DATABASE_NAME = "TRENDS_DB";
    public static final String TABLE_NAME = "TABLE_TRENDS";

    //version 1 coloumns
    public static final String KEY_LOCAL_ID = "local_id";
    public static final String KEY_SERVER_ID="id";
    public static final String KEY_TREND_ID ="opinion_id";
    public static final String KEY_TOPIC_ID="topic_id";
    public static final String KEY_TOPIC="topic";
    public static final String KEY_OPINION="opinion";
    public static final String KEY_UPVOTES="upvotes";
    public static final String KEY_DOWNVOTES="downvotes";
    public static final String KEY_HOURS_LEFT = "time";

    //version 2 coloumns
    public static final String KEY_VOTE_STATUS="vote_status";

    //version 3 columns
    public static final String KEY_UID = "uid";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LOCAL_TOPIC = "is_local_topic";

}
