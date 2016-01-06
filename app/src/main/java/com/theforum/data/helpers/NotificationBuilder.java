package com.theforum.data.helpers;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.theforum.TheForumApplication;
import com.theforum.User;
import com.theforum.data.dataModels.OpinionNotification;
import com.theforum.data.dataModels.TopicNotification;
import com.theforum.data.dataModels.user;

import java.util.List;

/**
 * Created by Ashish on 1/6/2016.
 */
public class NotificationBuilder {
    //this class has none of the server methods built with asyncTask as
    //asyncTask is already included during the call of any of the above methods

    MobileServiceClient mobileServiceClient;
    MobileServiceTable<TopicNotification>topicNotificationTable;
    MobileServiceTable<OpinionNotification>opinionNotificationTable;

    public NotificationBuilder(){
        this.mobileServiceClient = TheForumApplication.getClient();
        this.opinionNotificationTable = mobileServiceClient.getTable(OpinionNotification.class);
        this.topicNotificationTable = mobileServiceClient.getTable(TopicNotification.class);
    }

    public void readNotification(){

       opinionNotificationTable.where().field("uid").eq(User.getInstance().getForumId()).execute(new TableQueryCallback<OpinionNotification>() {
            @Override
            public void onCompleted(List<OpinionNotification> result, int count, Exception exception, ServiceFilterResponse response) {
                Log.e("readNotif opi" , String.valueOf(count));
            }
        });

        topicNotificationTable.where().field("uid").eq(User.getInstance().getForumId()).execute(new TableQueryCallback<TopicNotification>() {
            @Override
            public void onCompleted(List<TopicNotification> result, int count, Exception exception, ServiceFilterResponse response) {
                Log.e("readNotif topic" , String.valueOf(count));
            }
        });
    }


}
