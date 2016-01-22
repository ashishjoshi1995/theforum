package com.theforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theforum.data.helpers.LoginHelper;
import com.theforum.data.server.user;
import com.theforum.ui.home.HomeActivity;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.fragment_login_age)
    EditText mAge;

    @Bind(R.id.fragment_login_button)
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mAge.getText().toString().equals("")) {

                    register(Integer.parseInt(mAge.getText().toString()));

                } else CommonUtils.showToast(LoginActivity.this, "Please enter your age. Don't Panic!!");
            }
        });


    }

    private void register(final int age) {
        user user = new user();
        user.setAge(age);

        LoginHelper loginHelper = new LoginHelper();
        loginHelper.login(user, new LoginHelper.OnLoginCompleteListener() {
            @Override
            public void onCompleted(user user) {
                CommonUtils.showToast(LoginActivity.this, "Successfully Registered");

                /*
                 *  updates the local database
                 */
                User localUser = User.getInstance();
                localUser.setId(user.getmUid());
                localUser.setServerId(user.getmId());
                localUser.setAge(age);
                localUser.setStatus("Rookie");
                localUser.setPointCollected(0);
                localUser.setTopicsCreated(0);
                Log.e("login", "" + user.getmUid() + "/" + user.getmId());

                /*
                 *  show home UI
                 */

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String error) {
                Log.e("error register", error);
            }
        });
    }

    }
