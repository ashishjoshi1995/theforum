package com.theforum.data.interfaces;


import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;

import java.util.List;

/**
 * Created by Ashish on 1/6/2016.
 */
public interface NotificationIfAny  {

     void topicNotif (List<topic> topicNotifications);
     void opinionNotif (List<opinion> opinionNotifications);

}
