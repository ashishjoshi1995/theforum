package com.theforum.data.helpers;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.theforum.Constants;
import com.theforum.data.dataModels.topic;
import com.theforum.data.dataModels.user;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ashish on 1/5/2016.
 */
public class CreateTopic {


    private MobileServiceClient mClient;
    private MobileServiceTable<topic> mTopic;
    private topic topic;
    private String uid;

    public CreateTopic(MobileServiceClient client,topic topic1) {
        this.mClient = client;
        this.topic = topic1;
        mTopic = mClient.getTable(topic.class);
    }



    public void addTopic() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mTopic.insert(topic, new TableOperationCallback<topic>() {
                        @Override
                        public void onCompleted(topic entity, Exception exception, ServiceFilterResponse response) {
                            Log.e("done","done" );
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

}