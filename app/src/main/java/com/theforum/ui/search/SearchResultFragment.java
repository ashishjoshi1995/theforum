package com.theforum.ui.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.ui.home.HomeActivity;
import com.theforum.utils.views.DividerItemDecorator;
import com.theforum.utils.views.MaterialSearchView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchResultFragment extends Fragment {


    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;

    ResultAdapter mAdapter;

    MaterialSearchView mMaterialSearchView;

    private ArrayList<TopicDataModel>  mAllTopics;
    private ArrayList<TopicDataModel>  mFilteredList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));

        mAllTopics = TopicDBHelper.getHelper().getAllTopics();
        mFilteredList = new ArrayList<>();

        mAdapter = new ResultAdapter(getContext(),mAllTopics);
        recyclerView.setAdapter(mAdapter);

        mMaterialSearchView = ((HomeActivity)getActivity()).getSearchView();
        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public void onQueryTextSubmit(String query) {

            }

            @Override
            public void onQueryTextChange(CharSequence changedText) {

                mAdapter.animateTo(filter(changedText.toString(),mFilteredList));
                recyclerView.scrollToPosition(0);
            }
        });

    }

    /**
     *
     * @param query changed text to search in list
     * @param filteredList empty list to return filtered objects
     *
     * @return filteredList
     */
    private ArrayList<TopicDataModel> filter(String query, final ArrayList<TopicDataModel> filteredList) {
        query = query.toLowerCase();

        filteredList.clear();
        for (TopicDataModel model : mAllTopics) {
            final String text = model.getTopicName().toLowerCase();
            if (text.contains(query)) {
                filteredList.add(model);
            }
        }
        return filteredList;
    }
}
