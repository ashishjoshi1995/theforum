package com.theforum.data.local.models;

import com.theforum.constants.NotificationType;
import com.theforum.utils.enums.VoteStatus;

/**
 * @author Ashish
 * @since 1/6/2016.
 */

public class NotificationDataModel {

    /*
     * type of notification, hours left to delete and topic
     */
    public int notificationType = NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES;
    public String topicId;
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
    public int hoursLeft;


   //notification of opinions

    public int upVoteCount;
    public int downVoteCount;
    public String description;
    public VoteStatus voteStatus;

    //notification of renewal request
    public boolean ifRenewed;
    public int renewalRequest;

}
