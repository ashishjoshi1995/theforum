package com.theforum.ui.opinion;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.constants.Messages;
import com.theforum.data.helpers.OpinionHelper;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.server.opinion;
import com.theforum.ui.ProgressDialog;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
import com.theforum.utils.listeners.SoftKeyboardStateWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class NewOpinionFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.new_opinion_text_holder)
    TextInputLayout mTopicNameHolder;

    @Bind(R.id.new_opinion_upload_btn)
    Button mUpload;

    @Bind(R.id.new_opinion_root_view)
    RelativeLayout mRootView;

    EditText mUploadText;

    private TopicDataModel topicModel;
    private int mKeyboardHeight;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            topicModel = getArguments().getParcelable(LayoutType.TOPIC_MODEL);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return inflater.inflate(R.layout.fragment_new_opinion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mTopicNameHolder.setHint("Your Opinion On " + topicModel.getTopicName());
        mUploadText = mTopicNameHolder.getEditText();
        mUploadText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf"));

        SoftKeyboardStateWatcher softKeyboardStateWatcher
                = new SoftKeyboardStateWatcher(mRootView, getActivity());
        softKeyboardStateWatcher.addSoftKeyboardStateListener(new SoftKeyboardStateWatcher.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeight) {
                ViewGroup.LayoutParams params = mRootView.getLayoutParams();
                params.height = mRootView.getHeight()- keyboardHeight;
                mRootView.setLayoutParams(params);
                mKeyboardHeight = keyboardHeight;
            }

            @Override
            public void onSoftKeyboardClosed() {
                ViewGroup.LayoutParams params = mRootView.getLayoutParams();
                params.height = mRootView.getHeight()+ mKeyboardHeight;
                mRootView.setLayoutParams(params);
            }
        });

        mUpload.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf"));
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mUploadText.getText().toString().equals("")){
                   uploadData();
                }else CommonUtils.showToast(getContext(),"Opinion Empty");
            }
        });

    }

    private void uploadData(){
        opinion opinion = new opinion(mUploadText.getText().toString());
        opinion.setTopicId(topicModel.getTopicId());
        opinion.setTopicName(topicModel.getTopicName());
        opinion.setUserId(User.getInstance().getId());

        final ProgressDialog dialog = ProgressDialog.createDialog(getContext());
        dialog.show();

        OpinionHelper.getHelper().addOpinion(opinion, new OpinionHelper.OnOpinionAddListener() {
            @Override
            public void onCompleted(OpinionDataModel opinion) {
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
