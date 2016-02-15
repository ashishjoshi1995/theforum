package com.theforum.ui.topic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.theforum.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */

public class TopicsFragment extends Fragment{

    @Bind(R.id.topic_toggle_button)
    Switch topicsToggleButton;

    private FragmentManager mFragmentManager;
    private TopicsGlobalListFragment mGlobalTopicsList;
    private TopicsLocalListFragment mLocalTopicsList;

    private boolean ifLocalToDisplay = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        topicsToggleButton.setChecked(false);
        mGlobalTopicsList = new TopicsGlobalListFragment();
        mLocalTopicsList = new TopicsLocalListFragment();

        mFragmentManager = getChildFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.topics_list_holder, mGlobalTopicsList)
                .commit();

        topicsToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                ifLocalToDisplay = b;

                if (ifLocalToDisplay) {
                    mFragmentManager.beginTransaction()
                            .replace(R.id.topics_list_holder, mLocalTopicsList)
                            .commit();
                } else {
                    mFragmentManager.beginTransaction()
                            .replace(R.id.topics_list_holder, mGlobalTopicsList)
                            .commit();
                }

            }

        });

    }





}
