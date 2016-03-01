package com.theforum.utils.listeners;

import com.theforum.data.server.topic;

import java.util.List;

/**
 * @author Ashish
 * @since 2/29/2016
 */
public interface KillerNotificationListener {
    void topicsGot(List<topic> topicNotifications, int count);
}
