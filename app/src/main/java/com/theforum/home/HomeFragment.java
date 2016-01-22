package com.theforum.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.theforum.Constants;
import com.theforum.HomePagerAdapter;
import com.theforum.R;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.listeners.OnHomeUiChangeListener;

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

    private int mPosition;
    private OnHomeUiChangeListener homeUiChangeListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeUiChangeListener = (OnHomeUiChangeListener) getActivity();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mViewPager.setAdapter(new HomePagerAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

       // mFab.hide();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openContainerActivity(getContext(), Constants.SORT_FRAGMENT);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                homeUiChangeListener.onPageSelected(position);

                if (position != 1) {
                    Animation animFadeOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
                    mFab.setAnimation(animFadeOut);
                    mFab.setVisibility(View.GONE);
                } else {
                    mFab.setVisibility(View.VISIBLE);
                    Animation animFadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                    mFab.setAnimation(animFadeIn);
                }

                mPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
