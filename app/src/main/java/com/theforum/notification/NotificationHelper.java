package com.theforum.notification;


import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.theforum.TheForumApplication;
import com.theforum.data.helpers.localHelpers.LocalOpinionHelper;
import com.theforum.data.helpers.notificationClearApi.NotificationClearApiRequest;
import com.theforum.data.helpers.notificationClearApi.NotificationClearApiResponse;
import com.theforum.data.server.areaopinions;
import com.theforum.data.server.areatopics;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.utils.User;
import com.theforum.utils.listeners.KillerNotificationListener;
import com.theforum.utils.listeners.NotificationListener;

import java.util.List;

/**
 * @author Ashish on 1/6/2016.
 */

/**
 * this class has none of the server methods built with asyncTask as
 * asyncTask is already included during the call of any of the above methods
 */
public class NotificationHelper {

    MobileServiceClient mobileServiceClient;
    MobileServiceTable<topic> topic;
    MobileServiceTable<opinion> opinion;
    MobileServiceTable<areaopinions> localOpinions;
    MobileServiceTable<areatopics> localTopics;

    public static boolean one = false;
    public static boolean two = false;
    public static boolean three = false;
    public static boolean four = false;


    public NotificationHelper(){
        this.mobileServiceClient = TheForumApplication.getClient();
        this.opinion = mobileServiceClient.getTable(opinion.class);
        this.topic = mobileServiceClient.getTable(topic.class);
        this.localOpinions = mobileServiceClient.getTable(areaopinions.class);
        this.localTopics = mobileServiceClient.getTable(areatopics.class);
    }

    public void readKillerNotification(final KillerNotificationListener listener){
        Log.e("inside killer helper","sdsdsd");
        topic.where().execute(new TableQueryCallback<topic>() {
            @Override
            public void onCompleted(List<topic> result, int count, Exception exception, ServiceFilterResponse response) {
                two = true;
                if (count > 0) {
                    Log.e("inside killer helrer","onCompleted");
                    listener.topicsGot(result, count);
                }
            }
        });
    }

    public void readNotification(final NotificationListener notificationListener){

       opinion.where().field("uid").eq(User.getInstance().getId()).and().field("notif_upvotes").gt(0)
               .execute(new TableQueryCallback<opinion>() {

                   @Override
                   public void onCompleted(List<opinion> result, int count, Exception exception,
                                           ServiceFilterResponse response) {
                       one = true;
                       if (count > 0) {
                           notificationListener.opinionNotification(result);
                       }
                   }

               });

        topic.where().field("uid").eq(User.getInstance().getId()).and().field("notif_count").gt(0).
                execute(new TableQueryCallback<topic>() {
                    @Override
                    public void onCompleted(List<topic> result, int count, Exception exception, ServiceFilterResponse response) {
                        two = true;
                        if (count > 0) {
                            notificationListener.topicNotification(result);
                        }
                    }
                });
        localTopics.where().field("uid").eq(User.getInstance().getId()).and().field("notif_count").gt(0).
                execute(new TableQueryCallback<areatopics>() {
                    @Override
                    public void onCompleted(List<areatopics> result, int count, Exception exception, ServiceFilterResponse response) {
                        three = true;
                        if(count>0){
                            notificationListener.areaTopicNotification(result);
                        }
                    }
                });
       localOpinions.where().field("uid").eq(User.getInstance().getId()).and().field("notif_upvotes").gt(0)
                .execute(new TableQueryCallback<areaopinions>() {

                    @Override
                    public void onCompleted(List<areaopinions> result, int count, Exception exception,
                                            ServiceFilterResponse response) {
                        four = true;
                        if (count > 0) {
                            notificationListener.areaOpinionNotification(result);
                        }
                    }

                });

    }

    public void cleanItUp(){
        NotificationClearApiRequest request = new NotificationClearApiRequest();
        request.uid = User.getInstance().getId();

        mobileServiceClient.invokeApi("notificationclearapi", request, NotificationClearApiResponse.class,
                new ApiOperationCallback<NotificationClearApiResponse>() {
            @Override
            public void onCompleted(NotificationClearApiResponse result, Exception exception,
                                    ServiceFilterResponse response) {

            }
        });

        one = false;
        two = false;
        three = false;
        four = false;
    }


}
