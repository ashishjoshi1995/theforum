package com.theforum.data.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.theforum.TheForumApplication;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVRequest;
import com.theforum.data.helpers.upvoteDownvoteApi.UPDVResponse;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.data.server.opinion;
import com.theforum.utils.User;

import java.util.ArrayList;

/**
 * @author  Ashish on 12/31/2015.
 */
public class OpinionHelper {

    private static OpinionHelper mOpinionHelper;
    private  MobileServiceTable<opinion> mOpinion;

    private ArrayList<OpinionDataModel> opinionList;


    public static OpinionHelper getHelper(){
        if(mOpinionHelper==null) mOpinionHelper = new OpinionHelper();
        return mOpinionHelper;
    }

    private OpinionHelper(){
        mOpinion = TheForumApplication.getClient().getTable(opinion.class);
        opinionList = new ArrayList<>();
    }


    public  void getTopicSpecificOpinions(final String topicId, final OnOpinionsReceivedListener listener){

        AsyncTask<Void, Void, MobileServiceList<opinion>> task = new AsyncTask<Void, Void, MobileServiceList<opinion>>() {

            @Override
            protected MobileServiceList<opinion> doInBackground(Void... voids) {
                MobileServiceList<opinion> result = null;
                try {
                    result = mOpinion.where().field("topic_id").eq(topicId).execute().get();
                } catch (Exception e) {
                    listener.onError(e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(MobileServiceList<opinion> opinions) {
                super.onPostExecute(opinions);

                if(opinions!=null) {

                    OpinionDataModel opinionDataModel;
                    for (int i = 0; i < opinions.size(); i++) {
                        opinionDataModel = new OpinionDataModel(opinions.get(i));
                        opinionList.add(opinionDataModel);
                    }

                    listener.onCompleted(opinionList);
                    opinionList.clear();
                }
            }

        };

        runAsyncTask2(task);
    }

    public void upVoteDownVote(Boolean ifUpVote,String opinionId,
                               final OnUVDVOperationCompleteListener listener){

        UPDVRequest updvRequest= new UPDVRequest();
        updvRequest.opinion_id = opinionId;
        updvRequest.opinion_owner_id = User.getInstance().getId();

        if(ifUpVote){
            //update UI
            //update Local db
            updvRequest.operation_chosen = 1;
        }
        else{

            //update UI
            updvRequest.operation_chosen = 0;
        }
        //update server


        TheForumApplication.getClient().invokeApi("upvote", updvRequest, UPDVResponse.class,
                new ApiOperationCallback<UPDVResponse>() {
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
        void onCompleted(ArrayList<OpinionDataModel> opinions);
        void onError(String error);
    }

    public interface OnUVDVOperationCompleteListener{
        /**
         *
         * @param  message opinion data model with updated params
         */
        void onCompleteMessage(String message);
    }


}
