package com.theforum.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.theforum.R;
import com.theforum.data.helpers.ProfileHelper;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.data.helpers.localHelpers.LocalTopicHelper;
import com.theforum.ui.home.HomeActivity;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.locationTracker.GPSTracker;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_TIME_OUT = 3500;
    private double latitude=0.0;
    private double longitude =0.0;

    @Bind(R.id.frog_body) ImageView frogBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        ((GradientDrawable) frogBody.getBackground()).setColor(Color.parseColor("#30ed17"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getLocation();
        TopicHelper.getHelper().loadTopics(SettingsUtils.getInstance().getIntFromPreferences(
                SettingsUtils.TOPIC_FEED_SORT_STATUS), false);
        LocalTopicHelper.getHelper().loadTopics(latitude,longitude, false);
        TrendsHelper.getHelper().loadTrends(false);
        ProfileHelper.getHelper().loadProfile();

    }

    private void getLocation(){

        GPSTracker gps;

        gps = new GPSTracker(this);

        if(gps.canGetLocation()||(gps.getLongitude()!=0.0&&gps.getLatitude()!=0.0)) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if(latitude==0.0 && longitude == 0.0){
              //  gps.showSettingsAlert();
            }
            gps.stopUsingGPS();
        } else {
            gps.showSettingsAlert();
        }


    }
}
