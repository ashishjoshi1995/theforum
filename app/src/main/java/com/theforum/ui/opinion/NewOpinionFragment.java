package com.theforum.ui.opinion;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.theforum.constants.LayoutType;
import com.theforum.R;
import com.theforum.data.helpers.OpinionHelper;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.data.server.opinion;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
import com.theforum.utils.views.KeyboardListenerEditText;
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

    KeyboardListenerEditText mUploadText;

    private TopicDataModel topicModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            topicModel = (TopicDataModel) getArguments().getSerializable(LayoutType.TOPIC_MODEL);
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

        mUploadText = (KeyboardListenerEditText)mTopicNameHolder.getEditText();
        mUploadText.setHint("Your Opinion On " + topicModel.getTopicName());
        mUploadText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf"));
        mUploadText.setOnBackPressListener(new KeyboardListenerEditText.OnBackPressListener() {
            @Override
            public boolean onBackPressed() {
                getActivity().finish();
                return true;
            }
        });

        SoftKeyboardStateWatcher softKeyboardStateWatcher
                = new SoftKeyboardStateWatcher(mRootView,getActivity(),true);
        softKeyboardStateWatcher.addSoftKeyboardStateListener(new SoftKeyboardStateWatcher.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeight) {
                ViewGroup.LayoutParams params = mRootView.getLayoutParams();
                params.height = mRootView.getHeight()-keyboardHeight;
                mRootView.setLayoutParams(params);
            }

            @Override
            public void onSoftKeyboardClosed() {

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
        final ProgressDialog pd = new ProgressDialog(getActivity(), R.style.MyDialog);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.setMessage("Adding your Opinion. Please Wait...");
        pd.show();

        OpinionHelper.getHelper().addOpinion(opinion, new OpinionHelper.OnOpinionAddListener() {
            @Override
            public void onCompleted(OpinionDataModel opinion) {
                CommonUtils.showToast(getContext(),"Your Opinion is added");
                pd.dismiss();
                getActivity().finish();
            }

            @Override
            public void onError(String error) {
                Log.e("error",error);
                pd.dismiss();
            }
        });

    }

}
