package com.theforum.search;

import android.app.SearchManager;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.theforum.R;
import com.theforum.data.dataModels.topic;
import com.theforum.data.helpers.SearchHelper;

import java.util.ArrayList;

public class HandleSearchResult extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<topic> topics = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_search_result);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ResultAdapter(topics);
        mRecyclerView.setAdapter(mAdapter);
        Log.e("HandleSearchResult","oncreate");
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            String rec = appData.getString("hello");
            doMySearch(rec);
        }
        /*if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
            Log.e("intent","intent");
        }*/
    }
    void doMySearch(String query){
        SearchHelper helper = new SearchHelper();
       topics =  helper.Search(query);
    }

}
