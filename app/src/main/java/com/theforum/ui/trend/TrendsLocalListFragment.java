package com.theforum.ui.trend;

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
import com.theforum.data.helpers.localHelpers.LocalTrendsHelper;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.enums.RequestStatus;
import com.theforum.utils.locationTracker.GPSTracker;
import com.theforum.utils.views.DividerItemDecorator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 16-02-2016.
 */
public class TrendsLocalListFragment extends Fragment{

    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.topics_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TrendsListAdapter mAdapter;
    private boolean dataReceived;
    //Location
    private double latitude= 0.0;
    private double longitude = 0.0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topics_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getLocation();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));

        mAdapter = new TrendsListAdapter(getActivity(), new ArrayList<TrendsDataModel>());
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LocalTrendsHelper.getHelper().loadTrends(true,latitude,longitude);
            }
        });

        getData();

    }

    @Override
    public void onStart() {
        super.onStart();

        if(LocalTrendsHelper.getHelper().requestStatus == RequestStatus.IDLE && !dataReceived){
            LocalTrendsHelper.getHelper().loadTrends(false,latitude,longitude);
        }

        if(LocalTrendsHelper.getHelper().requestStatus == RequestStatus.EXECUTING ){
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    private void getData(){
        getLocation();

        LocalTrendsHelper.getHelper().getTrends(new LocalTrendsHelper.OnTrendsReceivedListener() {
            @Override
            public void onCompleted(ArrayList<TrendsDataModel> trends) {
                dataReceived = true;
                mAdapter.clearList();
                mAdapter.addAllTrends(trends);
                swipeRefreshLayout.setRefreshing(false);
                Log.e("localHelper", "onCompleted");
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
