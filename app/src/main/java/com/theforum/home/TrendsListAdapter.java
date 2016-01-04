package com.theforum.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.CustomFontTextView;

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
    private List<TrendsModel> mFeeds;


    public TrendsListAdapter(Context context, List<TrendsModel> feeds){
        mContext = context;
        mFeeds = feeds;
    }


    public class TrendsItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.trends_topic_name) TextView topicName;
        @Bind(R.id.trends_description) TextView description;
        @Bind(R.id.trends_decay_time)TextView decayTimeHolder;
        @Bind(R.id.trends_upvote_btn) TextView upVoteBtn;
        @Bind(R.id.trends_downvote_btn) TextView downVoteBtn;

        @BindDrawable(R.drawable.upvote) Drawable upvote;
        @BindDrawable(R.drawable.downvote) Drawable downvote;

        public TrendsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            upVoteBtn.setCompoundDrawablesWithIntrinsicBounds(null, CommonUtils.tintDrawable(upvote, "#adadad"),
                    null, null);
            downVoteBtn.setCompoundDrawablesWithIntrinsicBounds(null, CommonUtils.tintDrawable(downvote,"#adadad"),
                    null,null);
        }


    }

    @Override
    public TrendsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrendsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.trends_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TrendsItemViewHolder holder, int position) {

    }


    public void addFeedItem(TrendsModel trendsDataModel){
        mFeeds.add(0,trendsDataModel);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {return mFeeds.size();}
}
