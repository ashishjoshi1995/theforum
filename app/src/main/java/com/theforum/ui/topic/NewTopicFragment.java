package com.theforum.ui.topic;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.theforum.R;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.server.topic;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class NewTopicFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.new_topic_name)
    TextInputLayout mTopicNameHolder;

    EditText mTopicText;

    @Bind(R.id.new_topic_description)
    EditText mDescription;

    @Bind(R.id.new_topic_upload_btn)
    Button mUpload;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_topic, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        mTopicText = mTopicNameHolder.getEditText();

        mUpload.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf"));
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isEditTextEmpty(mTopicText)) {

                    if(!isEditTextEmpty(mDescription)){
                        /**
                         * We have topic and description. Now we can upload the topic
                         * to server
                         */

                        uploadData();

                    }else CommonUtils.showToast(getContext(),"Description Empty");

                } else CommonUtils.showToast(getActivity(), "Topic Empty");

            }
        });
    }

    private void uploadData(){
        topic topic = new topic();
        topic.setTopicName(mTopicText.getText().toString());
        topic.setTopicDescription(mDescription.getText().toString());
        topic.setUserId(User.getInstance().getId());

        final ProgressDialog pd = new ProgressDialog(getActivity(), R.style.MyDialog);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.setMessage("Adding a New Topic. Please Wait...");
        pd.show();
        TopicHelper.getHelper().addTopic(topic, new TopicHelper.OnTopicInsertListener() {
            @Override
            public void onCompleted() {
                CommonUtils.showToast(getActivity(), "Topic created");
                pd.dismiss();
                getActivity().finish();
            }

            @Override
            public void onError(String error) {
                Log.e("UploadTopic error", "" + error);
                pd.dismiss();
            }
        });
    }

    private boolean isEditTextEmpty(EditText editText){
        return (editText.getText().toString().equals("")) ;
    }
}
