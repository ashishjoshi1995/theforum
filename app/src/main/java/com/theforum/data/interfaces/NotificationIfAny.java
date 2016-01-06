package com.theforum.data.interfaces;

import com.theforum.data.dataModels.OpinionNotification;
import com.theforum.data.dataModels.TopicNotification;

import java.util.List;

/**
 * Created by Ashish on 1/6/2016.
 */
public interface NotificationIfAny  {

     void topicNotif (List<TopicNotification> topicNotifications);
     void opinionNotif (List<OpinionNotification> opinionNotifications);

}
