package com.theforum.data.helpers;

import android.os.AsyncTask;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.theforum.TheForumApplication;
import com.theforum.data.dataModels.topic;
import com.theforum.data.dataModels.user;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ashish on 1/3/2016.
 */
public class SearchHelper {
    MobileServiceClient mMobileServiceClient;
    MobileServiceTable<topic> mTopicTable;

    public SearchHelper(){
        this.mMobileServiceClient = TheForumApplication.getClient();
        mTopicTable = mMobileServiceClient.getTable(topic.class);
    }


    public ArrayList<topic> Search(final String field){

        AsyncTask<Void, Void, ArrayList<topic>> task = new AsyncTask<Void, Void, ArrayList<topic>>(){
            MobileServiceList<topic> ash = null;
            @Override
            protected ArrayList<topic> doInBackground(Void... voids) {
                try {
                    ash = mTopicTable.where().field("uid").eq(field).orderBy("points", QueryOrder.Descending).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return ash;
            }

            @Override
            protected void onPostExecute(ArrayList<topic> topics) {
                super.onPostExecute(topics);


            }
        };
        return null;
    }

}
