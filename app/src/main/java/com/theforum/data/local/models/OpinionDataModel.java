package com.theforum.data.local.models;

import com.theforum.utils.enums.VoteStatus;

import java.io.Serializable;

/**
 * @author  Deepankar on 1/6/2016.
 */
public class OpinionDataModel implements Serializable{

    private String serverId;

    private String opinionId;

    private String opinionText;

    private int downVoteCount;

    private int upVoteCount;

    private VoteStatus voteStatus = VoteStatus.NONE;


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
    
    public VoteStatus getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(VoteStatus voteStatus) {
        this.voteStatus = voteStatus;
    }
}
