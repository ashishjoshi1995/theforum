package com.theforum.ui.search;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;

import java.io.Serializable;
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
        mDataSet = new ArrayList<>(myDataSet);
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.topics_name) TextView topicName;
        @Bind(R.id.topics_time_holder) TextView timeHolder;
        @Bind(R.id.topics_renew_btn)TextView renewCountBtn;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            renewCountBtn.setVisibility(View.GONE);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonUtils.openContainerActivity(mContext, LayoutType.OPINIONS_FRAGMENT,
                            Pair.create(LayoutType.TOPIC_MODEL, (Serializable) mDataSet.get(getLayoutPosition())));
                }
            });
        }

    }

    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TopicDataModel topic = mDataSet.get(position);

        holder.topicName.setText(topic.getTopicName());
        holder.renewCountBtn.setText(String.valueOf(topic.getRenewalRequests()));
        holder.timeHolder.setText(mContext.getResources().getQuantityString(R.plurals.time_holder_message,
                topic.getRenewedCount()+1,
                topic.getHoursLeft(),
                topic.getRenewedCount()));
    }

    public TopicDataModel removeItem(int position) {
        final TopicDataModel model = mDataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, TopicDataModel model) {
        mDataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final TopicDataModel model = mDataSet.remove(fromPosition);
        mDataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<TopicDataModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(ArrayList<TopicDataModel> newModels) {
        for (int i = mDataSet.size() - 1; i >= 0; i--) {
            final TopicDataModel model = mDataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<TopicDataModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final TopicDataModel model = newModels.get(i);
            if (!mDataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<TopicDataModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final TopicDataModel model = newModels.get(toPosition);
            final int fromPosition = mDataSet.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}
