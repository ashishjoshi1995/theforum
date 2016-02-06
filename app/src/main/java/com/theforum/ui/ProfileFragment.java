package com.theforum.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.data.helpers.ProfileHelper;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.notification.NotificationActivity;
import com.theforum.ui.home.HomeFragment;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class ProfileFragment extends Fragment {

    @Bind(R.id.profile_notification_btn) TextView notifications;
    @Bind(R.id.profile_status) TextView status;
    @Bind(R.id.profile_points) TextView points;
    @Bind(R.id.profile_topics) TextView topics;

    @Bind(R.id.profile_status_icon) ImageView statusIcon;
    @Bind(R.id.profile_points_icon) ImageView pointsIcon;
    @Bind(R.id.profile_topics_icon) ImageView topicsIcon;

    @Bind(R.id.frog_body) ImageView frogBody;

    private TabLayout.Tab mProfileTab;
    private User mUser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUser = User.getInstance();
        mProfileTab = ((HomeFragment)getParentFragment()).getTab(2);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        status.setText(mUser.getStatus());
        points.setText("$ "+mUser.getPointCollected());
        topics.setText(String.valueOf(mUser.getTopicsCreated()));

        ProfileHelper.getHelper().getProfile(new ProfileHelper.OnProfileLoadListener() {
            @Override
            public void onCompleted() {
                status.setText(mUser.getStatus());
                points.setText("$ " + mUser.getPointCollected());
                topics.setText(String.valueOf(mUser.getTopicsCreated()));
            }

            @Override
            public void onError(String error) {}
        });

        setBackgroundColor(statusIcon, "#313c44");
        setBackgroundColor(pointsIcon,"#d9ab1d");
        setBackgroundColor(topicsIcon, "#643173");

        ((GradientDrawable)frogBody.getBackground()).setColor(Color.parseColor("#ff2222"));
        frogBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openContainerActivity(getActivity(), LayoutType.STATS_FRAGMENT);
            }
        });

        setNotifications();

    }

    private void setNotifications(){
        notifications.getBackground().setColorFilter(Color.parseColor("#d0d4d9"), PorterDuff.Mode.SRC_ATOP);

        int count = NotificationDBHelper.getHelper().getNewNotificationCount();
        notifications.setText(getContext().getResources().getQuantityString(R.plurals.profile_notifications,
                    count+1,count));

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NotificationDBHelper.getHelper().checkIfNotificationExist()) {
                    startActivity(new Intent(getActivity(), NotificationActivity.class));
                    notifications.setText(getContext().getResources().getQuantityString(R.plurals.profile_notifications,
                            1, 0));
                    mProfileTab.setText("PROFILE");
                } else {
                    CommonUtils.showToast(getContext(), "You do not have any Notifications");
                }

            }
        });
    }

    private void setBackgroundColor(ImageView imageView,String hexColor){
        imageView.getBackground().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP);
    }
}
