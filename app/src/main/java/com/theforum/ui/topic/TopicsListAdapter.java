package com.theforum.ui.topic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.listeners.OnLoadMoreListener;

import java.io.Serializable;
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

    private List<TopicDataModel> mTopics;
    private OnLoadMoreListener loadMoreListener;

    private final static int VIEW_TYPE_TOPIC = 0;

    public TopicsListAdapter(Context context, List<TopicDataModel> feeds){
        mContext = context;
        mTopics = feeds;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener){
        this.loadMoreListener = loadMoreListener;
    }

    public class TopicsItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.topics_name) TextView topicName;
        @Bind(R.id.topics_time_holder) TextView timeHolder;
        @Bind(R.id.topics_renew_btn)TextView renewBtn;

        @BindDrawable(R.drawable.renew_icon) Drawable renewIcon;
        @BindDrawable(R.drawable.renew_icon_on) Drawable renewedIcon;


        public TopicsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.openContainerActivity(mContext, LayoutType.OPINIONS_FRAGMENT,
                            Pair.create(LayoutType.TOPIC_MODEL, (Serializable) mTopics.get(getLayoutPosition())));
                }
            });

            renewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TopicDataModel mTopicModel = mTopics.get(getLayoutPosition());

                    final int b = mTopicModel.getRenewalRequests();

                    if(!mTopicModel.isRenewed()) {
                        renewBtn.setBackgroundDrawable(renewedIcon);
                        timeHolder.setText(Html.fromHtml(mContext.getResources().getQuantityString(
                                R.plurals.opinion_time_holder_message,
                                b + 1, mTopicModel.getHoursLeft(), b + 1)));

                        TopicHelper.getHelper().addRenewalRequest(mTopicModel.getTopicId(),
                                new TopicHelper.OnRenewalRequestListener() {

                                    @Override
                                    public void onCompleted() {
                                        // update the local database

                                        mTopicModel.setRenewalRequests(b + 1);
                                        mTopicModel.setIsRenewed(true);
                                        TopicDBHelper.getHelper().updateTopicRenewalStatus(mTopicModel);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        // notify the user that renew have failed
                                        CommonUtils.showToast(mContext, error);

                                        // revert the changes made in the UI
                                        renewBtn.setBackgroundDrawable(renewedIcon);
                                        timeHolder.setText(Html.fromHtml(mContext.getResources().getQuantityString(
                                                R.plurals.opinion_time_holder_message,
                                                b, mTopicModel.getHoursLeft(), b)));
                                    }
                                });

                    } else {
                        renewBtn.setBackgroundDrawable(renewIcon);
                        timeHolder.setText(Html.fromHtml(mContext.getResources().getQuantityString(
                                R.plurals.opinion_time_holder_message,
                                b - 1, mTopicModel.getHoursLeft(), b - 1)));

                        TopicHelper.getHelper().removeRenewal(mTopicModel.getTopicId(),
                                new TopicHelper.OnRemoveRenewalRequestListener() {
                                    @Override
                                    public void onCompleted() {
                                        mTopicModel.setRenewalRequests(b-1);
                                        mTopicModel.setIsRenewed(false);
                                        TopicDBHelper.getHelper().updateTopicRenewalStatus(mTopicModel);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        // notify the user that renew removal have failed
                                        CommonUtils.showToast(mContext, error);

                                        // revert the changes made in the UI
                                        renewBtn.setBackgroundDrawable(renewedIcon);
                                        timeHolder.setText(Html.fromHtml(mContext.getResources().getQuantityString(
                                                R.plurals.opinion_time_holder_message,
                                                b, mTopicModel.getHoursLeft(), b)));
                                    }

                                });

                    }
                }
            });

        }

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_TOPIC;
    }

    @Override
    public TopicsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TopicsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.topics_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TopicsItemViewHolder holder, int position) {

        TopicDataModel topic = mTopics.get(position);

        holder.topicName.setText(topic.getTopicName());
        holder.renewBtn.setText(String.valueOf(topic.getRenewalRequests()));

        holder.timeHolder.setText(Html.fromHtml(mContext.getResources().getQuantityString(
                R.plurals.time_holder_message,
                topic.getRenewedCount() + 1,
                topic.getHoursLeft(),
                topic.getRenewedCount())));

        if(topic.isRenewed()){
            setCompoundDrawables(holder.renewBtn,holder.renewedIcon);
        }else setCompoundDrawables(holder.renewBtn,holder.renewIcon);

    }

    public void addTopic(TopicDataModel topicDataModel, int position){
        mTopics.add(position, topicDataModel);
        notifyDataSetChanged();
    }

    private void setCompoundDrawables(TextView textView, Drawable drawable){
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }



    /**
     *
     * @param topics list of topics to update ui
     */

    public void addTopics(ArrayList<TopicDataModel> topics){
        mTopics.addAll(0,topics);
        notifyDataSetChanged();
    }

    public void removeTopic(TopicDataModel topicDataModel){
        mTopics.remove(topicDataModel);
        notifyDataSetChanged();
    }

    public void removeAllTopics(){
        mTopics.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {return mTopics.size();}


}
