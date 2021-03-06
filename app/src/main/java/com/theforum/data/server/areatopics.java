package com.theforum.data.server;

/**
 * @author Ashish
 * @since 2/11/2016
 */
public class areatopics {
    @com.google.gson.annotations.SerializedName("id")
    private String serverId;

    @com.google.gson.annotations.SerializedName("topic_id")
    private String topicId;

    @com.google.gson.annotations.SerializedName("opinion_ids")
    private String opinionIds;

    @com.google.gson.annotations.SerializedName("renewal_requests")
    private int renewalRequests;

    @com.google.gson.annotations.SerializedName("renewal_request_ids")
    private String renewalRequestIds;

    @com.google.gson.annotations.SerializedName("renewed_count")
    private int renewedCount;

    @com.google.gson.annotations.SerializedName("total_opinions")
    private int totalOpinions;

    @com.google.gson.annotations.SerializedName("description")
    private String topicDescription;

    @com.google.gson.annotations.SerializedName("uid")
    private String userId;

    @com.google.gson.annotations.SerializedName("topic")
    private String topicName;

    @com.google.gson.annotations.SerializedName("hours_left")
    private int hoursLeft;

    @com.google.gson.annotations.SerializedName("notif_new_opinions")
    private int notifOpinions;

    @com.google.gson.annotations.SerializedName("notif_new_renewal_request")
    private int notifRenewalRequests;

    @com.google.gson.annotations.SerializedName("points")
    private int mPoints;

    @com.google.gson.annotations.SerializedName("notif_count")
    private int notificationCount;

    @com.google.gson.annotations.SerializedName("latitude")
    private double latitude;

    @com.google.gson.annotations.SerializedName("longitude")
    private double longitude;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
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

    public String getRenewalRequestIds() {
        return renewalRequestIds;
    }

    public void setRenewalRequestIds(String renewalRequestIds) {
        this.renewalRequestIds = renewalRequestIds;
    }

    public int getRenewedCount() {
        return renewedCount;
    }

    public void setRenewedCount(int renewedCount) {
        this.renewedCount = renewedCount;
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getHoursLeft() {
        return hoursLeft;
    }

    public void setHoursLeft(int hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public int getNotifOpinions() {
        return notifOpinions;
    }

    public void setNotifOpinions(int notifOpinions) {
        this.notifOpinions = notifOpinions;
    }

    public int getNotifRenewalRequests() {
        return notifRenewalRequests;
    }

    public void setNotifRenewalRequests(int notifRenewalRequests) {
        this.notifRenewalRequests = notifRenewalRequests;
    }

    public int getmPoints() {
        return mPoints;
    }

    public void setmPoints(int mPoints) {
        this.mPoints = mPoints;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
