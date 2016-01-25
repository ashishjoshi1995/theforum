package com.theforum;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.theforum.notification.NotificationFragment;
import com.theforum.ui.opinion.NewOpinionFragment;
import com.theforum.ui.opinion.OpinionsFragment;
import com.theforum.ui.topic.NewTopicFragment;
import com.theforum.ui.settings.SettingsFragment;
import com.theforum.ui.SortFragment;

public class ContainerActivity extends AppCompatActivity {

    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        id = getIntent().getExtras().getInt("id");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (id){

            case Constants.NEW_TOPIC_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, new NewTopicFragment());
                break;

            case Constants.OPINIONS_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, Fragment.instantiate(this,
                        OpinionsFragment.class.getName(), getIntent().getExtras()));
                break;

            case Constants.NEW_OPINION_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, Fragment.instantiate(this,
                        NewOpinionFragment.class.getName(), getIntent().getExtras()));
                break;

            case Constants.SETTINGS_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container,new SettingsFragment());
                break;

            case Constants.NOTIFICATION_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container,new NotificationFragment());
                break;

            case Constants.SORT_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container,new SortFragment());
                break;
        }

        fragmentTransaction.commit();

    }



}
