package com.theforum.data.local;

import java.util.Stack;

/**
 * Created by Ashish on 1/6/2016.
 */
public class NotificationStack {
   static Stack notificationStack = new Stack();


    public static void pushNotificationInflatorItemData(NotificationDataModel notificationDataModel){
        notificationStack.push(notificationDataModel);
    }
}
