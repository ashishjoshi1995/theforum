package com.theforum.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.CountryCodesIso;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.ProfileUtils;
import com.theforum.utils.SettingsUtils;
import com.theforum.utils.NetworkUtils;
import com.theforum.utils.TypefaceSpan;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsFragment extends Fragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Bind(R.id.settings_location) TextView location;
    @Bind(R.id.settings_location_toggle_button) Switch turnOnLocation;

    @Bind(R.id.settings_notification_opinions) CheckBox opinionsReceived;
    @Bind(R.id.settings_notification_renewal_requests) CheckBox renewalRequest;
    @Bind(R.id.settings_notification_topic_renewed) CheckBox topicRenewed;
    @Bind(R.id.settings_notification_up_votes) CheckBox upVotesReceived;

    @Bind(R.id.settings_application_feedback) TextView applicationFeedback;
    @Bind(R.id.settings_application_rate_us) TextView rateUs;
    @Bind(R.id.settings_application_contact_us) TextView contactUs;
    @Bind(R.id.settings_application_share_app) TextView shareTheApp;

    @Bind(R.id.settings_legal_copyrights) TextView copyRights;
    @Bind(R.id.settings_legal_privacy_policy) TextView privacyPolicy;
    @Bind(R.id.settings_legal_terms_of_service) TextView termsOfService;


    @Bind(R.id.settings_footer) TextView footer;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
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
        upVotesReceived.setOnCheckedChangeListener(this);

        opinionsReceived.setChecked(SettingsUtils.getInstance().getBoolPreference(
                SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION));
        renewalRequest.setChecked(SettingsUtils.getInstance().getBoolPreference(
                SettingsUtils.ENABLE_RENEWAL_REQUESTS_NOTIFICATION));
        topicRenewed.setChecked(SettingsUtils.getInstance().getBoolPreference(
                SettingsUtils.ENABLE_TOPIC_RENEWED_NOTIFICATION));
        upVotesReceived.setChecked(SettingsUtils.getInstance().getBoolPreference(
                SettingsUtils.ENABLE_UPVOTES_RECIEVED_NOTIFICATION));

        turnOnLocation.setOnCheckedChangeListener(this);
        turnOnLocation.setChecked(false);

        location.setText("Location: "+ProfileUtils.getInstance().getFromPreferences(ProfileUtils.COUNTRY));

        SpannableString styledString
                = new SpannableString("theforum | speak free");
        styledString.setSpan(new TypefaceSpan(getActivity(), "Roboto-Light.ttf"), 0, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledString.setSpan(new TypefaceSpan(getActivity(), "Roboto-Medium.ttf"), 4, 8,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledString.setSpan(new TypefaceSpan(getActivity(), "Roboto-Light.ttf"), 9, 21,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledString.setSpan(new RelativeSizeSpan(0.67f), 10, 21, 0);
        footer.setText(styledString);
    }

    @Override
    public void onClick(View v) {

            switch(v.getId()){
                case R.id.settings_application_share_app:
                    NetworkUtils.shareViaWatsapp(getActivity(), "Try this app theforum,\nregister as a tester on\nhttps://play.google.com/apps/testing/com.theforum " +
                            "\nThen download it from playstore link on the page that follows.\n" +
                            "For more details visit\nhttp://theforumapp.co/terms.html");
                    break;
                case R.id.settings_application_rate_us:
                    NetworkUtils.goToUrl(getActivity(), "https://play.google.com/store/apps/details?id=com.theforum");
                    break;
                case R.id.settings_application_feedback:
                    NetworkUtils.emailIntent(getActivity());
                    break;
                case R.id.settings_application_contact_us:
                    NetworkUtils.emailIntent(getActivity());
                    break;
                case R.id.settings_legal_terms_of_service:
                    NetworkUtils.goToUrl(getActivity(), "http://theforumapp.co/terms.html");
                    break;
                case R.id.settings_legal_privacy_policy:
                    NetworkUtils.goToUrl(getActivity(), "http://theforumapp.co/privacy.html");
                    break;
                case R.id.settings_legal_copyrights:
                    //CommonUtils.showToast(getActivity());
                    break;
            }
        }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){

            case R.id.settings_notification_up_votes:
                if(isChecked) SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_UPVOTES_RECIEVED_NOTIFICATION,true);
                else SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_UPVOTES_RECIEVED_NOTIFICATION,false);
                break;

            case R.id.settings_notification_renewal_requests:

                if(isChecked)SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_RENEWAL_REQUESTS_NOTIFICATION,true);
                else SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_RENEWAL_REQUESTS_NOTIFICATION,false);
                break;

            case R.id.settings_notification_topic_renewed:
                if(isChecked)SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_TOPIC_RENEWED_NOTIFICATION,true);
                else SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_TOPIC_RENEWED_NOTIFICATION,false);
                break;

            case R.id.settings_notification_opinions:
                if(isChecked) SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION,true);
                else SettingsUtils.getInstance().saveBooleanPreference(SettingsUtils.ENABLE_OPINIONS_RECEIVED_NOTIFICATION,false);
                break;

            case R.id.settings_location_toggle_button:

            if (isChecked) {
                String country;
                TelephonyManager teleMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                if (teleMgr != null) {

                    country = teleMgr.getNetworkCountryIso();
                   // Log.e("asasas",country);
                    String sentence="";
                    for(int i=0;i<country.length();i++)
                    {
                        if(Character.isUpperCase(country.charAt(i))==true)
                        {
                            char ch2= (char)(country.charAt(i)+32);
                            sentence = sentence + ch2;
                        }
                        else if(Character.isLowerCase(country.charAt(i))==true)
                        {
                            char ch2= (char)(country.charAt(i)-32);
                            sentence = sentence + ch2;
                        }
                        else
                            sentence= sentence + country.charAt(i);

                    }
                  //  CountryCodesIso iso = new CountryCodesIso(sentence);
                    //Log.e("jjjjjjj",sentence);
                    //Log.e("ffffffffff", "" + CountryCodesIso.valueOf(sentence).getCountryyName(sentence).toString());
                    ProfileUtils.getInstance().savePreferences(ProfileUtils.COUNTRY, country);

                    location.setText("Location: "+ "" + CountryCodesIso.valueOf(sentence).getCountryyName(sentence).toString());

                } else{
                    CommonUtils.showToast(getActivity(),"Cannot find your country, please try after some time");
                }
            }
            break;
        }
    }
}

