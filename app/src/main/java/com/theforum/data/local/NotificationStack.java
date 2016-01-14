package com.theforum.data.local;

import com.theforum.data.server.NotificationDataModel;

import java.util.Stack;

/**
 * Created by Ashish on 1/6/2016.
 */
public class NotificationStack {
   public static Stack notificationStack = new Stack();


    public static void pushNotificationInflatorItemData(NotificationDataModel notificationDataModel){
        notificationStack.push(notificationDataModel);
    }


}
