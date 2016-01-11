package com.theforum.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.dataModels.topic;
import com.theforum.data.helpers.LoadTopicHelper;
import com.theforum.utils.customViews.DividerItemDecorator;

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

    private TopicsListAdapter mAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        ArrayList<topic> mFeeds = new ArrayList<>();
     /*   for(int i=0;i<9;i++){
            mFeeds.add(new topic());
        }*/

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));

        mAdapter = new TopicsListAdapter(getActivity(), mFeeds);
        recyclerView.setAdapter(mAdapter);

        getTopicsFromServer();

    }

    private void getTopicsFromServer(){




            LoadTopicHelper.getHelper().getTopics(new LoadTopicHelper.OnTopicsReceiveListener() {
                @Override
                public void onCompleted(ArrayList<topic> topics) {
                    mAdapter.addTopics(topics);
                }

                @Override
                public void onError(String error) {
                    Log.e("TopicsFragment","onError");
                }
            });

    }

    //TODO: Also load topics from local database to inflate layout
    private void getTopicsFromDataBase(){

    }
}
