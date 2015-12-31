package com.theforum.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 08-12-2015.
 */

@SuppressWarnings("deprecation")
public class TopicsListAdapter extends RecyclerView.Adapter<TopicsListAdapter.TopicsItemViewHolder> {

    private Context mContext;

    /* list of feed data */
    private List<TopicsModel> mFeeds;


    public TopicsListAdapter(Context context, List<TopicsModel> feeds){
        mContext = context;
        mFeeds = feeds;
    }


    public class TopicsItemViewHolder extends RecyclerView.ViewHolder {

     /*   @Bind(R.id.feeds_video_view) TextureView videoView;
        @Bind(R.id.feeds_profile_pic) ImageView profilePic;
        @Bind(R.id.feeds_user_name) TextView username;
        @Bind(R.id.feeds_person_name) TextView personName;
        @Bind(R.id.feeds_follow_btn) ImageButton personFollowBtn;
        @Bind(R.id.feeds_description)TextView description;
        @Bind(R.id.feeds_country) TextView country;
        @Bind(R.id.feeds_count_holder) TextView section;
        @Bind(R.id.feeds_reflur_btn) ImageView reflurBtn;
        @Bind(R.id.feeds_comment_btn) ImageView commentBtn;
        @Bind(R.id.feeds_share_btn) ImageView shareBtn;
        @Bind(R.id.feeds_more_btn) ImageView moreBtn;*/


        public TopicsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }

    }

    @Override
    public TopicsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.topics_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TopicsItemViewHolder holder, int position) {

    }


    public void addFeedItem(TopicsModel feedDataModel){
        mFeeds.add(0,feedDataModel);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {return mFeeds.size();}
}
