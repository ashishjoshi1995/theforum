package com.theforum.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.theforum.data.server.areatopics;
import com.theforum.data.server.topic;

/**
 * @author  Deepankar
 * @since  1/6/2016.
 */

public class TopicDataModel implements Parcelable{

    private String serverId;
    private String topicId;
    private String topicName;
    private String topicDescription;
    private int renewalRequests;
    private int renewedCount;
    private int hoursLeft;
    private boolean isRenewed;
    private boolean isMyTopic;
    private boolean isLocalTopic;
    private double latitude;
    private double longitude;
    private String uid;

    public TopicDataModel(){
        isRenewed = false;
        isMyTopic = false;
    }

    public TopicDataModel(topic topic){
        this.serverId = topic.getServerId();
        this.topicId = topic.getTopicId();
        this.topicName = topic.getTopicName();
        this.topicDescription = topic.getTopicDescription();
        this.renewalRequests = topic.getRenewalRequests();
        this.renewedCount = topic.getRenewedCount();
        this.hoursLeft = topic.getHoursLeft();

        this.uid= topic.getUserId();
        this.latitude=topic.getLatitude();
        this.longitude=topic.getLongitude();
    }

    public TopicDataModel(areatopics topic){
        this.serverId = topic.getServerId();
        this.topicId = topic.getTopicId();
        this.topicName = topic.getTopicName();
        this.topicDescription = topic.getTopicDescription();
        this.renewalRequests = topic.getRenewalRequests();
        this.renewedCount = topic.getRenewedCount();
        this.hoursLeft = topic.getHoursLeft();
        this.uid= topic.getUserId();
        this.latitude=topic.getLatitude();
        this.longitude=topic.getLongitude();

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

    protected TopicDataModel(Parcel in) {
        serverId = in.readString();
        topicId = in.readString();
        topicName = in.readString();
        topicDescription = in.readString();
        renewalRequests = in.readInt();
        renewedCount = in.readInt();
        hoursLeft = in.readInt();

        latitude=in.readDouble();
        longitude=in.readDouble();
        uid=in.readString();

        isRenewed = in.readByte() != 0x00;
        isMyTopic = in.readByte() != 0x00;
        isLocalTopic = (in.readByte() != 0x00 ? true : false);
        Log.e("sdsfread","is"+isLocalTopic);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serverId);
        dest.writeString(topicId);
        dest.writeString(topicName);
        dest.writeString(topicDescription);
        dest.writeInt(renewalRequests);
        dest.writeInt(renewedCount);
        dest.writeInt(hoursLeft);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(uid);
        dest.writeByte((byte) (isRenewed ? 0x01 : 0x00));
        dest.writeByte((byte) (isMyTopic ? 0x01 : 0x00));
        dest.writeByte((byte) (isLocalTopic ? 0x01 : 0x00));
        Log.e("sdsfwrite","is"+isLocalTopic);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TopicDataModel> CREATOR = new Parcelable.Creator<TopicDataModel>() {
        @Override
        public TopicDataModel createFromParcel(Parcel in) {
            return new TopicDataModel(in);
        }

        @Override
        public TopicDataModel[] newArray(int size) {
            return new TopicDataModel[size];
        }
    };

    public boolean isLocalTopic() {
        return isLocalTopic;
    }

    public void setIsLocalTopic(boolean isLocalTopic) {
        this.isLocalTopic = isLocalTopic;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
