package com.theforum.other.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.local.models.TrendsDataModel;
import com.theforum.utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchResultFragment extends Fragment {

    //@Bind(R.id.home_recycler_view)
    //RecyclerView recyclerView;
    // List view
    @Bind(R.id.list_view)
    ListView lv;

    // Listview Adapter

    ArrayAdapter<String> adapter;

    // Search EditText
    @Bind(R.id.inputSearch)
    EditText inputSearch;


    ArrayList<HashMap<String, String>> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_search_result, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        // Listview Data

        List<String>  topic= TopicDBHelper.getHelper().getMyTopicText();
        Log.e("string ",topic.toString());
        String products[] = new String[topic.size()];
        for (int i = 0; i < topic.size(); i++) {
           products[i]= topic.get(i);
        }




        // Adding items to listview
        adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, R.id.topic_name, products);
        lv.setAdapter(adapter);


        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setAdapter(new com.theforum.other.search.ResultAdapter(getContext(), TopicDBHelper.getHelper().getAllTopics()));
        //lv = (ListView) findViewById(R.id.list_view);
        //inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                SearchResultFragment.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        // Item Click Listener for the listview
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
               Log.e("item", lv.getItemAtPosition(position).toString());
               // TrendsDataModel trends = mFeeds.get(getLayoutPosition());
                //getting the topic data from server
                TrendsHelper.getHelper().getTopicByName(lv.getItemAtPosition(position).toString(), new TrendsHelper.OnTopicDetailReceived() {
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
