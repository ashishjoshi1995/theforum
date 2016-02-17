package com.theforum.ui.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.ui.topic.TopicsFragment;
import com.theforum.utils.CommonUtils;

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

        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getContext(), getChildFragmentManager());
        mViewPager.setAdapter(homePagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

        mFab.hide();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openContainerActivity(getContext(), LayoutType.SORT_FRAGMENT);
            }
        });

        final TopicsFragment topicsFragment = (TopicsFragment)homePagerAdapter.getItem(2);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position != 1) {
                    mFab.hide();
                } else if(!topicsFragment.ifLocalToDisplay){
                    mFab.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    public TabLayout.Tab getTab(int position){
        return mTabLayout.getTabAt(position);
    }

    public FloatingActionButton getFab(){
        return mFab;
    }

}
