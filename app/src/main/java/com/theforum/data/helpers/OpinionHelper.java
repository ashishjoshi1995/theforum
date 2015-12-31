package com.theforum.data.helpers;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.theforum.data.dataModels.opinion;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.util.concurrent.ExecutionException;

/**
 * Created by Ashish on 12/31/2015.
 */
public class OpinionHelper {
    //create Opinion, get opinions, upvoteDownvote
    private MobileServiceClient mClient;
    private MobileServiceTable<opinion> mOpinion;
    opinion opinion;

    private String uid;

    public OpinionHelper(MobileServiceClient mobileServiceClient,int operation,opinion opinion){
        this.mClient = mobileServiceClient;

        mOpinion = mClient.getTable(opinion.class);

    switch (operation){
        case 0:
            this.opinion = opinion;
            //add opinion to server, local db
            addOpinion();

            break;
        case 1:

            //retrieve opinions from server
            getAllOpinions();
            break;
    }
    }


    private void getAllOpinions(){
        AsyncTask<Void, Void, MobileServiceList<opinion>> task = new AsyncTask<Void, Void, MobileServiceList<opinion>>() {


            @Override
            protected MobileServiceList<opinion> doInBackground(Void... voids) {
                MobileServiceList<opinion> result = null;
                try {
                   result = mOpinion.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (MobileServiceException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(MobileServiceList<opinion> opinions) {
                super.onPostExecute(opinions);
                //UI update
            }
        };
        runAsyncTask2(task);
    }

    private void addOpinion(){
    AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mOpinion.insert(opinion, new TableOperationCallback<opinion>() {
                    @Override
                    public void onCompleted(opinion entity, Exception exception, ServiceFilterResponse response) {
                        Log.e("opinionAdded", "opinionAdded");
                    }


                });

            } catch (Exception e) {

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
}
