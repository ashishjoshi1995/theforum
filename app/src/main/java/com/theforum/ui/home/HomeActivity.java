package com.theforum.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.theforum.R;
import com.theforum.constants.LayoutType;
import com.theforum.data.local.database.topicDB.TopicDBHelper;
import com.theforum.data.local.database.trendsDB.TrendsDBHelper;
import com.theforum.ui.activity.LoginActivity;
import com.theforum.ui.search.SearchResultFragment;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.ProfileUtils;
import com.theforum.utils.TypefaceSpan;
import com.theforum.utils.views.MaterialSearchView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.home_material_search_view)
    MaterialSearchView mMaterialSearchView;

    private FragmentManager mFragmentManager;

    private SearchResultFragment mSearchResultFragment;

    private boolean mSearchActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!ProfileUtils.getInstance().contains(ProfileUtils.USER_ID)){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SpannableString spannableString = new SpannableString("theforum_debug");
        spannableString.setSpan(new TypefaceSpan(this, "Roboto-Light.ttf"), 0, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TypefaceSpan(this, "Roboto-Medium.ttf"), 4, 8,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mToolbar.setTitle(spannableString);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.home_fragment_container, new HomeFragment()).commit();

        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                mSearchActive = true;
                mToolbar.setVisibility(View.GONE);

                mSearchResultFragment = new SearchResultFragment();
                mFragmentManager.beginTransaction().add(R.id.home_fragment_container, mSearchResultFragment).commit();
            }

            @Override
            public void onSearchViewClosed() {
                mSearchActive = false;
                mToolbar.setVisibility(View.VISIBLE);

                mFragmentManager.beginTransaction().remove(mSearchResultFragment).commit();
            }
        });

    }

    public MaterialSearchView getSearchView(){
        return mMaterialSearchView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(mSearchActive){
               mMaterialSearchView.closeSearch();
               mSearchActive = false;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                CommonUtils.openContainerActivity(this, LayoutType.SETTINGS_FRAGMENT);
                break;
            case R.id.action_add_opinion:
                CommonUtils.openContainerActivity(this, LayoutType.NEW_TOPIC_FRAGMENT);
                break;
            case R.id.action_search:
                mMaterialSearchView.showSearch();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        TopicDBHelper.getHelper().closeDataBase();
        TrendsDBHelper.getHelper().closeDatabase();
    }
}
