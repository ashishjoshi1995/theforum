package com.theforum.data.helpers;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.theforum.data.dataModels.topic;
import com.theforum.data.dataModels.user;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ashish on 12/31/2015.
 */
public class ProfileHelper {
    private MobileServiceClient mClient;
    private MobileServiceTable<user> mUser;
    private String uid;

    public ProfileHelper(MobileServiceClient mClient, String uid){
        this.mClient = mClient;
        this.uid = uid;
        mUser = mClient.getTable(user.class);

    }

    public user viewProfile(){

        AsyncTask<Void, user, user> task = new AsyncTask<Void, user, user>(){
            MobileServiceList<user> ash = null;
            @Override
            protected user doInBackground(Void... voids) {
                try {
                    ash = mUser.where().field("uid").eq(uid).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return ash.get(0);
            }

            @Override
            protected void onPostExecute(user user) {
                super.onPostExecute(user);
            }
        };
        runAsyncTask(task);
        return null;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AsyncTask<Void, user, user> runAsyncTask(AsyncTask<Void, user, user> task) {

        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
