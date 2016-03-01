package com.theforum.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theforum.R;

import com.theforum.TheForumApplication;

import com.theforum.constants.Messages;

import com.theforum.data.helpers.LoginHelper;
import com.theforum.data.server.user;
import com.theforum.notification.NotificationService;
import com.theforum.ui.ProgressDialog;
import com.theforum.ui.home.HomeActivity;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.NetworkUtils;
import com.theforum.utils.ProfileUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.User;
import com.theforum.utils.enums.CountryCodesIso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Tracker mTracker;


    @Bind(R.id.login_age)
    EditText mAge;

    @Bind(R.id.login_button)
    Button mLogin;

    @Bind(R.id.frog_body)
    ImageView frogBody;

    @Bind(R.id.login_privacy_policy)
    TextView privacyPolicy;

    @Bind(R.id.login_terms)
    TextView termsOfService;

    @Bind(R.id.login_contact_us)
    TextView contactUs;

    @Bind(R.id.login_country)
    TextView loginCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        ((GradientDrawable) frogBody.getBackground()).setColor(Color.parseColor("#30ed17"));

        mLogin.setOnClickListener(this);
        termsOfService.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        String j =getCountry();
        String s = "Location: "+j +" You can always change this in Options";
        loginCountry.setText(s);
        setNotificationSettings();
        //NotificationDBHelper.getHelper().openDatabase();

        ProfileUtils.getInstance().savePreferences(ProfileUtils.COUNTRY, j);
        TheForumApplication application = (TheForumApplication) getApplication();
        mTracker = application.getDefaultTracker();

    }


    @Override
    public void onClick(View v) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Login")
                .build());
        switch (v.getId()) {

            case R.id.login_contact_us:
                NetworkUtils.emailIntent(this);
                break;

            case R.id.login_terms:
                NetworkUtils.goToUrl(this, "http://theforumapp.co/terms.html");
                break;

            case R.id.login_privacy_policy:
                NetworkUtils.goToUrl(this, "http://theforumapp.co/privacy.html");
                break;

            case R.id.login_button:

                if (!mAge.getText().toString().equals("")) {

                    int age = Integer.parseInt(mAge.getText().toString());
                    if (age > 12 && age < 123) {
                        register(age);

                    } else if (age <= 12) {
                        CommonUtils.showToast(LoginActivity.this, "Sorry!! The minimum age for login is 13");
                    } else {
                        CommonUtils.showToast(LoginActivity.this, "Please enter a valid age");
                    }

                } else
                    CommonUtils.showToast(LoginActivity.this, "Please enter your age. Don't Panic!!");

                break;
        }
    }


    private void setNotificationSettings() {
        SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION, true);
        SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_RENEWAL_REQUESTS_NOTIFICATION, true);
        SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_TOPIC_RENEWED_NOTIFICATION, true);
        SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_UPVOTES_RECIEVED_NOTIFICATION, true);
        SettingsUtils.getInstance().saveIntegerarPreference(SettingsUtils.INACTIVITY_KILLER_NOTIFICATION,0);
    }

    private void register(final int age) {
        user user = new user();
        user.setAge(age);

        LoginHelper loginHelper = new LoginHelper();

        final ProgressDialog progressDialog = ProgressDialog.createDialog(this);
        progressDialog.show();

        loginHelper.login(user, new LoginHelper.OnLoginCompleteListener() {
            @Override
            public void onCompleted(user user) {
                CommonUtils.showToast(LoginActivity.this, "Successfully Registered");

                /**
                 *  updates the local database with the user details
                 */
                User localUser = User.getInstance();
                localUser.setId(user.getmUid());
                localUser.setServerId(user.getmId());
                localUser.setAge(age);
                localUser.setStatus("Rookie");
                localUser.setPointCollected(0);
                localUser.setTopicsCreated(0);

                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecDownvoted, user.getDownvotes_received());
                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecOpinions, user.getOpinions_received());
                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecRenewals, user.getRenewal_request_received());
                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecTopicsRenewed, user.getToatal_topic_renewed());
                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mRecUpvotes, user.getUpvotesReceived());

                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcDownvotes, user.getDownvotes_croaked());
                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcOpinions, user.getmOpinionCount());
                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcUpvotes, user.getUpvotes_croaked());
                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcRenewals, user.getRenewal_request_croaked());
                ProfileUtils.getInstance().saveIntegralPreference(ProfileUtils.mCrcTopicsRenewed, user.getToatal_topic_renewed());


                progressDialog.dismiss();

                /**
                 * starting notification service
                 */
                int minutes = 39;
                AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0,
                        new Intent(getApplicationContext(), NotificationService.class), 0);
                am.cancel(pi);

                am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + minutes * 60 * 1000,
                        minutes * 60 * 1000, pi);

                /**
                 *  show home UI
                 */
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtils.showToast(LoginActivity.this, Messages.NO_NET_CONNECTION);
                    }
                });
            }
        });
    }

    private String getCountry() {
        try {
            //Log.e("yuyyyyyyyyyy", "teri maa ka bhosda");
            String country;
            TelephonyManager teleMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (teleMgr != null) {

                country = teleMgr.getNetworkCountryIso();
                // Log.e("asasas",country);
                String sentence = "";
                for (int i = 0; i < country.length(); i++) {
                    if (Character.isUpperCase(country.charAt(i)) == true) {
                        char ch2 = (char) (country.charAt(i) + 32);
                        sentence = sentence + ch2;
                    } else if (Character.isLowerCase(country.charAt(i)) == true) {
                        char ch2 = (char) (country.charAt(i) - 32);
                        sentence = sentence + ch2;
                    } else
                        sentence = sentence + country.charAt(i);

                }
                //  CountryCodesIso iso = new CountryCodesIso(sentence);
                //Log.e("jjjjjjj", sentence);
              //  Log.e("ffffffffff", "" + CountryCodesIso.valueOf(sentence).getCountryyName(sentence).toString());

                return CountryCodesIso.valueOf(sentence).getCountryyName(sentence).toString();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "India";
        }
}
