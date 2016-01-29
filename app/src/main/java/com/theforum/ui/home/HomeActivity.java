package com.theforum.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.theforum.constants.LayoutType;
import com.theforum.ui.LoginActivity;
import com.theforum.R;
import com.theforum.notification.NotificationService;
import com.theforum.ui.search.SearchResultFragment;
import com.theforum.utils.CommonUtils;
import com.theforum.utils.ProfileUtils;
import com.theforum.utils.TypefaceSpan;
import com.theforum.utils.listeners.OnHomeUiChangeListener;
import com.theforum.utils.views.MaterialSearchView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements OnHomeUiChangeListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.home_material_search_view)
    MaterialSearchView mMaterialSearchView;

    private FragmentManager mFragmentManager;

    private SearchResultFragment mSearchResultFragment;
    private int currentViewPagerPosition;

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

        SpannableString spannableString = new SpannableString("theforum");
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
                mToolbar.setVisibility(View.GONE);

                mSearchResultFragment = new SearchResultFragment();
                mFragmentManager.beginTransaction().add(R.id.home_fragment_container, mSearchResultFragment).commit();
            }

            @Override
            public void onSearchViewClosed() {
                mToolbar.setVisibility(View.VISIBLE);

                mFragmentManager.beginTransaction().remove(mSearchResultFragment).commit();
            }
        });

        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }


    public MaterialSearchView getSearchView(){
        return mMaterialSearchView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
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
    public void onPageSelected(int position) {

        currentViewPagerPosition = position;
    }

}
