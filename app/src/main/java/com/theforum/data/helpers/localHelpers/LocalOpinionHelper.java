package com.theforum.data.helpers.localHelpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.theforum.TheForumApplication;
import com.theforum.constants.Messages;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.data.server.areaopinions;
import com.theforum.utils.User;
import com.theforum.utils.enums.VoteStatus;

import java.util.ArrayList;

/**
 * @author Ashish
 * @since 2/11/2016
 */
public class LocalOpinionHelper {
    private static LocalOpinionHelper mOpinionHelper;
    private MobileServiceTable<areaopinions> mOpinion;
    private ArrayList<OpinionDataModel> opinionList;
    private OnOpinionAddListener opinionAddListener;

    public static LocalOpinionHelper getHelper(){
        if(mOpinionHelper==null) mOpinionHelper = new LocalOpinionHelper();
        return mOpinionHelper;
    }

    private LocalOpinionHelper(){
        mOpinion = TheForumApplication.getClient().getTable(areaopinions.class);
        opinionList = new ArrayList<>();
    }

    public  void getTopicSpecificOpinions(final String topicId, final OnOpinionsReceivedListener listener){

        AsyncTask<Void, Void, MobileServiceList<areaopinions>> task = new AsyncTask<Void, Void, MobileServiceList<areaopinions>>() {

            @Override
            protected MobileServiceList<areaopinions> doInBackground(Void... voids) {
                MobileServiceList<areaopinions> result = null;
                try {
                    result = mOpinion.where().field("topic_id").eq(topicId).execute().get();
                } catch (Exception e) {
                    listener.onError(Messages.NO_NET_CONNECTION);
                }
                return result;
            }

            @Override
            protected void onPostExecute(MobileServiceList<areaopinions> opinions) {
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
                                    break;
                                }
                            }
                        }

                        if(opinions.get(i).getDownVotes_ids()!=null) {
                            String downid = opinions.get(i).getDownVotes_ids();
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


    public void addOpinion(final areaopinions opinion , final OnOpinionAddListener listener){
        Log.e("Loacalhelper","addpopion called");
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mOpinion.insert(opinion, new TableOperationCallback<areaopinions>() {
                        @Override
                        public void onCompleted(areaopinions entity, Exception exception, ServiceFilterResponse response) {

                            if(exception == null) {
                                OpinionDataModel opinion = new OpinionDataModel(entity);
                                listener.onCompleted(opinion, false);

                                if(opinionAddListener!= null){
                                    opinionAddListener.onCompleted(opinion,false);
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

    public void updateOpinion(final OpinionDataModel opinionDataModel,final OnOpinionAddListener listener) {
        // VoteStatus voteStatus =
        AsyncTask<Void, Void,Void> task= new AsyncTask<Void, Void, Void>() {
            areaopinions o;
            MobileServiceList<areaopinions> result;
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    result = mOpinion.where().field("opinion_id").eq(opinionDataModel.getOpinionId()).execute().get();

                } catch (Exception e) {
                    listener.onError(Messages.NO_NET_CONNECTION);

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                o= result.get(0);
                o.setOpinionName(opinionDataModel.getOpinionText());
                mOpinion.update(o, new TableOperationCallback<areaopinions>() {
                    @Override
                    public void onCompleted(areaopinions entity, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            //OpinionDataModel opinion = new OpinionDataModel(entity);
                            listener.onCompleted(opinionDataModel, true);
                            if(opinionAddListener!= null){
                                opinionAddListener.onCompleted(opinionDataModel, true);
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

    private AsyncTask<Void, Void, Void> runAsyncTask3(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AsyncTask<Void, Void, MobileServiceList<areaopinions>> runAsyncTask2(AsyncTask<Void, Void,
            MobileServiceList<areaopinions>> task) {
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
        void onCompleted(OpinionDataModel opinion, boolean isUpdated);
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


}
