package com.theforum.data.local.models;

/**
 * @author Ashish
 * @since 1/15/2016
 */
public class NotificationDataModel {
    public NotificationDataModel(){
    }

    private int key;
    private int notificationType;
    private int viewType;
    private String timeHolder;
    private String mainText;
    private String header;
    private String description;
    private String topicId;

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

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
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
}
