package com.theforum.ui.trend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.theforum.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class TrendsFragment extends Fragment {

    @Bind(R.id.topic_toggle_button)
    Switch switchBtn;

    private FragmentManager mFragmentManager;
    private TrendsGlobalListFragment mGlobalTrendsList;
    private TrendsLocalListFragment mLocalTrendsList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        switchBtn.setChecked(false);
        mGlobalTrendsList = new TrendsGlobalListFragment();
        mLocalTrendsList = new TrendsLocalListFragment();

        mFragmentManager = getChildFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.topics_list_holder,mLocalTrendsList)
                .add(R.id.topics_list_holder, mGlobalTrendsList)
                .hide(mLocalTrendsList)
                .commit();

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /**
                 * when isChecked is true it means display local data
                 * is true.
                 */
                if (isChecked) {
                    mFragmentManager.beginTransaction().hide(mGlobalTrendsList).show(mLocalTrendsList).commit();
                } else {
                    mFragmentManager.beginTransaction().hide(mLocalTrendsList).show(mGlobalTrendsList).commit();
                }


            }
        });

    }



/*
    @Override
    public void onLongCLick(opinion opinion, TrendsDataModel trendsDataModel,View v) {
        Log.e("werwre","ytuytiuyioou");
        String id = trendsDataModel.getTrendId();
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }
    */
}