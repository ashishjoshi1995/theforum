package com.theforum.data.local.database.topicDB;

/**
 * @author Ashish
 * @since 1/11/2016
 */
public class TopicDBConstants {

    // Database version
    public static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "TOPIC_DATABASE";

    // Table name
    public static final String TABLE_NAME = "TOPICS_TABLE";


    // table one column keys version 1
    public static final String KEY_ID = "id";
    public static final String KEY_SERVER_ID="server_id";
    public static final String KEY_TOPIC_ID="topic_id";
    public static final String KEY_TOPIC="topic";
    public static final String KEY_DESCRIPTION="description";
    public static final String KEY_RENEWAL_REQUEST="renewal_request";
    public static final String KEY_IS_RENEWED="is_renewed";
    public static final String KEY_RENEWED_COUNT="renewed_count";
    public static final String KEY_HOURS_LEFT = "hours_left";
    public static final String KEY_MY_TOPIC = "my_topic";


    //version 2 coloums
    public static final String KEY_LOCAL_TOPIC = "is_local_topic";

}
