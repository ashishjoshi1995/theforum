package com.theforum.ui.trend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.enums.RequestStatus;
import com.theforum.utils.views.DividerItemDecorator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class TrendsFragment extends Fragment {

    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.topics_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TrendsListAdapter mAdapter;
    private boolean dataReceived;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));

        mAdapter = new TrendsListAdapter(getActivity(), new ArrayList<TrendsDataModel>());
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TrendsHelper.getHelper().loadTrends(true);
            }
        });

        getData();

    }

    @Override
    public void onStart() {
        super.onStart();

        if(TrendsHelper.getHelper().requestStatus == RequestStatus.IDLE && !dataReceived){
            TrendsHelper.getHelper().loadTrends(false);
        }

        if(TrendsHelper.getHelper().requestStatus == RequestStatus.EXECUTING ){
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }


    private void getData(){

        TrendsHelper.getHelper().getTrends(new TrendsHelper.OnTrendsReceivedListener() {

            @Override
            public void onCompleted(ArrayList<TrendsDataModel> trends) {
                dataReceived = true;
                mAdapter.clearList();
                mAdapter.addAllTrends(trends);
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
