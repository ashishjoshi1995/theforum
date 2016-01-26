package com.theforum.data.local.database.notificationDB;

/**
 * @author Ashish
 * @since 1/15/2016
 */
public class NotificationDBConstants {
    // Database version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "NOTIFICATION_DATABASE";

    // Table name
    public static final String TABLE_NAME = "NOTIFICATION_TABLE";

    //Coloumn names
    public static final String KEY_ID = "id";
    public static final String KEY_TOPIC_ID = "topic_id";
    public static final String KEY_NOTIFICATION_TYPE = "notification_type";
    public static final String KEY_TIME_HOLDER = "time_holder_text";
    public static final String KEY_MAIN_TEXT = "main_text";
    public static final String KEY_HEADER = "header";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IS_READ = "is_read";
}
