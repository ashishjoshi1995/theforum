package com.theforum.login;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.theforum.R;
import com.theforum.login.LoginViewPagerAdapter;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.customViews.ViewPagerIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.login_view_pager)
    ViewPager mViewPager;

    @Bind(R.id.login_view_pager_indicator)
    ViewPagerIndicator mViewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mViewPagerIndicator.setTotalNoOfPages(2);
        mViewPagerIndicator.setDotSpacing((int) CommonUtils.convertDpToPixel(3, this));

        mViewPager.setAdapter(new LoginViewPagerAdapter(this, getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mViewPagerIndicator.setActiveDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    }
