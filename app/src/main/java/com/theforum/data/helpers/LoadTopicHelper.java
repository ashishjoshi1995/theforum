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
import com.theforum.data.helpers.renewalRequestApi.Request;
import com.theforum.data.helpers.renewalRequestApi.Response;
import com.theforum.data.helpers.sortBasisCreatedByMe.InputClass;
import com.theforum.data.helpers.sortBasisCreatedByMe.ResponseClass;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.server.topic;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Ashish on 1/5/2016.
 */
public class LoadTopicHelper {

    private static LoadTopicHelper mLoadTopicHelper;
    private MobileServiceClient mClient;
    private MobileServiceTable<topic> mTopicTable;
    private String mUid;
    private ArrayList<TopicDataModel> topicArrayList;
    private OnTopicsReceiveListener topicsReceiveListener;
    private boolean topicsReceived = false;

    public static LoadTopicHelper getHelper(){
        if(mLoadTopicHelper==null) mLoadTopicHelper = new LoadTopicHelper();
        return mLoadTopicHelper;
    }

    private LoadTopicHelper(){
        this.mClient = TheForumApplication.getClient();
        mTopicTable = mClient.getTable(topic.class);
        mUid = User.getInstance().getId();
    }


    public void addTopic(final topic topic, final OnTopicInsertListener onTopicInsertListener) {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mTopicTable.insert(topic, new TableOperationCallback<topic>() {
                        @Override
                        public void onCompleted(topic entity, Exception exception, ServiceFilterResponse response) {
                            if (exception == null) {
                                onTopicInsertListener.onCompleted(entity);
                            } else {
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


    public void getTopics(OnTopicsReceiveListener listener){
        topicsReceiveListener = listener;

        if(topicsReceived){
            topicsReceiveListener.onCompleted(topicArrayList);
            topicsReceived= false;
            Log.e("topics send2", "called");
        }
    }



    public void loadTopics(int times, final int sortMode){

        final int n = times*20;

        if(CommonUtils.isInternetAvailable()){

            AsyncTask<Void, Void, ArrayList<topic>> task = new AsyncTask<Void, Void, ArrayList<topic>>() {
                MobileServiceList<topic> topics = null;

                @Override
                protected ArrayList<topic> doInBackground(Void... params) {
                    try {
                        switch (sortMode) {

                            case Constants.SORT_BASIS_MOST_POPULAR:
                                topics = mTopicTable.orderBy("points", QueryOrder.Descending).skip(n).
                                        top(20).execute().get();
                                break;

                            case Constants.SORT_BASIS_LATEST:
                                topics = mTopicTable.orderBy("hours_left", QueryOrder.Ascending).skip(n).top(20).execute().get();
                                break;

                            case Constants.SORT_BASIS_CREATED_BY_ME:
                                InputClass inputClass = new InputClass();
                                inputClass.uid = User.getInstance().getId();
                                mClient.invokeApi("getmytopics", inputClass, ResponseClass.class, new ApiOperationCallback<ResponseClass>() {
                                    @Override
                                    public void onCompleted(ResponseClass result, Exception exception, ServiceFilterResponse response) {
                                        Log.e("qwertyu", ""+result.message);
                                        if (exception == null){
                                            try {
                                                JSONArray jsonArray = new JSONArray(result.message);
                                                // ArrayList<topic> topicList = new ArrayList<topic>();
                                                //JSONArray jArray = (JSONArray)jsonObject;

                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                    topic topic = new topic();
                                                    topic.setServerId(jsonObject.get("id").toString());
                                                    topic.setTopicDescription(jsonObject.get("description").toString());
                                                    topic.setHoursLeft(Integer.parseInt(jsonObject.get("hours_left").toString()));
                                                    topic.setOpinionIds(jsonObject.get("opinion_ids").toString());
                                                    topic.setRenewalRequests(Integer.parseInt(jsonObject.get("renewal_request").toString()));
                                                    topic.setTopicName(jsonObject.get("topic").toString());
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

                                            } catch (JSONException e) {
                                                if(topicsReceiveListener!= null)
                                                    topicsReceiveListener.onError(e.getMessage());
                                            }

                                        } else {
                                            if(topicsReceiveListener!= null)
                                                topicsReceiveListener.onError(exception.getMessage());
                                        }

                                    }
                                });
                                break;

                            case Constants.SORT_BASIS_LEAST_RENEWAL:
                                topics = mTopicTable.orderBy("renewal_requests",QueryOrder.Ascending)
                                        .skip(n).top(20).execute().get();
                                break;

                            case Constants.SORT_BASIS_MOST_RENEWAL:
                                topics = mTopicTable.orderBy("renewal_requests",QueryOrder.Descending)
                                        .skip(n).top(20).execute().get();
                                break;

                        }
                    } catch (Exception e) {
                        if(topicsReceiveListener!= null)
                            topicsReceiveListener.onError(e.getCause().getMessage());
                    }
                    return topics;
                }

                @Override
                protected void onPostExecute(ArrayList<topic> topics) {
                    super.onPostExecute(topics);

                    if(topics!=null) {
                        topicsReceived = true;
                        convertDataModel(topics);
                        Log.e("topics received", "called");

                        if (topicsReceiveListener != null) {
                            Log.e("topics send", "called");
                            topicsReceiveListener.onCompleted(topicArrayList);
                            topicsReceived = false;
                        }

                        TopicDBHelper.getHelper().addTopicsFromServer(topicArrayList);
                    }

                }
            };

            runAsyncTask(task);

        }else {

            topicArrayList = TopicDBHelper.getHelper().getAllTopics();
            topicsReceived = true;

            if(topicsReceiveListener!= null){
                topicsReceiveListener.onCompleted(topicArrayList);
                topicsReceived = false;
            }
        }



    }


    public void addRenewalRequest(String topic_id , final OnRenewalRequestListener listener) {
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


    private void convertDataModel(ArrayList<topic> topics){
        topicArrayList = new ArrayList<>();
        topic topic ;
        for(int i=0; i<topics.size();i++) {
            TopicDataModel topicDataModel = new TopicDataModel();
            topic = topics.get(i);
            topicDataModel.setServerId(topic.getServerId());
            topicDataModel.setTopicId(topic.getTopicId());
            topicDataModel.setTopicName(topic.getTopicName());
            topicDataModel.setTopicDescription(topic.getTopicDescription());
            topicDataModel.setRenewalRequests(topic.getRenewalRequests());
            topicDataModel.setRenewedCount(topic.getRenewedCount());
            topicDataModel.setIsRenewed(false);
            topicArrayList.add(topicDataModel);
        }

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
        void onCompleted(ArrayList<TopicDataModel> topics);
        void onError(String error);
    }

    public interface OnRenewalRequestListener {
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

    /*
    public void loadTopicsOnPull(final int sortMode) {
    Log.e("mmmm","mmmmmmmmmm");
        AsyncTask<Void, Void, ArrayList<topic>> task = new AsyncTask<Void, Void, ArrayList<topic>>() {
            MobileServiceList<topic> topics = null;

            @Override
            protected ArrayList<topic> doInBackground(Void... params) {
                try {
                switch (sortMode) {
                    case Constants.SORT_BASIS_MOST_POPULAR:
                        topics = mTopicTable.orderBy("points", QueryOrder.Descending).top(5).execute().get();
                        break;
                    case Constants.SORT_BASIS_LATEST:
                        topics = mTopicTable.orderBy("hours_left", QueryOrder.Ascending).top(5).execute().get();
                        break;
                    case Constants.SORT_BASIS_CREATED_BY_ME:
                        InputClass inputClass = new InputClass();
                        inputClass.uid = User.getInstance().getId();
                        mClient.invokeApi("getmytopics", inputClass, ResponseClass.class, new ApiOperationCallback<ResponseClass>() {
                            @Override
                            public void onCompleted(ResponseClass result, Exception exception, ServiceFilterResponse response) {
                                Log.e("herewego", result.message);
                                if (exception == null){

                                    try {
                                        JSONArray jsonArray = new JSONArray(result.message);
                                        // ArrayList<topic> topicList = new ArrayList<topic>();
                                        //JSONArray jArray = (JSONArray)jsonObject;

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            topic topic = new topic();
                                            topic.setServerId(jsonObject.get("id").toString());
                                            topic.setTopicDescription(jsonObject.get("description").toString());
                                            topic.setHoursLeft(Integer.parseInt(jsonObject.get("hours_left").toString()));
                                            topic.setOpinionIds(jsonObject.get("opinion_ids").toString());
                                            topic.setRenewalRequests(Integer.parseInt(jsonObject.get("renewal_request").toString()));
                                            topic.setTopicName(jsonObject.get("topic").toString());
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

                                    } catch (JSONException e) {
                                        if(topicsReceiveListener!= null)
                                        topicsReceiveListener.onError(e.getMessage());
                                    }

                            } else {
                                    if(topicsReceiveListener!= null)
                                        topicsReceiveListener.onError(exception.getMessage());
                                }

                            }
                        });
                        break;
                    case Constants.SORT_BASIS_LEAST_RENEWAL:
                        topics = mTopicTable.orderBy("renewal_requests",QueryOrder.Ascending).top(5).execute().get();
                        break;
                    case Constants.SORT_BASIS_MOST_RENEWAL:
                        topics = mTopicTable.orderBy("renewal_requests",QueryOrder.Descending).top(5).execute().get();
                        break;

                }
                } catch (Exception e) {
                    if(topicsReceiveListener!= null)
                        topicsReceiveListener.onError(e.getCause().getMessage());
                }
                return topics;
            }

            @Override
            protected void onPostExecute(ArrayList<topic> topics) {
                super.onPostExecute(topics);

                if(topics!=null) {
                    topicsReceived = true;
                    convertDataModel(topics);
                    Log.e("topics received", "called");

                    if (topicsReceiveListener != null) {
                        Log.e("topics send", "called");
                        topicsReceiveListener.onCompleted(topicArrayList);
                        topicsReceived = false;
                    }
                    TopicDBHelper.getTopicDBHelper(TheForumApplication.getAppContext()).deleteAll();
                    TopicDBHelper.getTopicDBHelper(TheForumApplication.getAppContext()).addTopicsFromServer(topics);
                }

            }
        };

        runAsyncTask(task);
    }*/


}
