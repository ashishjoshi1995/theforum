package com.theforum.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.theforum.R;
import com.theforum.data.local.database.notificationDB.NotificationDBHelper;
import com.theforum.data.local.models.NotificationDataModel;
import com.theforum.utils.views.DividerItemDecorator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author DEEPANKAR
 * @since 07-01-2016.
 */

public class NotificationActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.settings_recycler_view)
    RecyclerView mRecyclerView;

    private NotificationListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        mToolbar.setTitle("Notifications");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecorator(this, R.drawable.recycler_view_divider));

        ArrayList<NotificationDataModel> dataModels = NotificationDBHelper.getHelper().getAllNotifications();
        mAdapter = new NotificationListAdapter(this, dataModels);
        mRecyclerView.setAdapter(mAdapter);

        NotificationDBHelper.getHelper().updateNotificationReadStatus(dataModels);

    }


}
