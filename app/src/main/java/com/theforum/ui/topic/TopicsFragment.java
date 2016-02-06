package com.theforum.ui.topic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.constants.Messages;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.enums.RequestStatus;
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
    private int classification;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        classification = SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS);

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

    }

    @Override
    public void onStart() {
        super.onStart();

        if(TopicHelper.getHelper().requestStatus == RequestStatus.IDLE){
            TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                    .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(classification!=SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS))
            TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                    .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS));

        if(TrendsHelper.getHelper().requestStatus == RequestStatus.EXECUTING){
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    private void getTopics(){

        TopicHelper.getHelper().getTopics(new TopicHelper.OnTopicsReceiveListener() {
            @Override
            public void onCompleted(ArrayList<TopicDataModel> topics) {
                swipeRefreshLayout.setRefreshing(false);

                if (topics.size() == 1 && topics.get(0).isMyTopic()) {
                    mAdapter.addTopic(topics.get(0), 0);
                } else {
                    mAdapter.removeAllTopics();
                    mAdapter.addTopics(topics);
                }
            }

            @Override
            public void onError(String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        CommonUtils.showToast(getContext(), Messages.NO_NET_CONNECTION);
                    }
                });

               // Log.e("TopicsFragment error", error);

            }
        });

    }


}
