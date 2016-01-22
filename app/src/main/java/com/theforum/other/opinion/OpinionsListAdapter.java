package com.theforum.other.opinion;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.data.helpers.OpinionHelper;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.enums.VoteStatus;

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
    private List<OpinionDataModel> mOpinionList;


    public OpinionsListAdapter(Context context, List<OpinionDataModel> feeds){
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
                    OpinionDataModel opinionModel = mOpinionList.get(getLayoutPosition());

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
                        OpinionHelper.getHelper().upVoteDownVote(true, opinionModel.getOpinionId(),
                                new OpinionHelper.OnUVDVOperationCompleteListener() {
                                    @Override
                                    public void onCompleteMessage(String message) {
                                        CommonUtils.showToast(mContext, message);
                                    }
                                });
                    }else CommonUtils.showToast(mContext, "Cannot UpVote");
                    break;

                case R.id.downvote_btn:
                    final OpinionDataModel opinionModel2 = mOpinionList.get(getLayoutPosition());
                    if(opinionModel2.getVoteStatus() == VoteStatus.NONE) {

                        int downvotes = opinionModel2.getDownVoteCount();
                        downvotes = downvotes + 1;
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVotedIcon);
                        opinionModel2.setDownVoteCount(downvotes);
                        opinionModel2.setVoteStatus(VoteStatus.DOWNVOTED);

                        /*
                         *  send the request to server to decrease the count
                         */
                        OpinionHelper.getHelper().upVoteDownVote(true, opinionModel2.getOpinionId(),
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

        final OpinionDataModel opinionModel = mOpinionList.get(position);
        holder.opinionText.setText(opinionModel.getOpinionText());
        holder.upVoteBtn.setText(String.valueOf(opinionModel.getUpVoteCount()));
        holder.downVoteBtn.setText(String.valueOf(opinionModel.getDownVoteCount()));

        if(opinionModel.getVoteStatus() == VoteStatus.NONE){
            setCompoundDrawables(holder.upVoteBtn, holder.upVoteIcon);
            setCompoundDrawables(holder.downVoteBtn, holder.downVoteIcon);
        }else if(opinionModel.getVoteStatus()== VoteStatus.UPVOTED){
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


    public void addOpinion(OpinionDataModel opinionDataModel, int position){
        mOpinionList.add(position, opinionDataModel);
        notifyDataSetChanged();
    }

    public void addOpinions(ArrayList<OpinionDataModel> list){
        mOpinionList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearAll(){
        mOpinionList.clear();
    }


    @Override
    public int getItemCount() {return mOpinionList.size();}
}
