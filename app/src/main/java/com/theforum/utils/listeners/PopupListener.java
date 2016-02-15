package com.theforum.utils.listeners;

import android.view.View;

import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.data.server.opinion;

/**
 * @author Ashish
 * @since 2/15/2016
 */
public interface PopupListener {
    void onLongCLick(opinion opinion, TrendsDataModel trendsDataModel,View v);
}
