package com.theforum.data.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.theforum.TheForumApplication;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.data.helpers.trendinOpinionApi.TrendingInput;
import com.theforum.data.helpers.trendinOpinionApi.TrendingResponse;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVRequest;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVResponse;
import com.theforum.utils.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author  Ashish on 12/31/2015.
 */
public class OpinionHelper {

    private static OpinionHelper mOpinionHelper;
    private  MobileServiceTable<opinion> mOpinion;
    private String uid;

    public static OpinionHelper getHelper(){
        if(mOpinionHelper==null) mOpinionHelper = new OpinionHelper();
        return mOpinionHelper;
    }

    private OpinionHelper(){
        mOpinion = TheForumApplication.getClient().getTable(opinion.class);
    }


    public void getTrendingOpinions(final OnTrendingReceiveListener listener){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            MobileServiceList<topic> topics = null;
            MobileServiceList<opinion> opinions = null;


            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    TrendingInput inputClass = new TrendingInput();
                    inputClass.uid = User.getInstance().getId();
                    TheForumApplication.getClient().invokeApi("trendingopininos", inputClass, TrendingResponse.class,
                            new ApiOperationCallback<TrendingResponse>() {
                                @Override
                                public void onCompleted(TrendingResponse result, Exception exception, ServiceFilterResponse response) {
                                    Log.e("herewadhwanigo", result.message);
                                    if (exception == null) {
                                        try {
                                            //JSONObject jsnobject = new JSONObject(result.message);
                                            String[] a = result.message.split("|||||");
                                            Log.e("asasas",a[1].toString());
                                            Log.e("wewewe",a[0].toString());
                                            JSONArray jsonArray = new JSONArray(a[1]);
                                            JSONArray jsonArray1 = new JSONArray(a[0]);
                                            // ArrayList<topic> topicList = new ArrayList<topic>();
                                            //JSONArray jArray = (JSONArray)jsonObject;
                                            Log.e("test1","test");
                                            if (jsonArray != null) {
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

                                            } else listener.onError("empty JSON");
                                            if (jsonArray1 != null) {
                                                for (int i = 0; i < jsonArray1.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                                    opinion opinion = new opinion();
                                                    opinion.setServerId(jsonObject1.get("id").toString());
                                                    opinion.setDownVotes(Integer.parseInt(jsonObject1.get("downvotes").toString()));
                                                    opinion.setUpVotes(Integer.parseInt(jsonObject1.get("upvotes").toString()));
                                                    opinion.setmNotifCount(Integer.parseInt(jsonObject1.get("notif_count").toString()));
                                                    opinion.setmNotifNewDownvotes(Integer.parseInt(jsonObject1.get("notif_newdownvotes").toString()));
                                                    opinion.setmNotifNewUpvotes(Integer.parseInt(jsonObject1.get("notif_newupvotes").toString()));
                                                    opinion.setOpinionName(jsonObject1.get("opinion").toString());
                                                    opinion.setOpinionId(jsonObject1.get("opinion_id").toString());
                                                    opinion.setUserId(jsonObject1.get("uid").toString());
                                                    opinion.setTopicId(jsonObject1.get("topic_id").toString());
                                                    opinion.setTopicName(jsonObject1.get("topic").toString());
                                                    Log.e("test2", "test");

                                                    opinions.add(opinion);
                                                    Log.e("ashish", topics.get(i).getServerId());
                                                }

                                            } else listener.onError("empty JSON");
                                        } catch (JSONException e) {
                                            listener.onError(e.getMessage());
                                        }
                                    } else {
                                        listener.onError(exception.getMessage());
                                    }


                                }
                            });
                } catch (Exception e) {
                    listener.onError(e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                listener.onCompleted(topics, opinions);
                Log.e("test3", "test");
            }
        };
        runAsyncTask(task);
    }

    public  void getTopicSpecificOpinions(final String topic_id, final OnOpinionsReceivedListener listener){

        if(mOpinion == null) mOpinion = TheForumApplication.getClient().getTable(opinion.class);

        AsyncTask<Void, Void, MobileServiceList<opinion>> task = new AsyncTask<Void, Void, MobileServiceList<opinion>>() {

            @Override
            protected MobileServiceList<opinion> doInBackground(Void... voids) {
                MobileServiceList<opinion> result = null;
                try {
                    result = mOpinion.where().field("topic_id").eq(topic_id).execute().get();
                } catch (Exception e) {
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
                if(exception == null){
                    listener.onCompleteMessage("The process has fucking been completed");
                Log.e("message UpdvAPi", result.message);
            }else {
                    listener.onCompleteMessage(exception.getMessage());
                }
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

    private AsyncTask<Void, Void, MobileServiceList<opinion>> runAsyncTask2(AsyncTask<Void, Void,
            MobileServiceList<opinion>> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private static AsyncTask<Void, Void,Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
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

    public interface OnUVDVOperationCompleteListener{
        /**
         *
         * @param  message opinion data model with updated params
         */
        void onCompleteMessage(String message);
    }
    public interface OnTrendingReceiveListener{
        /**
         *
         * @param  topics model with updated params
         */
        void onCompleted(ArrayList<topic> topics ,ArrayList<opinion> opinions);
        void onError(String error);
    }
}
