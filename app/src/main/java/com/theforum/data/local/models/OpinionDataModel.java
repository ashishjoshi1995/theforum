package com.theforum.data.local.models;

/**
 * @author  Deepankar on 1/6/2016.
 */
public class OpinionDataModel {

    private String serverId;

    private String opinionId;

    private String opinionText;

    private int downVoteCount;

    private int upVoteCount;


    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getOpinionId() {
        return opinionId;
    }

    public void setOpinionId(String opinionId) {
        this.opinionId = opinionId;
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


}
