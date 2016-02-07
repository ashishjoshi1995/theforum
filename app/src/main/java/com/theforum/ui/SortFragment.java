package com.theforum.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.theforum.R;
import com.theforum.constants.SortType;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.local.database.topicDB.TopicDB;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.utils.SettingsUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Ashish
 * @since 1/8/2016
 */

public class SortFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.sort_radio_group)
    RadioGroup radioGroup;

    @Bind(R.id.sort_done_btn)
    Button done;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        switch(SettingsUtils.getInstance().getIntFromPreferences(SettingsUtils.TOPIC_FEED_SORT_STATUS)){

            case SortType.SORT_BASIS_CREATED_BY_ME:
                radioGroup.check(R.id.sort_created_by_me_btn);
                break;
            case SortType.SORT_BASIS_LATEST:
                radioGroup.check(R.id.sort_latest_btn);
                break;
            case SortType.SORT_BASIS_MOST_POPULAR:
                radioGroup.check(R.id.sort_relevance_btn);
                break;
            case SortType.SORT_BASIS_LEAST_RENEWAL:
                radioGroup.check(R.id.sort_least_renewal_btn);
                break;
            case SortType.SORT_BASIS_MOST_RENEWAL:
                radioGroup.check(R.id.sort_most_renewal_btn);
                break;
        }

        radioGroup.setOnCheckedChangeListener(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.sort_relevance_btn:
                SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.TOPIC_FEED_SORT_STATUS,
                        SortType.SORT_BASIS_MOST_POPULAR);
                break;
            case R.id.sort_latest_btn:
                SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.TOPIC_FEED_SORT_STATUS,
                        SortType.SORT_BASIS_LATEST);
                break;
            case R.id.sort_created_by_me_btn:
                SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.TOPIC_FEED_SORT_STATUS,
                        SortType.SORT_BASIS_CREATED_BY_ME);
                break;
            case R.id.sort_most_renewal_btn:
                SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.TOPIC_FEED_SORT_STATUS,
                        SortType.SORT_BASIS_MOST_RENEWAL);
                break;
            case R.id.sort_least_renewal_btn:
                SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.TOPIC_FEED_SORT_STATUS,
                        SortType.SORT_BASIS_LEAST_RENEWAL);
                break;

        }
    }
}

