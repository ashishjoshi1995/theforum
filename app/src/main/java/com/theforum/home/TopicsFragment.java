package com.theforum.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.helpers.LoadTopicHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.customViews.DividerItemDecorator;
import com.theforum.utils.listeners.OnLoadMoreListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class TopicsFragment extends Fragment {

    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.topics_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TopicsListAdapter mAdapter;
    public  int times = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));

        mAdapter = new TopicsListAdapter(getActivity(), new ArrayList<TopicDataModel>());
        recyclerView.setAdapter(mAdapter);

        getTopics();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadTopicHelper.getHelper().loadTopics(times, Constants.SORT_BASIS_LATEST);
                times = 0;
                mAdapter.setAllTopicsLoaded(false);
            }
        });

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                LoadTopicHelper.getHelper().loadTopics(times, Constants.SORT_BASIS_LATEST);
            }
        });

    }

    private void getTopics(){

            LoadTopicHelper.getHelper().getTopics(new LoadTopicHelper.OnTopicsReceiveListener() {
                @Override
                public void onCompleted(ArrayList<TopicDataModel> topics) {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.e("ui ui", "data received " + topics.size());
                    if(topics.size()== 1 && topics.get(0).isMyTopic()) {
                        mAdapter.addTopic(topics.get(0),0);
                    }else {
                        if (times == 0) {
                            mAdapter.removeAllTopics();
                            mAdapter.addTopics(topics, true);
                        } else {
                            mAdapter.addTopics(topics, false);
                        }
                        if (topics.size() < 20) mAdapter.setAllTopicsLoaded(true);
                        times++;
                    }
                }

                @Override
                public void onError(String error) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

                    Log.e("TopicsFragment error", error);

                    if(error.equals("404")){
                        CommonUtils.showToast(getContext(), "Check Your Internet Connection");
                    }
                }
            });

    }


}
