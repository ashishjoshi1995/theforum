package com.theforum.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.dataModels.topic;
import com.theforum.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
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

    public TopicsListAdapter(Context context, List<topic> feeds){
        mContext = context;
        mTopics = feeds;
        resources = mContext.getResources();
    }


    public class TopicsItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.topics_name) TextView topicName;
        @Bind(R.id.topics_time_holder) TextView timeHolder;
        @Bind(R.id.topics_renew_btn)TextView renewCountBtn;

        @BindDrawable(R.drawable.renew_icon) Drawable renewIcon;

        public TopicsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.openContainerActivity(mContext, Constants.OPINIONS_FRAGMENT);
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
        if(topic!=null) {
            holder.topicName.setText(topic.getmTopic());
            holder.renewCountBtn.setText(String.valueOf(topic.getmRenewalRequests()));
            holder.timeHolder.setText(resources.getString(R.string.time_holder_message, topic.getmHoursLeft(),
                    topic.getmRenewedCount()));

            final boolean isRenewed = topic.getIsRenewed();
            if(isRenewed){
                holder.renewCountBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                        CommonUtils.tintDrawable(holder.renewIcon, "#30ed17"), null, null);
            }else holder.renewCountBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                    CommonUtils.tintDrawable(holder.renewIcon, "#adadad"), null, null);

            holder.renewCountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click", "yes");
                    if(!isRenewed) {
                        holder.renewCountBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                                CommonUtils.tintDrawable(holder.renewIcon, "#30ed17"), null, null);
                        topic.setIsRenewed(true);
                        //call api
                    }

                }
            });
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

}
