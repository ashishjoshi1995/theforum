package com.theforum.data.helpers;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.theforum.TheForumApplication;
import com.theforum.data.dataModels.opinion;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.theforum.data.dataModels.topic;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVRequest;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVResponse;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ashish on 12/31/2015.
 */
public class OpinionHelper {
    //create Opinion, get opinions, upvoteDownvote
    private MobileServiceClient mClient;
    private MobileServiceTable<opinion> mOpinion;
    //opinion opinion;


    private String uid;

    public OpinionHelper(){
        this.mClient = TheForumApplication.getClient();
        getTable();
    }


   private void getTable(){
       mOpinion = mClient.getTable(opinion.class);
   }


    public void getTrendingOpinions(final OnOpinionsReceivedListener listener){
        AsyncTask<Void, Void, MobileServiceList<opinion>> task = new AsyncTask<Void, Void, MobileServiceList<opinion>>() {


            @Override
            protected MobileServiceList<opinion> doInBackground(Void... voids) {
                MobileServiceList<opinion> result = null;
                try {
                   result = mOpinion.orderBy("upvotes", QueryOrder.Descending).execute().get();
                } catch (InterruptedException e) {
                    listener.onError(e.getMessage());
                } catch (ExecutionException e) {
                    listener.onError(e.getMessage());
                }

                return result;
            }

            @Override
            protected void onPostExecute(MobileServiceList<opinion> opinions) {
                super.onPostExecute(opinions);
                listener.onCompleted(opinions);
            }
        };
        runAsyncTask2(task);
    }

    public void getTopicSpecificOpinions(final String topic_id, final OnOpinionsReceivedListener listener){
        AsyncTask<Void, Void, MobileServiceList<opinion>> task = new AsyncTask<Void, Void, MobileServiceList<opinion>>() {


            @Override
            protected MobileServiceList<opinion> doInBackground(Void... voids) {
                MobileServiceList<opinion> result = null;
                try {
                    result = mOpinion.where().field("topic_id").eq(topic_id).execute().get();
                } catch (InterruptedException e) {
                    listener.onError(e.getMessage());
                } catch (ExecutionException e) {
                    listener.onError(e.getMessage());
                }

                return result;
            }

            @Override
            protected void onPostExecute(MobileServiceList<opinion> opinions) {
                super.onPostExecute(opinions);
                listener.onCompleted(opinions);
            }
        };
        runAsyncTask2(task);
    }

    public void upvoteDownvote(Boolean ifUpvote,opinion opinion1){
        UPDVRequest updvRequest= new UPDVRequest();
        updvRequest.opinion_id = opinion1.getmOpinionId();
        updvRequest.opinion_owner_id = opinion1.getmUid();
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


        mClient.invokeApi("upvote", updvRequest, UPDVResponse.class, new ApiOperationCallback<UPDVResponse>() {
            @Override
            public void onCompleted(UPDVResponse result, Exception exception, ServiceFilterResponse response) {
                Log.e("message UpdvAPi",result.message);
            }
        });

    }

    public void addOpinion(final opinion opinion , final OnOpinionAddListener listener){

    AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mOpinion.insert(opinion, new TableOperationCallback<opinion>() {
                    @Override
                    public void onCompleted(opinion entity, Exception exception, ServiceFilterResponse response) {
                        Log.e("opinionAdded", "opinionAdded");
                        if(exception == null) {
                            listener.onCompleted(entity);
                        }
                        else {
                            listener.onError(exception.getMessage());
                        }

                    }


                });

            } catch (Exception e) {
                listener.onError(e.getMessage());
            }

            return null;
        }


    };
    runAsyncTask(task);

}


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AsyncTask<Void, Void, MobileServiceList<opinion>> runAsyncTask2(AsyncTask<Void, Void, MobileServiceList<opinion>> task) {

        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AsyncTask<Void, Void,Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {

        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public interface OnOpinionAddListener{
        /**
         *
         * @param  opinion opinion data model with updated params
         */
        void onCompleted(opinion opinion);
        void onError(String error);
    }

    public interface OnOpinionsReceivedListener{
        /**
         *
         * @param  opinions opinion data model with updated params
         */
        void onCompleted(ArrayList<opinion> opinions);
        void onError(String error);
    }
}
