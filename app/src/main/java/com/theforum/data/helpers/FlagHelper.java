package com.theforum.data.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.theforum.TheForumApplication;
import com.theforum.data.server.flags;
import com.theforum.utils.User;

import java.util.List;

/**
 * @author Ashish
 * @since 2/9/2016
 */
public class FlagHelper {

    private MobileServiceTable<flags> mFlagTable;
    public FlagHelper(){mFlagTable = TheForumApplication.getClient().getTable(flags.class);}

    public void addFlagOpinionRequest(final String opinionId, final String opinionText , final String topicId){
        Log.e("item_flag", "ok");
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            MobileServiceList<flags> ash = null;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.e("try","try");
                 mFlagTable.where().field("opinion_id").eq(opinionId).execute(new TableQueryCallback<flags>() {
                    @Override
                    public void onCompleted(List<flags> result, int count, Exception exception, ServiceFilterResponse response) {
                        Log.e("onCompleted","OnCLompleted");
                        if( result!=null && result.size()>0){
                            Log.e("item_flag","ok");
                            String s = result.get(0).getApndUidOfFlaggers();
                            String[] s2 = s.split(" ");
                            for(int i =0;i<s2.length;i++){
                            //    if(User.getInstance().getId().equals(s2[i])){
                                    //already flagged
                              //      Log.e("item_flag","ok");
                                //    break;
                               // }
                               // else {
                                    int j = result.get(0).getFlagCount();
                                Log.e("knkinjn",""+j);
                                    j++;
                                    result.get(0).setFlagCount(j);
                                    Log.e("reasasasasa",result.get(0).getFlagCount()+"");
                                    s+=" ";
                                    s+= User.getInstance().getId();
                                    mFlagTable.update(result.get(0), new TableOperationCallback<flags>() {
                                        @Override
                                        public void onCompleted(flags entity, Exception exception, ServiceFilterResponse response) {
                                            if(exception!=null){

                                            }
                                            else {
                                                Log.e("asasaszzzzzz", )
                                            }
                                        }
                                    });
                                    Log.e("item_flag", "ok");
                                //}
                            }
                        }
                        else{
                            flags f = new flags();
                            f.setFlagCount(1);
                            f.setOpinionText(opinionText);
                            f.setTopicId(topicId);
                            f.setApndUidOfFlaggers(User.getInstance().getId());
                            f.setOpinionId(opinionId);
                            mFlagTable.insert(f);
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

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
