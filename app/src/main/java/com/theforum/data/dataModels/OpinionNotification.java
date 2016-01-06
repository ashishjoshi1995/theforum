package com.theforum.data.dataModels;

/**
 * Created by Ashish on 1/6/2016.
 */
public class OpinionNotification {
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("count")
    private int mCount;

    @com.google.gson.annotations.SerializedName("newdownvotes")
    private int mNewDownvotes;

    @com.google.gson.annotations.SerializedName("newupvotes")
    private int mNewUpvotes;

    @com.google.gson.annotations.SerializedName("opinion")
    private String mOpinion;

    @com.google.gson.annotations.SerializedName("opinion_id")
    private String mOpinionId;

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

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public int getmNewDownvotes() {
        return mNewDownvotes;
    }

    public void setmNewDownvotes(int mNewDownvotes) {
        this.mNewDownvotes = mNewDownvotes;
    }

    public int getmNewUpvotes() {
        return mNewUpvotes;
    }

    public void setmNewUpvotes(int mNewUpvotes) {
        this.mNewUpvotes = mNewUpvotes;
    }

    public String getmOpinion() {
        return mOpinion;
    }

    public void setmOpinion(String mOpinion) {
        this.mOpinion = mOpinion;
    }

    public String getmOpinionId() {
        return mOpinionId;
    }

    public void setmOpinionId(String mOpinionId) {
        this.mOpinionId = mOpinionId;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }
}
