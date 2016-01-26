package com.theforum.data.helpers;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.theforum.TheForumApplication;
import com.theforum.data.server.user;
import com.theforum.utils.User;

/**
 * @author  Ashish on 12/31/2015.
 */

public class ProfileHelper {

    private MobileServiceTable<user> mUser;
    private String uid;
    public static ProfileHelper mHelper;

    public static ProfileHelper getHelper(){
        if(mHelper == null) mHelper = new ProfileHelper();
        return mHelper;
    }

    private ProfileHelper(){
        this.uid = User.getInstance().getId();
        mUser = TheForumApplication.getClient().getTable(user.class);
    }

    public void viewProfile(){

        AsyncTask<Void, user, user> task = new AsyncTask<Void, user, user>(){
            MobileServiceList<user> ash = null;
            @Override
            protected user doInBackground(Void... voids) {
                try {
                    ash = mUser.where().field("uid").eq(uid).execute().get();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(ash!=null) {
                    return ash.get(0);

                }else return null;

            }

            @Override
            protected void onPostExecute(user user) {
                super.onPostExecute(user);

                if(user!=null) {
                    User.getInstance().setPointCollected(user.getmPointCollected());
                    User.getInstance().setCurrentTopics(user.getmCurrentTopics());
                    User.getInstance().setTopicsCreated(user.getmTopicsCreated());
                    User.getInstance().setStatus(user.getmStatus());
                }
            }

        };

        runAsyncTask(task);

    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AsyncTask<Void, user, user> runAsyncTask(AsyncTask<Void, user, user> task) {

        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
