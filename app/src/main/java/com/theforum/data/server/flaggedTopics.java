package com.theforum.data.server;

/**
 * @author Ashish
 * @since 2/16/2016
 */
public class flaggedTopics {
    @com.google.gson.annotations.SerializedName("id")
    private String serverId;

    @com.google.gson.annotations.SerializedName("flag_count")
    private int flagCount ;

    @com.google.gson.annotations.SerializedName("topic_id")
    private String topicId;

    @com.google.gson.annotations.SerializedName("topic_text")
    private String topicText;

    @com.google.gson.annotations.SerializedName("topic_description")
    private String topicDescription;

    @com.google.gson.annotations.SerializedName("apnd_flgger_ids")
    private String apndFlaggerIds;


    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicText() {
        return topicText;
    }

    public void setTopicText(String topicText) {
        this.topicText = topicText;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public String getApndFlaggerIds() {
        return apndFlaggerIds;
    }

    public void setApndFlaggerIds(String apndFlaggerIds) {
        this.apndFlaggerIds = apndFlaggerIds;
    }
}
