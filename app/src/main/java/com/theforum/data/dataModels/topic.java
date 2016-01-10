package com.theforum.data.dataModels;

import java.io.Serializable;

/**
 * @author Ashish on 12/31/2015.
 */

public class topic implements Serializable{
    @com.google.gson.annotations.SerializedName("id")
    private String serverId;

    @com.google.gson.annotations.SerializedName("opinion_ids")
    private String opinionIds;

    @com.google.gson.annotations.SerializedName("renewal_requests")
    private int renewalRequests;

    @com.google.gson.annotations.SerializedName("renewed_count")
    private int renewedCount;

    @com.google.gson.annotations.SerializedName("topic_id")
    private String topicId;

    @com.google.gson.annotations.SerializedName("total_opinions")
    private int totalOpinions;

    @com.google.gson.annotations.SerializedName("description")
    private String topicDescription;

    @com.google.gson.annotations.SerializedName("uid")
    private String userId;

    @com.google.gson.annotations.SerializedName("topic")
    private String mTopic;

    @com.google.gson.annotations.SerializedName("notif_new_opinions")
    private int mNotifOpinions;

    @com.google.gson.annotations.SerializedName("notif_new_renewal_request")
    private int mNotifRenewalRequests;

    @com.google.gson.annotations.SerializedName("hours_left")
    private int mHoursLeft;

    @com.google.gson.annotations.SerializedName("points")
    private int mPoints;


    private boolean isRenewed = false;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getOpinionIds() {
        return opinionIds;
    }

    public void setOpinionIds(String opinionIds) {
        this.opinionIds = opinionIds;
    }

    public int getRenewalRequests() {
        return renewalRequests;
    }

    public void setRenewalRequests(int renewalRequests) {
        this.renewalRequests = renewalRequests;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getTotalOpinions() {
        return totalOpinions;
    }

    public void setTotalOpinions(int totalOpinions) {
        this.totalOpinions = totalOpinions;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getmTopic() {
        return mTopic;
    }

    public void setmTopic(String mTopic) {
        this.mTopic = mTopic;
    }

    public int getmNotifOpinions() {
        return mNotifOpinions;
    }

    public void setmNotifOpinions(int mNotifOpinionIds) {
        this.mNotifOpinions = mNotifOpinionIds;
    }

    public int getmNotifRenewalRequests() {
        return mNotifRenewalRequests;
    }

    public void setmNotifRenewalRequests(int mNotifRenewalRequests) {
        this.mNotifRenewalRequests = mNotifRenewalRequests;
    }

    public int getmHoursLeft() {
        return mHoursLeft;
    }

    public void setmHoursLeft(int mHoursLeft) {
        this.mHoursLeft = mHoursLeft;
    }


    public int getRenewedCount() {
        return renewedCount;
    }

    public void setRenewedCount(int renewedCount) {
        this.renewedCount = renewedCount;
    }

    public int getmPoints() {
        return mPoints;
    }

    public void setmPoints(int mPoints) {
        this.mPoints = mPoints;
    }


    public boolean getIsRenewed() {
        return isRenewed;
    }

    public void setIsRenewed(boolean isRenewed) {
        this.isRenewed = isRenewed;
    }

}
