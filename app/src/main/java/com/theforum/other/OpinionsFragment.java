package com.theforum.other;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.home.TopicsListAdapter;
import com.theforum.home.TopicsModel;
import com.theforum.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class OpinionsFragment extends Fragment {

    @Bind(R.id.opinion_recycler_view) RecyclerView recyclerView;

    @Bind(R.id.opinion_toolbar) Toolbar toolbar;

    @Bind(R.id.opinion_fab) FloatingActionButton fab;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opinion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        List<TopicsModel> mFeeds = new ArrayList<>();
        for (int i=0;i<10;i++){
            mFeeds.add(new TopicsModel());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new TopicsListAdapter(getActivity(), mFeeds));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openContainerActivity(getActivity(), Constants.NEW_OPINION_FRAGMENT);
            }
        });
    }
}
