package com.theforum.other;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.helpers.OpinionHelper;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.customViews.DividerItemDecorator;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class OpinionsFragment extends Fragment {

    @Bind(R.id.opinion_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.home_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.opinion_toolbar) Toolbar toolbar;
    @Bind(R.id.opinion_collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.opinion_topic_description) TextView topicDescription;
    @Bind(R.id.opinion_fab) FloatingActionButton fab;

    private OpinionsListAdapter mAdapter;
    private TopicDataModel mTopicModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments()!=null){
            mTopicModel = (TopicDataModel) getArguments().getSerializable(Constants.TOPIC_MODEL);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opinion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        toolbar.setTitle(mTopicModel.getTopicName());
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        collapsingToolbarLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setPadding(0, collapsingToolbarLayout.getHeight(), 0, 0);
                
            }
        });

        topicDescription.setText(mTopicModel.getTopicDescription());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));

        mAdapter = new OpinionsListAdapter(getActivity(), new ArrayList<OpinionDataModel>());
        recyclerView.setAdapter(mAdapter);
        getOpinionsFromServer();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openContainerActivity(getActivity(), Constants.NEW_OPINION_FRAGMENT,
                        Pair.create(Constants.TOPIC_MODEL, (Serializable) mTopicModel));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOpinionsFromServer();
            }
        });

    }


    private void getOpinionsFromServer(){
        OpinionHelper.getHelper().getTopicSpecificOpinions(mTopicModel.getTopicId(),
                new OpinionHelper.OnOpinionsReceivedListener() {
            @Override
            public void onCompleted(ArrayList<OpinionDataModel> opinions) {
                mAdapter.clearAll();
                mAdapter.addOpinions(opinions);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onError(final String error) {
                Log.e("error Opinion",error);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtils.showToast(getContext(),error);
                    }
                });
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
