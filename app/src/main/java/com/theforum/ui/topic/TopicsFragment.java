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
import com.theforum.data.local.database.topicDB.TopicDB;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.enums.RequestStatus;
import com.theforum.utils.listeners.OnListItemClickListener;
import com.theforum.utils.listeners.OnLongClickItemListener;
import com.theforum.utils.locationTracker.GPSTracker;
import com.theforum.utils.views.DividerItemDecorator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class TopicsFragment extends Fragment implements OnListItemClickListener,OnLongClickItemListener{

    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.topics_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;


  //  @Bind(R.id.new_topic_Iflocal_toggle_button)
    private  Switch topicsANDtrends_toggle_button;

    private TopicsListAdapter mAdapter;
    private ArrayList<TopicDataModel> mTopicsList;

    //Location
    private double latitude=0.0;
    private double longitude =0.0;

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
        getLocation();

        topicsANDtrends_toggle_button=(Switch)view.findViewById(R.id.new_trend_Iflocal_toggle_button);
        topicsANDtrends_toggle_button.setChecked(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));
        mAdapter = new TopicsListAdapter(getActivity(), mTopicsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnListItemClickListener(this);
        mAdapter.setOnLongClickItemListener(this);
        topicsANDtrends_toggle_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.getId() == R.id.new_trend_Iflocal_toggle_button) {
                    ifLocalToDisplay = b;
                    if(ifLocalToDisplay){
                        mAdapter.removeAllTopics();
                        mAdapter.addTopics(TopicDBHelper.getHelper().getAllLocalTopics());
                    }
                    else {
                        mAdapter.removeAllTopics();
                        mAdapter.addTopics(TopicDBHelper.getHelper().getAllGlobalTopics());
                    }
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!ifLocalToDisplay)
                    TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                            .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS), true);
                else
                    LocalTopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                            .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS), latitude, longitude, true);
            }
        });

        getTopics();

        TopicHelper.getHelper().setTopicInsertListener(new TopicHelper.OnTopicInsertListener() {
            @Override
            public void onCompleted(TopicDataModel topicDataModel, boolean isUpdated) {
                if (topicDataModel.isLocalTopic() == ifLocalToDisplay) {
                    Log.e("" + topicDataModel.isLocalTopic(), "" + ifLocalToDisplay);
                    if (!isUpdated) mAdapter.addTopic(topicDataModel, 0);
                }
            }

            @Override
            public void onError(String error) {
            }
        });

        LocalTopicHelper.getHelper().setTopicInsertListener(new LocalTopicHelper.OnTopicInsertListener() {
            @Override
            public void onCompleted(TopicDataModel topicDataModel, boolean isUpdated) {
                if (topicDataModel.isLocalTopic() == ifLocalToDisplay) {
                    Log.e("" + topicDataModel.isLocalTopic(), "" + ifLocalToDisplay);
                    if (!isUpdated) mAdapter.addTopic(topicDataModel, 0);
                }
            }

            @Override
            public void onError(String error) {

            }
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
            .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS),latitude,longitude,false);
        }

        if(classification!=SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS)){
            if(!ifLocalToDisplay)
            TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                    .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS), true);
            else
            LocalTopicHelper.getHelper().loadTopics(SettingsUtils.getInstance()
                    .getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS),latitude,longitude,true);
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

    private void getTopics() {
        Log.e("gettopics", "gettopics");
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
            Log.e("false", "fa;se");
            LocalTopicHelper.getHelper().getTopics(new LocalTopicHelper.OnTopicsReceiveListener() {
                @Override
                public void onCompleted(ArrayList<TopicDataModel> topics) {
                    dataReceived = true;
                    mAdapter.removeAllTopics();
                    mAdapter.addTopics(topics);
                    Log.e("onCompleted local", "" + topics.size());
                    Log.e("mujhko", "pehchan lo mai hu don");
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
                Pair.create(LayoutType.TOPIC_MODEL, (Parcelable) mTopicsList.get(position)));
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

    private void getLocation(){
        GPSTracker gps;
        gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()||(gps.getLongitude()!=0.0&&gps.getLatitude()!=0.0)) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }
        else {
            gps.showSettingsAlert();
        }

    }

}
