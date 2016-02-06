package com.theforum.data.helpers;

import android.os.AsyncTask;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.theforum.TheForumApplication;
import com.theforum.constants.Messages;
import com.theforum.data.server.user;
import com.theforum.utils.ProfileUtils;
import com.theforum.utils.User;
import com.theforum.utils.enums.RequestStatus;

/**
 * @author  Ashish on 12/31/2015.
 */

public class ProfileHelper {

    private MobileServiceTable<user> mUser;
    private String uid;
    public static ProfileHelper mHelper;
    private RequestStatus mStatus;
    private OnProfileLoadListener mOnProfileLoadListener;

    public static ProfileHelper getHelper(){
        if(mHelper == null) mHelper = new ProfileHelper();
        return mHelper;
    }

    private ProfileHelper(){
        this.uid = User.getInstance().getId();
        mUser = TheForumApplication.getClient().getTable(user.class);
        mStatus = RequestStatus.IDLE;
    }

    public void getProfile(OnProfileLoadListener loadListener){
        mOnProfileLoadListener = loadListener;

        if(mStatus == RequestStatus.COMPLETED){
            mOnProfileLoadListener.onCompleted();
            mStatus = RequestStatus.IDLE;
        }
    }

    public void loadProfile(){
        mStatus = RequestStatus.EXECUTING;
        AsyncTask<Void, user, user> task = new AsyncTask<Void, user, user>(){
            MobileServiceList<user> ash = null;
            @Override
            protected user doInBackground(Void... voids) {
                try {
                    ash = mUser.where().field("uid").eq(uid).execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(ash!= null && ash.size() >0) {
                    return ash.get(0);
                }else return null;

            }

            @Override
            protected void onPostExecute(user user) {
                super.onPostExecute(user);

                if(user!=null) {
                    mStatus = RequestStatus.COMPLETED;
                    User localUser = User.getInstance();
                    localUser.setStatus(user.getmStatus());
                    localUser.setPointCollected(user.getmPointCollected());
                    localUser.setTopicsCreated(user.getmTopicsCreated());

                    if(mOnProfileLoadListener!= null){
                        mOnProfileLoadListener.onCompleted();
                        mStatus = RequestStatus.IDLE;
                    }

                    saveData(user);

                }else {
                    mStatus = RequestStatus.IDLE;
                    if(mOnProfileLoadListener!= null){
                        mOnProfileLoadListener.onError(Messages.NO_NET_CONNECTION);
                    }
                }
            }

        };

        runAsyncTask(task);

    }

    private void saveData(user user){
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecDownvoted,
                user.getDownvotes_received());
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecOpinions,
                user.getOpinions_received());
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecRenewals,
                user.getRenewal_request_received());
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecTopicsRenewed,
                user.getToatal_topic_renewed());
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecUpvotes,
                user.getUpvotesReceived());

        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcDownvotes,
                user.getDownvotes_croaked());
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcOpinions,
                user.getmOpinionCount());
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcUpvotes,
                user.getUpvotes_croaked());
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcRenewals,
                user.getRenewal_request_croaked());
        ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcTopicsRenewed,
                user.getToatal_topic_renewed());
    }

    private AsyncTask<Void, user, user> runAsyncTask(AsyncTask<Void, user, user> task) {

        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public interface OnProfileLoadListener{

        void onCompleted();
        void onError(String error);
    }

}
