package com.theforum.other;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.dataModels.opinion;
import com.theforum.data.dataModels.topic;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.customViews.DividerItemDecorator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class OpinionsFragment extends Fragment {

    @Bind(R.id.opinion_recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.opinion_toolbar) Toolbar mToolbar;
    @Bind(R.id.opinion_collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.opinion_fab) FloatingActionButton fab;

    private topic topicModel;
    private boolean first;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments()!=null){
            topicModel = (topic) getArguments().getSerializable(Constants.TOPIC_MODEL);
            Log.e("topicId",""+topicModel.getmTopicId());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opinion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mToolbar.setTitle("#Take Them back");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mCollapsingToolbarLayout.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setPadding(0, mCollapsingToolbarLayout.getHeight(), 0, 0);
                mRecyclerView.setClipToPadding(false);
            }
        });


        List<opinion> mFeeds = new ArrayList<>();
        for (int i=0;i<10;i++){
            mFeeds.add(new opinion());
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));
        mRecyclerView.setAdapter(new OpinionsListAdapter(getActivity(), mFeeds));
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("dy",""+dy);
            }
        };

       // mRecyclerView.addOnScrollListener(onScrollListener);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openContainerActivity(getActivity(), Constants.NEW_OPINION_FRAGMENT);
            }
        });
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_other, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_settings) CommonUtils.openContainerActivity(getContext(),
                Constants.SETTINGS_FRAGMENT);

        return super.onOptionsItemSelected(item);
    }


}
