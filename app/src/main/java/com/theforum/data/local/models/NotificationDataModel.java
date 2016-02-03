package com.theforum.data.local.models;

/**
 * @author Ashish
 * @since 1/6/2016.
 */

/**
 * data models for all type of notification
 *
 * Opinion UpVoted/DownVoted
 * Renewal Requests
 * Renewal Days
 * Opinions Added
 */
public class NotificationDataModel {

    /**
     * type of notification
     */
    public int notificationType;

    /**
     * topic Id of the notification
     */
    public String topicId;

    /**
     * name of the topic
     */
    public String topicText;

    /**
     * contains count of total notifications in each type of notification
     *
     * Opinion UpVoted/DownVoted - number of upVotes or downVotes
     * Renewal Requests          - number of renewal requests
     * Renewal Days              - number of days for renewal
     * Opinions Added            - number of new opinions added
     */
    public int notificationCount;

    /**
     * contains additional description if any
     *
     * Opinion UpVoted/DownVoted - opinion text
     * Renewal Days              - number of renewal requests
     */
    public String description;

    /**
     * holds hours left for topic to decay and local time when notification was received
     */
    public String timeHolder;

    /**
     * whether the notification is seen/read by user or not
     */
    public boolean isRead;

}
