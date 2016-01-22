package com.theforum.other.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.data.local.database.topicDB.TopicDBHelper;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchResultFragment extends Fragment {

    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ResultAdapter(getContext(),TopicDBHelper.getHelper().getAllTopics()));
    }
}
