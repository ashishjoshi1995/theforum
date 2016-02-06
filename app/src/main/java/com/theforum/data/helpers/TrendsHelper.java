package com.theforum.data.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.theforum.TheForumApplication;
import com.theforum.constants.Messages;
import com.theforum.data.helpers.directUpDownApi.DUDARequest;
import com.theforum.data.helpers.directUpDownApi.DUDAResponse;
import com.theforum.data.helpers.removeUpDownApi.RUDAResponse;
import com.theforum.data.helpers.trendinOpinionApi.TrendingInput;
import com.theforum.data.helpers.trendinOpinionApi.TrendingResponse;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVRequest;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVResponse;
import com.theforum.data.local.database.trendsDB.TrendsDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.data.server.topic;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
import com.theforum.utils.enums.RequestStatus;
import com.theforum.utils.enums.VoteStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author DEEPANKAR
 * @since 19-01-2016.
 */
public class TrendsHelper {

    public RequestStatus requestStatus;

    private static TrendsHelper trendsHelper;
    private MobileServiceTable<topic> mTopic;

    private ArrayList<TrendsDataModel> trends;
    private OnTrendsReceivedListener trendsReceivedListener;


    public static TrendsHelper getHelper(){
        if(trendsHelper ==null) trendsHelper = new TrendsHelper();
        return trendsHelper;
    }

    private TrendsHelper(){
        trends = new ArrayList<>();
        requestStatus = RequestStatus.IDLE;
    }


    public void loadTrends(){

        if(requestStatus == RequestStatus.IDLE) {

            requestStatus = RequestStatus.EXECUTING;
            if (CommonUtils.isInternetAvailable()) {
                loadTopicsFromServer();
            } else {
                trends.addAll(TrendsDBHelper.getHelper().getAllTrends());
                requestStatus = RequestStatus.COMPLETED;

                if(trendsReceivedListener!= null){
                    trendsReceivedListener.onCompleted(trends);
                    requestStatus = RequestStatus.IDLE;
                    trends.clear();
                }
            }
        }

    }



    public void getTrends(OnTrendsReceivedListener listener){
        this.trendsReceivedListener = listener;

        if(requestStatus == RequestStatus.COMPLETED){
            listener.onCompleted(trends);
            requestStatus = RequestStatus.IDLE;
            trends.clear();
        }

    }
    private void loadTopicsFromServer() {
        TrendingInput updvRequest= new TrendingInput();

        updvRequest.uid = User.getInstance().getId();

        TheForumApplication.getClient().invokeApi("trendingopininos", updvRequest, TrendingResponse.class,
                new ApiOperationCallback<TrendingResponse>() {

                    @Override
                    public void onCompleted(TrendingResponse result, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {

                            try {

                                if (result.message != null) {
                                    JSONArray jsonArray = new JSONArray(result.message);
                                    requestStatus = RequestStatus.COMPLETED;

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TrendsDataModel topic = new TrendsDataModel();

                                        topic.setHoursLeft(Integer.parseInt(jsonObject.get("hours_left").toString()));
                                        topic.setTrendId(jsonObject.get("trends_id").toString());
                                        topic.setUpVoteCount(Integer.parseInt(jsonObject.get("upvotes").toString()));
                                        topic.setDownVoteCount(Integer.parseInt(jsonObject.get("downvotes").toString()));
                                        topic.setTopicName(jsonObject.get("topic_name").toString());
                                        topic.setTopicId(jsonObject.get("topic_id").toString());
                                        topic.setOpinionText(jsonObject.get("opinionText").toString());
                                        topic.setServerId(jsonObject.get("serverId").toString());
                                        topic.setDescription(jsonObject.get("description").toString());
                                        topic.setRenewalIds(jsonObject.get("renewalIds").toString());
                                        topic.setRenewCount(Integer.parseInt(jsonObject.get("renewal").toString()));


                                        boolean statusReceived = false;
                                        if(jsonObject.get("upvote_ids") != null) {
                                            String upid = jsonObject.get("upvote_ids").toString();
                                            String[] upids = upid.split(" ");

                                            for(int j=0;j<upids.length;j++){
                                                if(upids[j].equals(User.getInstance().getId())){
                                                    topic.setVoteStatus(VoteStatus.UPVOTED);
                                                    statusReceived = true;
                                                    break;
                                                }
                                            }

                                        }

                                        if(jsonObject.get("downvote_ids").toString()!=null && !statusReceived) {
                                            String downid = jsonObject.get("downvote_ids").toString();
                                            String[] downids = downid.split(" ");
                                            for(int j=0;j<downids.length;j++){
                                                if(downids[j].equals(User.getInstance().getId())){
                                                    topic.setVoteStatus(VoteStatus.DOWNVOTED);
                                                    break;
                                                }
                                            }
                                        }


                                        trends.add(topic);
                                    }


                                    // save the data to local database.
                                    TrendsDBHelper.getHelper().deleteAllTrends();
                                    TrendsDBHelper.getHelper().addTrends(trends);

                                    /**
                                     * passing the data to ui
                                     */
                                    if (trendsReceivedListener != null) {
                                        trendsReceivedListener.onCompleted(trends);
                                        requestStatus = RequestStatus.IDLE;
                                        trends.clear();
                                    }

                                }  else {
                                    if (trendsReceivedListener != null) {
                                        trendsReceivedListener.onError(Messages.NO_NET_CONNECTION);
                                    }
                                    requestStatus = RequestStatus.IDLE;

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                           //Log.e("fuck","you");
                        }
                    }
                });
    }


    public void getTopicByName(final String topic_name,final OnTopicDetailReceived listener){
        if(mTopic == null) mTopic = TheForumApplication.getClient().getTable(topic.class);
        AsyncTask<Void, Void,topic> task= new AsyncTask<Void, Void, topic>() {

            @Override
            protected topic doInBackground(Void... voids) {
                MobileServiceList<topic> result;
                try {
                    result = mTopic.where().field("topic").eq(topic_name).execute().get();
                    return result.get(0);

                } catch (Exception e) {
                    listener.onError(Messages.NO_NET_CONNECTION);

                    return null;
                }

            }

            @Override
            protected void onPostExecute(topic topic) {
                super.onPostExecute(topic);

                if(topic!=null) {
                    TopicDataModel topicDataModel = new TopicDataModel(topic);
                    listener.onCompleted(topicDataModel);
                }
            }
        };

        runAsyncTask3(task);
    }

    public void getTopicDetails(final String topic_id,final OnTopicDetailReceived listener){
        if(mTopic == null) mTopic = TheForumApplication.getClient().getTable(topic.class);
        AsyncTask<Void, Void,topic> task= new AsyncTask<Void, Void, topic>() {

            @Override
            protected topic doInBackground(Void... voids) {
                MobileServiceList<topic> result;
                try {
                    result = mTopic.where().field("topic_id").eq(topic_id).execute().get();
                    return result.get(0);
                } catch (Exception e) {
                    listener.onError(Messages.NO_NET_CONNECTION);
                    return null;
                }

            }

            @Override
            protected void onPostExecute(topic topic) {
                super.onPostExecute(topic);

                if(topic!=null) {
                    TopicDataModel topicDataModel = new TopicDataModel(topic);
                    listener.onCompleted(topicDataModel);
                }
            }
        };

        runAsyncTask3(task);
    }


    public void upVoteDownVote(boolean ifUpVote, String opinionId, final OnUVDVOperationCompleteListener listener){
        UPDVRequest updvRequest= new UPDVRequest();
        updvRequest.opinion_id = opinionId;
        updvRequest.id = User.getInstance().getId();

        if(ifUpVote){
            updvRequest.operation_chosen = 1;
        }
        else{
            updvRequest.operation_chosen = 0;
        }

        //update server

        TheForumApplication.getClient().invokeApi("upvote", updvRequest, UPDVResponse.class,
                new ApiOperationCallback<UPDVResponse>() {
                    @Override
                    public void onCompleted(UPDVResponse result, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            listener.onCompleteMessage("Opinion has been UpVoted");
                        } else {
                            listener.onErrorMessage(exception.getMessage());
                        }
                    }
                });

    }
    public void removeUpDownVote(final boolean ifUpVote, String opinionId, final OnRUDAOperationCompleteListener listener){
        UPDVRequest updvRequest= new UPDVRequest();
        updvRequest.opinion_id = opinionId;
        updvRequest.id = User.getInstance().getId();

        if(ifUpVote){
            updvRequest.operation_chosen = 1;
        }
        else{
            updvRequest.operation_chosen = 0;
        }

        //update server

        TheForumApplication.getClient().invokeApi("remove_up_down", updvRequest, RUDAResponse.class,
                new ApiOperationCallback<RUDAResponse>() {
                    @Override
                    public void onCompleted(RUDAResponse result, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            if(ifUpVote)
                            listener.onCompleteMessage("Upvote Removed");
                            else listener.onCompleteMessage("Downvote Removed");
                        } else {
                            //listener.onCompleteMessage(exception.getMessage());
                            listener.onErrorMessage(Messages.NO_NET_CONNECTION);
                        }
                    }
                });

    }
    public void directUpDownVoteChange(final boolean ifUpVote, String opinionId, final OnDUDAOperationCompleteListener listener){
        DUDARequest updvRequest= new DUDARequest();
        updvRequest.opinion_id = opinionId;
        updvRequest.id = User.getInstance().getId();

        if(ifUpVote){
            updvRequest.operation_chosen = 1;
        }
        else{
            updvRequest.operation_chosen = 0;
        }

        //update server

        TheForumApplication.getClient().invokeApi("direct_up_downChange", updvRequest, DUDAResponse.class, new ApiOperationCallback<DUDAResponse>() {
            @Override
            public void onCompleted(DUDAResponse result, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    if(!ifUpVote)
                    listener.onCompleteMessage("Opinion Downvoted");
                    else listener.onCompleteMessage("Opinion Upvoted");
                } else {
                    //listener.onCompleteMessage(exception.getMessage());
                    listener.onErrorMessage(Messages.NO_NET_CONNECTION);
                }
            }
        });

    }


    private static AsyncTask<Void, Void,topic> runAsyncTask3(AsyncTask<Void, Void, topic> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    public interface OnTrendsReceivedListener {
        /**
         *
         * @param  trends trends data model with updated params
         */
        void onCompleted(ArrayList<TrendsDataModel> trends);
        void onError(String error);
    }

    public interface OnUVDVOperationCompleteListener{
        /**
         *
         * @param  message opinion data model with updated params
         */
        void onCompleteMessage(String message);
        void onErrorMessage(String message);
    }
    public interface OnRUDAOperationCompleteListener{
        /**
         *
         * @param  message opinion data model with updated params
         */
        void onCompleteMessage(String message);
        void onErrorMessage(String message);
    }
    public interface OnDUDAOperationCompleteListener{
        /**
         *
         * @param  message opinion data model with updated params
         */
        void onCompleteMessage(String message);
        void onErrorMessage(String message);
    }

    public interface OnTopicDetailReceived{
        /**
         *
         * @param  topic topic data model with updated params
         */

        void onCompleted(TopicDataModel topic);
        void onError(String error);
    }
}
