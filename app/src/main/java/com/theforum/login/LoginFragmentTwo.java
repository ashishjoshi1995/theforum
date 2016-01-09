package com.theforum.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.theforum.R;
import com.theforum.User;
import com.theforum.data.dataModels.user;
import com.theforum.data.helpers.LoginHelper;
import com.theforum.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Ashish
 * @since 1/9/2016
 */
public class LoginFragmentTwo extends Fragment {

    @Bind(R.id.fragment_login_age)
    EditText mAge;

    @Bind(R.id.fragment_login_button)
    Button mLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_two, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mAge.getText().toString().equals("")){
                    register();
                }else CommonUtils.showToast(getContext(),"PLease enter your age. Don't Panic!!");
            }
        });

    }

    private void register() {
        user user = new user();
        user.setAge(Integer.parseInt(mAge.getText().toString()));

        LoginHelper loginHelper = new LoginHelper();
        loginHelper.login(user, new LoginHelper.OnLoginCompleteListener() {
                    @Override
                    public void onCompleted(user user) {
                        Log.e("success","");
                        CommonUtils.showToast(getContext(),"Successfully Registered");

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("error",error);
                    }
                });
    }


}
