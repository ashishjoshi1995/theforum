package com.theforum.utils.listeners;


import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;

import java.util.List;

/**
 * @author  Ashish on 1/6/2016.
 */
public interface NotificationListener {

     void topicNotification(List<topic> topicNotifications);
     void opinionNotification(List<opinion> opinionNotifications);
}
