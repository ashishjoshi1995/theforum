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
        Log.e("item_flag", "ok");
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            MobileServiceList<flags> ash = null;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.e("try","try");
                 mFlagTable.where().field("opinion_id").eq(opinionId).execute(new TableQueryCallback<flags>() {
                    @Override
                    public void onCompleted(final List<flags> result, int count, Exception exception, ServiceFilterResponse response) {
                        Log.e("onCompleted","OnCLompleted");
                        if( result!=null && result.size()>0){
                            Log.e("item_flag","ok");
                            String s = result.get(0).getApndUidOfFlaggers();
                            String[] s2 = s.split(" ");
                            for(int i =0;i<s2.length;i++){
                                if(User.getInstance().getId().equals(s2[i])){
                                    //already flagged
                                   Log.e("item_flag","ok");
                                    break;
                                }
                                else {
                                    int j = result.get(0).getFlagCount();
                                    Log.e("knkinjn",""+j);
                                    j++;
                                    result.get(0).setFlagCount(j);
                                    Log.e("reasasasasa", result.get(0).getFlagCount() + "");
                                    Log.e("1", result.get(0).getServerId());
                                    Log.e("2", result.get(0).getOpinionId());
                                    s+=" ";
                                    s+= User.getInstance().getId();

                                    mFlagTable.update(result.get(0), new TableOperationCallback<flags>() {
                                        @Override
                                        public void onCompleted(flags entity, Exception exception, ServiceFilterResponse response) {
                                            if(exception!=null){
                                                Log.e("asasas",exception.getMessage()+"");
                                                Log.e("id",entity.getOpinionId()+"");
                                                Log.e("server  id", entity.getServerId()+"");
                                                Log.e("5",result.get(0).getOpinionId()+"");
                                                Log.e("6", result.get(0).getServerId()+"");
                                            }
                                            else {
                                                Log.e("asasaszzzzzz", entity.getFlagCount()+"");
                                            }
                                        }
                                    });
                                    Log.e("3", result.get(0).getServerId());
                                    Log.e("4", result.get(0).getOpinionId());
                                    Log.e("item_flag", "ok");
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
                                        Log.e("6",entity.getServerId()+"");
                                    }
                                }
                            });
                            Log.e("item_flag", "null");
                        }
                    }
                });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("asasas","exception");
                }
                Log.e("return","return");
                return null;
            }

        };
        runAsyncTask(task);
    }

    public void addFlagTopicRequest(final String topicId){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            MobileServiceList<flags> ash = null;
            @Override
            protected Void doInBackground(Void... params) {
                mFlaggedTopics.where().field("topic_id").eq(topicId).execute(new TableQueryCallback<flaggedTopics>() {
                    @Override
                    public void onCompleted(List<flaggedTopics> result, int count, Exception exception, ServiceFilterResponse response) {
                        if(exception==null){
                            if(result!=null && result.size()>0){
                              boolean  boolToAct = true;
                                //flag exist,
                                String ids = result.get(0).getApndFlaggerIds();
                                String[] idsS = ids.split(" ");
                                for(int i=0;i<idsS.length;i++){
                                    if(idsS[i]==User.getInstance().getId()){
                                        boolToAct = false;
                                        break;
                                        //take no action
                                    }

                                }
                                if(boolToAct){
                                    //id not found
                                    int j = result.get(0).getFlagCount();
                                    j++;
                                    result.get(0).setFlagCount(j);
                                    String d = result.get(0).getApndFlaggerIds();
                                    d+=" ";
                                    d+=User.getInstance().getId();
                                    result.get(0).setApndFlaggerIds(d);
                                    mFlaggedTopics.update(result.get(0));
                                }
                            }
                            else {
                                //first flag request
                                flaggedTopics topic = new flaggedTopics();
                                topic.setApndFlaggerIds(User.getInstance().getId());
                                topic.setFlagCount(1);
                                topic.setTopicId(topicId);
                                mFlaggedTopics.insert(topic);
                            }
                        }
                        else {
                            Log.e("exception",exception.getMessage()+"");
                        }
                    }
                });

              return null;
            }

        };
        runAsyncTask(task);

    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
