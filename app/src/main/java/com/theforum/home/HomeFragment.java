package com.theforum.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.HomePagerAdapter;
import com.theforum.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 06-01-2016.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.home_sliding_tabs)
    TabLayout mTabLayout;

    @Bind(R.id.home_viewpager)
    ViewPager mViewPager;

    @Bind(R.id.home_fab)
    FloatingActionButton mFab;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mViewPager.setAdapter(new HomePagerAdapter(getChildFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
