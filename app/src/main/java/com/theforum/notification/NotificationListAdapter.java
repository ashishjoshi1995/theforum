package com.theforum.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.data.local.NotificationInflatorItemData;
import com.theforum.utils.OnListItemClickListener;

import java.util.ArrayList;

/**
 * @author DEEPANKAR
 * @since 07-01-2016.
 */


public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NotificationInflatorItemData> mData;
    private Context mContext;
    private OnListItemClickListener onListItemClickListener;

    private final static int VIEW_TYPE_ONE = 0;
    private final static int VIEW_TYPE_TWO = 1;

    public NotificationListAdapter(ArrayList<NotificationInflatorItemData> dataSet,Context context) {
        mData = dataSet;
        mContext = context;
    }

    public static void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        //  SettingsMainAdapter.onListItemClickListener = onListItemClickListener;
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        public ViewHolderOne(View v) {
            super(v);
           // headerSection = (TextView)v.findViewById(R.id.settings_item_section_text);
        }
    }


    public static class ViewHolderTwo extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView optionName;
        TextView popUpOptionName;
        CheckBox checkBox;

        public ViewHolderTwo(View itemView) {
            super(itemView);

        /*    optionName = (TextView) itemView.findViewById(R.id.settings_item_option_name);
            popUpOptionName = (TextView) itemView.findViewById(R.id.settings_item_pop_up_option);
            checkBox = (CheckBox) itemView.findViewById(R.id.settings_item_check_box);*/

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // onListItemClickListener.onItemClick(getLayoutPosition(), v);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0||position==6||position==8||position==10||position==14||position==17){
            return VIEW_TYPE_ONE;
        }else return VIEW_TYPE_TWO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== VIEW_TYPE_ONE){
            return new ViewHolderOne(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notifications_list_item_one, parent, false));
        }else{
            return new ViewHolderTwo(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notifications_list_item_two, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

        if(holder.getItemViewType()== VIEW_TYPE_ONE){
          //  ((ViewHolderOne)holder).headerSection.setText(mData.get(position).getOptionName());
        }else {
            final ViewHolderTwo viewHolderTwo = (ViewHolderTwo)holder;

            }
        }


    }







