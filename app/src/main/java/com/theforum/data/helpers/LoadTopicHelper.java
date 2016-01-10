package com.theforum.data.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.theforum.Constants;
import com.theforum.TheForumApplication;
import com.theforum.User;
import com.theforum.data.dataModels.topic;
import com.theforum.data.helpers.renewalRequestApi.Request;
import com.theforum.data.helpers.renewalRequestApi.Response;
import com.theforum.data.helpers.sortBasisCreatedByMe.InputClass;
import com.theforum.data.helpers.sortBasisCreatedByMe.ResponseClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @author Ashish on 1/5/2016.
 */
public class LoadTopicHelper {

    private static LoadTopicHelper mLoadTopicHelper;
    private MobileServiceClient mClient;
    private MobileServiceTable<topic> mTopic;
    private String mUid;

    public static LoadTopicHelper getHelper(){
        if(mLoadTopicHelper==null) mLoadTopicHelper = new LoadTopicHelper();
        return mLoadTopicHelper;
    }

    private LoadTopicHelper(){
        this.mClient = TheForumApplication.getClient();
        mTopic = mClient.getTable(topic.class);
        mUid = User.getInstance().getId();
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

        runAsyncTask2(task);
    }


    public void loadTopics( final int sortMode , final OnTopicsReceiveListener listener) {

        AsyncTask<Void, Void, ArrayList<topic>> task = new AsyncTask<Void, Void, ArrayList<topic>>() {
            MobileServiceList<topic> topics = null;

            @Override
            protected ArrayList<topic> doInBackground(Void... params) {
                try {
                switch (sortMode)
                {
                    case Constants.SORT_BASIS_MOST_POPULAR:
                        topics = mTopic.orderBy("points", QueryOrder.Descending).execute().get();
                        break;
                    case Constants.SORT_BASIS_LATEST:
                        topics = mTopic.orderBy("hours_left", QueryOrder.Ascending).execute().get();
                        break;
                    case Constants.SORT_BASIS_CREATED_BY_ME:
                        InputClass inputClass = new InputClass();
                        inputClass.uid = User.getInstance().getId();
                        mClient.invokeApi("getmytopics", inputClass, ResponseClass.class, new ApiOperationCallback<ResponseClass>() {
                            @Override
                            public void onCompleted(ResponseClass result, Exception exception, ServiceFilterResponse response) {
                                Log.e("herewego", "herewego");
                                if (exception == null){
                                    try {
                                        //JSONObject jsnobject = new JSONObject(result.message);
                                        JSONArray jsonArray = new JSONArray(result.message);
                                        // ArrayList<topic> topicList = new ArrayList<topic>();
                                        //JSONArray jArray = (JSONArray)jsonObject;
                                        if (jsonArray != null) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                topic topic = new topic();
                                                topic.setServerId(jsonObject.get("id").toString());
                                                topic.setTopicDescription(jsonObject.get("description").toString());
                                                topic.setmHoursLeft(Integer.parseInt(jsonObject.get("hours_left").toString()));
                                                topic.setOpinionIds(jsonObject.get("opinion_ids").toString());
                                                topic.setRenewalRequests(Integer.parseInt(jsonObject.get("renewal_request").toString()));
                                                topic.setmTopic(jsonObject.get("topic").toString());
                                                topic.setTopicId(jsonObject.get("topic_id").toString());
                                                topic.setUserId(jsonObject.get("uid").toString());
                                                topic.setRenewedCount(Integer.parseInt(jsonObject.get("renewed_count").toString()));
                                                topic.setTotalOpinions(Integer.parseInt(jsonObject.get("total_opinions").toString()));
                                                topic.setmNotifRenewalRequests(Integer.parseInt(jsonObject.get("notif_new_renewal_request").toString()));
                                                topic.setmNotifOpinions(Integer.parseInt(jsonObject.get("notif_new_opinions").toString()));
                                                topic.setmPoints(Integer.parseInt(jsonObject.get("points").toString()));

                                                topics.add(topic);
                                                Log.e("ashish", topics.get(i).getServerId());
                                            }

                                        } else listener.onError("empty JSON");
                                    } catch (JSONException e) {
                                        listener.onError(e.getMessage());
                                    }
                            }
                                else {
                                    listener.onError(exception.getMessage());
                                }


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
                    listener.onError(e.getMessage());
                } catch (ExecutionException e) {
                    listener.onError(e.getMessage());
                }
                return topics;
            }

            @Override
            protected void onPostExecute(ArrayList<topic> topics) {
                super.onPostExecute(topics);
                listener.onCompleted(topics);


            }

        };
        runAsyncTask(task);
    }

    public void addRenewalRequest(String topic_id , final OnRenewalRequestAddedListener listener) {
        final Request request = new Request();
        request.topic_id = topic_id;
        request.uid = mUid;
        final boolean[] bool = {false};


        mClient.invokeApi("addrenewalrequest", request, Response.class, new ApiOperationCallback<Response>() {
            @Override
            public void onCompleted(Response result, Exception exception, ServiceFilterResponse response) {
                Log.e("result", "" + result.message);
                if (exception == null) {
                    if (result.message > 1) {

                        listener.response("You and " + result.message + " others added a renewal request");
                        bool[0] = false;
                    } else {
                        listener.response("A renewal request has been added for this topic");
                    }
                } else {
                    listener.response(exception.getMessage());
                    bool[0] = true;
                }
            }
        });
    }


    private AsyncTask<Void, Void,ArrayList<topic>> runAsyncTask(AsyncTask<Void, Void, ArrayList<topic>> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AsyncTask<Void, Void,Void> runAsyncTask2(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public interface OnTopicsReceiveListener{
        /**
         *
         * @param  topics model with updated params
         */
        void onCompleted(ArrayList<topic> topics);
        void onError(String error);
    }

    public interface OnRenewalRequestAddedListener{
        /**
         *
         * @param  s model with updated params
         */
        void response(String s);

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
