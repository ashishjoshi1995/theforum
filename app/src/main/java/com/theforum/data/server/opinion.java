package com.theforum.data.server;

/**
 * @author  Ashish on 12/31/2015.
 */
public class opinion {

    @com.google.gson.annotations.SerializedName("id")
    private String serverId;

    @com.google.gson.annotations.SerializedName("downvotes")
    private int downVotes;

    @com.google.gson.annotations.SerializedName("upvote_ids")
    private String upVoted_ids;

    @com.google.gson.annotations.SerializedName("downvote_ids")
    private String downVotes_ids;

    @com.google.gson.annotations.SerializedName("upvotes")
    private int upVotes;

    @com.google.gson.annotations.SerializedName("opinion")
    private String opinionName;

    @com.google.gson.annotations.SerializedName("opinion_id")
    private String opinionId;

    @com.google.gson.annotations.SerializedName("uid")
    private String userId;

    @com.google.gson.annotations.SerializedName("topic_id")
    private String topicId;

    @com.google.gson.annotations.SerializedName("notif_count")
    private int mNotifCount;

    @com.google.gson.annotations.SerializedName("notif_downvotes")
    private int mNotifNewDownvotes;

    @com.google.gson.annotations.SerializedName("notif_upvotes")
    private int mNotifNewUpvotes;

    @com.google.gson.annotations.SerializedName("topic")
    private String topicName;


    public opinion(){}
    
    public opinion(String opinionName){
        this.opinionName = opinionName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpinionId() {
        return opinionId;
    }

    public void setOpinionId(String opinionId) {
        this.opinionId = opinionId;
    }

    public String getOpinionName() {
        return opinionName;
    }

    public void setOpinionName(String opinionName) {
        this.opinionName = opinionName;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

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

    public int getmNotifCount() {
        return mNotifCount;
    }

    public void setmNotifCount(int mNotifCount) {
        this.mNotifCount = mNotifCount;
    }

    public int getmNotifNewDownvotes() {
        return mNotifNewDownvotes;
    }

    public void setmNotifNewDownvotes(int mNotifNewDownvotes) {
        this.mNotifNewDownvotes = mNotifNewDownvotes;
    }

    public int getmNotifNewUpvotes() {
        return mNotifNewUpvotes;
    }

    public void setmNotifNewUpvotes(int mNotifNewUpvotes) {
        this.mNotifNewUpvotes = mNotifNewUpvotes;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }


    public String getDownVotes_ids() {
        return downVotes_ids;
    }

    public void setDownVotes_ids(String downVotes_ids) {
        this.downVotes_ids = downVotes_ids;
    }

    public String getUpVoted_ids() {
        return upVoted_ids;
    }

    public void setUpVoted_ids(String upVoted_ids) {
        this.upVoted_ids = upVoted_ids;
    }
}
