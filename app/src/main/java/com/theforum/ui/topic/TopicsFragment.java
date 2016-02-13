package com.theforum.ui.topic;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.helpers.localHelpers.LocalTopicHelper;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.enums.RequestStatus;
import com.theforum.utils.listeners.OnListItemClickListener;
import com.theforum.utils.listeners.OnLongClickItemListener;
import com.theforum.utils.views.DividerItemDecorator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class TopicsFragment extends Fragment implements OnListItemClickListener,OnLongClickItemListener,CompoundButton.OnCheckedChangeListener{

    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.topics_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.new_topic_Iflocal_toggle_button)
    Switch topicsANDtrends_toggle_button;

    private TopicsListAdapter mAdapter;
    private ArrayList<TopicDataModel> mTopicsList;

    private int classification;
    private boolean dataReceived;
    private int mPosition;
    private boolean ifLocalToDisplay = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        classification = SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS);
        mTopicsList = new ArrayList<>();
        ifLocalToDisplay = false;
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));
        mAdapter = new TopicsListAdapter(getActivity(), mTopicsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnListItemClickListener(this);
        mAdapter.setOnLongClickItemListener(this);
        topicsANDtrends_toggle_button.setOnCheckedChangeListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!ifLocalToDisplay)
                TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                        .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS), true);
                else
                LocalTopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                        .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS),true);
            }
        });

        getTopics();

        TopicHelper.getHelper().setTopicInsertListener(new TopicHelper.OnTopicInsertListener() {
            @Override
            public void onCompleted(TopicDataModel topicDataModel, boolean isUpdated) {
                if(!isUpdated) mAdapter.addTopic(topicDataModel, 0);
            }

            @Override
            public void onError(String error) {}
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if(TopicHelper.getHelper().requestStatus == RequestStatus.IDLE && !dataReceived){
            if(!ifLocalToDisplay)
            TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                    .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS), false);
            else
            LocalTopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
            .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS),false);
        }

        if(classification!=SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS)){
            if(!ifLocalToDisplay)
            TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                    .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS), true);
            else
            LocalTopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                    .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS),true);
        }

        if(TopicHelper.getHelper().requestStatus == RequestStatus.EXECUTING){
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }

        if(mTopicsList.size()>0){
            String topicId = mTopicsList.get(mPosition).getTopicId();
            mTopicsList.remove(mPosition);
            mTopicsList.add(mPosition, TopicDBHelper.getHelper().getTopicById(topicId));
            mAdapter.notifyItemChanged(mPosition);
        }
    }


    private void getTopics(){
        Log.e("gettopics","gettopics");
        if(!ifLocalToDisplay){
            Log.e("false","false");
            TopicHelper.getHelper().getTopics(new TopicHelper.OnTopicsReceiveListener() {
            @Override
            public void onCompleted(ArrayList<TopicDataModel> topics) {
                dataReceived = true;
                mAdapter.removeAllTopics();
                mAdapter.addTopics(topics);
                Log.e("onCompleted global", "" + topics.size());
                Log.e("mujhko", "pehchan lo");
                classification = SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        CommonUtils.showToast(getActivity(), error);
                    }
                });
            }
        });
        }
        else {
            Log.e("false","fa;se");
            LocalTopicHelper.getHelper().getTopics(new LocalTopicHelper.OnTopicsReceiveListener() {
                @Override
                public void onCompleted(ArrayList<TopicDataModel> topics) {
                    dataReceived = true;
                    mAdapter.removeAllTopics();
                    mAdapter.addTopics(topics);
                    Log.e("onCompleted local", "" + topics.size());
                    Log.e("mujhko","pehchan lo mai hu don");
                    classification = SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onError(final String error) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            CommonUtils.showToast(getActivity(), error);
                        }
                    });
                }
            });
    }
    }


    @Override
    public void onItemClick(View v, int position) {
        mPosition = position;
        CommonUtils.openContainerActivity(getContext(), LayoutType.OPINIONS_FRAGMENT,
                Pair.create(LayoutType.TOPIC_MODEL,  (Parcelable) mTopicsList.get(position)));
    }

    @Override
    public boolean onLongClicked(int position) {
        mPosition = position;
        final TopicDataModel dataModel = mTopicsList.get(position);
        if (dataModel.isMyTopic()) {
            CommonUtils.openContainerActivity(getContext(), LayoutType.NEW_TOPIC_FRAGMENT,
                    Pair.create(LayoutType.TOPIC_MODEL, (Parcelable) dataModel));
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.new_topic_Iflocal_toggle_button){

            swipeRefreshLayout.setRefreshing(true);
            //onStart();
            if(isChecked){
                Log.e("ischecked","true");
                //change adapter to get local display topics
                ifLocalToDisplay = true;

                LocalTopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                        .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS),true);
                getTopics();
                Log.e("asasa",ifLocalToDisplay+"");
            }
            else {
                Log.e("ischecked","false");
                //rechange adapter to get global display topics
                ifLocalToDisplay = false;
                TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                        .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS), true);
                getTopics();
                Log.e("asasaasasa", ifLocalToDisplay + "");
            }
        }
    }
}
