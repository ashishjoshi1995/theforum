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
    private int mOpinionCount;

    @com.google.gson.annotations.SerializedName("topics_created")
    private int mTopicsCreated;

    @com.google.gson.annotations.SerializedName("age")
    private int age;

    @com.google.gson.annotations.SerializedName("country")
    private String country;

    @com.google.gson.annotations.SerializedName("upvotes_received")
    private int upvotesReceived;

    @com.google.gson.annotations.SerializedName("downvotes_croaked")
    private int downvotes_croaked;

    @com.google.gson.annotations.SerializedName("upvotes_croaked")
    private int upvotes_croaked;

    @com.google.gson.annotations.SerializedName("downvotes_received")
    private int downvotes_received;

    @com.google.gson.annotations.SerializedName("renewal_request_croaked")
    private int renewal_request_croaked;

    @com.google.gson.annotations.SerializedName("renewal_request_received")
    private int renewal_request_received;

    @com.google.gson.annotations.SerializedName("toatal_topic_renewed")
    private int toatal_topic_renewed;

    @com.google.gson.annotations.SerializedName("total_topic_renewed_croaked")
    private int total_topic_renewed_croaked;

    @com.google.gson.annotations.SerializedName("opinions_received")
    private int opinions_received;





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


    public int getmTotalCharacters() {
        return mTotalCharacters;
    }

    public void setmTotalCharacters(int mTotalCharacters) {
        this.mTotalCharacters = mTotalCharacters;
    }

    public int getmOpinionCount() {
        return mOpinionCount;
    }

    public void setmOpinionCount(int mOpinionCount) {
        this.mOpinionCount = mOpinionCount;
    }

    public int getUpvotesReceived() {
        return upvotesReceived;
    }

    public void setUpvotesReceived(int upvotesReceived) {
        this.upvotesReceived = upvotesReceived;
    }

    public int getDownvotes_croaked() {
        return downvotes_croaked;
    }

    public void setDownvotes_croaked(int downvotes_croaked) {
        this.downvotes_croaked = downvotes_croaked;
    }

    public int getUpvotes_croaked() {
        return upvotes_croaked;
    }

    public void setUpvotes_croaked(int upvotes_croaked) {
        this.upvotes_croaked = upvotes_croaked;
    }

    public int getDownvotes_received() {
        return downvotes_received;
    }

    public void setDownvotes_received(int downvotes_received) {
        this.downvotes_received = downvotes_received;
    }

    public int getRenewal_request_croaked() {
        return renewal_request_croaked;
    }

    public void setRenewal_request_croaked(int renewal_request_croaked) {
        this.renewal_request_croaked = renewal_request_croaked;
    }

    public int getRenewal_request_received() {
        return renewal_request_received;
    }

    public void setRenewal_request_received(int renewal_request_received) {
        this.renewal_request_received = renewal_request_received;
    }

    public int getToatal_topic_renewed() {
        return toatal_topic_renewed;
    }

    public void setToatal_topic_renewed(int toatal_topic_renewed) {
        this.toatal_topic_renewed = toatal_topic_renewed;
    }

    public int getTotal_topic_renewed_croaked() {
        return total_topic_renewed_croaked;
    }

    public void setTotal_topic_renewed_croaked(int total_topic_renewed_croaked) {
        this.total_topic_renewed_croaked = total_topic_renewed_croaked;
    }

    public int getOpinions_received() {
        return opinions_received;
    }

    public void setOpinions_received(int opinions_received) {
        this.opinions_received = opinions_received;
    }
}
