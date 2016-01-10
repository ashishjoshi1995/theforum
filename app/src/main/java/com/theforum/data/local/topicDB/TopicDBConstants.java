package com.theforum.data.local.topicDB;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TopicDBConstants {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TOPIC_DB";

    private static final String TABLE_NAME = "TOPICS_TABLE_FEED";
    private static final String MY_DATA_TABLE = "MY_ADDED_TOPICS";


    //statistics table coloumn
    private static final String KEY_ID = "id";
    private static final String KEY_SERVER_ID="server_id";
    private static final String KEY_TOPIC_ID="topic_id";
    private static final String KEY_RENEWAL_REQUEST="renewal_request";
    private static final String KEY_TOTAL_OPINIONS="total_opinions";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_TOPIC="topic";
    private static final String KEY_TIME = "time";
}
