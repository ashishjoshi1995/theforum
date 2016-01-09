package com.theforum.data.helpers;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.theforum.TheForumApplication;
import com.theforum.data.dataModels.topic;

/**
 * Created by Ashish on 1/5/2016.
 */
public class CreateTopic {


    private MobileServiceClient mClient;
    private MobileServiceTable<topic> mTopic;
    //public  topic topic;
    private String uid;

    public CreateTopic() {
        this.mClient = TheForumApplication.getClient();
        mTopic = mClient.getTable(topic.class);
    }



    public void addTopic(final topic topic, final OnTopicInsertListener onTopicInsertListener) {
        //this.topic = topic;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mTopic.insert(topic, new TableOperationCallback<topic>() {
                        @Override
                        public void onCompleted(topic entity, Exception exception, ServiceFilterResponse response) {
                            Log.e("done","done" );
                            if(exception == null){

                                onTopicInsertListener.onCompleted(entity);
                            }
                            else{
                                onTopicInsertListener.onError(exception.getMessage());
                            }
                            //add file to the local db
                        }
                    });

                } catch (Exception e) {
                    //listener.onFailure(e.toString());
                }

                return null;
            }


        };

        runAsyncTask(task);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AsyncTask<Void, Void,Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {

        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public interface OnTopicInsertListener{
        /**
         *
         * @param topic topic data model with updated params
         */
        void onCompleted(topic topic);
        void onError(String error);
    }

}