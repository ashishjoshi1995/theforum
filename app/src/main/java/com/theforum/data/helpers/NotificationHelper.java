package com.theforum.data.helpers;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.theforum.Constants;
import com.theforum.TheForumApplication;
import com.theforum.data.helpers.notificationClearApi.NotificationClearApiRequest;
import com.theforum.data.helpers.notificationClearApi.NotificationClearApiResponse;
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
    public static Boolean one = false;
    public static Boolean two = false;


    public NotificationHelper(){
        this.mobileServiceClient = TheForumApplication.getClient();
        this.opinion = mobileServiceClient.getTable(opinion.class);
        this.topic = mobileServiceClient.getTable(topic.class);
    }

    public void readNotification(final NotificationIfAny notificationIfAny){
       opinion.where().field("uid").eq(User.getInstance().getId()).and().field("notif_count").gt(0).
               execute(new TableQueryCallback<opinion>() {

            @Override
            public void onCompleted(List<opinion> result, int count, Exception exception, ServiceFilterResponse response) {
                Log.e("readNotif opi", String.valueOf(count));
                one = true;
                if(count>0) {
                    notificationIfAny.opinionNotif(result);
                }
            }

        });

        topic.where().field("uid").eq(User.getInstance().getId()).and().field("notif_count").gt(0).
                execute(new TableQueryCallback<topic>() {
            @Override
            public void onCompleted(List<topic> result, int count, Exception exception, ServiceFilterResponse response) {
                Log.e("readNotif topic", String.valueOf(count));
                two = true;
                if (count > 0) {
                    notificationIfAny.topicNotif(result);
                }
            }
        });
    }

    public void cleanItUP(){
        NotificationClearApiRequest request = new NotificationClearApiRequest();
        request.uid = User.getInstance().getId();
        mobileServiceClient.invokeApi("notificationclearapi", request, NotificationClearApiResponse.class, new ApiOperationCallback<NotificationClearApiResponse>() {
            @Override
            public void onCompleted(NotificationClearApiResponse result, Exception exception, ServiceFilterResponse response) {
                Log.e("sdsdsd",result.message);
            }
        });
        one = false;
        two = false;
    }


}
