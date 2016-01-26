package com.theforum.ui.trend;

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
import com.theforum.utils.enums.VoteStatus;

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



        @BindDrawable(R.drawable.upvote) Drawable upVoteIcon;
        @BindDrawable(R.drawable.upvote_on) Drawable upVotedIcon;
        @BindDrawable(R.drawable.downvote) Drawable downVoteIcon;
        @BindDrawable(R.drawable.downvote_on) Drawable downVotedIcon;

        public TrendsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(this);

            upVoteBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  TrendsDataModel opinionModel = mFeeds.get(getLayoutPosition());

                  if(opinionModel.getVoteStatus() == VoteStatus.NONE) {
                      int upvotes = opinionModel.getUpVoteCount();
                      upvotes = upvotes+1;

                      upVoteBtn.setText(String.valueOf(upvotes));
                      setCompoundDrawables(upVoteBtn, upVotedIcon);
                      opinionModel.setUpVoteCount(upvotes);
                      opinionModel.setVoteStatus(VoteStatus.UPVOTED);

                        /*
                         *  send the request to server to increase the count
                         */
                      TrendsHelper.getHelper().upvoteDownvote(true, opinionModel.getTrendId(), new TrendsHelper.OnUVDVOperationCompleteListener() {
                          @Override
                          public void onCompleteMessage(String message) {
                              CommonUtils.showToast(mContext, message);
                          }
                      });

                  }else CommonUtils.showToast(mContext, "Cannot UpVote");
              }
          });

            downVoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TrendsDataModel opinionModel2 = mFeeds.get(getLayoutPosition());
                    if (opinionModel2.getVoteStatus() == VoteStatus.NONE) {

                        int downvotes = opinionModel2.getDownVoteCount();
                        downvotes = downvotes + 1;
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVotedIcon);
                        opinionModel2.setDownVoteCount(downvotes);
                        opinionModel2.setVoteStatus(VoteStatus.DOWNVOTED);

                        /*
                         *  send the request to server to decrease the count
                         */
                        TrendsHelper.getHelper().upvoteDownvote(true, opinionModel2.getTrendId(), new TrendsHelper.OnUVDVOperationCompleteListener() {
                            @Override
                            public void onCompleteMessage(String message) {
                                CommonUtils.showToast(mContext, message);
                            }

                    });
                }

                else CommonUtils.showToast(mContext,"Cannot DownVote");
            }
        });
        }


        @Override
        public void onClick(View v) {

            TrendsDataModel trend = mFeeds.get(getLayoutPosition());

            TopicDataModel topicDataModel = new TopicDataModel();
            topicDataModel.setTopicDescription(trend.getDescription());
            topicDataModel.setRenewalRequests(trend.getRenewCount());
            topicDataModel.setTopicName(trend.getTopicName());
            topicDataModel.setTopicId(trend.getTopicId());
            topicDataModel.setHoursLeft(trend.getHoursLeft());

            //TODO: set renew status

            CommonUtils.openContainerActivity(mContext, Constants.OPINIONS_FRAGMENT,
                    Pair.create(Constants.TOPIC_MODEL, (Serializable) topicDataModel),0);

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
        if(trendsDataModel.getRenewCount()==0) {
            holder.decayTimeHolder.setText(String.valueOf(trendsDataModel.getHoursLeft()) + "hrs left to decay");
        }
        else {
            holder.decayTimeHolder.setText(String.valueOf(trendsDataModel.getHoursLeft()) + "hrs left to decay | "+String.valueOf(trendsDataModel.getHoursLeft())+" Renewal");
        }
        if(trendsDataModel.getVoteStatus() == VoteStatus.NONE){
            setCompoundDrawables(holder.upVoteBtn, holder.upVoteIcon);
            setCompoundDrawables(holder.downVoteBtn, holder.downVoteIcon);
        }else if(trendsDataModel.getVoteStatus()== VoteStatus.UPVOTED){
            setCompoundDrawables(holder.upVoteBtn, holder.upVotedIcon);
            setCompoundDrawables(holder.downVoteBtn, holder.downVoteIcon);
        }else {
            setCompoundDrawables(holder.upVoteBtn, holder.upVoteIcon);
            setCompoundDrawables(holder.downVoteBtn, holder.downVotedIcon);
        }


    }

    public void addTrendItem(TrendsDataModel trendsDataModel, int position){
        mFeeds.add(position, trendsDataModel);
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

    private void setCompoundDrawables(TextView textView, Drawable drawable){
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,null);
    }
}
