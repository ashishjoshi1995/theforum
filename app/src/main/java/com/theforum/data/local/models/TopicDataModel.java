package com.theforum.data.local.models;

import com.theforum.data.server.topic;
import java.io.Serializable;

/**
 * @author  Deepankar
 * @since  1/6/2016.
 */

public class TopicDataModel implements Serializable{

    private String serverId;
    private String topicId;
    private String topicName;
    private String topicDescription;
    private int renewalRequests;
    private int renewedCount;
    private int hoursLeft;
    private boolean isRenewed;
    private boolean isMyTopic;

    public TopicDataModel(){}

    public TopicDataModel(topic topic){
        this.serverId = topic.getServerId();
        this.topicId = topic.getTopicId();
        this.topicName = topic.getTopicName();
        this.topicDescription = topic.getTopicDescription();
        this.renewalRequests = topic.getRenewalRequests();
        this.renewedCount = topic.getRenewedCount();
        this.hoursLeft = topic.getHoursLeft();

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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public int getRenewalRequests() {
        return renewalRequests;
    }

    public void setRenewalRequests(int renewalRequests) {
        this.renewalRequests = renewalRequests;
    }

    public int getRenewedCount() {
        return renewedCount;
    }

    public void setRenewedCount(int renewedCount) {
        this.renewedCount = renewedCount;
    }

    public int getHoursLeft() {
        return hoursLeft;
    }

    public void setHoursLeft(int hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public boolean isRenewed() {
        return isRenewed;
    }

    public void setIsRenewed(boolean isRenewed) {
        this.isRenewed = isRenewed;
    }

    public boolean isMyTopic() {
        return isMyTopic;
    }

    public void setIsMyTopic(boolean isMyTopic) {
        this.isMyTopic = isMyTopic;
    }
}
