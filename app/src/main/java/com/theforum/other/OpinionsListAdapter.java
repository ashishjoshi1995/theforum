package com.theforum.other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


    public class OpinionsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.opinion_opinion) TextView opinionText;
        @Bind(R.id.upvote_btn) TextView upVoteBtn;
        @Bind(R.id.downvote_btn) TextView downVoteBtn;

        @BindDrawable(R.drawable.upvote) Drawable upVoteIcon;
        @BindDrawable(R.drawable.upvote_on) Drawable upVotedIcon;
        @BindDrawable(R.drawable.downvote) Drawable downVoteIcon;
        @BindDrawable(R.drawable.downvote_on) Drawable downVotedIcon;

        public OpinionsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            upVoteBtn.setOnClickListener(this);
            downVoteBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.upvote_btn:
                    opinion opinionModel = mOpinionList.get(getLayoutPosition());
                    if(opinionModel.getVoteStatus() == opinion.VoteStatus.NONE) {
                        int upvotes = opinionModel.getUpVotes();
                        upvotes = upvotes+1;

                        upVoteBtn.setText(String.valueOf(upvotes));
                        setCompoundDrawables(upVoteBtn, upVotedIcon);
                        opinionModel.setUpVotes(upvotes);
                        opinionModel.setVoteStatus(opinion.VoteStatus.UPVOTED);

                        /*
                         *  send the request to server to increase the count
                         */
                        OpinionHelper.getHelper().upvoteDownvote(true, opinionModel,
                                new OpinionHelper.OnUVDVOperationCompleteListener() {
                                    @Override
                                    public void onCompleteMessage(String message) {
                                        CommonUtils.showToast(mContext, message);
                                    }
                                });
                    }else CommonUtils.showToast(mContext, "Cannot UpVote");
                    break;

                case R.id.downvote_btn:
                    final opinion opinionModel2 = mOpinionList.get(getLayoutPosition());
                    if(opinionModel2.getVoteStatus() == opinion.VoteStatus.NONE) {

                        int downvotes = opinionModel2.getDownVotes();
                        downvotes = downvotes + 1;
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVotedIcon);
                        opinionModel2.setDownVotes(downvotes);
                        opinionModel2.setVoteStatus(opinion.VoteStatus.DOWNVOTED);

                        /*
                         *  send the request to server to decrease the count
                         */
                        OpinionHelper.getHelper().upvoteDownvote(true, opinionModel2,
                                new OpinionHelper.OnUVDVOperationCompleteListener() {
                                    @Override
                                    public void onCompleteMessage(String message) {
                                        CommonUtils.showToast(mContext, message);
                                    }
                                });

                    }else CommonUtils.showToast(mContext, "Cannot DownVote");

                    break;
            }
        }
    }

    @Override
    public OpinionsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OpinionsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.opinion_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final OpinionsItemViewHolder holder, final int position) {
        final opinion opinionModel = mOpinionList.get(position);
        holder.opinionText.setText(opinionModel.getOpinionName());
        holder.upVoteBtn.setText(String.valueOf(opinionModel.getUpVotes()));
        holder.downVoteBtn.setText(String.valueOf(opinionModel.getDownVotes()));

        Log.e("vote Status",""+ opinionModel.getVoteStatus());
        if(opinionModel.getVoteStatus() == opinion.VoteStatus.NONE){
            setCompoundDrawables(holder.upVoteBtn, holder.upVoteIcon);
            setCompoundDrawables(holder.downVoteBtn, holder.downVoteIcon);
        }else if(opinionModel.getVoteStatus()== opinion.VoteStatus.UPVOTED){
            setCompoundDrawables(holder.upVoteBtn, holder.upVotedIcon);
            setCompoundDrawables(holder.downVoteBtn, holder.downVoteIcon);
        }else {
            setCompoundDrawables(holder.upVoteBtn, holder.upVoteIcon);
            setCompoundDrawables(holder.downVoteBtn, holder.downVotedIcon);
        }

    }

    private void setCompoundDrawables(TextView textView, Drawable drawable){
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,null);
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
