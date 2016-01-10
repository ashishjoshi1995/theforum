package com.theforum.data.local.topicDB;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TopicDBConstants {
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "TOPIC_DB";

    public static final String TABLE_NAME = "TOPICS_TABLE_FEED";
    public static final String MY_DATA_TABLE = "MY_ADDED_TOPICS";


    //statistics table coloumn
    public static final String KEY_ID = "id";
    public static final String KEY_SERVER_ID="server_id";
    public static final String KEY_TOPIC_ID="topic_id";
    public static final String KEY_RENEWAL_REQUEST="renewal_request";
    public static final String KEY_TOTAL_OPINIONS="total_opinions";
    public static final String KEY_DESCRIPTION="description";
    public static final String KEY_TOPIC="topic";
    public static final String KEY_TIME = "time";
}
