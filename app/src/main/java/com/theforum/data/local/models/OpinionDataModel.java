package com.theforum.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.theforum.data.server.areaopinions;
import com.theforum.data.server.opinion;
import com.theforum.utils.enums.VoteStatus;

/**
 * @author  Deepankar on 1/6/2016.
 */
public class OpinionDataModel implements Parcelable{

    private String uId;
    private String opinionId;
    private String opinionText;
    private String topicName;
    private String topicId;
    private int downVoteCount;
    private int upVoteCount;
    private VoteStatus voteStatus = VoteStatus.NONE;
    private double latitude;
    private double longitude;
    private boolean isLocal;
    private String serverId;

    public OpinionDataModel(){}

    public OpinionDataModel(opinion opinion){

        this.uId = opinion.getUserId();
        this.opinionId = opinion.getOpinionId();
        this.opinionText = opinion.getOpinionName();
        this.topicName = opinion.getTopicName();
        this.topicId = opinion.getTopicId();
        this.upVoteCount = opinion.getUpVotes();
        this.downVoteCount = opinion.getDownVotes();
        this.longitude=opinion.getLongitude();
        this.latitude=opinion.getLatitude();
        this.isLocal=false;
        this.serverId = opinion.getServerId();
    }

    public OpinionDataModel(areaopinions opinion){
        this.uId = opinion.getUserId();
        this.opinionId = opinion.getOpinionId();
        this.opinionText = opinion.getOpinionName();
        this.topicName = opinion.getTopicName();
        this.topicId = opinion.getTopicId();
        this.upVoteCount = opinion.getUpVotes();
        this.longitude=opinion.getLongitude();
        this.latitude=opinion.getLatitude();
        this.downVoteCount = opinion.getDownVotes();
        this.isLocal=true;
        this.serverId = opinion.getServerId();
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public boolean isLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    protected OpinionDataModel(Parcel in) {
        uId = in.readString();
        opinionId = in.readString();
        opinionText = in.readString();
        topicName = in.readString();
        topicId = in.readString();
        downVoteCount = in.readInt();
        upVoteCount = in.readInt();
        voteStatus = (VoteStatus) in.readValue(VoteStatus.class.getClassLoader());
        latitude = in.readDouble();
        longitude = in.readDouble();
        isLocal = in.readByte() != 0x00;
        serverId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uId);
        dest.writeString(opinionId);
        dest.writeString(opinionText);
        dest.writeString(topicName);
        dest.writeString(topicId);
        dest.writeInt(downVoteCount);
        dest.writeInt(upVoteCount);
        dest.writeValue(voteStatus);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isLocal ? 0x01 : 0x00));
        dest.writeString(serverId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OpinionDataModel> CREATOR = new Parcelable.Creator<OpinionDataModel>() {
        @Override
        public OpinionDataModel createFromParcel(Parcel in) {
            return new OpinionDataModel(in);
        }

        @Override
        public OpinionDataModel[] newArray(int size) {
            return new OpinionDataModel[size];
        }
    };
    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
