package com.theforum.data.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.theforum.TheForumApplication;
import com.theforum.data.server.flaggedTopics;
import com.theforum.data.server.flags;
import com.theforum.utils.User;

import java.util.List;

/**
 * @author Ashish
 * @since 2/9/2016
 */
public class FlagHelper {

    private MobileServiceTable<flags> mFlagTable;
    private MobileServiceTable<flaggedTopics> mFlaggedTopics;
    public FlagHelper(){mFlagTable = TheForumApplication.getClient().getTable(flags.class);}
    public FlagHelper(String topic){mFlaggedTopics = TheForumApplication.getClient().getTable(flaggedTopics.class);}

    public void addFlagOpinionRequest(final String opinionId, final String opinionText , final String topicId, final String serverId){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            MobileServiceList<flags> ash = null;
            @Override
            protected Void doInBackground(Void... params) {
                try {

                 mFlagTable.where().field("opinion_id").eq(opinionId).execute(new TableQueryCallback<flags>() {
                    @Override
                    public void onCompleted(final List<flags> result, int count, Exception exception, ServiceFilterResponse response) {

                        if( result!=null && result.size()>0){

                            String s = result.get(0).getApndUidOfFlaggers();
                            String[] s2 = s.split(" ");
                            for(int i =0;i<s2.length;i++){
                                if(User.getInstance().getId().equals(s2[i])){
                                    //already flagged

                                    break;
                                }
                                else {
                                    int j = result.get(0).getFlagCount();

                                    j++;
                                    result.get(0).setFlagCount(j);

                                    s+=" ";
                                    s+= User.getInstance().getId();

                                    mFlagTable.update(result.get(0), new TableOperationCallback<flags>() {
                                        @Override
                                        public void onCompleted(flags entity, Exception exception, ServiceFilterResponse response) {
                                            if(exception!=null){

                                            }
                                            else {

                                            }
                                        }
                                    });

                                }
                            }
                        }
                        else{
                            flags f = new flags();
                            f.setFlagCount(1);
                            f.setOpinionText(opinionText);
                            f.setTopicId(topicId);
                            f.setApndUidOfFlaggers(User.getInstance().getId());
                            f.setOpinionId(opinionId);
                            f.setTo_delete_id(serverId);
                            mFlagTable.insert(f, new TableOperationCallback<flags>() {
                                @Override
                                public void onCompleted(flags entity, Exception exception, ServiceFilterResponse response) {
                                    if(exception==null){

                                    }
                                }
                            });

                        }
                    }
                });
                } catch (Exception e) {
                    e.printStackTrace();

                }

                return null;
            }

        };
        runAsyncTask(task);
    }

    public void addFlagTopicRequest(final String topicId){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            MobileServiceList<flaggedTopics> ash = null;
            @Override
            protected Void doInBackground(Void... params) {
                mFlaggedTopics.where().field("topic_id").eq(topicId).execute(new TableQueryCallback<flaggedTopics>() {
                    @Override
                    public void onCompleted(List<flaggedTopics> result, int count, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            if (result != null && result.size() > 0) {
                                boolean boolToAct = true;
                                //flag exist,
                                String ids = result.get(0).getApndFlaggerIds();
                                String[] idsS = ids.split(" ");
                                for (int i = 0; i < idsS.length; i++) {
                                    if (idsS[i] == User.getInstance().getId()) {
                                        boolToAct = false;
                                        break;
                                        //take no action
                                    }

                                }
                                if (boolToAct) {
                                    //id not found
                                    int j = result.get(0).getFlagCount();
                                    j++;
                                    result.get(0).setFlagCount(j);
                                    String d = result.get(0).getApndFlaggerIds();
                                    d += " ";
                                    d += User.getInstance().getId();
                                    result.get(0).setApndFlaggerIds(d);
                                    mFlaggedTopics.update(result.get(0));
                                }
                            } else {
                                //first flag request
                                flaggedTopics topic = new flaggedTopics();
                                topic.setApndFlaggerIds(User.getInstance().getId());
                                topic.setFlagCount(1);
                                topic.setTopicId(topicId);
                                mFlaggedTopics.insert(topic);
                            }
                        } else {

                        }
                    }
                });

              return null;
            }

        };
        runAsyncTask1(task);

    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private AsyncTask<Void, Void, Void> runAsyncTask1(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
