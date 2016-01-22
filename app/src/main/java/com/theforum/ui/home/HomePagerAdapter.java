package com.theforum.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.theforum.ui.ProfileFragment;
import com.theforum.ui.topic.TopicsFragment;
import com.theforum.ui.trend.TrendsFragment;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Trends", "Topics", "Profile" };

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Fragment fragment = null;
        switch (index){
            case 0:
                fragment =  new TrendsFragment();
                break;
            case 1:
                fragment =  new TopicsFragment();
                break;
            case 2:
                fragment =  new ProfileFragment();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
