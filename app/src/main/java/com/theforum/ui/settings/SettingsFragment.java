package com.theforum.ui.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.theforum.R;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.views.DividerItemDecorator;
import com.theforum.utils.listeners.OnListItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsFragment extends Fragment implements OnListItemClickListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.settings_recycler_view)
    RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mToolbar.setTitle("Options");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int)CommonUtils.convertDpToPixel(12, getContext()), 0, 0);
        mRecyclerView.setLayoutParams(params);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));
        mRecyclerView.setAdapter(new SettingsListAdapter(this, getListData()));

    }

    private ArrayList<String> getListData(){
        ArrayList<String> data = new ArrayList<>();
        data.add("Spread The World");
        data.add("Share the App");
        data.add("App");
        data.add("Rate Us");
        data.add("Feedback");
        data.add("Reach Us");
        data.add("Support");
        data.add("Contact Us");
        data.add("Legal");
        data.add("Terms Of Service");

        return data;
    }

    @Override
    public void onItemClick(View v, int position) {

    }
}