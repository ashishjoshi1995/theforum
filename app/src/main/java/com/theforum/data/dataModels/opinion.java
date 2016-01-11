package com.theforum.data.dataModels;

/**
 * @author  Ashish on 12/31/2015.
 */
public class opinion {
    @com.google.gson.annotations.SerializedName("id")
    private String serverId;

    @com.google.gson.annotations.SerializedName("downvotes")
    private int downVotes;

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

    @com.google.gson.annotations.SerializedName("notif_newdownvotes")
    private int mNotifNewDownvotes;

    @com.google.gson.annotations.SerializedName("notif_newupvotes")
    private int mNotifNewUpvotes;

    @com.google.gson.annotations.SerializedName("topic")
    private String mTopic;

    private boolean isUpVoted;

    private boolean isDownVoted;

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

    public boolean isUpVoted() {
        return isUpVoted;
    }

    public void setUpVoted(boolean upVoted) {
        this.isUpVoted = upVoted;
    }

    public boolean isDownVoted() {
        return isDownVoted;
    }

    public void setDownVoted(boolean downVoted) {
        this.isDownVoted = downVoted;
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

    public String getmTopic() {
        return mTopic;
    }

    public void setmTopic(String mTopic) {
        this.mTopic = mTopic;
    }

}
