package com.theforum.ui.opinion;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.data.helpers.FlagHelper;
import com.theforum.data.helpers.OpinionHelper;
import com.theforum.data.helpers.TopicHelper;
import com.theforum.data.helpers.localHelpers.LocalOpinionHelper;
import com.theforum.data.helpers.localHelpers.LocalTopicHelper;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.models.OpinionDataModel;
import com.theforum.data.local.models.TopicDataModel;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.User;
import com.theforum.utils.listeners.OnListItemClickListener;
import com.theforum.utils.views.DividerItemDecorator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
@SuppressWarnings("deprecation")
public class OpinionsFragment extends Fragment implements OnListItemClickListener{

    @Bind(R.id.opinion_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.opinion_recycler_view) RecyclerView recyclerView;

    @Bind(R.id.opinion_toolbar) Toolbar toolbar;
    @Bind(R.id.opinion_collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.opinion_topic_description) TextView topicDescription;
    @Bind(R.id.opinion_renew_btn) ImageButton renewBtn;
    @Bind(R.id.opinion_time_holder) TextView timeHolder;

    @Bind(R.id.opinion_fab) FloatingActionButton fab;

    @BindDrawable(R.drawable.renew_icon) Drawable renewIcon;
    @BindDrawable(R.drawable.renew_icon_on) Drawable renewedIcon;

    private OpinionsListAdapter mAdapter;
    private ArrayList<OpinionDataModel> mOpinions;
    private TopicDataModel mTopicModel;
    private int mPositionClicked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments()!=null){
            mTopicModel = getArguments().getParcelable(LayoutType.TOPIC_MODEL);
        }
        mOpinions = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opinion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        toolbar.setTitle(mTopicModel.getTopicName());
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        collapsingToolbarLayout.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setPadding(0, collapsingToolbarLayout.getHeight(), 0, 0);
                int actionBarSize = (int) CommonUtils.convertDpToPixel(56, getContext());
                int progressViewStart = collapsingToolbarLayout.getHeight() - actionBarSize;
                int progressViewEnd = progressViewStart + (int) (actionBarSize * 1.2f);
                swipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);
            }
        });

        topicDescription.setText(mTopicModel.getTopicDescription());
        timeHolder.setText(Html.fromHtml(getContext().getResources().getQuantityString(
                R.plurals.opinion_time_holder_message,
                mTopicModel.getRenewedCount() + 1,
                mTopicModel.getHoursLeft(),
                mTopicModel.getRenewalRequests())));
        if(mTopicModel.isRenewed()){
            renewBtn.setBackgroundDrawable(renewedIcon);
        }else renewBtn.setBackgroundDrawable(renewIcon);

        renewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRenewButton();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), R.drawable.recycler_view_divider));
        mAdapter = new OpinionsListAdapter(getActivity(), mOpinions);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnListItemClickListener(this);
        getOpinionsFromServer();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openContainerActivity(getActivity(), LayoutType.NEW_OPINION_FRAGMENT,
                        Pair.create(LayoutType.TOPIC_MODEL, (Parcelable) mTopicModel));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOpinionsFromServer();
            }
        });

        OpinionHelper.getHelper().addNewOpinionAddedListener(new OpinionHelper.OnOpinionAddListener() {
            @Override
            public void onCompleted(OpinionDataModel opinion, boolean isUpdated) {
                if (isUpdated) {
                    mOpinions.remove(mPositionClicked);
                    mOpinions.add(mPositionClicked, opinion);
                    mAdapter.notifyItemChanged(mPositionClicked);
                } else mAdapter.addOpinion(opinion, 0);
            }

            @Override
            public void onError(String error) {
            }
        });

        LocalOpinionHelper.getHelper().addNewOpinionAddedListener(new LocalOpinionHelper.OnOpinionAddListener() {
            @Override
            public void onCompleted(OpinionDataModel opinion, boolean isUpdated) {
                if(isUpdated){
                    mOpinions.remove(mPositionClicked);
                    mOpinions.add(mPositionClicked, opinion);
                    mAdapter.notifyItemChanged(mPositionClicked);
                }else mAdapter.addOpinion(opinion,0);
            }

            @Override
            public void onError(String error) {}
        });

    }

    private void getOpinionsFromServer(){
            if (!mTopicModel.isLocalTopic()) {
                OpinionHelper.getHelper().getTopicSpecificOpinions(mTopicModel.getTopicId(),
                        new OpinionHelper.OnOpinionsReceivedListener() {
                            @Override
                            public void onCompleted(ArrayList<OpinionDataModel> opinions) {
                                mAdapter.clearAll();
                                mAdapter.addOpinions(opinions);
                                swipeRefreshLayout.setRefreshing(false);

                            }

                            @Override
                            public void onError(final String error) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);
                                        CommonUtils.showToast(getContext(), error);
                                    }
                                });
                            }
                        });
            }
        else {
                LocalOpinionHelper.getHelper().getTopicSpecificOpinions(mTopicModel.getTopicId(),
                        new LocalOpinionHelper.OnOpinionsReceivedListener() {
                            @Override
                            public void onCompleted(ArrayList<OpinionDataModel> opinions) {
                                mAdapter.clearAll();
                                mAdapter.addOpinions(opinions);
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onError(final String error) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);
                                        CommonUtils.showToast(getContext(), error);
                                    }
                                });
                            }
                        });
            }
    }

    private void handleRenewButton(){
        final int b = mTopicModel.getRenewalRequests();

        if(!mTopicModel.isRenewed()) {
            renewBtn.setBackgroundDrawable(renewedIcon);
            timeHolder.setText(Html.fromHtml(getContext().getResources().getQuantityString(
                    R.plurals.opinion_time_holder_message,
                    b + 1, mTopicModel.getHoursLeft(), b + 1)));
            mTopicModel.setRenewalRequests(b + 1);
            mTopicModel.setIsRenewed(true);
              if(!mTopicModel.isLocalTopic()){
            TopicHelper.getHelper().addRenewalRequest(mTopicModel,
                    new TopicHelper.OnRenewalRequestListener() {

                        @Override
                        public void onCompleted() {
                            Log.e("renew non local", "renew non local");
                            TopicDBHelper.getHelper().updateTopicRenewalStatus(mTopicModel);
                        }

                        @Override
                        public void onError(String error) {
                            // notify the user that renew have failed
                            CommonUtils.showToast(getContext(), error);

                            // revert the changes in local dataModel
                            mTopicModel.setRenewalRequests(b);
                            mTopicModel.setIsRenewed(false);

                            // revert the changes made in the UI
                            renewBtn.setBackgroundDrawable(renewIcon);
                            timeHolder.setText(Html.fromHtml(getContext().getResources().getQuantityString(
                                    R.plurals.opinion_time_holder_message,
                                    b, mTopicModel.getHoursLeft(), b)));
                        }
                    });
        }
            else {
                  LocalTopicHelper.getHelper().addRenewalRequest(mTopicModel, new LocalTopicHelper.OnRenewalRequestListener() {
                      @Override
                      public void onCompleted() {
                          Log.e("local topic renewed","local topic renewed");
                          TopicDBHelper.getHelper().updateTopicRenewalStatus(mTopicModel);
                      }

                      @Override
                      public void onError(String error) {
                          // notify the user that renew have failed
                          CommonUtils.showToast(getContext(), error);

                          // revert the changes in local dataModel
                          mTopicModel.setRenewalRequests(b);
                          mTopicModel.setIsRenewed(false);

                          // revert the changes made in the UI
                          renewBtn.setBackgroundDrawable(renewIcon);
                          timeHolder.setText(Html.fromHtml(getContext().getResources().getQuantityString(
                                  R.plurals.opinion_time_holder_message,
                                  b, mTopicModel.getHoursLeft(), b)));
                      }
                  });
              }

        } else {
            renewBtn.setBackgroundDrawable(renewIcon);
            timeHolder.setText(Html.fromHtml(getContext().getResources().getQuantityString(
                    R.plurals.opinion_time_holder_message,
                    b - 1, mTopicModel.getHoursLeft(), b - 1)));
            mTopicModel.setRenewalRequests(b - 1);
            mTopicModel.setIsRenewed(false);
            if(!mTopicModel.isLocalTopic()){
            TopicHelper.getHelper().removeRenewal(mTopicModel.getTopicId(),
                    new TopicHelper.OnRemoveRenewalRequestListener() {
                        @Override
                        public void onCompleted() {
                            Log.e("remoed renewal","removed renewal global");
                            TopicDBHelper.getHelper().updateTopicRenewalStatus(mTopicModel);
                        }

                        @Override
                        public void onError(String error) {
                            // notify the user that renew removal have failed
                            CommonUtils.showToast(getContext(), error);

                            // revert the changes in local dataModel
                            mTopicModel.setRenewalRequests(b);
                            mTopicModel.setIsRenewed(true);

                            // revert the changes made in the UI
                            renewBtn.setBackgroundDrawable(renewedIcon);
                            timeHolder.setText(Html.fromHtml(getContext().getResources().getQuantityString(
                                    R.plurals.opinion_time_holder_message,
                                    b, mTopicModel.getHoursLeft(), b)));
                        }

                    });
        }
            else{
                LocalTopicHelper.getHelper().removeRenewal(mTopicModel.getTopicId(),
                        new LocalTopicHelper.OnRemoveRenewalRequestListener() {
                    @Override
                    public void onCompleted() {
                        Log.e("local tpc remv renewal","local tpc remv renewal");
                        TopicDBHelper.getHelper().updateTopicRenewalStatus(mTopicModel);
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showToast(getContext(), error);

                        // revert the changes in local dataModel
                        mTopicModel.setRenewalRequests(b);
                        mTopicModel.setIsRenewed(true);

                        // revert the changes made in the UI
                        renewBtn.setBackgroundDrawable(renewedIcon);
                        timeHolder.setText(Html.fromHtml(getContext().getResources().getQuantityString(
                                R.plurals.opinion_time_holder_message,
                                b, mTopicModel.getHoursLeft(), b)));
                    }
                });
            }
        }

    }


    @Override
    public void onItemClick(View v, final int position) {
        mPositionClicked = position;
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);

        if(!mOpinions.get(position).getuId().equals(User.getInstance().getId())){
            popupMenu.getMenu().removeItem(R.id.item_edit);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_edit:
                        CommonUtils.openContainerActivity(getContext(), LayoutType.NEW_OPINION_FRAGMENT,
                                Pair.create(LayoutType.OPINION_MODEL,(Parcelable)mOpinions.get(position)));
                        break;

                    case R.id.item_flag:
                        FlagHelper helper = new FlagHelper();

                        helper.addFlagOpinionRequest(mOpinions.get(position).getOpinionId(),
                                mOpinions.get(position).getOpinionText(), mOpinions.get(position).getTopicId(),
                                mOpinions.get(position).getServerId());
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_other, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.action_settings) CommonUtils.openContainerActivity(getContext(),
                LayoutType.SETTINGS_FRAGMENT);
        return super.onOptionsItemSelected(item);
    }
}
