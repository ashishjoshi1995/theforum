package com.theforum.ui.topic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.helpers.localHelpers.LocalTopicHelper;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.listeners.OnListItemClickListener;
import com.theforum.utils.listeners.OnLongClickItemListener;

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
    private OnListItemClickListener onListItemClickListener;
    private OnLongClickItemListener onLongClickItemListener;

    public TopicsListAdapter(Context context, List<TopicDataModel> feeds){
        mContext = context;
        mTopics = feeds;
    }

    public void setOnListItemClickListener(OnListItemClickListener listItemClickListener){
        this.onListItemClickListener = listItemClickListener;
    }

    public void setOnLongClickItemListener(OnLongClickItemListener listener){
        this.onLongClickItemListener = listener;
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
                    onListItemClickListener.onItemClick(v, getLayoutPosition());
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onLongClickItemListener.onLongClicked(getLayoutPosition());
                }

            });


            renewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("clicked","clicked");
                    final TopicDataModel mTopicModel = mTopics.get(getLayoutPosition());
                    final int b = mTopicModel.getRenewalRequests();

                    if (!mTopicModel.isRenewed()) {

                            setCompoundDrawables(renewBtn, renewedIcon);
                            renewBtn.setText(String.valueOf(b + 1));
                            mTopicModel.setRenewalRequests(b + 1);
                            mTopicModel.setIsRenewed(true);
                        if(!mTopicModel.isLocalTopic()) {
                            TopicHelper.getHelper().addRenewalRequest(mTopicModel,
                                    new TopicHelper.OnRenewalRequestListener() {

                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(String error) {
                                            // notify the user that renew have failed
                                            CommonUtils.showToast(mContext, error);

                                            //revert changes in local dataModel
                                            mTopicModel.setRenewalRequests(b);
                                            mTopicModel.setIsRenewed(false);

                                            // revert the changes made in the UI
                                            setCompoundDrawables(renewBtn, renewIcon);
                                            renewBtn.setText(String.valueOf(b));

                                        }
                                    });
                        }
                        else {
                            LocalTopicHelper.getHelper().addRenewalRequest(mTopicModel, new LocalTopicHelper.OnRenewalRequestListener() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(String error) {
                                    // notify the user that renew have failed
                                    CommonUtils.showToast(mContext, error);

                                    //revert changes in local dataModel
                                    mTopicModel.setRenewalRequests(b);
                                    mTopicModel.setIsRenewed(false);

                                    // revert the changes made in the UI
                                    setCompoundDrawables(renewBtn, renewIcon);
                                    renewBtn.setText(String.valueOf(b));

                                }
                            });
                        }
                    } else {
                        setCompoundDrawables(renewBtn, renewIcon);
                        renewBtn.setText(String.valueOf(b - 1));
                        mTopicModel.setRenewalRequests(b - 1);
                        mTopicModel.setIsRenewed(false);
                        if(!mTopicModel.isLocalTopic()){

                        TopicHelper.getHelper().removeRenewal(mTopicModel.getTopicId(),
                                new TopicHelper.OnRemoveRenewalRequestListener() {
                                    @Override
                                    public void onCompleted() {
                                        TopicDBHelper.getHelper().updateTopicRenewalStatus(mTopicModel);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        // notify the user that renew removal have failed
                                        CommonUtils.showToast(mContext, error);

                                        //revert changes in local dataModel
                                        mTopicModel.setRenewalRequests(b);
                                        mTopicModel.setIsRenewed(true);

                                        // revert the changes made in the UI
                                        setCompoundDrawables(renewBtn, renewedIcon);
                                        renewBtn.setText(String.valueOf(b));
                                    }

                                });
                    }else {
                            LocalTopicHelper.getHelper().removeRenewal(mTopicModel.getTopicId(), new LocalTopicHelper.OnRemoveRenewalRequestListener() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(String error) {
                                    // notify the user that renew removal have failed
                                    CommonUtils.showToast(mContext, error);

                                    //revert changes in local dataModel
                                    mTopicModel.setRenewalRequests(b);
                                    mTopicModel.setIsRenewed(true);

                                    // revert the changes made in the UI
                                    setCompoundDrawables(renewBtn, renewedIcon);
                                    renewBtn.setText(String.valueOf(b));
                                }
                            });
                        }
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
    public void onBindViewHolder(TopicsItemViewHolder holder, int position) {

        TopicDataModel topic = mTopics.get(position);

        holder.topicName.setText(topic.getTopicName());
        holder.renewBtn.setText(String.valueOf(topic.getRenewalRequests()));
        holder.timeHolder.setText(Html.fromHtml(mContext.getResources().getQuantityString(R.plurals.time_holder_message,
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
     * @param topics list of topics to update ui
     */

    public void addTopics(ArrayList<TopicDataModel> topics){
        mTopics.addAll(0,topics);
        Log.e("addTopics",topics.size()+"");
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
