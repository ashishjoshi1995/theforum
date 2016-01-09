package com.theforum.data.helpers;

import android.os.AsyncTask;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.theforum.TheForumApplication;
import com.theforum.data.dataModels.user;



/**
 * @author Ashish
 * @since 1/9/2016
 */
public class LoginHelper {

    private MobileServiceTable<user> mUser;

    public LoginHelper(){
        mUser = TheForumApplication.getClient().getTable(user.class);
    }

    public void login(final user user, final OnLoginCompleteListener listener){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                mUser.insert(user, new TableOperationCallback<com.theforum.data.dataModels.user>() {
                    @Override
                    public void onCompleted(user entity, Exception exception, ServiceFilterResponse response) {
                        if(exception == null){
                            listener.onCompleted(entity);
                        }
                        else {
                            listener.onError(exception.getMessage());
                        }
                    }
                });
                return null;
            }

        };
        runAsyncTask(task);
    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public interface OnLoginCompleteListener{
        /**
         *
         * @param user user data model with updated params
         */
        void onCompleted(user user);
        void onError(String error);
    }
}
