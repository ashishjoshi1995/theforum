package com.theforum.data.helpers;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.theforum.TheForumApplication;

/**
 * Created by Ashish on 1/6/2016.
 */
public class NotificationBuilder {
    //this class has none of the server methods built with asyncTask as
    //asyncTask is already included during the call of any of the above methods

    MobileServiceClient mobileServiceClient;


    public NotificationBuilder(){
        mobileServiceClient = TheForumApplication.getClient();
    }

    public void readNotification(){

    }

}
