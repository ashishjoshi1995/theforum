package com.theforum;

import com.theforum.utils.ProfileUtils;

/**
 * @author Ashish on 1/2/2016.
 */
public class User {
    public static User mUser;
    ProfileUtils profileUtils;

    private String mUid;
    private String mCurrentTopics;
    private String mStatus;
    private String mServerId;
    
    private int mTopicsCreated = -1;
    private int mPointCollected = -1;
    private int mAge = -1;


    public static User getInstance(){
        if(mUser==null) mUser = new User();
        return mUser;
    }

    private User(){
        profileUtils = ProfileUtils.getInstance();
    }

    public String getId() {
        if(mUid==null) mUid = profileUtils.getFromPreferences(ProfileUtils.FORUM_ID);
        return mUid;
    }

    public void setId(String id){
        profileUtils.savePreferences(ProfileUtils.FORUM_ID,id);
        mUid = id;
    }

    public String getCurrentTopics(){
        if(mCurrentTopics==null) mCurrentTopics = profileUtils.getFromPreferences(ProfileUtils.CURRENT_TOPICS);
        return mCurrentTopics;
    }

    public void setCurrentTopics(String currentTopics){
        profileUtils.savePreferences(ProfileUtils.CURRENT_TOPICS,currentTopics);
        mCurrentTopics =currentTopics ;
    }


    public int getPointCollected() {
        if(mPointCollected==-1) mPointCollected = Integer.parseInt(profileUtils.getFromPreferences(
                ProfileUtils.POINTS_COLLECTED));
        return mPointCollected;
    }

    public void setPointCollected(int points){
        profileUtils.savePreferences(ProfileUtils.POINTS_COLLECTED,String.valueOf(points));
        mPointCollected = points;
    }

    public int getTopicsCreated() {
        if(mTopicsCreated==-1)mTopicsCreated = Integer.parseInt(profileUtils.getFromPreferences(
                ProfileUtils.TOPICS_CREATED));
        return mTopicsCreated;
    }

    public void setTopicsCreated(int topicsCreated){
        profileUtils.savePreferences(ProfileUtils.TOPICS_CREATED,String.valueOf(topicsCreated));
        mPointCollected = topicsCreated;
    }

    public String getStatus() {
        if(mStatus==null) mStatus = profileUtils.getFromPreferences(ProfileUtils.STATUS);
        return mStatus;
    }

    public void setStatus(String status){
        profileUtils.savePreferences(ProfileUtils.STATUS,status);
        mStatus = status;
    }

    public String getServerId() {
        if(mServerId==null)mServerId = profileUtils.getFromPreferences(ProfileUtils.SERVER_ID);
        return mServerId;
    }

    public void setServerId(String serverId){
        profileUtils.savePreferences(ProfileUtils.SERVER_ID,serverId);
        mServerId = serverId;
    }

    public int getAge() {
        if(mAge == -1)mAge = Integer.parseInt(profileUtils.getFromPreferences(ProfileUtils.AGE));
        return mAge;
    }

    public void setAge(int age){
        profileUtils.savePreferences(ProfileUtils.AGE, String.valueOf(age));
        mAge = age;
    }


}
