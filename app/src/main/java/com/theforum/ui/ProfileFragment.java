package com.theforum.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theforum.constants.LayoutType;
import com.theforum.R;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        status.setText(User.getInstance().getStatus());
        points.setText(""+User.getInstance().getPointCollected());
        topics.setText("$ "+User.getInstance().getTopicsCreated());

        setBackgroundColor(statusIcon, "#313c44");
        setBackgroundColor(pointsIcon,"#d9ab1d");
        setBackgroundColor(topicsIcon,"#643173");


        ((GradientDrawable)frogBody.getBackground()).setColor(Color.parseColor("#a56c1f"));
        notifications.getBackground().setColorFilter(Color.parseColor("#d0d4d9"), PorterDuff.Mode.SRC_ATOP);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationDBHelper.getHelper().openDatabase();
                if(NotificationDBHelper.getHelper().checkIfNotificationExist())
                CommonUtils.openContainerActivity(getContext(), LayoutType.NOTIFICATION_FRAGMENT);
                else {
                    CommonUtils.showToast(getContext(),"No new Notification");
                }

                NotificationDBHelper.getHelper().closeDataBase();
            }
        });
    }

    private void setBackgroundColor(ImageView imageView,String hexColor){
        imageView.getBackground().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP);
    }
}
