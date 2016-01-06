package com.theforum;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.theforum.other.NewOpinionFragment;
import com.theforum.other.NewTopicFragment;
import com.theforum.other.OpinionsFragment;
import com.theforum.other.SettingsFragment;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (getIntent().getExtras().getInt("id")){

            case Constants.NEW_TOPIC_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, new NewTopicFragment());
                break;

            case Constants.OPINIONS_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, new OpinionsFragment());
                break;

            case Constants.NEW_OPINION_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, new NewOpinionFragment());
                break;

            case Constants.SETTINGS_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container,new SettingsFragment());
                break;
        }
        fragmentTransaction.commit();



    }
}
