package com.theforum.data.helpers;

import android.os.AsyncTask;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.theforum.Constants;
import com.theforum.User;
import com.theforum.data.dataModels.topic;
import com.theforum.data.dataModels.user;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ashish on 1/5/2016.
 */
public class LoadTopicHelper {
    private MobileServiceClient mClient;
    private MobileServiceTable<topic> mTopic;
    private MobileServiceTable<user> mUser;
    private String uid;

    public LoadTopicHelper(MobileServiceClient client){
        this.mClient = client;
        mTopic = mClient.getTable(topic.class);
    }

    private void initUserTable(){
        mUser = mClient.getTable(user.class);
    }

    public void loadLatestTopics(String operation, final int sortMode) {

        AsyncTask<Void, Void, ArrayList<topic>> task = new AsyncTask<Void, Void, ArrayList<topic>>() {
            MobileServiceList<topic> topics = null;
            MobileServiceList<user> users = null;
            @Override
            protected ArrayList<topic> doInBackground(Void... params) {
                try {
                switch (sortMode)
                {
                    case Constants.SORT_BASIS_LATEST:
                        topics = mTopic.orderBy("hours_left", QueryOrder.Ascending).execute().get();
                        break;
                    case Constants.SORT_BASIS_CREATED_BY_ME:
                        //to be written
                        initUserTable();
                       users =  mUser.where().field("uid").eq(User.getInstance().getForumId()).execute(new );
                        break;
                    case Constants.SORT_BASIS_LEAST_RENEWAL:
                        topics = mTopic.orderBy("renewal_request",QueryOrder.Ascending).execute().get();
                        break;
                    case Constants.SORT_BASIS_MOST_RENEWAL:
                        topics = mTopic.orderBy("renewal_request",QueryOrder.Descending).execute().get();
                        break;
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return topics;
            }

            @Override
            protected void onPostExecute(ArrayList<topic> topics) {
                super.onPostExecute(topics);
                if(sortMode==Constants.SORT_BASIS_CREATED_BY_ME){
                    users.get(0).getmCurrentTopics();
                }
            }
        };
    }
}
