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
    private Fragment mGlobalTopicsList;
    private Fragment mLocalTopicsList;

    private boolean ifLocalToDisplay = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        topicsToggleButton.setChecked(false);
        mGlobalTopicsList = Fragment.instantiate(getContext(),TopicsGlobalListFragment.class.getName());
        mLocalTopicsList = Fragment.instantiate(getContext(), TopicsLocalListFragment.class.getName());

        mFragmentManager = getChildFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.topics_list_holder, mLocalTopicsList)
                .add(R.id.topics_list_holder, mGlobalTopicsList)
                .hide(mLocalTopicsList)
                .commit();

        topicsToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                ifLocalToDisplay = b;

                if (ifLocalToDisplay) {
                    mFragmentManager.beginTransaction().hide(mGlobalTopicsList).show(mLocalTopicsList).commit();
                } else {
                    mFragmentManager.beginTransaction().hide(mLocalTopicsList).show(mGlobalTopicsList).commit();
                }
            }

        });

    }





}
