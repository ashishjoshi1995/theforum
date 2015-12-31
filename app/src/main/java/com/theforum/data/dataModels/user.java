package com.theforum.data.dataModels;

/**
 * Created by Ashish on 12/31/2015.
 */
public class user {

    @com.google.gson.annotations.SerializedName("uid")
    private String mUid;

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("current_topics")
    private String mCurrentTopics;

    @com.google.gson.annotations.SerializedName("points_collected")
    private int mPointCollected;

    @com.google.gson.annotations.SerializedName("status")
    private String mStatus;

    @com.google.gson.annotations.SerializedName("topics_created")
    private int mTopicsCreated;

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmCurrentTopics() {
        return mCurrentTopics;
    }

    public void setmCurrentTopics(String mCurrentTopics) {
        this.mCurrentTopics = mCurrentTopics;
    }

    public int getmPointCollected() {
        return mPointCollected;
    }

    public void setmPointCollected(int mPointCollected) {
        this.mPointCollected = mPointCollected;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public int getmTopicsCreated() {
        return mTopicsCreated;
    }

    public void setmTopicsCreated(int mTopicsCreated) {
        this.mTopicsCreated = mTopicsCreated;
    }
}
