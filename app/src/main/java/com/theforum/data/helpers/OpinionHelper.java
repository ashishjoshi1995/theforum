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
import com.theforum.utils.enums.VoteStatus;

import java.util.ArrayList;

/**
 * @author  Ashish on 12/31/2015.
 */
public class OpinionHelper {

    private static OpinionHelper mOpinionHelper;
    private  MobileServiceTable<opinion> mOpinion;

    private ArrayList<OpinionDataModel> opinionList;

    private OnOpinionAddListener opinionAddListener;


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

                        if(opinions.get(i).getUpVoted_ids()!=null) {
                            String upid = opinions.get(i).getUpVoted_ids();
                            String[] upids = upid.split(" ");

                            for(int j=0;j<upids.length;j++){
                                if(upids[j].equals(User.getInstance().getId())){
                                    opinionDataModel.setVoteStatus(VoteStatus.UPVOTED);
                                }
                                break;
                            }
                        }

                        if(opinions.get(i).getDownVotes_ids()!=null) {
                            String downid = opinions.get(i).getUpVoted_ids();
                            String[] downids = downid.split(" ");
                            for(int j=0;j<downids.length;j++){
                                if(downids[j].equals(User.getInstance().getId())){
                                    opinionDataModel.setVoteStatus(VoteStatus.DOWNVOTED);
                                    break;
                                }
                            }
                        }

                        opinionList.add(opinionDataModel);
                    }


                    listener.onCompleted(opinionList);
                    opinionList.clear();
                }
            }

        };

        runAsyncTask2(task);
    }
    /*public void upVoteDownVote(Boolean ifUpVote,String opinionId,
                               final OnUVDVOperationCompleteListener listener){

        TrendingInput updvRequest= new TrendingInput();

        updvRequest.uid = User.getInstance().getId();



        TheForumApplication.getClient().invokeApi("trendingopininos", updvRequest, TrendingResponse.class,
                new ApiOperationCallback<TrendingResponse>() {
            @Override
            public void onCompleted(TrendingResponse result, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    listener.onCompleteMessage("The process has fucking been completed");
                    Log.e("message UpdvAPi", result.message);
                    try {
                        JSONArray jsonArray = new JSONArray(result.message);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.e("message UpdvAPi", jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    listener.onCompleteMessage(exception.getMessage());
                }
            }
        });

    }*/

    public void upVoteDownVote(Boolean ifUpVote,String opinionId,
                               final OnUVDVOperationCompleteListener listener){

        UPDVRequest updvRequest= new UPDVRequest();
        updvRequest.opinion_id = opinionId;

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
                    listener.onCompleteMessage("Opinion Upvoted");
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

                            OpinionDataModel opinion = new OpinionDataModel(entity);
                            if(exception == null) {
                                listener.onCompleted(opinion);

                                if(opinionAddListener!= null){
                                    opinionAddListener.onCompleted(opinion);
                                }
                            }
                            else {
                                listener.onError(exception.getMessage());
                                if(opinionAddListener!= null){
                                    opinionAddListener.onError(exception.getMessage());
                                }
                            }
                        }

                    });
                } catch (Exception e) {
                    listener.onError(e.getMessage());

                    if(opinionAddListener!= null){
                        opinionAddListener.onError(e.getMessage());
                    }
                }
                return null;
            }
        };

        runAsyncTask(task);
    }

    /**
     * called by ui activity to receive callback whenever new opinion is added, so that
     * it can update its ui
     *
     * @param listener when new opinion is added this interface gives the callback
     *
     */
    public void addNewOpinionAddedListener(OnOpinionAddListener listener){
        this.opinionAddListener = listener;
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
        void onCompleted(OpinionDataModel opinion);
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
