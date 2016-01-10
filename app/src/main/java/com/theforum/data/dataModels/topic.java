package com.theforum.data.dataModels;

import java.io.Serializable;

/**
 * @author Ashish on 12/31/2015.
 */

public class topic implements Serializable{
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("opinion_ids")
    private String mOpinionIds;

    @com.google.gson.annotations.SerializedName("renewal_requests")
    private int mRenewalRequests;

    @com.google.gson.annotations.SerializedName("renewed_count")
    private int mRenewedCount;

    @com.google.gson.annotations.SerializedName("topic_id")
    private String mTopicId;

    @com.google.gson.annotations.SerializedName("total_opinions")
    private int mTotalOpinions;

    @com.google.gson.annotations.SerializedName("description")
    private String mDescription;

    @com.google.gson.annotations.SerializedName("uid")
    private String mUid;

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

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmOpinionIds() {
        return mOpinionIds;
    }

    public void setmOpinionIds(String mOpinionIds) {
        this.mOpinionIds = mOpinionIds;
    }

    public int getmRenewalRequests() {
        return mRenewalRequests;
    }

    public void setmRenewalRequests(int mRenewalRequests) {
        this.mRenewalRequests = mRenewalRequests;
    }

    public String getmTopicId() {
        return mTopicId;
    }

    public void setmTopicId(String mTopicId) {
        this.mTopicId = mTopicId;
    }

    public int getmTotalOpinions() {
        return mTotalOpinions;
    }

    public void setmTotalOpinions(int mTotalOpinions) {
        this.mTotalOpinions = mTotalOpinions;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
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


    public int getmRenewedCount() {
        return mRenewedCount;
    }

    public void setmRenewedCount(int mRenewedCount) {
        this.mRenewedCount = mRenewedCount;
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
