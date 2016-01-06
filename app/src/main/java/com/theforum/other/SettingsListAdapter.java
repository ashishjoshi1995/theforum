package com.theforum.other;

/**
 * @author DEEPANKAR
 * @since 07-01-2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theforum.utils.CommonUtils;
import com.theforum.utils.OnListItemClickListener;

import java.util.ArrayList;

public class SettingsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> mData;
    private Context mContext;
    private OnListItemClickListener onListItemClickListener;

    private final static int VIEW_TYPE_HEADER = 0;
    private final static int VIEW_TYPE_OPTION = 1;

    public SettingsListAdapter(Fragment fragment, ArrayList<String> dataSet) {
        mData = dataSet;
        mContext = fragment.getActivity();
        onListItemClickListener = (OnListItemClickListener)fragment;
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView header;

        public HeaderViewHolder(View v) {
            super(v);
            header = (TextView)v;
            header.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) CommonUtils.convertDpToPixel(34,mContext)));
            header.setGravity(Gravity.CENTER_VERTICAL);
            header.setTextColor(Color.parseColor("#1f81dd"));
            header.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        }
    }


    public class OptionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView option;

        public OptionsViewHolder(View itemView) {
            super(itemView);
            option = (TextView)itemView;
            option.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) CommonUtils.convertDpToPixel(56,mContext)));
            option.setGravity(Gravity.CENTER_VERTICAL);
            option.setBackgroundColor(Color.parseColor("#fafafa"));
            option.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
             onListItemClickListener.onItemClick(v, getLayoutPosition());
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0||position==2||position==5||position==8){
            return VIEW_TYPE_HEADER;
        }else return VIEW_TYPE_OPTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_HEADER){
            return new HeaderViewHolder(new TextView(mContext));
        }else{
            return new OptionsViewHolder(new TextView(mContext));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

        if(holder.getItemViewType()==VIEW_TYPE_HEADER){
            ((HeaderViewHolder)holder).header.setText(mData.get(position));
        }else {
            final OptionsViewHolder optionsViewHolder = (OptionsViewHolder)holder;
                optionsViewHolder.option.setText(mData.get(position));
            }
        }


    }






