package com.theforum.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.ui.opinion.NewOpinionFragment;
import com.theforum.ui.opinion.OpinionsFragment;
import com.theforum.ui.topic.NewTopicFragment;
import com.theforum.ui.settings.SettingsFragment;

public class ContainerActivity extends AppCompatActivity {

    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        id = getIntent().getExtras().getInt("id");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (id){

            case LayoutType.NEW_TOPIC_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, new NewTopicFragment());
                break;

            case LayoutType.OPINIONS_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, Fragment.instantiate(this,
                        OpinionsFragment.class.getName(), getIntent().getExtras()));
                break;

            case LayoutType.NEW_OPINION_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, Fragment.instantiate(this,
                        NewOpinionFragment.class.getName(), getIntent().getExtras()));
                break;

            case LayoutType.SETTINGS_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container,new SettingsFragment());
                break;

            case LayoutType.SORT_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container,new SortFragment());
                break;

            case LayoutType.STATS_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container,new StatisticsFragment());
                break;
        }

        fragmentTransaction.commit();

    }



}
