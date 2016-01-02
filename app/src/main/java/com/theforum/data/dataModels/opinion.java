package com.theforum.data.dataModels;

/**
 * Created by Ashish on 12/31/2015.
 */
public class opinion {
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("downvotes")
    private int mDownVotes;

    @com.google.gson.annotations.SerializedName("upvotes")
    private int mUpVotes;

    @com.google.gson.annotations.SerializedName("opinion")
    private String mOpinion;

    @com.google.gson.annotations.SerializedName("opinion_id")
    private String mOpinionId;

    @com.google.gson.annotations.SerializedName("uid")
    private String mUid;

    @com.google.gson.annotations.SerializedName("topic_id")
    private String mTopicId;

    private boolean upvoted;
    private boolean downvoted;

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmOpinionId() {
        return mOpinionId;
    }

    public void setmOpinionId(String mOpinionId) {
        this.mOpinionId = mOpinionId;
    }

    public String getmOpinion() {
        return mOpinion;
    }

    public void setmOpinion(String mOpinion) {
        this.mOpinion = mOpinion;
    }

    public int getmUpVotes() {
        return mUpVotes;
    }

    public void setmUpVotes(int mUpVotes) {
        this.mUpVotes = mUpVotes;
    }

    public int getmDownVotes() {
        return mDownVotes;
    }

    public void setmDownVotes(int mDownVotes) {
        this.mDownVotes = mDownVotes;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTopicId() {
        return mTopicId;
    }

    public void setmTopicId(String mTopicId) {
        this.mTopicId = mTopicId;
    }

    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }

    public boolean isDownvoted() {
        return downvoted;
    }

    public void setDownvoted(boolean downvoted) {
        this.downvoted = downvoted;
    }
}
