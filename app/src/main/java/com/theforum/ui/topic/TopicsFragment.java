package com.theforum.ui.topic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.enums.RequestStatus;
import com.theforum.utils.listeners.OnLoadMoreListener;
import com.theforum.utils.views.DividerItemDecorator;

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

                TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                        .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS));
            }
        });

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                        .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if(TopicHelper.getHelper().requestStatus == RequestStatus.IDLE){
            TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                    .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS));
        }
    }

    private void getTopics(){

            TopicHelper.getHelper().getTopics(new TopicHelper.OnTopicsReceiveListener() {
                @Override
                public void onCompleted(ArrayList<TopicDataModel> topics) {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.e("ui ui", "data received " + topics.size());

                    if(topics.size()== 1 && topics.get(0).isMyTopic()) {
                        mAdapter.addTopic(topics.get(0),0);

                    }else {

                        mAdapter.removeAllTopics();
                        mAdapter.addTopics(topics, true);
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
                    
                    CommonUtils.showToast(getContext(), "Check Your Internet Connection");

                }
            });

    }


}
