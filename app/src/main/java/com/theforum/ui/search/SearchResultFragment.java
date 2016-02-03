package com.theforum.ui.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.ui.home.HomeActivity;
import com.theforum.utils.views.MaterialSearchView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchResultFragment extends Fragment {


    @Bind(R.id.home_recycler_view)
    RecyclerView recyclerView;

    ResultAdapter mAdapter;

    MaterialSearchView mMaterialSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mMaterialSearchView = ((HomeActivity)getActivity()).getSearchView();

        ArrayList<TopicDataModel>  topics = TopicDBHelper.getHelper().getAllTopics();

        mAdapter = new ResultAdapter(getContext(),topics);
        recyclerView.setAdapter(mAdapter);

        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public void onQueryTextSubmit(String query) {

            }

            @Override
            public void onQueryTextChange(CharSequence changedText) {
                // SearchResultFragment.this.adapter.getFilter().filter(changedText);
            }
        });

    }
/*
    private List<ExampleModel> filter(List<ExampleModel> models, String query) {
        query = query.toLowerCase();

        final List<ExampleModel> filteredModelList = new ArrayList<>();
        for (ExampleModel model : models) {
            final String text = model.getText().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }*/
}
