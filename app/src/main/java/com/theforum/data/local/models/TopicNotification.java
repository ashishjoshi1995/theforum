package com.theforum.data.local.models;

/**
 * Created by Ashish on 1/6/2016.
 */
public class TopicNotification {
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("new_opinions")
    private int mOpinionIds;

    @com.google.gson.annotations.SerializedName("new_renewal_request")
    private int mRenewalRequests;

    @com.google.gson.annotations.SerializedName("topic_id")
    private String mTopicId;


    @com.google.gson.annotations.SerializedName("uid")
    private String mUid;

    @com.google.gson.annotations.SerializedName("topic")
    private String mTopic;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public int getmOpinionIds() {
        return mOpinionIds;
    }

    public void setmOpinionIds(int mOpinionIds) {
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
}
