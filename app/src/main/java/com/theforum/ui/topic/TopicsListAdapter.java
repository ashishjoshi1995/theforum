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
public class TopicsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<TopicDataModel> mTopics;
    private OnLoadMoreListener loadMoreListener;

    private final static int VIEW_TYPE_TOPIC = 0;
    private final static int VIEW_TYPE_LOAD_MORE_BTN = 1;

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
        @Bind(R.id.topics_renew_btn)TextView renewCountBtn;
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

            renewCountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicDataModel topic = mTopics.get(getLayoutPosition());

                    if(!topic.isRenewed()) {
                        int b=topic.getRenewalRequests();
                        b=b+1;
                        renewCountBtn.setText(String.valueOf(b));
                        setCompoundDrawables(renewCountBtn,renewedIcon);

                        topic.setRenewalRequests(b);
                        topic.setIsRenewed(true);

                        TopicHelper.getHelper().addRenewalRequest(topic.getTopicId(),
                                new TopicHelper.OnRenewalRequestListener() {
                                    @Override
                                    public void response(String s) {
                                        Log.e("response", s);
                                    }
                                });
                    }else {
                        int b=topic.getRenewalRequests();
                        b=b-1;
                        renewCountBtn.setText(String.valueOf(b));
                        setCompoundDrawables(renewCountBtn, renewIcon);

                        topic.setRenewalRequests(b);
                        topic.setIsRenewed(false);
                        TopicHelper.getHelper().removeRenewal(topic.getTopicId(), new TopicHelper.OnRemoveRenewalRequestListener() {
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

    public class LoadMoreItemViewHolder extends RecyclerView.ViewHolder{

        Button loadMore;

        public LoadMoreItemViewHolder(View itemView) {
            super(itemView);
            loadMore = (Button)itemView;
            //loadMore.setText("LOAD MORE");

            loadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     loadMoreListener.loadMore();
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_TOPIC;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TopicsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.topics_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


            TopicsItemViewHolder topicsItemViewHolder = (TopicsItemViewHolder)holder;
            final TopicDataModel topic = mTopics.get(position);

            topicsItemViewHolder.topicName.setText(topic.getTopicName());
            topicsItemViewHolder.renewCountBtn.setText(String.valueOf(topic.getRenewalRequests()));

            topicsItemViewHolder.timeHolder.setText(Html.fromHtml(mContext.getResources().getQuantityString(
                    R.plurals.time_holder_message,
                    topic.getRenewedCount(),
                    topic.getHoursLeft(),
                    topic.getRenewedCount())));


            if(topic.isRenewed()){
                setCompoundDrawables(topicsItemViewHolder.renewCountBtn,topicsItemViewHolder.renewedIcon);

            }else {
                setCompoundDrawables(topicsItemViewHolder.renewCountBtn,topicsItemViewHolder.renewIcon);

            }

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
