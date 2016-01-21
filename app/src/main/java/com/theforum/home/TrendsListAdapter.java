package com.theforum.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.local.models.TrendsDataModel;
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
    private Activity mActivity;

    /* list of feed data */
    private List<TrendsDataModel> mFeeds;


    public TrendsListAdapter(Activity activity, List<TrendsDataModel> feeds){
        mContext = activity;
        mActivity = activity;
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

            TrendsDataModel trends = mFeeds.get(getLayoutPosition());
            //getting the topic data from server
            TrendsHelper.getHelper().getTopicDetails(trends.getTopicId(), new TrendsHelper.OnTopicDetailReceived() {
                @Override
                public void onCompleted(TopicDataModel topic) {

                    CommonUtils.openContainerActivity(mContext, Constants.OPINIONS_FRAGMENT,
                            Pair.create(Constants.TOPIC_MODEL, (Serializable) topic));
                }

                @Override
                public void onError(final String error) {

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.showToast(mContext, error);
                        }
                    });

                }
            });


        }
    }

    @Override
    public TrendsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrendsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.trends_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TrendsItemViewHolder holder, int position) {
        TrendsDataModel trendsDataModel = mFeeds.get(position);

        holder.topicName.setText(trendsDataModel.getTopicName());
        holder.description.setText(trendsDataModel.getOpinionText());
        holder.upVoteBtn.setText(String.valueOf(trendsDataModel.getUpVoteCount()));
        holder.downVoteBtn.setText(String.valueOf(trendsDataModel.getDownVoteCount()));

    }

    public void addTrendItem(TrendsDataModel trendsDataModel, int position){
        mFeeds.add(position,trendsDataModel);
        notifyDataSetChanged();
    }

    public void addAllTrends(ArrayList<TrendsDataModel> trends){
        mFeeds.addAll(trends);
        notifyDataSetChanged();
    }

    public void clearList(){
        mFeeds.clear();
    }

    @Override
    public int getItemCount() {return mFeeds.size();}
}
