package com.theforum.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.TheForumApplication;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.views.DividerItemDecorator;
import com.theforum.utils.listeners.OnListItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.settings_layout_application_share_the_app) TextView shareTheApp;
    @Bind(R.id.settings_layout_legal_copyrights) TextView copyRights;
    @Bind(R.id.settings_layout_legal_privacy_policy) TextView privacyPolicy;
    @Bind(R.id.settings_layout_legal_terms_of_service) TextView termsOfService;
    @Bind(R.id.settings_layout_application_feedback) TextView applicationFeedback;
    @Bind(R.id.settings_layout_application_rate_us) TextView rateUs;
    @Bind(R.id.settings_layout_application_contact_us) TextView contactUs;
    @Bind(R.id.settings_layout_notification_opinions_received) CheckBox opinionsReceived;
    @Bind(R.id.settings_layout_notification_renewal_requests) CheckBox renewalRequest;
    @Bind(R.id.settings_layout_notification_topic_renewed) CheckBox topicRenewed;
    @Bind(R.id.settings_layout_notification_upvotes_received) CheckBox upvotesReceived;
    @Bind(R.id.temp) TextView temp;
    @Bind(R.id.settings_fragment_toggle_button) Switch turnOnLocation;
    @Bind(R.id.settings_layout_location) TextView location;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_layout_temp, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mToolbar.setTitle("Options");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        shareTheApp.setOnClickListener(this);
        copyRights.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        termsOfService.setOnClickListener(this);
        applicationFeedback.setOnClickListener(this);
        rateUs.setOnClickListener(this);
        contactUs.setOnClickListener(this);

        opinionsReceived.setOnCheckedChangeListener(this);
        renewalRequest.setOnCheckedChangeListener(this);
        topicRenewed.setOnCheckedChangeListener(this);
        upvotesReceived.setOnCheckedChangeListener(this);

        opinionsReceived.setChecked(true);
        renewalRequest.setChecked(true);
        topicRenewed.setChecked(true);
        upvotesReceived.setChecked(true);

        turnOnLocation.setOnCheckedChangeListener(this);
        turnOnLocation.setChecked(false);
    }

    @Override
    public void onClick(View v) {

            switch(v.getId()){
                case R.id.settings_layout_application_share_the_app:
                    CommonUtils.shareViaWatsapp(getActivity(),"Try this app theforum,\nregister as a tester on\nhttps://play.google.com/apps/testing/com.theforum " +
                            "\nThen download it from playstore link on the page that follows.\n" +
                            "For more details visit\nhttp://theforumapp.co/terms.html");
                    break;
                case R.id.settings_layout_application_rate_us:
                    CommonUtils.goToUrl(getActivity(),"https://play.google.com/store/apps/details?id=com.theforum");
                    break;
                case R.id.settings_layout_application_feedback:
                    CommonUtils.emailIntent(getActivity());
                    break;
                case R.id.settings_layout_application_contact_us:
                    CommonUtils.emailIntent(getActivity());
                    break;
                case R.id.settings_layout_legal_terms_of_service:
                    CommonUtils.goToUrl(getActivity(),"http://theforumapp.co/terms.html");
                    break;
                case R.id.settings_layout_legal_privacy_policy:
                    CommonUtils.goToUrl(getActivity(),"http://theforumapp.co/privacy.html");
                    break;
                case R.id.settings_layout_legal_copyrights:
                    //CommonUtils.showToast(getActivity());
                    break;
            }
        }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.settings_layout_notification_upvotes_received:
                break;
            case R.id.settings_layout_notification_renewal_requests:
                break;
            case R.id.settings_layout_notification_topic_renewed:
                break;
            case R.id.settings_layout_notification_opinions_received:
                break;
            case R.id.settings_fragment_toggle_button:
            if (isChecked) {
                String country;
                TelephonyManager teleMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                if (teleMgr != null) {
                    //String Country_code= getApplicationContext().getResources().getConfiguration().locale.getCountry();
                    country = teleMgr.getSimCountryIso();
                    Log.e("qqqqqqqqqqqqqqqqqqqqqq", "" + country);
                    //Log.e("qwwwwwwwwwwwwwwwwwwwww",getApplicationContext().getResources().getConfiguration().locale.getDisplayLanguage());
                    //store in preference

                    //update UI
                    location.setText("Location: "+country);
                }
                else{
                    //store in preference
                }
            }


            break;
        }
    }
}

