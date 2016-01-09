package com.theforum.other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.data.dataModels.opinion;
import com.theforum.utils.CommonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 08-12-2015.
 */

@SuppressWarnings("deprecation")
public class OpinionsListAdapter extends RecyclerView.Adapter<OpinionsListAdapter.OpinionsItemViewHolder> {

    private Context mContext;

    /* list of data */
    private List<opinion> mFeeds;


    public OpinionsListAdapter(Context context, List<opinion> feeds){
        mContext = context;
        mFeeds = feeds;
    }


    public class OpinionsItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.opinion_opinion) TextView opinion;
        @Bind(R.id.upvote_btn) TextView upVoteBtn;
        @Bind(R.id.downvote_btn) TextView downVoteBtn;

        @BindDrawable(R.drawable.upvote) Drawable upvote;
        @BindDrawable(R.drawable.downvote) Drawable downvote;

        public OpinionsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            upVoteBtn.setCompoundDrawablesWithIntrinsicBounds(null, CommonUtils.tintDrawable(upvote, "#adadad"),
                    null, null);
            downVoteBtn.setCompoundDrawablesWithIntrinsicBounds(null, CommonUtils.tintDrawable(downvote, "#adadad"),
                    null, null);

        }

    }

    @Override
    public OpinionsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OpinionsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.opinion_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(OpinionsItemViewHolder holder, int position) {

    }


    public void addFeedItem(opinion opinionDataModel){
        mFeeds.add(0,opinionDataModel);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {return mFeeds.size();}
}
