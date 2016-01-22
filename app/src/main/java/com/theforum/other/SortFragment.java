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
import butterknife.ButterKnife;

/**
 * @author Ashish
 * @since 1/8/2016
 */
public class SortFragment extends Fragment implements CheckBox.OnCheckedChangeListener, View.OnClickListener {

   // @Bind(R.id.fragment_sort_latest)RadioGroup sortSelection;

    @Bind(R.id.sort_done_btn)Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort,container,false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

     /*   checkBoxLatest.setOnCheckedChangeListener(this);
        checkBoxCreatedByMe.setOnCheckedChangeListener(this);
        checkBoxLeastRenewal.setOnCheckedChangeListener(this);
        checkBoxMostRenewal.setOnCheckedChangeListener(this);*/

        button.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        /*
        if(isChecked){
        checkBoxLeastRenewal.setChecked(false);
        checkBoxMostRenewal.setChecked(false);
        checkBoxCreatedByMe.setChecked(false);
        checkBoxLatest.setChecked(false);

        buttonView.setChecked(true);
    }
        else {
          //  checkBoxMostRenewal.setChecked(true);
        }
        */
    }

    @Override
    public void onClick(View v) {
        //commit the change in shared preference
        getActivity().finish();
    }
}

