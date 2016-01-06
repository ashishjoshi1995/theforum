package com.theforum.data.local;

/**
 * Created by Ashish on 1/6/2016.
 */
public class NotificationInflatorItemData {
    //hrs left, topicname,

    public int notificationType;
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
