package com.theforum.data.helpers.localHelpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.theforum.TheForumApplication;
import com.theforum.constants.Messages;
import com.theforum.data.helpers.localTrendsApi.LTARequest;
import com.theforum.data.helpers.localTrendsApi.LTAResponse;
import com.theforum.data.helpers.local_direct_up_downChangeApi.LDUDCARequest;
import com.theforum.data.helpers.local_direct_up_downChangeApi.LDUDCResponse;
import com.theforum.data.helpers.local_remove_up_downApi.LRUDARequest;
import com.theforum.data.helpers.local_remove_up_downApi.LRUDAResponse;
import com.theforum.data.helpers.local_upvoteApi.LUARequest;
import com.theforum.data.helpers.local_upvoteApi.LUAResponse;
import com.theforum.data.local.database.trendsDB.TrendsDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.data.server.areatopics;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
import com.theforum.utils.enums.RequestStatus;
import com.theforum.utils.enums.VoteStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author  gaurav on 13-Feb-16.
 */
public class LocalTrendsHelper {

    public RequestStatus requestStatus;

    private static LocalTrendsHelper localTrendsHelper;
    private MobileServiceTable<areatopics> mTopic;

    private ArrayList<TrendsDataModel> trends;
    private OnTrendsReceivedListener trendsReceivedListener;
    //Location
    private double latitude= 0.0;
    private double longitude = 0.0;


    public static LocalTrendsHelper getHelper(){
        if(localTrendsHelper ==null) localTrendsHelper = new LocalTrendsHelper();
        return localTrendsHelper;
    }

    private LocalTrendsHelper(){
        trends = new ArrayList<>();
        requestStatus = RequestStatus.IDLE;
    }

    /**
     *
     * @param refresh true if current data is refreshed
     */
    public void loadTrends(boolean refresh,double latitude,double longitude){
        this.latitude = latitude;
        this.longitude = longitude;

        if(requestStatus == RequestStatus.IDLE) {

            requestStatus = RequestStatus.EXECUTING;
            if (CommonUtils.isInternetAvailable()) {
                loadTrendsFromServer();
            } else {
                if(!refresh) {
                    trends.addAll(TrendsDBHelper.getHelper().getAllLocalTrends());
                    requestStatus = RequestStatus.COMPLETED;

                    if (trendsReceivedListener != null) {
                        trendsReceivedListener.onCompleted(trends);
                        requestStatus = RequestStatus.IDLE;
                        trends.clear();
                    }
                }else {
                    sendError(Messages.NO_NET_CONNECTION);
                }
            }
        }

    }

    /**
     *
     * @param listener callback for trends data
     */
    public void getTrends(OnTrendsReceivedListener listener){
        this.trendsReceivedListener = listener;

        if(requestStatus == RequestStatus.COMPLETED){
            listener.onCompleted(trends);
            requestStatus = RequestStatus.IDLE;
            trends.clear();
        }
    }

    /**
     *
     * @param ifUpVote true if opinion is voted
     * @param opinionId unique id of opinion
     * @param listener callback from server
     */
    public void upVoteDownVote(boolean ifUpVote, String opinionId, final OnUVDVOperationCompleteListener listener){
        LUARequest updvRequest= new LUARequest();
        updvRequest.opinion_id = opinionId;
        updvRequest.id = User.getInstance().getId();

        if(ifUpVote){
            updvRequest.operation_chosen = 1;
        }
        else{
            updvRequest.operation_chosen = 0;
        }

        //update server

        TheForumApplication.getClient().invokeApi("local_upvote", updvRequest, LUAResponse.class,
                new ApiOperationCallback<LUAResponse>() {
                    @Override
                    public void onCompleted(LUAResponse result, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            listener.onCompleteMessage("Opinion has been UpVoted");
                        } else {
                            listener.onErrorMessage(exception.getMessage());
                        }
                    }
                });

    }

    public void removeUpDownVote(final boolean ifUpVote, String opinionId,
                                 final OnRUDAOperationCompleteListener listener){
        LRUDARequest updvRequest= new LRUDARequest();
        updvRequest.opinion_id = opinionId;
        updvRequest.id = User.getInstance().getId();

        if(ifUpVote){
            updvRequest.operation_chosen = 1;
        }
        else{
            updvRequest.operation_chosen = 0;
        }

        //update server

        TheForumApplication.getClient().invokeApi("local_remove_up_down", updvRequest, LRUDAResponse.class,
                new ApiOperationCallback<LRUDAResponse>() {
                    @Override
                    public void onCompleted(LRUDAResponse result, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            if (ifUpVote)
                                listener.onCompleteMessage("Upvote Removed");
                            else listener.onCompleteMessage("Downvote Removed");
                        } else {
                            //listener.onCompleteMessage(exception.getMessage());
                            listener.onErrorMessage(Messages.NO_NET_CONNECTION);
                        }
                    }

                });

    }

    public void directUpDownVoteChange(final boolean ifUpVote, String opinionId,
                                       final OnDUDAOperationCompleteListener listener){
        LDUDCARequest updvRequest= new LDUDCARequest();
        updvRequest.opinion_id = opinionId;
        updvRequest.id = User.getInstance().getId();

        if(ifUpVote){
            updvRequest.operation_chosen = 1;
        }
        else{
            updvRequest.operation_chosen = 0;
        }

        //update server

        TheForumApplication.getClient().invokeApi("local_direct_up_downChange", updvRequest, LDUDCResponse.class,
                new ApiOperationCallback<LDUDCResponse>() {
                    @Override
                    public void onCompleted(LDUDCResponse result, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            if (!ifUpVote)
                                listener.onCompleteMessage("Opinion Downvoted");
                            else listener.onCompleteMessage("Opinion Upvoted");

                        } else {
                            listener.onErrorMessage(Messages.NO_NET_CONNECTION);
                        }
                    }
                });

    }



    private void loadTrendsFromServer() {
        LTARequest updvRequest= new LTARequest();

        updvRequest.uid = User.getInstance().getId();
        updvRequest.latitude=latitude;
        updvRequest.longitude=longitude;

        TheForumApplication.getClient().invokeApi("nearbytrends", updvRequest, LTAResponse.class,
                new ApiOperationCallback<LTAResponse>() {

                    @Override
                    public void onCompleted(LTAResponse result, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            Log.e("messagenull1",""+result.message);
                            if(result.message.equals("null")){
                                result.message=null;
                            }
                            try {

                                if (result.message != null) {
                                    Log.e("messagenull",""+result.message);
                                    JSONArray jsonArray = new JSONArray(result.message);
                                    requestStatus = RequestStatus.COMPLETED;

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = null;

                                            jsonObject = jsonArray.getJSONObject(i);

                                        TrendsDataModel trendDataModel = new TrendsDataModel();

                                        trendDataModel.setHoursLeft(Integer.parseInt(jsonObject.get("hours_left").toString()));
                                        trendDataModel.setTrendId(jsonObject.get("trends_id").toString());
                                        trendDataModel.setUpVoteCount(Integer.parseInt(jsonObject.get("upvotes").toString()));
                                        trendDataModel.setDownVoteCount(Integer.parseInt(jsonObject.get("downvotes").toString()));
                                        trendDataModel.setTopicName(jsonObject.get("topic_name").toString());
                                        trendDataModel.setTopicId(jsonObject.get("topic_id").toString());
                                        trendDataModel.setOpinionText(jsonObject.get("opinionText").toString());
                                        trendDataModel.setServerId(jsonObject.get("serverId").toString());
                                        trendDataModel.setDescription(jsonObject.get("description").toString());
                                        trendDataModel.setRenewalIds(jsonObject.get("renewalIds").toString());
                                        trendDataModel.setuId(jsonObject.get("uid").toString());
                                        trendDataModel.setRenewCount(Integer.parseInt(jsonObject.get("renewal").toString()));
                                        trendDataModel.setLatitude(Double.parseDouble(jsonObject.get("latitude").toString()));
                                        trendDataModel.setLongitude(Double.parseDouble(jsonObject.get("longitude").toString()));
                                        trendDataModel.setIsLocal(true);
                                        boolean statusReceived = false;
                                        if (jsonObject.get("upvote_ids") != null) {
                                            String upid = jsonObject.get("upvote_ids").toString();
                                            String[] upids = upid.split(" ");

                                            for (int j = 0; j < upids.length; j++) {
                                                if (upids[j].equals(User.getInstance().getId())) {
                                                    trendDataModel.setVoteStatus(VoteStatus.UPVOTED);
                                                    statusReceived = true;
                                                    break;
                                                }
                                            }

                                        }


                                            if (jsonObject.get("downvote_ids").toString() != null && !statusReceived) {
                                                String downid = jsonObject.get("downvote_ids").toString();
                                                String[] downids = downid.split(" ");
                                                for (int j = 0; j < downids.length; j++) {
                                                    if (downids[j].equals(User.getInstance().getId())) {
                                                        trendDataModel.setVoteStatus(VoteStatus.DOWNVOTED);
                                                        break;
                                                    }
                                                }

                                            }
                                            trends.add(trendDataModel);
                                        }

                                        // save the data to local database.
                                        TrendsDBHelper.getHelper().deleteAllLocalTrends();
                                        TrendsDBHelper.getHelper().addTrends(trends);

                                        /**
                                         * passing the data to ui
                                         */
                                        if (trendsReceivedListener != null) {
                                            trendsReceivedListener.onCompleted(trends);
                                            requestStatus = RequestStatus.IDLE;
                                            trends.clear();
                                        }

                                    } else {
                                        //sendError(Messages.SERVER_ERROR);
                                    }

                                } catch (JSONException e) {
                                    sendError(Messages.NO_NET_CONNECTION);
                                }

                            } else {
                                sendError(Messages.SERVER_ERROR);
                            }
                        }


                    });
                }

    private void sendError(String message){
        requestStatus = RequestStatus.IDLE;
        if (trendsReceivedListener != null) {
            trendsReceivedListener.onError(message);
        }
    }


    public void getTopicDetails(final String topic_id,final OnTopicDetailReceived listener){
        if(mTopic == null) mTopic = TheForumApplication.getClient().getTable(areatopics.class);
        AsyncTask<Void, Void, areatopics> task= new AsyncTask<Void, Void, areatopics>() {

            @Override
            protected areatopics doInBackground(Void... voids) {
                MobileServiceList<areatopics> result;
                try {
                    result = mTopic.where().field("topic_id").eq(topic_id).execute().get();
                    return result.get(0);
                } catch (Exception e) {
                    listener.onError(Messages.NO_NET_CONNECTION);
                    return null;
                }

            }

            @Override
            protected void onPostExecute(areatopics topic) {
                super.onPostExecute(topic);

                if(topic!=null) {
                    TopicDataModel topicDataModel = new TopicDataModel(topic);
                    listener.onCompleted(topicDataModel);
                }
            }
        };

        runAsyncTask3(task);
    }







    private static AsyncTask<Void, Void, areatopics> runAsyncTask3(AsyncTask<Void, Void, areatopics> task) {
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