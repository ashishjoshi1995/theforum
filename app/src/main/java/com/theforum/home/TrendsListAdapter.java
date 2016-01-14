package com.theforum.home;

import android.content.Context;
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
import com.theforum.data.helpers.OpinionHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.utils.CommonUtils;

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

//@SuppressWarnings("deprecation")
public class TrendsListAdapter extends RecyclerView.Adapter<TrendsListAdapter.TrendsItemViewHolder> {

    private Context mContext;

    /* list of feed data */
    private List<opinion> mFeeds;


    public TrendsListAdapter(Context context, List<opinion> feeds){
        mContext = context;
        mFeeds = feeds;
    }


    public class TrendsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.trends_topic_name) TextView topicName;
        @Bind(R.id.trends_description) TextView description;
        @Bind(R.id.trends_decay_time)TextView decayTimeHolder;
        @Bind(R.id.upvote_btn) TextView upVoteBtn;
        @Bind(R.id.downvote_btn) TextView downVoteBtn;

        @BindDrawable(R.drawable.upvote) Drawable upvote;
        @BindDrawable(R.drawable.downvote) Drawable downvote;

        public TrendsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            upVoteBtn.setCompoundDrawablesWithIntrinsicBounds(null, CommonUtils.tintDrawable(upvote, "#adadad"),
                    null, null);
            downVoteBtn.setCompoundDrawablesWithIntrinsicBounds(null, CommonUtils.tintDrawable(downvote,"#adadad"),
                    null,null);

            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            opinion opinion = mFeeds.get(getLayoutPosition());
            //getting the topic data from server
            OpinionHelper.getHelper().getTopicDetails(opinion.getTopicId(), new OpinionHelper.OnTopicDetailReceived() {
                @Override
                public void onCompleted(topic topic) {
                    TopicDataModel topicDataModel = new TopicDataModel(topic);
                }

                @Override
                public void onError(String error) {
                    Log.e("onErrorTrendListAdapter",error);
                }
            });


            TopicDataModel topicModel = new TopicDataModel();
            topicModel.setTopicName(opinion.getTopicName());
            topicModel.setTopicId(opinion.getTopicId());

            CommonUtils.openContainerActivity(mContext, Constants.OPINIONS_FRAGMENT,
                    Pair.create(Constants.TOPIC_MODEL,(Serializable)topicModel));
        }
    }

    @Override
    public TrendsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrendsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.trends_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TrendsItemViewHolder holder, int position) {
        opinion opinionModel = mFeeds.get(position);

        holder.topicName.setText(opinionModel.getTopicName());
        holder.description.setText(opinionModel.getOpinionName());
        holder.upVoteBtn.setText(String.valueOf(opinionModel.getUpVotes()));
        holder.downVoteBtn.setText(String.valueOf(opinionModel.getDownVotes()));

    }

    public void addTrendItem(opinion trendsDataModel, int position){
        mFeeds.add(position,trendsDataModel);
        notifyDataSetChanged();
    }

    public void addAllTrends(ArrayList<opinion> trends){
        mFeeds.addAll(trends);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {return mFeeds.size();}
}
