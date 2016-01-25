package com.theforum.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.data.local.models.TopicDataModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author  Ashish on 1/7/2016.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private ArrayList<TopicDataModel> mDataSet;

    private Context mContext;


    public ResultAdapter(Context context, ArrayList<TopicDataModel> myDataSet) {
        mDataSet = myDataSet;
        mContext = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.topics_name) TextView topicName;
        @Bind(R.id.topics_time_holder) TextView timeHolder;
        @Bind(R.id.topics_renew_btn)TextView renewCountBtn;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            renewCountBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topics_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TopicDataModel topic = mDataSet.get(position);

        holder.topicName.setText(topic.getTopicName());
        holder.renewCountBtn.setText(String.valueOf(topic.getRenewalRequests()));
        holder.timeHolder.setText(mContext.getResources().getString(R.string.time_holder_message,
                topic.getHoursLeft(), topic.getRenewedCount()));

    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
