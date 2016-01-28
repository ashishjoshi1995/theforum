package com.theforum.data.server;

/**
 * @author Ashish on 12/31/2015.
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

    @com.google.gson.annotations.SerializedName("total_characters")
    private int mTotalCharacters;

    @com.google.gson.annotations.SerializedName("status")
    private String mStatus;

    @com.google.gson.annotations.SerializedName("opinions_count")
    private String mOpinionCount;

    @com.google.gson.annotations.SerializedName("topics_created")
    private int mTopicsCreated;

    @com.google.gson.annotations.SerializedName("age")
    private int age;

    @com.google.gson.annotations.SerializedName("country")
    private String country;

    @com.google.gson.annotations.SerializedName("total_downvotes_on_opinions")
    private int totalDownvotesOnOpinionsTillDate;

    @com.google.gson.annotations.SerializedName("total_renewalrequests_on_topics")
    private int totalRenewalRequestsOnTopics;

    @com.google.gson.annotations.SerializedName("total_renewals_received_on_topics")
    private int totalRenewalsReceivedOnTopics;

    @com.google.gson.annotations.SerializedName("total_upvotes_on_opinions")
    private int totalUpvotesOnOpinions;



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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public int getTotalDownvotesOnOpinionsTillDate() {
        return totalDownvotesOnOpinionsTillDate;
    }

    public void setTotalDownvotesOnOpinionsTillDate(int totalDownvotesOnOpinionsTillDate) {
        this.totalDownvotesOnOpinionsTillDate = totalDownvotesOnOpinionsTillDate;
    }

    public int getTotalRenewalRequestsOnTopics() {
        return totalRenewalRequestsOnTopics;
    }

    public void setTotalRenewalRequestsOnTopics(int totalRenewalRequestsOnTopics) {
        this.totalRenewalRequestsOnTopics = totalRenewalRequestsOnTopics;
    }

    public int getTotalRenewalsReceivedOnTopics() {
        return totalRenewalsReceivedOnTopics;
    }

    public void setTotalRenewalsReceivedOnTopics(int totalRenewalsReceivedOnTopics) {
        this.totalRenewalsReceivedOnTopics = totalRenewalsReceivedOnTopics;
    }

    public int getTotalUpvotesOnOpinions() {
        return totalUpvotesOnOpinions;
    }

    public void setTotalUpvotesOnOpinions(int totalUpvotesOnOpinions) {
        this.totalUpvotesOnOpinions = totalUpvotesOnOpinions;
    }

    public int getmTotalCharacters() {
        return mTotalCharacters;
    }

    public void setmTotalCharacters(int mTotalCharacters) {
        this.mTotalCharacters = mTotalCharacters;
    }

    public String getmOpinionCount() {
        return mOpinionCount;
    }

    public void setmOpinionCount(String mOpinionCount) {
        this.mOpinionCount = mOpinionCount;
    }
}
