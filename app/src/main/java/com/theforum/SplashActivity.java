package com.theforum;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.theforum.data.helpers.ProfileHelper;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.helpers.TrendsHelper;
import com.theforum.ui.home.HomeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_TIME_OUT = 3500;

    @Bind(R.id.frog_body) ImageView frogBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        TopicHelper.getHelper().loadTopics(0, Constants.SORT_BASIS_LATEST);
        TrendsHelper.getHelper().loadTrends();
        ProfileHelper.getHelper().viewProfile();

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

}
