package com.theforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.theforum.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.home_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.home_sliding_tabs)
    TabLayout mTabLayout;

    @Bind(R.id.home_viewpager)
    ViewPager mViewPager;

    @Bind(R.id.home_fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:

                        Intent intent = new Intent(this, OptionsActivity.class);
                        startActivity(intent);

                break;
            case R.id.action_add_opinion:
                CommonUtils.openContainerActivity(this, Constants.NEW_TOPIC_FRAGMENT);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
