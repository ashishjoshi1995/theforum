package com.theforum.data.helpers;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.theforum.TheForumApplication;
import com.theforum.utils.User;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.data.interfaces.NotificationIfAny;

import java.util.List;

/**
 * @author Ashish on 1/6/2016.
 */
public class NotificationHelper {
    //this class has none of the server methods built with asyncTask as
    //asyncTask is already included during the call of any of the above methods

    MobileServiceClient mobileServiceClient;
    MobileServiceTable<topic>topic;
    MobileServiceTable<opinion>opinion;


    public NotificationHelper(){
        this.mobileServiceClient = TheForumApplication.getClient();
        this.opinion = mobileServiceClient.getTable(opinion.class);
        this.topic = mobileServiceClient.getTable(topic.class);
    }

    public void readNotification(final NotificationIfAny notificationIfAny){
       opinion.where().field("uid").eq(User.getInstance().getId()).and().field("notif_count").ge(0).
               execute(new TableQueryCallback<opinion>() {

            @Override
            public void onCompleted(List<opinion> result, int count, Exception exception, ServiceFilterResponse response) {
                Log.e("readNotif opi", String.valueOf(count));
                if(count>0) {
                    notificationIfAny.opinionNotif(result);
                }
            }

        });

        topic.where().field("uid").eq(User.getInstance().getId()).and().field("notif_count").ge(0).execute(new TableQueryCallback<topic>() {
            @Override
            public void onCompleted(List<topic> result, int count, Exception exception, ServiceFilterResponse response) {
                Log.e("readNotif topic", String.valueOf(count));
                if(count>0){
                    notificationIfAny.topicNotif(result);
                }
            }
        });
    }


}
