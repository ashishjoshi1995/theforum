package com.theforum;

import com.theforum.data.localDB.ProfileUtils;

/**
 * Created by Ashish on 1/2/2016.
 */
public class User {
    public static User mUser;
    ProfileUtils profileUtils;

    private String mUid;
    private String mCurrentTopics;
    private int mPointCollected;
    private String mStatus;
    private int mTopicsCreated;
    private String mServerId;

    public static User getInstance(){
        if(mUser==null) mUser = new User();
        return mUser;
    }

    private User(){
        profileUtils = ProfileUtils.getInstance();
    }

    public String getForumId() {
        if(mUid==null) mUid = profileUtils.getFromPreferences(ProfileUtils.FORUM_ID);
        return mUid;
    }

    public String getmCurrentTopics(){
        if(mCurrentTopics==null) mCurrentTopics = profileUtils.getFromPreferences(ProfileUtils.CURRENT_TOPICS);
        return mCurrentTopics;
    }




    public int getmPointCollected() {
         mPointCollected = Integer.parseInt(profileUtils.getFromPreferences(ProfileUtils.POINTS_COLLECTED));
        return mPointCollected;
    }

    public int getmTopicsCreated() {
        mTopicsCreated = Integer.parseInt(profileUtils.getFromPreferences(ProfileUtils.TOPICS_CREATED));
        return mTopicsCreated;
    }

    public String getmStatus() {
        if(mStatus==null) mStatus = profileUtils.getFromPreferences(ProfileUtils.STATUS);
        return mStatus;
    }

    public String getmServerId() {
        if(mServerId==null)mServerId = profileUtils.getFromPreferences(ProfileUtils.SERVER_ID);
        return mServerId;
    }
}
