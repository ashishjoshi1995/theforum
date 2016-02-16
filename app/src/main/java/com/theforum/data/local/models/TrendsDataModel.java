package com.theforum.data.local.models;

import com.theforum.data.server.opinion;
import com.theforum.utils.enums.VoteStatus;

/**
 * @author DEEPANKAR
 * @since 19-01-2016.
 */
public class TrendsDataModel {

    private String uId;
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
    private double latitude;
    private double longitude;
    private VoteStatus voteStatus = VoteStatus.NONE;
    private String serverId;
    private boolean isLocal = false;

    public TrendsDataModel(){}

    public TrendsDataModel(opinion opinion){
        this.uId = opinion.getUserId();
        this.trendId = opinion.getOpinionId();
        this.topicId = opinion.getTopicId();
        this.topicName = opinion.getTopicName();
        this.opinionText = opinion.getOpinionName();
        this.downVoteCount = opinion.getDownVotes();
        this.upVoteCount = opinion.getUpVotes();
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }
}
