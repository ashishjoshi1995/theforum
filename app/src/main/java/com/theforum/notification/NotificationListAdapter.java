package com.theforum.notification;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.constants.Messages;
import com.theforum.constants.NotificationType;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.NotificationDataModel;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 07-01-2016.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NotificationDataModel> mData;
    private Context mContext;

    private final static int VIEW_TYPE_ONE = 0;

    public NotificationListAdapter(Activity activity, ArrayList<NotificationDataModel> dataSet) {
        mContext = activity;
        mData = dataSet;
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.notification_header) TextView header;
        @Bind(R.id.notification_main_text) TextView mainText;
        @Bind(R.id.notification_description) TextView description;
        @Bind(R.id.notification_decay_time) TextView timeHolder;

        public ViewHolderOne(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            NotificationDataModel noti = mData.get(getLayoutPosition());
            TopicDataModel topicDataModel= TopicDBHelper.getHelper().getTopicById(noti.topicId);

            if(topicDataModel!=null) {
                CommonUtils.openContainerActivity(mContext, LayoutType.OPINIONS_FRAGMENT,
                        Pair.create(LayoutType.TOPIC_MODEL, (Parcelable) topicDataModel));
            }
            else {
                CommonUtils.showToast(mContext, Messages.TOPIC_DELETE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {

     //   if(mData.get(position).notificationType == NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES){
            return VIEW_TYPE_ONE;
     //   }else return VIEW_TYPE_TWO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderOne(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

        if(holder.getItemViewType()== VIEW_TYPE_ONE){

            final ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            NotificationDataModel dataModel = mData.get(position);

            switch (dataModel.notificationType) {

                case NotificationType.NOTIFICATION_TYPE_OPINION_UP_VOTES:
                    viewHolderOne.header.setText(Html.fromHtml("<font color=\"#4b4b4b\">" + "Your Opinion on </font>"
                             + "<font color=\"#296acb\">" + dataModel.topicText + "</font>"
                             + "<font color=\"#4b4b4b\"> received</font>"));
                    viewHolderOne.mainText.setText(mContext.getResources().getQuantityString(
                            R.plurals.notification_up_vote,
                            dataModel.notificationCount,
                            dataModel.notificationCount));
                    break;

                case NotificationType.NOTIFICATION_TYPE_OPINIONS:
                    viewHolderOne.header.setText(Html.fromHtml("<font color=\"#4b4b4b\">" + "Your Topic </font>"
                            + "<font color=\"#296acb\">" + dataModel.topicText + "</font>"
                            + "<font color=\"#4b4b4b\"> received</font>"));
                    viewHolderOne.mainText.setText(mContext.getResources().getQuantityString(
                            R.plurals.notification_add_opinion,
                            dataModel.notificationCount,
                            dataModel.notificationCount));
                    break;

                case NotificationType.NOTIFICATION_TYPE_RENEWAL_REQUEST:
                    viewHolderOne.header.setText(Html.fromHtml("<font color=\"#4b4b4b\">" + "Your Topic </font>"
                            + "<font color=\"#296acb\">" + dataModel.topicText + "</font>"
                            + "<font color=\"#4b4b4b\"> received</font>"));
                    viewHolderOne.mainText.setText(mContext.getResources().getQuantityString(
                            R.plurals.notification_renewal_request,
                            dataModel.notificationCount,
                            dataModel.notificationCount));
                    break;

                case NotificationType.NOTIFICATION_TYPE_RENEWED:
                    viewHolderOne.header.setText(Html.fromHtml("<font color=\"#4b4b4b\">" + "Your Topic </font>"
                            + "<font color=\"#296acb\">" + dataModel.topicText + "</font>"
                            + "<font color=\"#4b4b4b\">received</font>"));
                    viewHolderOne.mainText.setText(dataModel.notificationCount + " Renewal");
                    break;
            }

            if(dataModel.description!=null){
                viewHolderOne.description.setText(dataModel.description);
            }else viewHolderOne.description.setVisibility(View.GONE);

            viewHolderOne.timeHolder.setText(dataModel.timeHolder);

        }
    }
}