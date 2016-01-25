package com.theforum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.theforum.data.helpers.LoginHelper;
import com.theforum.data.server.user;
import com.theforum.ui.home.HomeActivity;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.login_age)
    EditText mAge;

    @Bind(R.id.login_button)
    Button mLogin;

    @Bind(R.id.frog_body)
    ImageView frogBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        ((GradientDrawable)frogBody.getBackground()).setColor(Color.parseColor("#30ed17"));

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mAge.getText().toString().equals("")) {

                    register(Integer.parseInt(mAge.getText().toString()));
                    SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.TOPIC_FEED_SORT_STATUS,
                            Constants.SORT_BASIS_MOST_RENEWAL);

                } else CommonUtils.showToast(LoginActivity.this, "Please enter your age. Don't Panic!!");
            }
        });


    }

    private void register(final int age) {
        user user = new user();
        user.setAge(age);

        LoginHelper loginHelper = new LoginHelper();
        final ProgressDialog pd = new ProgressDialog(this, R.style.MyDialog);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        pd.show();
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
                pd.dismiss();
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
