package com.theforum.ui.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.utils.ProfileUtils;
import com.theforum.utils.TypefaceSpan;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Ashish
 * @since 1/30/2016
 */

public class StatisticsFragment extends Fragment {
    @Bind(R.id.received_card_downvotes_count) TextView downVotesCount;
    @Bind(R.id.received_card_opinions_count) TextView opinionsCount;
    @Bind(R.id.received_card_topics_renewed_count) TextView topicsRenewedCount;
    @Bind(R.id.received_card_renewals_count) TextView renewalsCount;
    @Bind(R.id.received_card_upvotes_count) TextView upVotesCount;

    @Bind(R.id.croaked_card_downvotes_count) TextView cDownVotesCount;
    @Bind(R.id.croaked_card_opinions_count) TextView cOpinionsCount;
    @Bind(R.id.croaked_card_renewals_count) TextView cRenewalsCount;
    @Bind(R.id.croaked_card_upvotes_count) TextView cUpvotesCount;
    @Bind(R.id.croaked_card_renewed_count) TextView cRenewedCount;

    @Bind(R.id.footer) TextView footer;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        downVotesCount.setText("" + ProfileUtils.getInstance().getIntPreference(ProfileUtils.mRecDownvoted));
        opinionsCount.setText("" + ProfileUtils.getInstance().getIntPreference(ProfileUtils.mRecOpinions));
        topicsRenewedCount.setText("" + ProfileUtils.getInstance().getIntPreference(ProfileUtils.mRecTopicsRenewed));
        renewalsCount.setText(""+ProfileUtils.getInstance().getIntPreference(ProfileUtils.mRecRenewals));
        upVotesCount.setText(""+ProfileUtils.getInstance().getIntPreference(ProfileUtils.mRecUpvotes));

        cDownVotesCount.setText(""+ProfileUtils.getInstance().getIntPreference(ProfileUtils.mCrcDownvotes));
        cOpinionsCount.setText(""+ProfileUtils.getInstance().getIntPreference(ProfileUtils.mCrcOpinions));
        cRenewalsCount.setText(""+ProfileUtils.getInstance().getIntPreference(ProfileUtils.mCrcRenewals));
        cUpvotesCount.setText("" + ProfileUtils.getInstance().getIntPreference(ProfileUtils.mCrcUpvotes));
        cRenewedCount.setText("" + ProfileUtils.getInstance().getIntPreference(ProfileUtils.mCrcTopicsRenewed));

        SpannableString styledString
                = new SpannableString("theforum | speak free");
        styledString.setSpan(new TypefaceSpan(getActivity(), "Roboto-Light.ttf"), 0, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledString.setSpan(new TypefaceSpan(getActivity(), "Roboto-Medium.ttf"), 4, 8,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledString.setSpan(new TypefaceSpan(getActivity(), "Roboto-Light.ttf"), 9, 21,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledString.setSpan(new RelativeSizeSpan(0.67f), 10,21 , 0);
        footer.setText(styledString);

        mToolbar.setTitle("Your Stats");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
