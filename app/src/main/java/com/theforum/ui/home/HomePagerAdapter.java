package com.theforum.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.theforum.R;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.ui.ProfileFragment;
import com.theforum.ui.topic.TopicsFragment;
import com.theforum.ui.trend.TrendsFragment;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "TRENDS", "TOPICS", "PROFILE" };

    private Context mContext;


    public HomePagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
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
        if(position==2 && NotificationDBHelper.getHelper().getNewNotificationCount()>0){

            Drawable image = mContext.getResources().getDrawable(R.drawable.profile_notification_indicator);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(tabTitles[2]+" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;

        }
        return tabTitles[position];
    }

}
