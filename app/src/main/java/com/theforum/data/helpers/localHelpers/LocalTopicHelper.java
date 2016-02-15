package com.theforum.data.helpers.localHelpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.theforum.TheForumApplication;
import com.theforum.constants.Messages;
import com.theforum.data.helpers.localTrendsApi.LTARequest;
import com.theforum.data.helpers.localTrendsApi.LTAResponse;
import com.theforum.data.helpers.local_addrenewalrequesApi.LARRRequest;
import com.theforum.data.helpers.local_addrenewalrequesApi.LARRResponse;
import com.theforum.data.helpers.local_remove_renewalApi.LRRARequest;
import com.theforum.data.helpers.local_remove_renewalApi.LRRAResponse;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.server.areatopics;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
import com.theforum.utils.enums.RequestStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashish
 * @since 2/11/2016
 */
public class LocalTopicHelper {
    private static LocalTopicHelper mTopicHelper;
    private MobileServiceClient mClient;
    private MobileServiceTable<areatopics> mTopicTable;
    private ArrayList<TopicDataModel>  topicArrayList;
    private OnTopicInsertListener topicInsertListener;
    private OnTopicsReceiveListener topicsReceiveListener;

    public RequestStatus requestStatus;

    public static LocalTopicHelper getHelper(){
        if(mTopicHelper ==null) mTopicHelper = new LocalTopicHelper();
        return mTopicHelper;
    }

    private LocalTopicHelper(){
        this.mClient = TheForumApplication.getClient();
        mTopicTable = mClient.getTable(areatopics.class);
        requestStatus = RequestStatus.IDLE;
    }

    public void addTopic(final areatopics topic, final OnTopicInsertListener onTopicInsertListener) {
        boolean name_exist = false;
        List<String> topic_name = TopicDBHelper.getHelper().getMyTopicText();

        for (int i = 0; i < topic_name.size(); i++) {
            if (topic.getTopicName().equals(topic_name.get(i))) {
                name_exist = true;
                break;
            }
        }
        if (!name_exist) {
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        mTopicTable.insert(topic, new TableOperationCallback<areatopics>() {
                            @Override
                            public void onCompleted(areatopics entity, Exception exception, ServiceFilterResponse response) {
                                if (exception == null) {
                                /*
                                   saving topic to local dataBase
                                 */
                                    User.getInstance().setTopicsCreated(User.getInstance().getTopicsCreated() + 1);

                                    TopicDataModel topicDataModel = new TopicDataModel(entity);
                                    topicDataModel.setIsMyTopic(true);
                                    topicDataModel.setIsLocalTopic(true);
                                    TopicDBHelper.getHelper().addTopic(topicDataModel);

                                    if (topicInsertListener != null) {
                                        topicInsertListener.onCompleted(topicDataModel, false);
                                    }

                                    onTopicInsertListener.onCompleted(topicDataModel, false);

                                } else {
                                    onTopicInsertListener.onError(exception.getMessage());
                                }

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
        else{
            CommonUtils.showToast(TheForumApplication.getAppContext(), "Topic Already Exists");
        }
    }

    public void setTopicInsertListener(OnTopicInsertListener listener){
        topicInsertListener = listener;
    }

    public void getTopics(OnTopicsReceiveListener listener){
        topicsReceiveListener = listener;

        if(requestStatus == RequestStatus.COMPLETED){
            topicsReceiveListener.onCompleted(topicArrayList);
            requestStatus = RequestStatus.IDLE;
        }
    }

    public void loadTopics(final int sortMode, final double latitude, final double longitude, boolean refresh) {
        requestStatus = RequestStatus.EXECUTING;
        Log.e("local load topics","local load topics");
        LTARequest request = new LTARequest();
        request.latitude = latitude;
        request.longitude = longitude;
        if (CommonUtils.isInternetAvailable()) {
            final ArrayList<TopicDataModel> topics = new ArrayList<TopicDataModel>();
            TheForumApplication.getClient().invokeApi("nearbytopics", request, LTAResponse.class,
                    new ApiOperationCallback<LTAResponse>() {
                        @Override
                        public void onCompleted(LTAResponse result, Exception exception, ServiceFilterResponse response) {
                            if (exception == null) {
                                Log.e("oncopleffffffffff","llllllllllllllllll");
                                try {
                                   // ArrayList<TopicDataModel> topics = new ArrayList<TopicDataModel>();
                                    if (result.message != null) {
                                        JSONArray jsonArray = new JSONArray(result.message);
                                        requestStatus = RequestStatus.COMPLETED;


                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            Log.e("test1",""+i);
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            TopicDataModel topicDataModel = new TopicDataModel();

                                            topicDataModel.setHoursLeft(Integer.parseInt(jsonObject.get("hours_left").toString()));
                                            topicDataModel.setTopicId(jsonObject.get("topic_id").toString());
                                            topicDataModel.setTopicDescription(jsonObject.get("description").toString());
                                            topicDataModel.setTopicName(jsonObject.get("topic").toString());
                                            topicDataModel.setRenewalRequests(Integer.parseInt(jsonObject.get("renewal_requests").toString()));
                                            topicDataModel.setServerId(jsonObject.get("id").toString());
                                            topicDataModel.setRenewedCount(Integer.parseInt(jsonObject.get("renewed_count").toString()));
                                            topicDataModel.setIsLocalTopic(true);
                                            topicDataModel.setLatitude(Double.parseDouble(jsonObject.get("latitude").toString()));
                                            topicDataModel.setLongitude(Double.parseDouble(jsonObject.get("longitude").toString()));
                                            topicDataModel.setUid(jsonObject.get("uid").toString());

                                            boolean statusReceived = false;
                                            topicDataModel.setIsRenewed(false);
                                            topicDataModel.setIsLocalTopic(true);
                                            if (jsonObject.get("renewal_request_ids") != null) {
                                                String upid = jsonObject.get("renewal_request_ids").toString();
                                                String[] upids = upid.split(" ");

                                                for (int j = 0; j < upids.length; j++) {
                                                    if (upids[j].equals(User.getInstance().getId())) {
                                                        topicDataModel.setIsRenewed(true);
                                                        statusReceived = true;
                                                        break;
                                                    }
                                                }

                                            }
                                            topicDataModel.setIsMyTopic(false);
                                            if (jsonObject.get("uid").equals(User.getInstance().getId()))
                                                topicDataModel.setIsMyTopic(true);

                                            topics.add(topicDataModel);
                                            Log.e("test2", "" + topics.size());
                                        }
                                    }
                                    if (topics != null) {
                                        requestStatus = RequestStatus.COMPLETED;
                                        topicArrayList = topics;
                                        if (topicsReceiveListener != null) {
                                            topicsReceiveListener.onCompleted(topicArrayList);
                                            requestStatus = RequestStatus.IDLE;
                                        }
                                        //TODO local and global, modify the method
                                        TopicDBHelper.getHelper().deleteAllLocalTopics();
                                        TopicDBHelper.getHelper().addTopicsFromServer(topicArrayList, true);
                                        Log.e("test3", topics.size() + "");
                                    } else {
                                        sendError("\n\n\n"+"sadsafdsa");
                                    }

                                } catch (JSONException e) {
                                    Log.e("mjhghjgjhgb",""+result.message);
                                    sendError(Messages.NO_NET_CONNECTION + "\n" + e.getMessage());
                                }

                            } else {
                                sendError(exception.getMessage());
                            }


                        }

                    });
         }
        else {
            if(!refresh) {
                topicArrayList = TopicDBHelper.getHelper().getAllLocalTopics();
                requestStatus = RequestStatus.COMPLETED;

                if (topicsReceiveListener != null) {
                    topicsReceiveListener.onCompleted(topicArrayList);
                    requestStatus = RequestStatus.IDLE;
                }
            }else {
                sendError(Messages.NO_NET_CONNECTION);
            }
        }
    }

    private void sendError(String error){
        requestStatus = RequestStatus.IDLE;
        if(topicsReceiveListener!= null) {
            topicsReceiveListener.onError(error);
        }
    }

    public void addRenewalRequest(final TopicDataModel topicDataModel , final OnRenewalRequestListener listener) {
       final LARRRequest request = new LARRRequest();
        request.topic_id = topicDataModel.getTopicId();
        request.uid = User.getInstance().getId();

        if(CommonUtils.isInternetAvailable()){
            mClient.invokeApi("local_addrenewalrequest", request, LARRResponse.class, new ApiOperationCallback<LARRResponse>() {
                @Override
                public void onCompleted(LARRResponse result, Exception exception, ServiceFilterResponse response) {
                    if (exception != null) {
                        listener.onError(exception.getMessage());
                    } else {
                        listener.onCompleted();

                        // update the local database
                        TopicDBHelper.getHelper().updateTopicRenewalStatus(topicDataModel);
                    }
                }
            });

        } else listener.onError(Messages.NO_NET_CONNECTION);
    }
    public void removeRenewal(String topic_id , final OnRemoveRenewalRequestListener listener) {
        final LRRARequest request = new LRRARequest();
        request.topic_id = topic_id;
        request.uid = User.getInstance().getId();

        if(CommonUtils.isInternetAvailable()) {
            mClient.invokeApi("local_remove_renewal", request, LRRAResponse.class, new ApiOperationCallback<LRRAResponse>() {
                @Override
                public void onCompleted(LRRAResponse result, Exception exception, ServiceFilterResponse response) {
                    if (exception != null) {
                        listener.onError(exception.getMessage());
                    } else listener.onCompleted();
                }
            });
        } else listener.onError(Messages.NO_NET_CONNECTION);
    }



    public void updateTopic(final TopicDataModel topicDataModel ,final OnTopicInsertListener listener) {

        AsyncTask<Void, Void,Void> task= new AsyncTask<Void, Void, Void>() {
            areatopics t;
            MobileServiceList<areatopics> result;
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    result = mTopicTable.where().field("topic_id").eq(topicDataModel.getTopicId()).execute().get();

                } catch (Exception e) {
                    listener.onError(Messages.NO_NET_CONNECTION);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                t= result.get(0);
                t.setTopicName(topicDataModel.getTopicName());
                t.setTopicDescription(topicDataModel.getTopicDescription());
                mTopicTable.update(t, new TableOperationCallback<areatopics>() {
                    @Override
                    public void onCompleted(areatopics entity, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            if(TopicDBHelper.getHelper().updateTopic(topicDataModel)>0) {
                                listener.onCompleted(null, true);
                            }
                        } else {
                            listener.onError(exception.getMessage());
                        }
                    }
                });

            }
        };

        runAsyncTask3(task);
    }

    private AsyncTask<Void, Void,ArrayList<areatopics>> runAsyncTask(AsyncTask<Void, Void, ArrayList<areatopics>> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AsyncTask<Void, Void,Void> runAsyncTask2(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private AsyncTask<Void, Void, Void> runAsyncTask3(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public interface OnTopicsReceiveListener{
        /**
         * @param  topics model with updated params
         */
        void onCompleted(ArrayList<TopicDataModel> topics);
        void onError(String error);
    }

    public interface OnRenewalRequestListener {

        void onCompleted();
        /**
         * @param  error error status with message
         */
        void onError(String error);

    }
    public interface OnRemoveRenewalRequestListener {

        void onCompleted();
        /**
         * @param  error error status with message
         */
        void onError(String error);

    }

    public interface OnTopicInsertListener{
        void onCompleted(TopicDataModel topicDataModel, boolean isUpdated);
        void onError(String error);
    }



}
