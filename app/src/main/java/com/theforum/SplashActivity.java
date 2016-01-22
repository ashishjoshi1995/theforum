package com.theforum;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.helpers.TrendsHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_TIME_OUT = 2000;

    @Bind(R.id.splash_frog_face) ImageView frogBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        TopicHelper.getHelper().loadTopics(0, Constants.SORT_BASIS_LATEST);
        TrendsHelper.getHelper().loadTrends();

        frogBody.getBackground().setColorFilter(Color.parseColor("#30ed17"), PorterDuff.Mode.SRC_ATOP);


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
