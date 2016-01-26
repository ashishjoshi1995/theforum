package com.theforum.data.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.theforum.TheForumApplication;
import com.theforum.data.helpers.trendinOpinionApi.TrendingInput;
import com.theforum.data.helpers.trendinOpinionApi.TrendingResponse;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVRequest;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVResponse;
import com.theforum.data.local.database.trendsDB.TrendsDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.data.server.opinion;
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

    private static TrendsHelper trendsHelper;
    private MobileServiceTable<opinion> mOpinion;
    private MobileServiceTable<topic> mTopic;

    private RequestStatus requestStatus;
    private ArrayList<TrendsDataModel> trends;
    private OnTrendsReceivedListener trendsReceivedListener;


    public static TrendsHelper getHelper(){
        if(trendsHelper ==null) trendsHelper = new TrendsHelper();
        return trendsHelper;
    }

    private TrendsHelper(){
        mOpinion = TheForumApplication.getClient().getTable(opinion.class);
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
                            //Log.e("The process has fucking been completed","yes");
                            Log.e("message UpdvAPi", result.message);
                            try {

                                if (result.message != null) {
                                    JSONArray jsonArray = new JSONArray(result.message);
                                    //JSONObject jsonObject = jsonArray.getJSONObject(0);
                                   // Log.e("message UpdvAPi", jsonObject.toString());
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


                                        if(jsonObject.get("upvote_ids").toString()!=null) {
                                            String upid = jsonObject.get("upvote_ids").toString();
                                            String[] upids = upid.split(" ");
                                            for(int j=0;j<upids.length;j++){
                                                if(upids[j].equals(User.getInstance().getId())){
                                                    topic.setVoteStatus(VoteStatus.UPVOTED);
                                                    break;
                                                }
                                            }

                                        }
                                        else if(jsonObject.get("downvote_ids").toString()!=null) {
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
                                    if (trendsReceivedListener != null) {
                                        trendsReceivedListener.onCompleted(trends);
                                        requestStatus = RequestStatus.IDLE;
                                        trends.clear();
                                    }

                                    // save the data to local database.
                                    TrendsDBHelper.getHelper().deleteAllTrends();
                                    TrendsDBHelper.getHelper().addTrends(trends);

                                }  else {
                                    if (trendsReceivedListener != null) {
                                        trendsReceivedListener.onError("Check Your Internet Connection");
                                    }
                                    requestStatus = RequestStatus.IDLE;

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                           Log.e("fuck","you");
                        }
                    }
                });
    }

   /* private void loadTopicsFromServer(){
        AsyncTask<Void, Void, MobileServiceList<opinion>> task = new AsyncTask<Void, Void,
                MobileServiceList<opinion>>() {

            @Override
            protected MobileServiceList<opinion> doInBackground(Void... voids) {
                MobileServiceList<opinion> result = null;
                try {
                    result = mOpinion.orderBy("upvotes", QueryOrder.Descending).top(30).execute().get();
                } catch (Exception e) {
                    if (trendsReceivedListener != null) {
                        trendsReceivedListener.onError(e.getMessage());
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(MobileServiceList<opinion> opinions) {
                super.onPostExecute(opinions);
                    Log.e("opinions",opinions.toString());
                if (opinions != null) {
                    requestStatus = RequestStatus.COMPLETED;
                    for (int i = 0; i < opinions.size(); i++) {
                        TrendsDataModel trendsDataModel = new TrendsDataModel(opinions.get(i));

                        if(opinions.get(i).getUpVoted_ids()!=null) {
                            String upid = opinions.get(i).getUpVoted_ids();
                            String[] upids = upid.split(" ");
                            for(int j=0;j<upids.length;j++){
                                if(upids[j].equals(User.getInstance().getId())){
                                    trendsDataModel.setVoteStatus(VoteStatus.UPVOTED);
                                    break;
                                }
                            }

                        }
                        if(opinions.get(i).getDownVotes_ids()!=null) {
                            String downid = opinions.get(i).getUpVoted_ids();
                            String[] downids = downid.split(" ");
                            for(int j=0;j<downids.length;j++){
                                if(downids[j].equals(User.getInstance().getId())){
                                    trendsDataModel.setVoteStatus(VoteStatus.DOWNVOTED);
                                    break;
                                }
                            }
                        }


                        trends.add(trendsDataModel);
                    }
                    if (trendsReceivedListener != null) {
                        trendsReceivedListener.onCompleted(trends);
                        requestStatus = RequestStatus.IDLE;
                        trends.clear();
                    }

                    // save the data to local database.

                    TrendsDBHelper.getHelper().addTrends(trends);

                }  else {
                    if (trendsReceivedListener != null) {
                        trendsReceivedListener.onError("Check Your Internet Connection");
                    }
                    requestStatus = RequestStatus.IDLE;

                }
            }
        };

        runAsyncTask2(task);
    }*/
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
                    listener.onError("Check Your Internet Connection");

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
                    listener.onError("Check Your Internet Connection");
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




    public void upvoteDownvote(Boolean ifUpvote,String opinionId, final OnUVDVOperationCompleteListener listener){
        UPDVRequest updvRequest= new UPDVRequest();
        updvRequest.opinion_id = opinionId;
        updvRequest.id = User.getInstance().getId();
        if(ifUpvote){
            //update UI
            //update Local db
            updvRequest.operation_chosen = 1;
        }
        else{

            //update UI
            //update Local db
            updvRequest.operation_chosen = 0;
        }
        //update server


        TheForumApplication.getClient().invokeApi("upvote", updvRequest, UPDVResponse.class, new ApiOperationCallback<UPDVResponse>() {
            @Override
            public void onCompleted(UPDVResponse result, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    listener.onCompleteMessage("Opinion has been Upvoted");
                    Log.e("message UpdvAPi", result.message);
                } else {
                    listener.onCompleteMessage(exception.getMessage());
                }
            }
        });

    }




    private AsyncTask<Void, Void, MobileServiceList<opinion>> runAsyncTask2(AsyncTask<Void, Void,
            MobileServiceList<opinion>> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
