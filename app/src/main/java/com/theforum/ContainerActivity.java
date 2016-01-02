package com.theforum;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.theforum.other.NewTopicFragment;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_container);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (getIntent().getExtras().getInt("id")){

            case Constants.NEW_TOPIC_FRAGMENT:
                fragmentTransaction.replace(R.id.menu_fragment_container, new NewTopicFragment());
                break;

        }
        fragmentTransaction.commit();



    }
}
