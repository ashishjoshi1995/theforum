package com.theforum.data.helpers;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.theforum.Constants;
import com.theforum.User;
import com.theforum.data.dataModels.topic;
import com.theforum.data.dataModels.user;
import com.theforum.data.helpers.renewalRequestApi.Request;
import com.theforum.data.helpers.renewalRequestApi.Response;
import com.theforum.data.helpers.sortBasisCreatedByMe.InputClass;
import com.theforum.data.helpers.sortBasisCreatedByMe.ResponseClass;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ashish on 1/5/2016.
 */
public class LoadTopicHelper {
    private MobileServiceClient mClient;
    private MobileServiceTable<topic> mTopic;
    private MobileServiceTable<user> mUser;
    private String uid;

    public LoadTopicHelper(MobileServiceClient client){
        this.mClient = client;
        mTopic = mClient.getTable(topic.class);
    }

    private void initUserTable(){
        mUser = mClient.getTable(user.class);
    }

    public void loadLatestTopics(String operation, final int sortMode) {

        AsyncTask<Void, Void, ArrayList<topic>> task = new AsyncTask<Void, Void, ArrayList<topic>>() {
            MobileServiceList<topic> topics = null;
            MobileServiceList<user> users = null;
            @Override
            protected ArrayList<topic> doInBackground(Void... params) {
                try {
                switch (sortMode)
                {
                    case Constants.SORT_BASIS_LATEST:
                        topics = mTopic.orderBy("hours_left", QueryOrder.Ascending).execute().get();
                        break;
                    case Constants.SORT_BASIS_CREATED_BY_ME:

                        InputClass inputClass = new InputClass();
                        inputClass.uid = User.getInstance().getForumId();
                        mClient.invokeApi("getmytopics", inputClass, ResponseClass.class, new ApiOperationCallback<ResponseClass>() {
                            @Override
                            public void onCompleted(ResponseClass result, Exception exception, ServiceFilterResponse response) {
                                Log.e("herewego","herewego");
                                //TODO convert the string to JSONARRAY AND THEN TO JAVA ARRAYLIST
                            }
                        });
                        break;
                    case Constants.SORT_BASIS_LEAST_RENEWAL:
                        topics = mTopic.orderBy("renewal_request",QueryOrder.Ascending).execute().get();
                        break;
                    case Constants.SORT_BASIS_MOST_RENEWAL:
                        topics = mTopic.orderBy("renewal_request",QueryOrder.Descending).execute().get();
                        break;
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return topics;
            }

            @Override
            protected void onPostExecute(ArrayList<topic> topics) {
                super.onPostExecute(topics);
                if(sortMode==Constants.SORT_BASIS_CREATED_BY_ME){
                    users.get(0).getmCurrentTopics();
                }
            }

        };
        runAsyncTask(task);
    }

    public String addRenewalRequest(String uid, String topic_id){
        Request request = new Request();
        request.topic_id = topic_id;
        request.uid = uid;
        final boolean[] bool = {false};
        final int[] c = new int[1];

        mClient.invokeApi("addrenewalrequest", request, Response.class, new ApiOperationCallback<Response>() {
            @Override
            public void onCompleted(Response result, Exception exception, ServiceFilterResponse response) {
                if(exception==null){
               c[0] = result.message;
                }
                else{
                    bool[0] = true;
                }

            }
        });

        if(bool[0])return "Could not add a request now, try after some time";
        if(c[0]>2 && !bool[0]){
            c[0]--;
        return "You and "+c[0]+" added a renewal request";}
        else {
            return "You successfully added a renewal request";
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AsyncTask<Void, Void,ArrayList<topic>> runAsyncTask(AsyncTask<Void, Void, ArrayList<topic>> task) {

        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
