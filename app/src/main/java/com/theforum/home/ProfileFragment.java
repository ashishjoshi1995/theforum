package com.theforum.home;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.theforum.Constants;
import com.theforum.R;
import com.theforum.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class ProfileFragment extends Fragment {

    @Bind(R.id.profile_notification_btn) Button notifications;
    @Bind(R.id.profile_status) TextView status;
    @Bind(R.id.profile_points) TextView points;
    @Bind(R.id.profile_topics) TextView topics;

    @Bind(R.id.profile_status_icon) ImageView statusIcon;
    @Bind(R.id.profile_points_icon) ImageView pointsIcon;
    @Bind(R.id.profile_topics_icon) ImageView topicsIcon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        status.setText("Smart");
        points.setText("930");
        topics.setText("100");

        setBackgroundColor(statusIcon, "#313c44");
        setBackgroundColor(pointsIcon,"#d9ab1d");
        setBackgroundColor(topicsIcon,"#643173");

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openContainerActivity(getContext(), Constants.NOTIFICATION_FRAGMENT);
            }
        });
    }

    private void setBackgroundColor(ImageView imageView,String hexColor){
        imageView.getBackground().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP);
    }
}
