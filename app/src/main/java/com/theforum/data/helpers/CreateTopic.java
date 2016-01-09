package com.theforum.data.helpers;

import android.os.AsyncTask;

import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.theforum.TheForumApplication;
import com.theforum.data.dataModels.topic;

/**
 * @author  Ashish on 1/5/2016.
 */
public class CreateTopic {

    private MobileServiceTable<topic> mTopic;

    public CreateTopic() {
        mTopic = TheForumApplication.getClient().getTable(topic.class);
    }



    public void addTopic(final topic topic, final OnTopicInsertListener onTopicInsertListener) {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mTopic.insert(topic, new TableOperationCallback<topic>() {
                        @Override
                        public void onCompleted(topic entity, Exception exception, ServiceFilterResponse response) {

                            if(exception == null){
                                onTopicInsertListener.onCompleted(entity);
                            }
                            else{
                                onTopicInsertListener.onError(exception.getMessage());
                            }
                            //TODO: add file to the local db
                        }
                    });

                } catch (Exception e) {
                    onTopicInsertListener.onError(e.getMessage());
                }

                return null;
            }


        };

        runAsyncTask(task);
    }


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