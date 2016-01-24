package com.theforum.notification;

import android.app.Activity;
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
import com.theforum.data.local.models.NotificationInflatorModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 07-01-2016.
 */


public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NotificationInflatorModel> mData;
    private Context mContext;
    private Activity mActivity;

    private final static int VIEW_TYPE_ONE = 0;
    private final static int VIEW_TYPE_TWO = 1;

    public NotificationListAdapter(Activity activity, ArrayList<NotificationInflatorModel> dataSet) {

        mContext = activity;
        mActivity = activity;
        mData = dataSet;

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
        Log.e("noti type",""+mData.get(position).getNotificationType());
        if(mData.get(position).getNotificationType() == Constants.NOTIFICATION_TYPE_OPINION_UP_VOTES){
            return VIEW_TYPE_ONE;
        }else return VIEW_TYPE_TWO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("test5","test");
        Log.e("test5",""+viewType);

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

        if(holder.getItemViewType()== VIEW_TYPE_ONE){
            Log.e("test3",mData.get(position).toString());
            final ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            if(mData.get(position).getNotificationType() == Constants.NOTIFICATION_TYPE_OPINION_UP_VOTES) {
                Log.e(mData.get(position).getHeader()+mData.get(position).getMainText(),mData.get(position).getDescription()+mData.get(position).getTimeHolder());
                viewHolderOne.header.setText(mData.get(position).getHeader());
                Log.e("one", mData.get(position).getHeader());
                viewHolderOne.mainText.setText(mData.get(position).getMainText());
                Log.e("two", mData.get(position).getMainText());
                viewHolderOne.description.setText(mData.get(position).getDescription());
                Log.e("three", mData.get(position).getDescription());
                viewHolderOne.timeHolder.setText(mData.get(position).getTimeHolder());
                Log.e("four", mData.get(position).getTimeHolder());
            }
        }else {
            final ViewHolderTwo viewHolderTwo = (ViewHolderTwo)holder;
            int j = mData.get(position).getNotificationType();
            viewHolderTwo.header.setText(mData.get(position).getHeader());
            switch (j) {
                case Constants.NOTIFICATION_TYPE_OPINIONS:
                    viewHolderTwo.mainText.setText(mData.get(position).getMainText());
                    Log.e("five", mData.get(position).getMainText());
                    break;
                case Constants.NOTIFICATION_TYPE_RENEWAL_REQUEST:
                    viewHolderTwo.mainText.setText(mData.get(position).getMainText());
                    Log.e("six", mData.get(position).getMainText());
                    break;
                case Constants.NOTIFICATION_TYPE_RENEWED:
                    viewHolderTwo.mainText.setText(mData.get(position).getMainText());
                    Log.e("seven", mData.get(position).getMainText());
                    break;
            }
            viewHolderTwo.timeHolder.setText(mData.get(position).getTimeHolder());
            Log.e("asasasass",mData.get(position).getTimeHolder());
            }
        }
    }