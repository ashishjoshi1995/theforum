package com.theforum.data.dataModels;

import com.theforum.Constants;

/**
 * @author Ashish
 * @since 1/6/2016.
 */

public class NotificationDataModel {

/*
 * type of notification, hours left to delete and topic
 */
    public int notificationType = Constants.NOTIFICATION_TYPE_OPINION_UP_VOTES;
    public int hoursLeft;
    public String topicText;

//notification of upvotes
    public int newCount;
    public int totalUpvotes;
    public int totalDownvotes;
    public String opinionText;
    public boolean ifUpvoted;
    public boolean ifDownvoted;
//break
    public boolean ifRenewalRequested;
    public String topicId;

//notification of renewal request
    public int renewalRequest;

//notification of opinions
    public int opinions;

//notification of renewal
    public int renewedCount;
    public int totalRenewalRequest;
//break


}
