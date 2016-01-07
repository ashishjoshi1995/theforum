package com.theforum;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.theforum.home.HomeFragment;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.TypefaceSpan;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.home_toolbar)
    Toolbar mToolbar;

    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SpannableString spannableString = new SpannableString("theforum");
        spannableString.setSpan(new TypefaceSpan(this, "Roboto-Light.ttf"), 0, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TypefaceSpan(this, "Roboto-Medium.ttf"), 4, 8,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mToolbar.setTitle(spannableString);

        mFragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment_container, new HomeFragment());
        mFragmentTransaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
/*
        int minutes = 100;

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        am.cancel(pi);
        // by my own convention, minutes <= 0 means notifications are disabled
        if (minutes > 0) {
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + minutes * 60 * 1000,
                    minutes * 60 * 1000, pi);

        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                CommonUtils.openContainerActivity(this,Constants.SETTINGS_FRAGMENT);
                break;
            case R.id.action_add_opinion:
                CommonUtils.openContainerActivity(this, Constants.NEW_TOPIC_FRAGMENT);
                break;
            case R.id.action_search:
                //onSearchRequested();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {

       Bundle appData = new Bundle();
        Log.e("asasas","asasas");
        appData.putString("hello", "world");
        startSearch(null, false, appData, false);
        return true;
    }

}
