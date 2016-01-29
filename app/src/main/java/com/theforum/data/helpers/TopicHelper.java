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
import com.theforum.TheForumApplication;
import com.theforum.constants.SortType;
import com.theforum.data.helpers.topic.renewalRequestApi.Request;
import com.theforum.data.helpers.topic.renewalRequestApi.Response;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.server.topic;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
import com.theforum.utils.enums.RequestStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashish on 1/5/2016.
 */
public class TopicHelper {

    private static TopicHelper mTopicHelper;
    private MobileServiceClient mClient;
    private MobileServiceTable<topic> mTopicTable;
    private ArrayList<TopicDataModel> topicArrayList;
    private OnTopicsReceiveListener topicsReceiveListener;

    public RequestStatus requestStatus;


    public static TopicHelper getHelper(){
        if(mTopicHelper ==null) mTopicHelper = new TopicHelper();
        return mTopicHelper;
    }

    private TopicHelper(){
        this.mClient = TheForumApplication.getClient();
        mTopicTable = mClient.getTable(topic.class);
        requestStatus = RequestStatus.IDLE;

    }


    public void addTopic(final topic topic, final OnTopicInsertListener onTopicInsertListener) {
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
                        mTopicTable.insert(topic, new TableOperationCallback<topic>() {
                            @Override
                            public void onCompleted(topic entity, Exception exception, ServiceFilterResponse response) {
                                if (exception == null) {
                                /*
                                   saving topic to local dataBase
                                 */
                                    User.getInstance().setTopicsCreated(User.getInstance().getTopicsCreated()+1);

                                    TopicDataModel topicDataModel = new TopicDataModel(entity);
                                    topicDataModel.setIsMyTopic(true);
                                    TopicDBHelper.getHelper().addTopic(topicDataModel);

                                    if (topicsReceiveListener != null) {
                                        topicArrayList.clear();
                                        topicArrayList.add(topicDataModel);
                                        topicsReceiveListener.onCompleted(topicArrayList);
                                    }

                                    onTopicInsertListener.onCompleted();

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
            CommonUtils.showToast(TheForumApplication.getAppContext(),"Topic Already Exists");
        }

    }


    public void getTopics(OnTopicsReceiveListener listener){
        topicsReceiveListener = listener;

        if(requestStatus == RequestStatus.COMPLETED){
            topicsReceiveListener.onCompleted(topicArrayList);
            requestStatus = RequestStatus.IDLE;
        }
    }


    public void loadTopics(final int sortMode){


        if(CommonUtils.isInternetAvailable()){

            requestStatus = RequestStatus.EXECUTING;

            AsyncTask<Void, Void, ArrayList<topic>> task = new AsyncTask<Void, Void, ArrayList<topic>>() {
                MobileServiceList<topic> topics = null;

                @Override
                protected ArrayList<topic> doInBackground(Void... params) {
                    try {
                        switch (sortMode) {

                            case SortType.SORT_BASIS_MOST_POPULAR:
                                topics = mTopicTable.orderBy("points", QueryOrder.Descending).execute().get();
                                break;

                            case SortType.SORT_BASIS_LATEST:
                                topics = mTopicTable.orderBy("hours_left", QueryOrder.Ascending).execute().get();
                                break;

                            case SortType.SORT_BASIS_CREATED_BY_ME:
                                topics = mTopicTable.where().field("uid").eq(User.getInstance().getId()).execute().get();
                                break;

                            case SortType.SORT_BASIS_LEAST_RENEWAL:
                                topics = mTopicTable.orderBy("renewal_requests", QueryOrder.Ascending)
                                        .execute().get();
                                break;

                            case SortType.SORT_BASIS_MOST_RENEWAL:
                                topics = mTopicTable.orderBy("renewal_requests", QueryOrder.Descending)
                                        .execute().get();
                                break;

                        }

                    } catch (Exception e) {
                        if(topicsReceiveListener!= null) {
                            topicsReceiveListener.onError(e.getCause().getMessage());
                        }
                    }
                    return topics;
                }

                @Override
                protected void onPostExecute(ArrayList<topic> topics) {
                    super.onPostExecute(topics);

                    if(topics!=null) {
                        requestStatus = RequestStatus.COMPLETED;
                        convertDataModel(topics);

                        if (topicsReceiveListener != null) {
                            topicsReceiveListener.onCompleted(topicArrayList);
                            requestStatus = RequestStatus.IDLE;
                        }
                        TopicDBHelper.getHelper().deleteAll();
                        TopicDBHelper.getHelper().addTopicsFromServer(topicArrayList);
                    }

                }
            };

            runAsyncTask(task);

        }else {

            topicArrayList = TopicDBHelper.getHelper().getAllTopics();
            requestStatus = RequestStatus.COMPLETED;

            if (topicsReceiveListener != null) {
                topicsReceiveListener.onCompleted(topicArrayList);
                requestStatus = RequestStatus.IDLE;
            }

        }

    }

    public void addRenewalRequest(String topic_id , final OnRenewalRequestListener listener) {
        final Request request = new Request();
        request.topic_id = topic_id;
        request.uid = User.getInstance().getId();



        mClient.invokeApi("addrenewalrequest", request, Response.class, new ApiOperationCallback<Response>() {
            @Override
            public void onCompleted(Response result, Exception exception, ServiceFilterResponse response) {
                Log.e("result", "" + result.message);
                if (exception == null) {
                    if (result.message > 1) {

                        listener.response("You and " + result.message + " others added a renewal request");

                    } else {
                        listener.response("A renewal request has been added for this topic");
                    }
                } else {
                    listener.response(exception.getMessage());

                }
            }
        });
    }


    private void convertDataModel(ArrayList<topic> topics){
        topicArrayList = new ArrayList<>();

        for(int i=0; i<topics.size();i++) {
            TopicDataModel topicDataModel = new TopicDataModel(topics.get(i));
            topicDataModel.setIsRenewed(false);
            if(topics.get(i).getRenewalRequestIds()!=null) {
                String[] r = topics.get(i).getRenewalRequestIds().split(" ");

                for (int k = 0; k < r.length; k++) {
                    if (r[k].equals(User.getInstance().getId())) {
                        topicDataModel.setIsRenewed(true);
                        break;
                    }
                }
            }
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
        void onCompleted();
        void onError(String error);
    }



}
