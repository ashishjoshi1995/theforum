package com.theforum.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.data.server.opinion;
import com.theforum.data.server.topic;
import com.theforum.data.helpers.OpinionHelper;
import com.theforum.utils.customViews.DividerItemDecorator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class TrendsFragment extends Fragment {

    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;

    private TrendsListAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        List<opinion> mFeeds = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));

        mAdapter = new TrendsListAdapter(getActivity(), mFeeds);
        recyclerView.setAdapter(mAdapter);

        getDataFromServer();
    }

    private void getDataFromServer(){
        OpinionHelper.getHelper().getTrendingOpinions(new OpinionHelper.OnTrendingReceiveListener() {
            @Override
            public void onCompleted(ArrayList<topic> topics, ArrayList<opinion> opinions) {
                if(opinions!=null){
                    mAdapter.addAllTrends(opinions);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("trends error",error);
            }
        });
    }
}
