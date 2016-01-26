package com.theforum.data.local.models;

/**
 * @author Ashish
 * @since 1/15/2016
 */
public class NotificationDataModel {

    private int key;
    private int notificationType;
    private String timeHolder;
    private String mainText;
    private String header;
    private String description;
    private String topicId;
    private boolean isRead;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public String getTimeHolder() {
        return timeHolder;
    }

    public void setTimeHolder(String timeHolder) {
        this.timeHolder = timeHolder;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
