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

public class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.OptionsViewHolder> {

    private ArrayList<String> mData;
    private Context mContext;
    private OnListItemClickListener onListItemClickListener;


    public SettingsListAdapter(Fragment fragment, ArrayList<String> dataSet) {
        mData = dataSet;
        mContext = fragment.getActivity();
        onListItemClickListener = (OnListItemClickListener) fragment;
    }


    public class OptionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView option;

        public OptionsViewHolder(View itemView) {
            super(itemView);
            option = (TextView) itemView;
            option.setGravity(Gravity.CENTER_VERTICAL);
            option.setPadding((int) CommonUtils.convertDpToPixel(16, mContext), 0, 0, 0);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            if (position == 1 || position == 3 || position == 4 || position == 6||position==7||position==9) {
                onListItemClickListener.onItemClick(v,position );
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public OptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OptionsViewHolder(new TextView(mContext));
    }

    @Override
    public void onBindViewHolder(OptionsViewHolder holder, int position) {

        holder.option.setText(mData.get(position));

        if (position == 0 || position == 2 || position == 5 || position == 8) {

            holder.option.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) CommonUtils.convertDpToPixel(34, mContext)));
            holder.option.setTextColor(Color.parseColor("#1f81dd"));
            holder.option.setBackgroundColor(Color.parseColor("#fafafa"));
            holder.option.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        } else {

            holder.option.setBackgroundColor(Color.WHITE);
            holder.option.setTextColor(Color.BLACK);
            holder.option.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) CommonUtils.convertDpToPixel(56, mContext)));
            holder.option.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        }

    }


}


