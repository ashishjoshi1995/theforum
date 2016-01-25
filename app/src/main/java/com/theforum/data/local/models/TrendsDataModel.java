package com.theforum.data.local.models;

import com.theforum.data.server.opinion;
import com.theforum.utils.enums.VoteStatus;

/**
 * @author DEEPANKAR
 * @since 19-01-2016.
 */
public class TrendsDataModel {

    private String serverId;
    private String trendId;
    private String topicId;
    private String topicName;
    private int hoursLeft;
    private int renewCount;
    private String opinionText;
    private String description;
    private String renewalIds;
    private int downVoteCount;
    private int upVoteCount;
    private VoteStatus voteStatus = VoteStatus.NONE;
    public TrendsDataModel(){}

    public TrendsDataModel(opinion opinion){
        this.serverId = opinion.getServerId();
        this.trendId = opinion.getOpinionId();
        this.topicId = opinion.getTopicId();
        this.topicName = opinion.getTopicName();
        this.opinionText = opinion.getOpinionName();
        this.downVoteCount = opinion.getDownVotes();
        this.upVoteCount = opinion.getUpVotes();
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getTrendId() {
        return trendId;
    }

    public void setTrendId(String trendId) {
        this.trendId = trendId;
    }

    public String getOpinionText() {
        return opinionText;
    }

    public void setOpinionText(String opinionText) {
        this.opinionText = opinionText;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(int downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getHoursLeft() {
        return hoursLeft;
    }

    public void setHoursLeft(int hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public int getRenewCount() {
        return renewCount;
    }

    public void setRenewCount(int renewCount) {
        this.renewCount = renewCount;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public VoteStatus getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(VoteStatus voteStatus) {
        this.voteStatus = voteStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRenewalIds() {
        return renewalIds;
    }

    public void setRenewalIds(String renewalIds) {
        this.renewalIds = renewalIds;
    }
}
