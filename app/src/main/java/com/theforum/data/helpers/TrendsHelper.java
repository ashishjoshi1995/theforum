package com.theforum.data.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.theforum.TheForumApplication;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVRequest;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVResponse;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;

import java.util.ArrayList;

/**
 * @author DEEPANKAR
 * @since 19-01-2016.
 */
public class TrendsHelper {

    private static TrendsHelper trendsHelper;
    private MobileServiceTable<opinion> mOpinion;
    private MobileServiceTable<topic> mTopic;

    private ArrayList<TrendsDataModel> trends;


    public static TrendsHelper getHelper(){
        if(trendsHelper ==null) trendsHelper = new TrendsHelper();
        return trendsHelper;
    }

    private TrendsHelper(){
        mOpinion = TheForumApplication.getClient().getTable(opinion.class);
        trends = new ArrayList<>();
    }


    public void getTrendingOpinions(final OnTrendsReceivedListener listener){

        AsyncTask<Void, Void, MobileServiceList<opinion>> task = new AsyncTask<Void, Void, MobileServiceList<opinion>>() {
            @Override
            protected MobileServiceList<opinion> doInBackground(Void... voids) {
                MobileServiceList<opinion> result = null;
                try {
                    result = mOpinion.orderBy("upvotes", QueryOrder.Descending).top(30).execute().get();
                } catch (Exception e) {
                    listener.onError(e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(MobileServiceList<opinion> opinions) {
                super.onPostExecute(opinions);

                if(opinions!=null) {
                    for (int i = 0; i < opinions.size(); i++) {
                        TrendsDataModel trendsDataModel = new TrendsDataModel(opinions.get(i));
                        trends.add(trendsDataModel);
                    }

                    listener.onCompleted(trends);
                }else listener.onError("Check Your Internet Connection");
            }
        };

        runAsyncTask2(task);
    }

    public void getTopicDetails(final String topic_id,final OnTopicDetailReceived listener){
        if(mTopic == null) mTopic = TheForumApplication.getClient().getTable(topic.class);
        AsyncTask<Void, Void,topic> task= new AsyncTask<Void, Void, topic>() {

            @Override
            protected topic doInBackground(Void... voids) {
                MobileServiceList<topic> result = null;
                try {
                    result = mTopic.where().field("topic_id").eq(topic_id).execute().get();
                } catch (Exception e) {
                    listener.onError(e.getMessage());
                }
                return result.get(0);
            }

            @Override
            protected void onPostExecute(topic topic) {
                super.onPostExecute(topic);

                if(topic!=null) {
                    TopicDataModel topicDataModel = new TopicDataModel(topic);
                    listener.onCompleted(topicDataModel);
                }else listener.onError("Check Your Internet Connection");
            }
        };

        runAsyncTask3(task);
    }


    public void upvoteDownvote(Boolean ifUpvote,opinion opinion1, final OnUVDVOperationCompleteListener listener){
        UPDVRequest updvRequest= new UPDVRequest();
        updvRequest.opinion_id = opinion1.getOpinionId();
        updvRequest.opinion_owner_id = opinion1.getUserId();
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
                    listener.onCompleteMessage("The process has fucking been completed");
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
