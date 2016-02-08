package com.theforum.data.server;

/**
 * @author Ashish
 * @since 2/9/2016
 */
public class flags {
    @com.google.gson.annotations.SerializedName("id")
    private String serverId;

    @com.google.gson.annotations.SerializedName("flag_count")
    private int flagCount ;

    @com.google.gson.annotations.SerializedName("opinion_id")
    private String opinionId;

    @com.google.gson.annotations.SerializedName("opinion_text")
    private String opinionText;

    @com.google.gson.annotations.SerializedName("topic_id")
    private String topicId;

    @com.google.gson.annotations.SerializedName("apnd_uid")
    private String apndUidOfFlaggers;

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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getApndUidOfFlaggers() {
        return apndUidOfFlaggers;
    }

    public void setApndUidOfFlaggers(String apndUidOfFlaggers) {
        this.apndUidOfFlaggers = apndUidOfFlaggers;
    }
}
