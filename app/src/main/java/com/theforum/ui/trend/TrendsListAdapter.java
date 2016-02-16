package com.theforum.ui.trend;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.constants.Messages;
import com.theforum.data.helpers.FlagHelper;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.data.helpers.localHelpers.LocalTrendsHelper;
import com.theforum.data.local.database.trendsDB.TrendsDBHelper;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
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

public class TrendsListAdapter extends RecyclerView.Adapter<TrendsListAdapter.TrendsItemViewHolder> {

    private Context mContext;

    private List<TrendsDataModel> mFeeds;
   // private OnListItemClickListener onListItemClickListener2;


    public TrendsListAdapter(Context context, List<TrendsDataModel> feeds){
        mContext = context;
        mFeeds = feeds;
    }


    public class TrendsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.trends_topic_name) TextView topicName;
        @Bind(R.id.trends_description) TextView description;
        @Bind(R.id.trends_decay_time)TextView timeHolder;
        @Bind(R.id.upvote_btn) TextView upVoteBtn;
        @Bind(R.id.down_vote_btn) TextView downVoteBtn;

        @BindDrawable(R.drawable.upvote) Drawable upVoteIcon;
        @BindDrawable(R.drawable.upvote_on) Drawable upVotedIcon;
        @BindDrawable(R.drawable.downvote) Drawable downVoteIcon;
        @BindDrawable(R.drawable.downvote_on) Drawable downVotedIcon;

        public TrendsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(this);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(mContext, v);
                    popupMenu.inflate(R.menu.popup_menu);
                    if(!mFeeds.get(getLayoutPosition()).getuId().equals(User.getInstance().getId())){
                        popupMenu.getMenu().removeItem(R.id.item_edit);
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.item_edit:
                                    OpinionDataModel opinionDataModel = new OpinionDataModel();
                                    opinionDataModel.setuId(mFeeds.get(getLayoutPosition()).getuId());
                                    opinionDataModel.setDownVoteCount(mFeeds.get(getLayoutPosition()).getDownVoteCount());
                                    opinionDataModel.setUpVoteCount(mFeeds.get(getLayoutPosition()).getUpVoteCount());
                                    opinionDataModel.setOpinionText(mFeeds.get(getLayoutPosition()).getOpinionText());
                                    opinionDataModel.setTopicName(mFeeds.get(getLayoutPosition()).getTopicName());
                                    opinionDataModel.setTopicId(mFeeds.get(getLayoutPosition()).getTopicId());
                                    opinionDataModel.setLatitude(mFeeds.get(getLayoutPosition()).getLatitude());
                                    opinionDataModel.setLongitude(mFeeds.get(getLayoutPosition()).getLongitude());
                                    opinionDataModel.setVoteStatus(mFeeds.get(getLayoutPosition()).getVoteStatus());
                                    opinionDataModel.setOpinionId(mFeeds.get(getLayoutPosition()).getTrendId());

                                    CommonUtils.openContainerActivity(mContext, LayoutType.NEW_OPINION_FRAGMENT,
                                            Pair.create(LayoutType.OPINION_MODEL, (Parcelable) opinionDataModel));
                                    break;

                                case R.id.item_flag:
                                    FlagHelper helper = new FlagHelper();
                                    helper.addFlagOpinionRequest(mFeeds.get(getLayoutPosition()).getTrendId(),
                                            mFeeds.get(getLayoutPosition()).getOpinionText(), mFeeds.get(getLayoutPosition()).getTopicId());
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    return false;
                }
            });

            upVoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtils.isInternetAvailable()) {
                        TrendsDataModel opinionModel = mFeeds.get(getLayoutPosition());

                        if (opinionModel.getVoteStatus() == VoteStatus.NONE) {
                            int upvotes = opinionModel.getUpVoteCount();
                            upvotes = upvotes + 1;

                            upVoteBtn.setText(String.valueOf(upvotes));
                            setCompoundDrawables(upVoteBtn, upVotedIcon);
                            opinionModel.setUpVoteCount(upvotes);
                            opinionModel.setVoteStatus(VoteStatus.UPVOTED);

                            Log.e("localtopic",""+opinionModel.isLocal());
                        /*
                         *  send the request to server to increase the count
                         */if(!opinionModel.isLocal()) {
                                TrendsHelper.getHelper().upVoteDownVote(true, opinionModel.getTrendId(), new TrendsHelper.OnUVDVOperationCompleteListener() {
                                    @Override
                                    public void onCompleteMessage(String message) {
                                        //CommonUtils.showToast(mContext, message);
                                    }

                                    @Override
                                    public void onErrorMessage(String message) {
                                        CommonUtils.showToast(mContext, message);
                                    }
                                });
                            }
                            else {
                                LocalTrendsHelper.getHelper().upVoteDownVote(true, opinionModel.getTrendId(), new LocalTrendsHelper.OnUVDVOperationCompleteListener() {
                                    @Override
                                    public void onCompleteMessage(String message) {

                                    }

                                    @Override
                                    public void onErrorMessage(String message) {
                                        CommonUtils.showToast(mContext, message);
                                    }
                                });
                            }

                        } else if (opinionModel.getVoteStatus() == VoteStatus.UPVOTED) {
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
                                TrendsHelper.getHelper().removeUpDownVote(true, opinionModel.getTrendId(),
                                        new TrendsHelper.OnRUDAOperationCompleteListener() {
                                            @Override
                                            public void onCompleteMessage(String message) {
                                                //CommonUtils.showToast(mContext, message);
                                            }

                                            @Override
                                            public void onErrorMessage(String message) {
                                                CommonUtils.showToast(mContext, message);
                                            }
                                        });
                            }
                            else {
                                LocalTrendsHelper.getHelper().removeUpDownVote(true, opinionModel.getTrendId(),
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
                        } else if (opinionModel.getVoteStatus() == VoteStatus.DOWNVOTED) {
                            int downvotes = opinionModel.getDownVoteCount();
                            if (downvotes != 0) {
                                downvotes = downvotes - 1;
                            }
                            downVoteBtn.setText(String.valueOf(downvotes));
                            setCompoundDrawables(downVoteBtn, downVoteIcon);
                            opinionModel.setDownVoteCount(downvotes);
                            //opinionModel.setVoteStatus(VoteStatus.NONE);
                            int upvotes = opinionModel.getUpVoteCount();
                            upvotes = upvotes + 1;

                            upVoteBtn.setText(String.valueOf(upvotes));
                            setCompoundDrawables(upVoteBtn, upVotedIcon);
                            opinionModel.setUpVoteCount(upvotes);
                            opinionModel.setVoteStatus(VoteStatus.UPVOTED);
                            if (!opinionModel.isLocal()) {


                                TrendsHelper.getHelper().directUpDownVoteChange(true, opinionModel.getTrendId(), new TrendsHelper.OnDUDAOperationCompleteListener() {
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
                                LocalTrendsHelper.getHelper().directUpDownVoteChange(true,opinionModel.getTrendId(), new LocalTrendsHelper.OnDUDAOperationCompleteListener() {
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
                        TrendsDBHelper.getHelper().updateUPDVStatus(opinionModel);
                    } else {
                        CommonUtils.showToast(mContext, Messages.NO_NET_CONNECTION);
                    }
                }
            });

            downVoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CommonUtils.isInternetAvailable()){
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
                        if (!opinionModel2.isLocal()) {
                            TrendsHelper.getHelper().upVoteDownVote(false, opinionModel2.getTrendId(), new TrendsHelper.OnUVDVOperationCompleteListener() {
                                @Override
                                public void onCompleteMessage(String message) {}

                                @Override
                                public void onErrorMessage(String message) {
                                    CommonUtils.showToast(mContext, message);
                                }
                            });
                        }
                        else {
                            LocalTrendsHelper.getHelper().upVoteDownVote(false, opinionModel2.getTrendId(), new LocalTrendsHelper.OnUVDVOperationCompleteListener() {
                                @Override
                                public void onCompleteMessage(String message) {

                                }

                                @Override
                                public void onErrorMessage(String message) {
                                    CommonUtils.showToast(mContext, message);
                                }
                            });
                        }

                    } else if (opinionModel2.getVoteStatus() == VoteStatus.DOWNVOTED) {
                        int downvotes = opinionModel2.getDownVoteCount();
                        if(downvotes!=0){
                            downvotes = downvotes - 1;}
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVoteIcon);
                        opinionModel2.setDownVoteCount(downvotes);
                        opinionModel2.setVoteStatus(VoteStatus.NONE);
                        // TrendsDBHelper.getHelper().updateUPDVStatus(opinionModel2);
                        if (!opinionModel2.isLocal()) {
                            TrendsHelper.getHelper().removeUpDownVote(false, opinionModel2.getTrendId(), new TrendsHelper.OnRUDAOperationCompleteListener() {
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
                            LocalTrendsHelper.getHelper().removeUpDownVote(false, opinionModel2.getTrendId(),
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
                    } else if (opinionModel2.getVoteStatus() == VoteStatus.UPVOTED) {
                        int upvotes = opinionModel2.getUpVoteCount();
                        if(upvotes!=0){
                            upvotes = upvotes - 1;}

                        upVoteBtn.setText(String.valueOf(upvotes));
                        setCompoundDrawables(upVoteBtn, upVoteIcon);
                        opinionModel2.setUpVoteCount(upvotes);

                        int downvotes = opinionModel2.getDownVoteCount();
                        downvotes = downvotes + 1;
                        downVoteBtn.setText(String.valueOf(downvotes));
                        setCompoundDrawables(downVoteBtn, downVotedIcon);
                        opinionModel2.setDownVoteCount(downvotes);
                        opinionModel2.setVoteStatus(VoteStatus.DOWNVOTED);

                        /*
                         *  send the request to server to increase the count
                         */
                        if (!opinionModel2.isLocal()) {
                            TrendsHelper.getHelper().directUpDownVoteChange(false, opinionModel2.getTrendId(),
                                    new TrendsHelper.OnDUDAOperationCompleteListener() {
                                        @Override
                                        public void onCompleteMessage(String message) {
                                        }

                                        @Override
                                        public void onErrorMessage(String message) {
                                            CommonUtils.showToast(mContext, Messages.SERVER_ERROR);
                                        }
                                    });
                        }
                        else {
                            LocalTrendsHelper.getHelper().directUpDownVoteChange(false,opinionModel2.getTrendId(), new LocalTrendsHelper.OnDUDAOperationCompleteListener() {
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
                    TrendsDBHelper.getHelper().updateUPDVStatus(opinionModel2);
                }else {
                        CommonUtils.showToast(mContext, Messages.NO_NET_CONNECTION);
                    }
            }
        });
        }


        @Override
        public void onClick(View v) {

            TrendsDataModel trend = mFeeds.get(getLayoutPosition());

            TopicDataModel topicDataModel = new TopicDataModel();
            topicDataModel.setTopicDescription(trend.getDescription());
            int p=0;
            if(trend.getRenewalIds()!=null) {
                String[] r = trend.getRenewalIds().split(" ");
                p=r.length;
                for (int k = 0; k < r.length; k++) {
                    if (r[k].equals(User.getInstance().getId())) {
                        topicDataModel.setIsRenewed(true);
                        break;
                    }
                }
            }

            topicDataModel.setRenewalRequests(p);
            topicDataModel.setTopicName(trend.getTopicName());
            topicDataModel.setTopicId(trend.getTopicId());
            topicDataModel.setHoursLeft(trend.getHoursLeft());
            if(trend.isLocal()){
                topicDataModel.setIsLocalTopic(true);
            }

            CommonUtils.openContainerActivity(mContext, LayoutType.OPINIONS_FRAGMENT,
                    Pair.create(LayoutType.TOPIC_MODEL, (Parcelable) topicDataModel));

        }

    }

    @Override
    public TrendsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrendsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_trend, parent, false));
    }

    @Override
    public void onBindViewHolder(TrendsItemViewHolder holder, final int position) {
        final TrendsDataModel trendsDataModel = mFeeds.get(position);

        holder.topicName.setText(trendsDataModel.getTopicName());
        holder.description.setText(trendsDataModel.getOpinionText());
        holder.upVoteBtn.setText(String.valueOf(trendsDataModel.getUpVoteCount()));
        holder.downVoteBtn.setText(String.valueOf(trendsDataModel.getDownVoteCount()));

        holder.timeHolder.setText(Html.fromHtml(mContext.getResources().getQuantityString(
                R.plurals.time_holder_message,
                trendsDataModel.getRenewCount() + 1,
                trendsDataModel.getHoursLeft(),
                trendsDataModel.getRenewCount())));

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
