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
import com.theforum.data.helpers.OpinionHelper;
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
public class OpinionsListAdapter extends RecyclerView.Adapter<OpinionsListAdapter.OpinionsItemViewHolder> {

    private Context mContext;

    /* list of data */
    private List<opinion> mOpinionList;


    public OpinionsListAdapter(Context context, List<opinion> feeds){
        mContext = context;
        mOpinionList = feeds;
    }


    public class OpinionsItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.opinion_opinion) TextView opinionText;
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
        final opinion opinionModel = mOpinionList.get(position);
        holder.opinionText.setText(opinionModel.getmOpinion());
        holder.upVoteBtn.setText(String.valueOf(opinionModel.getmUpVotes()));
        holder.downVoteBtn.setText(String.valueOf(opinionModel.getmDownVotes()));

        holder.upVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpinionHelper.getHelper().upvoteDownvote(true, opinionModel, new OpinionHelper.OnUVDVOperationCompleteListener() {
                    @Override
                    public void onCompleteMessage(String message) {
                        CommonUtils.showToast(mContext,message);
                    }
                });
            }
        });

        holder.downVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpinionHelper.getHelper().upvoteDownvote(false, opinionModel, new OpinionHelper.OnUVDVOperationCompleteListener() {
                    @Override
                    public void onCompleteMessage(String message) {
                        CommonUtils.showToast(mContext,message);
                    }
                });
            }
        });
    }


    public void addFeedItem(opinion opinionDataModel, int position){
        mOpinionList.add(position, opinionDataModel);
        notifyDataSetChanged();
    }

    public void addOpinions(ArrayList<opinion> list){
        mOpinionList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {return mOpinionList.size();}
}
