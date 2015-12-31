package com.theforum;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_container);

     /*   FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (getIntent().getExtras().getInt("id")){



        }
        fragmentTransaction.commit();

*/

    }
}
