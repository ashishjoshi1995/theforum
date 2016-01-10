package com.theforum.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.dataModels.topic;
import com.theforum.data.helpers.LoadTopicHelper;
import com.theforum.utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 08-12-2015.
 */

@SuppressWarnings("deprecation")
public class TopicsListAdapter extends RecyclerView.Adapter<TopicsListAdapter.TopicsItemViewHolder> {

    private Context mContext;

    /* list of feed data */
    private List<topic> mTopics;

    Resources resources;
    Drawable renewIcon;

    public TopicsListAdapter(Context context, List<topic> feeds){
        mContext = context;
        mTopics = feeds;
        resources = mContext.getResources();
        renewIcon = resources.getDrawable(R.drawable.renew_icon);
    }


    public class TopicsItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.topics_name) TextView topicName;
        @Bind(R.id.topics_time_holder) TextView timeHolder;
        @Bind(R.id.topics_renew_btn)TextView renewCountBtn;

        String renewedColor = "#000000";
        String unrenewedColor = "#ffffff";

        public TopicsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.openContainerActivity(mContext, Constants.OPINIONS_FRAGMENT,
                            Pair.create(Constants.TOPIC_MODEL, (Serializable) mTopics.get(getLayoutPosition())));
                }
            });

            renewCountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    topic topic = mTopics.get(getLayoutPosition());
                    Log.e("I m called",""+topic.getIsRenewed()+"/"+getLayoutPosition());
                    if(!topic.getIsRenewed()) {

                        renewCountBtn.setCompoundDrawablesWithIntrinsicBounds(null, tintDrawable(renewedColor),
                                null, null);
                        topic.setIsRenewed(true);

                        LoadTopicHelper.getHelper().addRenewalRequest(topic.getTopicId(),
                                new LoadTopicHelper.OnRenewalRequestAddedListener() {
                                    @Override
                                    public void response(String s) {
                                        Log.e("response", s);
                                    }
                                });
                    }
                }
            });
        }

    }

    @Override
    public TopicsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.topics_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final TopicsItemViewHolder holder, int position) {
        final topic topic = mTopics.get(position);

        holder.topicName.setText(topic.getmTopic());
        holder.renewCountBtn.setText(String.valueOf(topic.getRenewalRequests()));
        holder.timeHolder.setText(resources.getString(R.string.time_holder_message, topic.getmHoursLeft(),
                topic.getRenewedCount()));

        if(topic.getIsRenewed()){
            holder.renewCountBtn.setCompoundDrawablesWithIntrinsicBounds(null, tintDrawable(holder.renewedColor),
                    null, null);
        }else {
            holder.renewCountBtn.setCompoundDrawablesWithIntrinsicBounds(null, tintDrawable(holder.unrenewedColor),
                    null, null);
        }

    }

    public void addTopic(topic topicDataModel, int position){
        mTopics.add(position, topicDataModel);
        notifyDataSetChanged();
    }

    public void addTopics(ArrayList<topic> topics){
        mTopics.addAll(topics);
        notifyDataSetChanged();
    }

    public void removeTopic(topic topicDataModel){
        mTopics.remove(topicDataModel);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {return mTopics.size();}

    public Drawable tintDrawable(String color){
        renewIcon.mutate().setColorFilter(new PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY));
        return renewIcon;
    }

}
