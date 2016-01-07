package com.theforum.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.home.TopicsModel;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 08-12-2015.
 */

@SuppressWarnings("deprecation")
public class OpinionsListAdapter extends RecyclerView.Adapter<OpinionsListAdapter.OpinionsItemViewHolder> {

    private Context mContext;

    /* list of data */
    private List<TopicsModel> mFeeds;


    public OpinionsListAdapter(Context context, List<TopicsModel> feeds){
        mContext = context;
        mFeeds = feeds;
    }


    public class OpinionsItemViewHolder extends RecyclerView.ViewHolder {


        public OpinionsItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

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


    public void addFeedItem(TopicsModel feedDataModel){
        mFeeds.add(0,feedDataModel);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {return mFeeds.size();}
}
