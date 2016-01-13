package com.theforum.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.theforum.HomeActivity;
import com.theforum.R;
import com.theforum.utils.User;
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

                    register(Integer.parseInt(mAge.getText().toString()));

                }else CommonUtils.showToast(getContext(),"PLease enter your age. Don't Panic!!");
            }
        });

    }

    private void register(final int age) {
        user user = new user();
        user.setAge(age);
        Log.e("register called","");
        LoginHelper loginHelper = new LoginHelper();
        loginHelper.login(user, new LoginHelper.OnLoginCompleteListener() {
                    @Override
                    public void onCompleted(user user) {
                        CommonUtils.showToast(getContext(), "Successfully Registered");

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
                        Log.e("login",""+user.getmUid()+"/"+user.getmId());

                        /*
                         *  show home UI
                         */

                        Intent intent = new Intent(getContext(),HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("error register",error);
                    }
                });
    }


}
