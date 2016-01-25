package com.theforum.ui.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.ui.home.HomeActivity;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.views.MaterialSearchView;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchResultFragment extends Fragment {


    @Bind(R.id.list_view)
    ListView lv;

    ArrayAdapter<String> adapter;

    MaterialSearchView mMaterialSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_search_result, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mMaterialSearchView = ((HomeActivity)getActivity()).getSearchView();

        List<String>  topic= TopicDBHelper.getHelper().getMyTopicText();

        String products[] = new String[topic.size()];
        for (int i = 0; i < topic.size(); i++) {
           products[i]= topic.get(i);
        }


        // Adding items to listview
        adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, R.id.topic_name, products);
        lv.setAdapter(adapter);

        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public void onQueryTextSubmit(String query) {

            }

            @Override
            public void onQueryTextChange(CharSequence changedText) {
                SearchResultFragment.this.adapter.getFilter().filter(changedText);
            }
        });


        // Item Click Listener for the listview
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
               Log.e("item", lv.getItemAtPosition(position).toString());
               // TrendsDataModel trends = mFeeds.get(getLayoutPosition());
                //getting the topic data from server
                TrendsHelper.getHelper().getTopicByName(lv.getItemAtPosition(position).toString(),
                        new TrendsHelper.OnTopicDetailReceived() {
                    @Override
                    public void onCompleted(TopicDataModel topic) {

                        CommonUtils.openContainerActivity(getContext(), Constants.OPINIONS_FRAGMENT,
                                Pair.create(Constants.TOPIC_MODEL, (Serializable) topic));
                    }

                    @Override
                    public void onError(final String error) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommonUtils.showToast(getContext(), error);
                            }
                        });

                    }
                });

            }
        };

        // Setting the item click listener for the listview
        lv.setOnItemClickListener(itemClickListener);


    }
}
