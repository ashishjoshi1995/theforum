package com.theforum.login;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author DEEPANKAR
 * @since 16-12-2015.
 */
public class LoginViewPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public LoginViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment =  new LoginFragmentOne();
                break;
            case 1:
                fragment =  new LoginFragmentTwo();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
