package com.theforum.ui.opinion;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.Messages;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.data.helpers.localHelpers.LocalTrendsHelper;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.enums.VoteStatus;
import com.theforum.utils.listeners.OnListItemClickListener;

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
    private List<OpinionDataModel> mOpinionList;
    private OnListItemClickListener onListItemClickListener;

    public OpinionsListAdapter(Context context, List<OpinionDataModel> feeds){
        mContext = context;
        mOpinionList = feeds;
    }

    public void setOnListItemClickListener(OnListItemClickListener listItemClickListener){
        onListItemClickListener = listItemClickListener;
    }

    public class OpinionsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.opinion_opinion) TextView opinionText;
        @Bind(R.id.upvote_btn) TextView upVoteBtn;
        @Bind(R.id.down_vote_btn) TextView downVoteBtn;

        @BindDrawable(R.drawable.upvote) Drawable upVoteIcon;
        @BindDrawable(R.drawable.upvote_on) Drawable upVotedIcon;
        @BindDrawable(R.drawable.downvote) Drawable downVoteIcon;
        @BindDrawable(R.drawable.downvote_on) Drawable downVotedIcon;


        public OpinionsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            upVoteBtn.setOnClickListener(this);
            downVoteBtn.setOnClickListener(this);

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onListItemClickListener.onItemClick(v, getLayoutPosition());
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
           if( CommonUtils.isInternetAvailable()){
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
                        if (!opinionModel.isLocal()){
                        TrendsHelper.getHelper().upVoteDownVote(true, opinionModel.getOpinionId(),
                                new TrendsHelper.OnUVDVOperationCompleteListener() {
                            @Override
                            public void onCompleteMessage(String message) {}

                            @Override
                            public void onErrorMessage(String message) {
                                CommonUtils.showToast(mContext,Messages.SERVER_ERROR);
                            }
                        });}
                    else {
                        LocalTrendsHelper.getHelper().upVoteDownVote(true, opinionModel.getOpinionId(), new LocalTrendsHelper.OnUVDVOperationCompleteListener() {
                            @Override
                            public void onCompleteMessage(String message) {

                            }

                            @Override
                            public void onErrorMessage(String message) {
                                CommonUtils.showToast(mContext, message);
                            }
                        });
                    }

                    }
                    else if(opinionModel.getVoteStatus() == VoteStatus.UPVOTED){
                        int upvotes = opinionModel.getUpVoteCount();
                        if(upvotes!=0){
                            upvotes = upvotes - 1;}

                        upVoteBtn.setText(String.valueOf(upvotes));
                        setCompoundDrawables(upVoteBtn, upVoteIcon);
                        opinionModel.setUpVoteCount(upvotes);
                        opinionModel.setVoteStatus(VoteStatus.NONE);

                        /*
                         *  send the request to server to increase the count
                         */
                        if (!opinionModel.isLocal()) {
                            TrendsHelper.getHelper().removeUpDownVote(true, opinionModel.getOpinionId(),
                                    new TrendsHelper.OnRUDAOperationCompleteListener() {
                                        @Override
                                        public void onCompleteMessage(String message) {
                                            //CommonUtils.showToast(mContext, message);
                                        }

                                        @Override
                                        public void onErrorMessage(String message) {
                                            CommonUtils.showToast(mContext, Messages.SERVER_ERROR);
                                        }
                                    });
                        }
                        else {
                            LocalTrendsHelper.getHelper().removeUpDownVote(true, opinionModel.getOpinionId(),
                                    new LocalTrendsHelper.OnRUDAOperationCompleteListener() {
                                        @Override
                                        public void onCompleteMessage(String message) {

                                        }

                                        @Override
                                        public void onErrorMessage(String message) {
                                            CommonUtils.showToast(mContext, message);
                                        }
                                    });
                        }
                    }
                    else if(opinionModel.getVoteStatus() == VoteStatus.DOWNVOTED){
                        int downvotes = opinionModel.getDownVoteCount();
                        if(downvotes!=0){
                        downvotes = downvotes - 1;}
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVoteIcon);
                        opinionModel.setDownVoteCount(downvotes);
                        //opinionModel.setVoteStatus(VoteStatus.NONE);
                        int upvotes = opinionModel.getUpVoteCount();
                        upvotes = upvotes+1;

                        upVoteBtn.setText(String.valueOf(upvotes));
                        setCompoundDrawables(upVoteBtn, upVotedIcon);
                        opinionModel.setUpVoteCount(upvotes);
                        opinionModel.setVoteStatus(VoteStatus.UPVOTED);
                        if (!opinionModel.isLocal()) {
                            TrendsHelper.getHelper().directUpDownVoteChange(true, opinionModel.getOpinionId(), new TrendsHelper.OnDUDAOperationCompleteListener() {
                                @Override
                                public void onCompleteMessage(String message) {
                                    //CommonUtils.showToast(mContext, message);
                                }

                                @Override
                                public void onErrorMessage(String message) {
                                    CommonUtils.showToast(mContext, Messages.SERVER_ERROR);
                                }
                            });
                        }
                        else {
                            LocalTrendsHelper.getHelper().directUpDownVoteChange(true,opinionModel.getOpinionId(), new LocalTrendsHelper.OnDUDAOperationCompleteListener() {
                                @Override
                                public void onCompleteMessage(String message) {

                                }

                                @Override
                                public void onErrorMessage(String message) {
                                    CommonUtils.showToast(mContext, Messages.SERVER_ERROR);
                                }
                            });
                        }


                    }
                    break;

                case R.id.down_vote_btn:
                    final OpinionDataModel opinionModel2 = mOpinionList.get(getLayoutPosition());
                    if (opinionModel2.getVoteStatus() == VoteStatus.NONE) {

                        int downvotes = opinionModel2.getDownVoteCount();
                        downvotes = downvotes + 1;
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVotedIcon);
                        opinionModel2.setDownVoteCount(downvotes);
                        opinionModel2.setVoteStatus(VoteStatus.DOWNVOTED);
                        //TrendsDBHelper.getHelper().updateUPDVStatus(opinionModel2);
                        /*
                         *  send the request to server to decrease the count
                         */
                        if (!opinionModel2.isLocal()) {
                            TrendsHelper.getHelper().upVoteDownVote(false, opinionModel2.getOpinionId(), new TrendsHelper.OnUVDVOperationCompleteListener() {
                                @Override
                                public void onCompleteMessage(String message) {
                                    //CommonUtils.showToast(mContext, message);
                                }

                                @Override
                                public void onErrorMessage(String message) {
                                    CommonUtils.showToast(mContext, Messages.SERVER_ERROR);
                                }
                            });
                        }
                        else {
                            LocalTrendsHelper.getHelper().upVoteDownVote(false, opinionModel2.getOpinionId(), new LocalTrendsHelper.OnUVDVOperationCompleteListener() {
                                @Override
                                public void onCompleteMessage(String message) {

                                }

                                @Override
                                public void onErrorMessage(String message) {
                                    CommonUtils.showToast(mContext, message);
                                }
                            });
                        }
                    }

                    else if(opinionModel2.getVoteStatus() == VoteStatus.DOWNVOTED){
                        int downvotes = opinionModel2.getDownVoteCount();
                        if(downvotes!=0){
                            downvotes = downvotes - 1;}
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVoteIcon);
                        opinionModel2.setDownVoteCount(downvotes);
                        opinionModel2.setVoteStatus(VoteStatus.NONE);
                        // TrendsDBHelper.getHelper().updateUPDVStatus(opinionModel2);
                        if (!opinionModel2.isLocal()) {
                            TrendsHelper.getHelper().removeUpDownVote(false, opinionModel2.getOpinionId(), new TrendsHelper.OnRUDAOperationCompleteListener() {
                                @Override
                                public void onCompleteMessage(String message) {
                                    //CommonUtils.showToast(mContext, message);
                                }

                                @Override
                                public void onErrorMessage(String message) {
                                    CommonUtils.showToast(mContext, Messages.SERVER_ERROR);
                                }
                            });
                        }
                        else {
                            LocalTrendsHelper.getHelper().removeUpDownVote(false, opinionModel2.getOpinionId(),
                                    new LocalTrendsHelper.OnRUDAOperationCompleteListener() {
                                        @Override
                                        public void onCompleteMessage(String message) {

                                        }

                                        @Override
                                        public void onErrorMessage(String message) {
                                            CommonUtils.showToast(mContext, message);
                                        }
                                    });
                        }
                    }
                    else if(opinionModel2.getVoteStatus() == VoteStatus.UPVOTED){
                        int upvotes = opinionModel2.getUpVoteCount();
                        if(upvotes!=0){
                            upvotes = upvotes - 1;}

                        upVoteBtn.setText(String.valueOf(upvotes));
                        setCompoundDrawables(upVoteBtn, upVoteIcon);
                        opinionModel2.setUpVoteCount(upvotes);
                        //opinionModel2.setVoteStatus(VoteStatus.NONE);
                        int downvotes = opinionModel2.getDownVoteCount();
                        downvotes = downvotes + 1;
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVotedIcon);
                        opinionModel2.setDownVoteCount(downvotes);
                        opinionModel2.setVoteStatus(VoteStatus.DOWNVOTED);
                        // TrendsDBHelper.getHelper().updateUPDVStatus(opinionModel2);
                        /*
                         *  send the request to server to increase the count
                         */
                        if (!opinionModel2.isLocal()) {
                            TrendsHelper.getHelper().directUpDownVoteChange(false, opinionModel2.getOpinionId(),
                                    new TrendsHelper.OnDUDAOperationCompleteListener() {
                                        @Override
                                        public void onCompleteMessage(String message) {
                                            //CommonUtils.showToast(mContext, message);
                                        }

                                        @Override
                                        public void onErrorMessage(String message) {
                                            CommonUtils.showToast(mContext, Messages.SERVER_ERROR);
                                        }
                                    });
                        }
                        else {
                            LocalTrendsHelper.getHelper().directUpDownVoteChange(false,opinionModel2.getOpinionId(), new LocalTrendsHelper.OnDUDAOperationCompleteListener() {
                                @Override
                                public void onCompleteMessage(String message) {

                                }

                                @Override
                                public void onErrorMessage(String message) {
                                    CommonUtils.showToast(mContext, Messages.SERVER_ERROR);
                                }
                            });
                        }
                    }
                    break;
            }}
            else {
               CommonUtils.showToast(mContext, Messages.NO_NET_CONNECTION);
           }
        }

    }

    @Override
    public OpinionsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new OpinionsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_opinion, parent, false));
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
