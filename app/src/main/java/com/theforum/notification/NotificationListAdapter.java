package com.theforum.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.server.NotificationDataModel;

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
    private final static int VIEW_TYPE_TWO = 1;

    public NotificationListAdapter(Context context, ArrayList<NotificationDataModel> dataSet) {
        mData = dataSet;
        mContext = context;
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        @Bind(R.id.notification_header) TextView header;
        @Bind(R.id.notification_main_text) TextView mainText;
        @Bind(R.id.notification_description)TextView description;
        @Bind(R.id.notification_decay_time) TextView timeHolder;
        @Bind(R.id.upvote_btn) TextView upVoteBtn;
        @Bind(R.id.downvote_btn) TextView downVoteBtn;

        public ViewHolderOne(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class ViewHolderTwo extends RecyclerView.ViewHolder  {

        @Bind(R.id.notification_header) TextView header;
        @Bind(R.id.notification_main_text) TextView mainText;
        @Bind(R.id.notification_description)TextView description;
        @Bind(R.id.notification_decay_time) TextView timeHolder;
        @Bind(R.id.notification_renew_btn) ImageButton renewBtn;

        public ViewHolderTwo(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).notificationType == Constants.NOTIFICATION_TYPE_OPINION_UP_VOTES){
            return VIEW_TYPE_ONE;
        }else return VIEW_TYPE_TWO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("test5","test");
        if(viewType== VIEW_TYPE_ONE){
            Log.e("test7","test");
            return new ViewHolderOne(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notifications_list_item_one, parent, false));
        }else{
            Log.e("test6","test");
            return new ViewHolderTwo(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notifications_list_item_two, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        Log.e("test2",mData.get(position).toString());
        if(holder.getItemViewType()== VIEW_TYPE_ONE){
            Log.e("test3",mData.get(position).toString());
            final ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            if(mData.get(position).notificationType == Constants.NOTIFICATION_TYPE_OPINION_UP_VOTES) {
                String tempText = "Your Opinion on " + mData.get(position).topicText + " received";
                viewHolderOne.header.setText(tempText);

                tempText = mData.get(position).newCount + " more Upvotes";
                viewHolderOne.mainText.setText(tempText);

                tempText = mData.get(position).opinionText;
                viewHolderOne.description.setText(tempText);

                tempText = mData.get(position).hoursLeft + "hrs left to decay | 01:30 PM Today";
                viewHolderOne.timeHolder.setText(tempText);
            }
        }else {
            Log.e("test4",mData.get(position).toString());
            final ViewHolderTwo viewHolderTwo = (ViewHolderTwo)holder;
            int j = mData.get(position).notificationType;
            String tempText = "Your Topic " + mData.get(position).topicText + " recieved";
            viewHolderTwo.header.setText(tempText);
            switch (j){
                case Constants.NOTIFICATION_TYPE_OPINIONS:
                    tempText = mData.get(position).opinions + " Opinions";
                    viewHolderTwo.mainText.setText(tempText);
                    break;
                case Constants.NOTIFICATION_TYPE_RENEWAL_REQUEST:
                    tempText = mData.get(position).renewalRequest+ " Renewal Requests";
                    viewHolderTwo.mainText.setText(tempText);
                    break;
                case Constants.NOTIFICATION_TYPE_RENEWED:
                    tempText = mData.get(position).renewedCount + " Renewal";
                    viewHolderTwo.mainText.setText(tempText);
                    break;
            }
            tempText = mData.get(position).hoursLeft +"hrs left to decay | 01:30 PM Today";
            viewHolderTwo.timeHolder.setText(tempText);
            }
        }
    }