package com.theforum.ui.topic;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.constants.Messages;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.helpers.localHelpers.LocalTopicHelper;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.server.areatopics;
import com.theforum.data.server.topic;
import com.theforum.ui.ProgressDialog;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
import com.theforum.utils.locationTracker.GPSTracker;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class NewTopicFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.new_topic_name) TextInputLayout mTopicNameHolder;
    @Bind(R.id.new_topic_description) EditText mDescription;
    @Bind(R.id.new_topic_upload_btn) Button mUpload;
    @Bind(R.id.new_topic_toggle_button) Switch aSwitch;
    EditText mTopicText;

    private TopicDataModel mTopicModel;
    private boolean mUpdateTopic = false;
    private boolean isLocal = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUpdateTopic = false;
        if(getArguments()!= null){
            mTopicModel = getArguments().getParcelable(LayoutType.TOPIC_MODEL);
            if(mTopicModel!=null) mUpdateTopic = true;
        }
    }

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

                    if (!isEditTextEmpty(mDescription)) {
                        if (mUpdateTopic) {
                            updateTopic();
                        } else uploadTopic();

                    } else CommonUtils.showToast(getContext(), "Description Empty");

                } else CommonUtils.showToast(getActivity(), "Topic Empty");

            }
        });

        if(mUpdateTopic){
            mTopicText.setText(mTopicModel.getTopicName());
            mDescription.setText(mTopicModel.getTopicDescription());
        }

        aSwitch.setOnCheckedChangeListener(this);
        aSwitch.setChecked(false);
    }

    private void uploadTopic(){
        GPSTracker gps;
        double latitude =0 ;
        double longitude =0;
        gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()||(gps.getLongitude()!=0.0&&gps.getLatitude()!=0.0)) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        else {
            gps.showSettingsAlert();
        }

        final ProgressDialog dialog = ProgressDialog.createDialog(getActivity());
        dialog.show();

        if(!isLocal){
            topic topic = new topic();
            topic.setTopicName(mTopicText.getText().toString().trim());
            topic.setTopicDescription(mDescription.getText().toString().trim());
            topic.setUserId(User.getInstance().getId());
            topic.setLatitude(latitude);
            topic.setLongitude(longitude);
                TopicHelper.getHelper().addTopic(topic, new TopicHelper.OnTopicInsertListener() {
                    @Override
                    public void onCompleted(TopicDataModel model, boolean isUpdated) {
                        CommonUtils.showToast(getActivity(), "Topic created");
                        dialog.dismiss();
                        getActivity().finish();
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showToast(getContext(), Messages.NO_NET_CONNECTION);
                        dialog.dismiss();
                    }
                });

        } else{
            areatopics areatopics = new areatopics();
            areatopics.setLatitude(latitude);
            areatopics.setLongitude(longitude);
            areatopics.setTopicName(mTopicText.getText().toString().trim());
            areatopics.setTopicDescription(mDescription.getText().toString().trim());
            areatopics.setUserId(User.getInstance().getId());
            LocalTopicHelper.getHelper().addTopic(areatopics, new LocalTopicHelper.OnTopicInsertListener() {
                @Override
                public void onCompleted(TopicDataModel topicDataModel, boolean isUpdated) {
                    CommonUtils.showToast(getActivity(), "Topic created");
                    dialog.dismiss();
                    getActivity().finish();
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showToast(getContext(), Messages.NO_NET_CONNECTION);
                    dialog.dismiss();
                }
            });
        }
    }

    private void updateTopic(){

        mTopicModel.setTopicName(mTopicText.getText().toString().trim());
        mTopicModel.setTopicDescription(mDescription.getText().toString().trim());
        final ProgressDialog dialog = ProgressDialog.createDialog(getActivity());
        dialog.show();
        if(mTopicModel.isLocalTopic()){
            LocalTopicHelper.getHelper().updateTopic(mTopicModel, new LocalTopicHelper.OnTopicInsertListener() {
                @Override
                public void onCompleted(TopicDataModel topicDataModel, boolean isUpdated)    {
                    CommonUtils.showToast(getActivity(), "Topic Updated");
                    dialog.dismiss();
                    getActivity().finish();
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showToast(getContext(), Messages.NO_NET_CONNECTION);
                    dialog.dismiss();
                }
            });
        }
        else {

            TopicHelper.getHelper().updateTopic(mTopicModel, new TopicHelper.OnTopicInsertListener() {
                @Override
                public void onCompleted(TopicDataModel model,boolean isUpdated) {
                    CommonUtils.showToast(getActivity(), "Topic Updated");
                    dialog.dismiss();
                    getActivity().finish();
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showToast(getContext(), Messages.NO_NET_CONNECTION);
                    dialog.dismiss();
                }
            });
        }

    }

    private boolean isEditTextEmpty(EditText editText){
        return (editText.getText().toString().equals("")) ;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.new_topic_toggle_button){
            if(isChecked){
                isLocal = true;
            }
        }
    }
}
