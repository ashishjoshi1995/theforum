package com.theforum.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.data.server.topic;

import java.util.ArrayList;

/**
 * Created by Ashish on 1/7/2016.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private ArrayList<topic> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public TextView textView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.search_result_element_textView);
            textView = (TextView) v.findViewById(R.id.search_result_element_topic);
        }
    }


    public ResultAdapter(ArrayList<topic> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_element, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText(mDataset.get(position).getTopicName());
        int a  = mDataset.get(position).getHoursLeft();
        int b = mDataset.get(position).getRenewedCount();
        String c = a + "hrs left to decay |"+ b + "renewal";
        holder.mTextView.setText(c);

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
