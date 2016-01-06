package com.theforum.data.local;

import com.theforum.data.dataModels.OpinionNotification;
import com.theforum.data.dataModels.TopicNotification;

import java.util.List;
import java.util.Stack;

/**
 * Created by Ashish on 1/6/2016.
 */
public class NotificationStack {
   static Stack notificationStack = new Stack();
   public static void pushInStack(OpinionNotification opinionNotification){
       notificationStack.push(opinionNotification);
   }
   public static void pushInStack(TopicNotification topicNotification){
       notificationStack.push(topicNotification);
   }
   public static void pushTopicListInStack(List<TopicNotification> topicNotifications){
       notificationStack.push(topicNotifications);
   }
    public static void pushOpinionListInStack(List<OpinionNotification> opinionNotifications){
        notificationStack.push(opinionNotifications);
    }
}
