package com.theforum.notification;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.NotificationType;
import com.theforum.data.local.models.NotificationDataModel;

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
    //private final static int VIEW_TYPE_TWO = 1;

    public NotificationListAdapter(Activity activity, ArrayList<NotificationDataModel> dataSet) {
        mContext = activity;
        mData = dataSet;
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.notification_header)
        TextView header;
        @Bind(R.id.notification_main_text)
        TextView mainText;
        @Bind(R.id.notification_description)
        TextView description;
        @Bind(R.id.notification_decay_time)
        TextView timeHolder;


        public ViewHolderOne(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            NotificationDataModel noti = mData.get(getLayoutPosition());
            //TopicDBHelper.getHelper().getMyTopicId()
//            NotificationDataModel noti = mData.get(getLayoutPosition());
//
//            //final TopicDataModel topicDataModel = new TopicDataModel();
//            TrendsHelper.getHelper().getTopicDetails(noti.topicId, new TrendsHelper.OnTopicDetailReceived() {
//                @Override
//                public void onCompleted(TopicDataModel topicDataModel) {
//                    topicDataModel.setTopicDescription(topicDataModel.getDescription());
//                    int p = 0;
//                    if (trend.getRenewalIds() != null) {
//                        String[] r = trend.getRenewalIds().split(" ");
//                        p = r.length;
//                        for (int k = 0; k < r.length; k++) {
//                            if (r[k].equals(User.getInstance().getId())) {
//                                topicDataModel.setIsRenewed(true);
//                                break;
//                            }
//                        }
//                    }
//                    //topicDataModel.setIsRenewed();
//                    topicDataModel.setRenewalRequests(p);
//                    //topicDataModel.setRenewalRequests(trend.getRenewCount());
//                    topicDataModel.setTopicName(trend.getTopicName());
//                    topicDataModel.setTopicId(trend.getTopicId());
//                    topicDataModel.setHoursLeft(trend.getHoursLeft());
//
//
//                    CommonUtils.openContainerActivity(mContext, LayoutType.OPINIONS_FRAGMENT,
//                            Pair.create(LayoutType.TOPIC_MODEL, (Serializable) topicDataModel));
//                }
//
//                @Override
//                public void onError(String error) {
//
//                }
//            });
//
//
//        }
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
                .inflate(R.layout.notifications_list_item, parent, false));

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