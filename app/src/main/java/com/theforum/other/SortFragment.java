package com.theforum.other;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.theforum.R;

import butterknife.Bind;

/**
 * @author Ashish
 * @since 1/8/2016
 */
public class SortFragment extends Fragment implements CheckBox.OnCheckedChangeListener {

    @Bind(R.id.fragment_sort_latest)CheckBox checkBoxLatest;
    @Bind(R.id.fragment_sort_least_renewal)CheckBox checkBoxLeastRenewal;
    @Bind(R.id.fragment_sort_created_by_me)CheckBox checkBoxCreatedByMe;
    @Bind(R.id.fragment_sort_most_renewal)CheckBox checkBoxMostRenewal;

    @Bind(R.id.fragment_sort_done)Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        checkBoxLatest.setOnCheckedChangeListener(this);
        checkBoxCreatedByMe.setOnCheckedChangeListener(this);
        checkBoxLeastRenewal.setOnCheckedChangeListener(this);
        checkBoxMostRenewal.setOnCheckedChangeListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
        checkBoxLeastRenewal.setChecked(false);
        checkBoxMostRenewal.setChecked(false);
        checkBoxCreatedByMe.setChecked(false);
        checkBoxLatest.setChecked(false);

        buttonView.setChecked(true);
    }
        else {
            checkBoxMostRenewal.setChecked(true);
        }
    }
}

